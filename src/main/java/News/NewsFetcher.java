package News;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsFetcher {
    private static final String API_KEY = "7c2ad3303fbf489abf66ed85e1a6e31b"; // Replace with your API key
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;
    private final MongoDatabase database;

    // Define categories for categorization
    private final String[] categories = {"Sports", "Technology", "Health", "AI", "Business", "Entertainment", "General", "Science", "Politics"};

    public NewsFetcher(MongoDatabase database) {
        this.database = database;
    }

    public void fetchAndCategorizeNews() {
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
            JSONArray articles = jsonResponse.getJSONArray("articles"); // Correctly access articles

            // Initialize the categorizer
            NewsCategorizer categorizer = new NewsCategorizer();

            // Get the `categorized_news` collection
            MongoCollection<Document> categorizedNewsCollection = database.getCollection("categorized_news");

            // Process and categorize articles
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);

                String title = article.getString("title");
                String description = article.optString("description", ""); // Avoid null issues
                String urlStr = article.getString("url");

                // Categorize the article
                String category;
                try {
                    category = categorizer.categorize(title + " " + description, categories);
                } catch (Exception e) {
                    System.err.println("Error categorizing article: " + title);
                    category = "Uncategorized"; // Fallback category
                }

                // Prepare the document
                Document newsDocument = new Document("title", title)
                        .append("description", description)
                        .append("url", urlStr)
                        .append("category", category)
                        .append("timestamp", java.time.Instant.now().toString())
                        .append("readCount", 0)
                        .append("likeCount", 0);

                // Upsert the document in the categorized_news collection
                categorizedNewsCollection.replaceOne(
                        new Document("url", urlStr),
                        newsDocument,
                        new ReplaceOptions().upsert(true)
                );
            }
        } catch (Exception e) {
            System.err.println("Error fetching or storing news: " + e.getMessage());
        }
    }
}
