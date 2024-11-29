package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.bson.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AdminControl {
    private final MongoCollection<Document> usersCollection;
    private final MongoCollection<Document> newsCollection;

    public AdminControl(MongoDatabase database) {
        this.usersCollection = database.getCollection("Users");
        this.newsCollection = database.getCollection("categorized_news");
    }

    // Handle Deleting a User
    public void deleteUser(String username) {
        usersCollection.deleteOne(new Document("username", username));
        showAlert("User Deleted", "The user " + username + " has been deleted successfully.");
    }

    // Handle Resetting a User's Password
    public void resetPassword(String username, String newPassword) {
        Document user = usersCollection.find(new Document("username", username)).first();
        if (user == null) {
            showAlert("Error", "User not found: " + username);
            return;
        }

        usersCollection.updateOne(new Document("username", username),
                new Document("$set", new Document("password", hashPassword(newPassword))));
        showAlert("Password Reset", "Password for " + username + " has been reset.");
    }

    // Handle Adding a New Article
    public void addArticle(String title, String description, String url, String category) {
        Document newArticle = new Document("title", title)
                .append("description", description)
                .append("url", url)
                .append("category", category)
                .append("timestamp", java.time.Instant.now().toString());
        newsCollection.insertOne(newArticle);
        showAlert("Article Added", "The article has been added successfully.");
    }

    // Display Alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility: Hash Password
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
