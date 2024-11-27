package com.example.cw_ood;

import java.io.*;
import java.net.*;
import org.json.*;

public class NewsCategorizer {
    public static String categorize(String text, String[] categories) throws IOException {
        URL url = new URL("https://api-inference.huggingface.co/models/facebook/bart-large-mnli");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer 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");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Prepare JSON payload
        JSONObject payload = new JSONObject();
        payload.put("inputs", text);
        payload.put("parameters", new JSONObject().put("candidate_labels", String.join(",", categories)));

        // Send payload
        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.toString().getBytes());
        }

        // Read response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse and return top category
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getJSONArray("labels").getString(0);
    }

    public static void main(String[] args) throws IOException {
        String article = "New AI technology revolutionizes healthcare by improving diagnostics.";
        String[] categories = {"Sports", "Technology", "Health", "AI", "Politics"};
        String category = categorize(article, categories);
        System.out.println("Predicted Category: " + category);
    }
}
