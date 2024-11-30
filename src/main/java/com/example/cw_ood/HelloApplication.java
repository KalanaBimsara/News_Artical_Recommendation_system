package com.example.cw_ood;

import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Create MongoDB connection and UserService
            MongoDatabase database = MongoDBConnection.getDatabase();
            UserService userService = new UserService(database);

            // Initialize AdminControl
            AdminControl adminControl = new AdminControl(database);


            // Fetch news with error handling
            NewsFetcher newsFetcher = new NewsFetcher(database);
            try {
                newsFetcher.fetchAndCategorizeNews();
            } catch (Exception e) {
                System.err.println("Failed to fetch and categorize news: " + e.getMessage());
            }

            // Load FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cw_ood/News_Article.fxml"));
            Parent root = fxmlLoader.load();



            // Inject dependencies into the controller
            News_Controller controller = fxmlLoader.getController();
            controller.setUserService(userService);
            controller.setDatabase(database);
            // Set AdminControl in News_Controller
            controller.setAdminControl(adminControl);

            // Set up the scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/stage_icon.png"))));
            stage.setTitle("News Article Application");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application failed to start: " + e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        MongoDBConnection.close(); // Clean up resources if needed
        System.out.println("Application closed gracefully.");
    }

    public static void main(String[] args) {
        launch();
    }
}

