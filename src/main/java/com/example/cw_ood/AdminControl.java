package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AdminControl {
    private final MongoCollection<Document> usersCollection;
    private final MongoCollection<Document> newsCollection;
    private final MongoCollection<Document> categorized_newsCollection;


    public AdminControl(MongoDatabase database) {
        this.usersCollection = database.getCollection("Users");
        this.newsCollection = database.getCollection("categorized_news");
        this.categorized_newsCollection = database.getCollection("categorized_news");
    }

    // Handle Deleting a User
    public void deleteUser(String username) {
        usersCollection.deleteOne(new Document("username", username));
        showAlert("User Deleted", "The user " + username + " has been deleted successfully.");
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

    public List<News> fetchAllNews() {
        List<News> newsList = new ArrayList<>();
        for (Document doc : categorized_newsCollection.find()) {
            String id = doc.getObjectId("_id").toString();
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");
            String category = doc.getString("category");

            newsList.add(new News(id, title, description, url, category));
        }
        return newsList;
    }

    // Delete news by ID
    public boolean deleteNews(String newsId) {
        try {
            Document query = new Document("_id", new org.bson.types.ObjectId(newsId));
            return categorized_newsCollection.deleteOne(query).getDeletedCount() > 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid ObjectId: " + newsId);
            return false;
        }
    }




    /*public void deleteNews(String url) {
        newsCollection.deleteOne(new Document("url", url));
        showAlert("News Deleted", "The news article has been deleted successfully.");
    }*/

}
