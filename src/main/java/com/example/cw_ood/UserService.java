package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UserService {
    private final MongoDatabase database;

    public UserService(MongoDatabase database) {
        this.database = database;
    }

    // Existing login validation
    public boolean validateLogin(String username, String password) {
        MongoCollection<Document> usersCollection = database.getCollection("Users");
        Document query = new Document("username", username).append("password", password);
        return usersCollection.find(query).first() != null;
    }

    // New Sign-Up Method
    public boolean createUser(String username, String password) {
        MongoCollection<Document> usersCollection = database.getCollection("Users");

        // Check if the user already exists
        if (usersCollection.find(new Document("username", username)).first() != null) {
            return false; // User already exists
        }

        // Create new user document
        Document newUser = new Document("username", username).append("password", password);
        usersCollection.insertOne(newUser);
        return true; // User created successfully
    }

    // Reset Password Method
    public boolean resetPassword(String username, String newPassword) {
        MongoCollection<Document> usersCollection = database.getCollection("Users");

        // Check if the user exists
        Document user = usersCollection.find(new Document("username", username)).first();
        if (user == null) {
            return false; // User does not exist
        }

        // Update the user's password
        usersCollection.updateOne(new Document("username", username),
                new Document("$set", new Document("password", newPassword)));
        return true; // Password reset successful
    }
}
