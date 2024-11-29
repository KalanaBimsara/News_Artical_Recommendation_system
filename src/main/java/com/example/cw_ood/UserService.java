package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserService {
    private final MongoCollection<Document> usersCollection;
    private final MongoCollection<Document> adminsCollection;

    public UserService(MongoDatabase database) {
        this.usersCollection = database.getCollection("Users");
        this.adminsCollection = database.getCollection("Admins");
    }

    // Validate User Login
    public boolean validateLogin(String username, String password) {
        return validateCredentials(usersCollection, username, hashPassword(password));
    }

    // Validate Admin Login
    public boolean validateAdminLogin(String username, String password) {
        return validateCredentials(adminsCollection, username, hashPassword(password));
    }

    // Create a New User
    public boolean createUser(String username, String password) {
        if (userExists(usersCollection, username)) {
            return false; // User already exists
        }

        Document newUser = new Document("username", username)
                .append("password", hashPassword(password))
                .append("role", "User") // Default role is "User"
                .append("createdAt", java.time.Instant.now().toString());

        usersCollection.insertOne(newUser);
        return true; // User created successfully
    }

    // Reset User Password
    public boolean resetPassword(String username, String newPassword) {
        if (!userExists(usersCollection, username)) {
            return false; // User does not exist
        }

        usersCollection.updateOne(new Document("username", username),
                new Document("$set", new Document("password", hashPassword(newPassword))));
        return true; // Password reset successful
    }

    // Check if a User Exists
    private boolean userExists(MongoCollection<Document> collection, String username) {
        return collection.find(new Document("username", username)).first() != null;
    }

    // Validate Credentials (Reusable for Users/Admins)
    private boolean validateCredentials(MongoCollection<Document> collection, String username, String hashedPassword) {
        Document query = new Document("username", username).append("password", hashedPassword);
        return collection.find(query).first() != null;
    }

    // Hash Password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
