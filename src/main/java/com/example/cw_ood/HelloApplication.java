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
    public void start(Stage stage) throws Exception {
        // Create MongoDB connection and UserService
        MongoDatabase database = MongoDBConnection.getDatabase();
        UserService userService = new UserService(database);


        // Fetch news
        NewsFetcher newsFetcher = new NewsFetcher(database);
        newsFetcher.fetchAndCategorizeNews();

        // Load FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/cw_ood/News_Article.fxml"));
        Parent root = fxmlLoader.load();

        // Get the controller and inject UserService
        News_Controller controller = fxmlLoader.getController();
        controller.setUserService(userService);
        // Initialize MongoDB in the controller
        controller.setDatabase(database);




        // Set the scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/stage_icon.png"))));
        stage.setTitle("News Article Application");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
