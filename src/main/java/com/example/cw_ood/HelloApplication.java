package com.example.cw_ood;

import News.NewsFetcher;
import News.News_Controller;
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
            // loading screen
            FXMLLoader loadingLoader = new FXMLLoader(getClass().getResource("/com/example/cw_ood/loading_screen.fxml"));
            Parent loadingRoot = loadingLoader.load();
            Scene loadingScene = new Scene(loadingRoot);

            // loading screen
            stage.setScene(loadingScene);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/stage_icon.png"))));
            stage.setTitle("News Article Application");
            stage.show();

            // Create a background task
            Task<Void> initTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    // Simulate loading
                    Thread.sleep(2000);

                    // Create MongoDB connection and UserService
                    MongoDatabase database = MongoDBConnection.getDatabase();
                    UserService userService = new UserService(database);

                    // Initialize AdminControl
                    AdminControl adminControl = new AdminControl(database);

                    // Fetch news
                    NewsFetcher newsFetcher = new NewsFetcher(database);
                    try {
                        newsFetcher.fetchAndCategorizeNews();
                    } catch (Exception e) {
                        System.err.println("Failed to fetch and categorize news: " + e.getMessage());
                    }

                    FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/example/cw_ood/News_Article.fxml"));
                    Parent mainRoot = mainLoader.load();
                    News_Controller controller = mainLoader.getController();
                    controller.setUserService(userService);
                    controller.setDatabase(database);
                    controller.setAdminControl(adminControl);


                    javafx.application.Platform.runLater(() -> {
                        Scene mainScene = new Scene(mainRoot);
                        stage.setScene(mainScene);
                    });

                    return null;
                }
            };

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
        MongoDBConnection.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
