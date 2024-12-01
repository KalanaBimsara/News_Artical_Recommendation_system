package com.example.cw_ood;

import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.concurrent.Task;
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
            // Load the loading screen
            FXMLLoader loadingLoader = new FXMLLoader(getClass().getResource("/com/example/cw_ood/loading_screen.fxml"));
            Parent loadingRoot = loadingLoader.load();
            Scene loadingScene = new Scene(loadingRoot);

            // Set the loading screen
            stage.setScene(loadingScene);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/stage_icon.png"))));
            stage.setTitle("News Article Application");
            stage.show();

            // Create a background task to initialize the application
            Task<Void> initTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    // Simulate loading (optional)
                    Thread.sleep(2000);

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

                    // Pass database and services to main UI
                    FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/example/cw_ood/News_Article.fxml"));
                    Parent mainRoot = mainLoader.load();
                    News_Controller controller = mainLoader.getController();
                    controller.setUserService(userService);
                    controller.setDatabase(database);
                    controller.setAdminControl(adminControl);

                    // Switch to main UI after initialization
                    javafx.application.Platform.runLater(() -> {
                        Scene mainScene = new Scene(mainRoot);
                        stage.setScene(mainScene);
                    });

                    return null;
                }
            };

            // Handle initialization errors
            initTask.setOnFailed(event -> {
                System.err.println("Initialization failed: " + initTask.getException().getMessage());
            });

            // Start the background task
            new Thread(initTask).start();

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
