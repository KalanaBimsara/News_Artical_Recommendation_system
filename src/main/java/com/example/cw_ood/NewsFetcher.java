package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class NewsFetcher {
    private static final String API_KEY = "7c2ad3303fbf489abf66ed85e1a6e31b"; // Replace with your API key
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=" + API_KEY;
    private final MongoDatabase database;
    String[] categories = {"Sports", "Technology", "Health", "AI", "Business", "Entertainment", "general", "Science", "Politics"};

    public NewsFetcher(MongoDatabase database) {
        this.database = database;
    }

    public void fetchAndStoreNews() {
        try {
            // Fetch news from API
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");

            // Save news to MongoDB
            MongoCollection<Document> newsCollection = database.getCollection("News");
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);

                Document newsDocument = new Document("title", article.getString("title"))
                        .append("description", article.optString("description"))
                        .append("url", article.getString("url"));

                newsCollection.insertOne(newsDocument);
            }

            System.out.println("News articles fetched and stored successfully.");
        } catch (Exception e) {
            System.err.println("Error fetching or storing news: " + e.getMessage());
        }

        try {
            // Fetch news from API
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");

            NewsCategorizer categorizer = new NewsCategorizer();

            // Save categorized news to MongoDB
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);

                String title = article.getString("title");
                String description = article.optString("description");
                String urlStr = article.getString("url");

                // Categorize the article
                String category = categorizer.categorize(title + " " + description, categories);

                // Create or use a collection for the category
                MongoCollection<Document> categoryCollection = database.getCollection(category);

                Document newsDocument = new Document("title", title)
                        .append("description", description)
                        .append("url", urlStr);

                categoryCollection.insertOne(newsDocument);
            }

            System.out.println("News articles fetched, categorized, and stored successfully.");
        } catch (Exception e) {
            System.err.println("Error fetching or storing news: " + e.getMessage());
        }


    }


}
