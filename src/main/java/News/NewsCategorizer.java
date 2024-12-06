package News;

import java.io.*;
import java.net.*;
import org.json.*;
import javafx.concurrent.Task;

public class NewsCategorizer {
    private static final String API_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-mnli";
    private static final String API_TOKEN = "Bearer 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";

    // New method to categorize the news asynchronously
    public static Task<String> categorizeAsync(String text, String[] categories) {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                return categorize(text, categories); // Run the existing categorize logic in the background
            }
        };
    }

    public static String categorize(String text, String[] categories) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", API_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // make JSON payload
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

        } catch (Exception e) {
            System.err.println("Categorization API call failed: " + e.getMessage());
            return "general"; // Default category in case of error
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
