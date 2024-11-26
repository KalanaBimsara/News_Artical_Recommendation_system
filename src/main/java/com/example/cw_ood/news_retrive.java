package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class news_retrive {
    private final MongoDatabase database;

    public news_retrive(MongoDatabase database) {
        this.database = database;
    }

    // Fetch only title and description
    public List<Document> getNewsTitlesAndDescriptions() {
        List<Document> newsList = new ArrayList<>();
        try {
            MongoCollection<Document> newsCollection = database.getCollection("News");

            // Use a projection to fetch only the "title" and "description"
            MongoCursor<Document> cursor = newsCollection.find()
                    .projection(new Document("title", 1).append("description", 1).append("_id", 0))
                    .iterator();

            while (cursor.hasNext()) {
                newsList.add(cursor.next());
            }

            cursor.close();
            System.out.println("Fetched titles and descriptions successfully.");
        } catch (Exception e) {
            System.err.println("Error fetching news: " + e.getMessage());
        }
        return newsList;
    }
}
