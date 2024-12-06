package com.example.cw_ood;

import News.News;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
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


}
