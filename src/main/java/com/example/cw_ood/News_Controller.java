package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.bson.Document;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

import java.util.*;
import java.util.stream.Collectors;


public class News_Controller {
    @FXML
    public Button Admin_button;
    @FXML
    public Button user_button;
    @FXML
    public Hyperlink forgot_password;
    @FXML
    public Hyperlink signUp;
    @FXML
    public AnchorPane nav_panel;
    @FXML
    public AnchorPane side_panel;
    @FXML
    public AnchorPane main_panel;
    @FXML
    public AnchorPane Log_in_panel;
    @FXML
    public AnchorPane User_dashboard;
    @FXML
    public PasswordField Siignup_password_field;
    @FXML
    public AnchorPane Sign_up_panel;
    @FXML
    public TextField Signup_username_field;
    @FXML
    public PasswordField Signup_password_field;
    @FXML
    public Button Signup_button;
    @FXML
    public AnchorPane Password_reset;
    @FXML
    public TextField reset_username_field;
    @FXML
    public PasswordField reset_password_field;
    @FXML
    public Button Reset_Button;
    @FXML
    public AnchorPane Login_field;
    @FXML
    public Hyperlink Back_to_login;
    @FXML
    public Hyperlink back_to_login;
    @FXML
    public TextField Login_username_field;
    @FXML
    public PasswordField login_password_field;
    @FXML
    public Button login_button;
    @FXML
    public TableView history_table;
    @FXML
    public TableView like_table;
    @FXML
    public TableColumn headline_history;
    @FXML
    public TableColumn description_history;
    @FXML
    public TableColumn learnmore_history;
    @FXML
    public TableColumn headline_like;
    @FXML
    public TableColumn desciption_like;
    @FXML
    public TableColumn learnmore_like;
    @FXML
    public Button home_button;
    @FXML
    public Button liked_button;
    @FXML
    public Button history_button;
    @FXML
    public Label HI_admin;
    @FXML
    public Label HI_user;
    @FXML
    public TableView<News> Discover_panel; // TableView from FXML
    @FXML
    public TableColumn<News, String> headline_discover; // TableColumn for title
    @FXML
    public TableColumn<News, String> desc_discover; // TableColumn for description
    @FXML
    public TableColumn<News, String> learn_more;    // TableColumn for learn more
    @FXML
    public TableColumn<News, String> Cat_column;
    @FXML
    public AnchorPane admin_dashboard;
    @FXML
    public Button Dashboard_button;
    @FXML
    public Button user_control;
    @FXML
    public Button add_article;
    @FXML
    public TableView News_Admin;
    @FXML
    public TableColumn Cat_column_admin;
    @FXML
    public TableColumn headline_admin;
    @FXML
    public TableColumn desc_admin;
    @FXML
    public TableColumn learn_more_admin;
    @FXML
    public TableView user_control_table;
    @FXML
    public TableColumn user_name_admin;
    @FXML
    public TableColumn user_history_admin;
    @FXML
    public TableColumn delete_user;
    @FXML
    public TableColumn password_reset;
    @FXML
    public Button logout_button1;
    @FXML
    public AnchorPane add_article_panel;
    @FXML
    private TableColumn<News, Void> readColumn;
    @FXML
    private TableColumn<News, Void> likeColumn;
    @FXML
    public TableView<News> recommendations_table;
    @FXML
    public TableColumn<News, String> recommendations_headline;
    @FXML
    public TableColumn<News, String> recommendations_description;
    @FXML
    public TableColumn<News, String> recommendations_learnmore;
    @FXML
    public TableColumn<News, String> recommendations_cat;


    private MongoCollection<Document> categorized_newsCollection;
    private MongoCollection<Document> readHistoryCollection;
    private MongoCollection<Document> likedArticlesCollection;

    private String currentUser;
    private UserService userService;
    private MongoDatabase database;

    public News_Controller() {
        // Initialize optional defaults if necessary
        this.userService = null; // Will be set later by setter
    }

    private boolean isAdminMode = false; // Tracks if Admin mode is active

    public void setUserMode(ActionEvent actionEvent) {
        isAdminMode = false; // Set to User mode
        Admin_button.setStyle("-fx-background-color: #3c3f41;");
        user_button.setStyle("-fx-background-color: #272727;"); // Highlight active button
    }

    public void setAdminMode(ActionEvent actionEvent) {
        isAdminMode = true; // Set to Admin mode
        Admin_button.setStyle("-fx-background-color: #272727;");
        user_button.setStyle("-fx-background-color: #3c3f41;"); // Highlight active button
    }


    public void Login_to_system(ActionEvent actionEvent) {
        String username = Login_username_field.getText();
        String password = login_password_field.getText();

        if (isAdminMode) {
            // Admin Login Logic
            if (userService.validateAdminLogin(username, password)) {
                currentUser = username; // Set current admin
                admin_dashboard.setVisible(true); // Show admin dashboard
                Log_in_panel.setVisible(false);
                Login_field.setVisible(false);
                HI_admin.setText("Hi, " + username);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Admin Login Successful");
                alert.setHeaderText("Welcome, Admin " + username + "!");
                alert.showAndWait();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Admin Credentials", "Please check your username and password.");
            }
        } else {
            // User Login Logic
            if (userService.validateLogin(username, password)) {
                currentUser = username; // Set current user
                User_dashboard.setVisible(true); // Show user dashboard
                Log_in_panel.setVisible(false);
                Login_field.setVisible(false);
                HI_user.setText("Hi, " + username);
                Discover_panel.getItems().clear();
                fetchNewsFromDatabase(); // Load personalized news
                addActionButtons(currentUser); // Add buttons

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText("Welcome, " + username + "!");
                alert.showAndWait();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid User Credentials", "Please check your username and password.");
            }
        }
    }




    public void SignUp(ActionEvent actionEvent) {
        Login_field.setVisible(false);
        Password_reset.setVisible(false);
        Sign_up_panel.setVisible(true);
    }
    public void Reset_password(ActionEvent actionEvent) {
        Login_field.setVisible(false);
        Sign_up_panel.setVisible(false);
        Password_reset.setVisible(true);
    }
    public void Back_to_login(ActionEvent actionEvent) {
        Login_field.setVisible(true);
        Sign_up_panel.setVisible(false);
        Password_reset.setVisible(false);
    }
    public void log_out(ActionEvent actionEvent) {
        Log_in_panel.setVisible(true);
        Login_field.setVisible(true);
        User_dashboard.setVisible(false);
        admin_dashboard.setVisible(false);
    }
    public void Show_history(ActionEvent actionEvent) {
        history_table.setVisible(true);
        Discover_panel.setVisible(false);
        like_table.setVisible(false);
        recommendations_table.setVisible(false);
        loadHistoryTable();
    }
    public void Show_liked(ActionEvent actionEvent) {
        history_table.setVisible(false);
        Discover_panel.setVisible(false);
        like_table.setVisible(true);
        recommendations_table.setVisible(false);
        loadLikedTable();
    }
    public void Show_discover(ActionEvent actionEvent) {
        history_table.setVisible(false);
        Discover_panel.setVisible(true);
        like_table.setVisible(false);
        recommendations_table.setVisible(false);

    }
    public void Show_recommendations(ActionEvent actionEvent) {
        if (database == null) {
            throw new IllegalStateException("Database is not initialized!");
        }

        recommendations_table.setVisible(true);
        Discover_panel.setVisible(false);
        history_table.setVisible(false);
        like_table.setVisible(false);

        RecommendationEngine recommendationEngine = new RecommendationEngine(database);
        List<Document> recommendedDocs = recommendationEngine.recommendArticles(currentUser, 10);

        // Debugging: Log the recommended documents
        System.out.println("Recommended Documents: " + recommendedDocs);

        ObservableList<News> recommendations = FXCollections.observableArrayList();
        for (Document doc : recommendedDocs) {
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");
            String category = doc.getString("category");
            recommendations.add(new News(title, description, url, category));
        }

        recommendations_table.setItems(recommendations);
        recommendations_headline.setCellValueFactory(new PropertyValueFactory<>("title"));
        recommendations_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        recommendations_learnmore.setCellValueFactory(new PropertyValueFactory<>("url"));
        recommendations_cat.setCellValueFactory(new PropertyValueFactory<>("category"));
    }



    @FXML
    public void handleSignUp(ActionEvent actionEvent) {
        String username = Signup_username_field.getText();
        String password = Signup_password_field.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Username and password cannot be empty.");
            return;
        }

        if (userService.createUser(username, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Sign-Up Successful", "Account created successfully!");
            Signup_username_field.clear();
            Signup_password_field.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Sign-Up Failed", "Username already exists. Please choose a different one.");
        }
    }

    // Helper to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // Reset Password Handler
    @FXML
    public void handlePasswordReset(ActionEvent actionEvent) {
        String username = reset_username_field.getText();
        String newPassword = reset_password_field.getText();

        if (username.isEmpty() || newPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Username and password cannot be empty.");
            return;
        }

        if (userService.resetPassword(username, newPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "Reset Successful", "Password reset successfully!");
            reset_username_field.clear();
            reset_password_field.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Reset Failed", "Username not found. Please try again.");
        }
    }



    //load news from db
    @FXML
    public void initialize() {
        headline_discover.setCellValueFactory(new PropertyValueFactory<>("title"));
        desc_discover.setCellValueFactory(new PropertyValueFactory<>("description"));
        learn_more.setCellValueFactory(new PropertyValueFactory<>("url"));
        Cat_column.setCellValueFactory(new PropertyValueFactory<>("category"));


        // Initialize history table columns
        headline_history.setCellValueFactory(new PropertyValueFactory<>("title"));
        description_history.setCellValueFactory(new PropertyValueFactory<>("description"));
        learnmore_history.setCellValueFactory(new PropertyValueFactory<>("url"));

        // Initialize liked table columns
        headline_like.setCellValueFactory(new PropertyValueFactory<>("title"));
        desciption_like.setCellValueFactory(new PropertyValueFactory<>("description"));
        learnmore_like.setCellValueFactory(new PropertyValueFactory<>("url"));


        // Call addActionButtons with current user & Load user-specific history and liked articles
        if (currentUser != null) {
            addActionButtons(currentUser);
            loadHistoryTable();
            loadLikedTable();
        }

        // Ensure columns are added to the TableView
        if (!Discover_panel.getColumns().contains(readColumn)) {
            Discover_panel.getColumns().add(readColumn);
        }
        if (!Discover_panel.getColumns().contains(likeColumn)) {
            Discover_panel.getColumns().add(likeColumn);
        }

        learn_more.setCellFactory(tc -> new TableCell<>() {
            private final Hyperlink link = new Hyperlink();

            {
                link.setOnAction(event -> {
                    String url = link.getText();
                    if (url != null && !url.isEmpty()) {
                        try {
                            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                setGraphic(link);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    link.setText(null);
                    setGraphic(null);
                } else {
                    link.setText(item);
                    setGraphic(link);
                }
            }
        });

        // Enable text wrapping for the description column
        desc_discover.setCellFactory(tc -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                setGraphic(text);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText(null);
                } else {
                    text.setText(item);
                }
            }
        });

        headline_discover.setCellFactory(tc -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                setGraphic(text);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText(null);
                } else {
                    text.setText(item);
                }
            }
        });
    }


    private void fetchNewsFromDatabase() {
        if (categorized_newsCollection == null) {
            throw new IllegalStateException("newsCollection is not initialized!");
        }

        ObservableList<News> newsList = FXCollections.observableArrayList();

        for (Document doc : categorized_newsCollection.find()) {
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");
            String category = doc.getString("category"); // Fetch the category field from the document

            News news = new News(title, description, url, category); // Pass the category to the News object

            newsList.add(news);
        }

        Discover_panel.setItems(newsList);
    }




    //add button

    private void addActionButtons(String currentUser) {
        // "Mark as Read" Button Column
        readColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button readButton = new Button("Mark as Read");

            {
                readButton.setOnAction(event -> {
                    News news = getTableView().getItems().get(getIndex());
                    markAsRead(currentUser, news); // Save to ReadHistory collection
                    readButton.setStyle("-fx-background-color: green;");
                    readButton.setDisable(true); // Disable once marked as read
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(readButton);
                }
            }
        });


        // "Like" Button Column
        likeColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button likeButton = new Button("Like");

            {
                likeButton.setOnAction(event -> {
                    News news = getTableView().getItems().get(getIndex());
                    toggleLike(currentUser, news); // Save or remove from LikedArticles collection
                    boolean isLiked = likeButton.getText().equals("Unlike");
                    likeButton.setText(isLiked ? "Like" : "Unlike");
                    likeButton.setStyle(isLiked ? "-fx-background-color: gray;" : "-fx-background-color: red;");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(likeButton);
                }
            }
        });


    }



    public void setDatabase(MongoDatabase database) {
        this.database = database;
        this.categorized_newsCollection = database.getCollection("categorized_news");
        this.readHistoryCollection = database.getCollection("ReadHistory");
        this.likedArticlesCollection = database.getCollection("LikedArticles");
        fetchNewsFromDatabase(); // Initialize news collection
        System.out.println("newsCollection initialized: " + (this.categorized_newsCollection != null));
    }

    // Mark article as read
    private void markAsRead(String username, News news) {
        Document userRecord = readHistoryCollection.find(new Document("username", username)).first();
        if (userRecord == null) {
            userRecord = new Document("username", username).append("readArticles", new ArrayList<>());
        }

        // Avoid duplicates
        List<Document> readArticles = userRecord.getList("readArticles", Document.class);
        if (readArticles.stream().noneMatch(article -> article.getString("url").equals(news.getUrl()))) {
            readArticles.add(new Document("title", news.getTitle())
                    .append("description", news.getDescription())
                    .append("url", news.getUrl())
                    .append("timestamp", java.time.Instant.now().toString()));
        }

        // Update the database
        readHistoryCollection.replaceOne(new Document("username", username), userRecord, new ReplaceOptions().upsert(true));
    }

    // Mark or unmark article as liked
    private void toggleLike(String username, News news) {
        Document userRecord = likedArticlesCollection.find(new Document("username", username)).first();
        if (userRecord == null) {
            userRecord = new Document("username", username).append("likedArticles", new ArrayList<>());
        }

        // Check if already liked
        List<Document> likedArticles = userRecord.getList("likedArticles", Document.class);
        Optional<Document> existingArticle = likedArticles.stream()
                .filter(article -> article.getString("url").equals(news.getUrl()))
                .findFirst();

        if (existingArticle.isPresent()) {
            // Unlike: Remove the article
            likedArticles.remove(existingArticle.get());
        } else {
            // Like: Add the article with category
            likedArticles.add(new Document("title", news.getTitle())
                    .append("description", news.getDescription())
                    .append("url", news.getUrl())
                    .append("category", news.getCategory()) // Ensure category is added
                    .append("timestamp", java.time.Instant.now().toString()));
        }

        // Update the database
        likedArticlesCollection.replaceOne(new Document("username", username), userRecord, new ReplaceOptions().upsert(true));
    }



    private void loadHistoryTable() {
        ObservableList<News> historyList = FXCollections.observableArrayList();
        List<Document> readArticles = getReadArticlesFromDatabase(currentUser);

        for (Document doc : readArticles) {
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");
            String category = doc.getString("category"); // Optional: Use a default if category is not available
            if (category == null) {
                category = "Unknown";
            }
            historyList.add(new News(title, description, url, category)); // Include category
        }

        history_table.setItems(historyList);
    }


    private void loadLikedTable() {
        ObservableList<News> likedList = FXCollections.observableArrayList();
        List<Document> likedArticles = getLikedArticlesFromDatabase(currentUser);

        for (Document doc : likedArticles) {
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");
            String category = doc.getString("category"); // Optional: Use a default if category is not available
            if (category == null) {
                category = "Unknown";
            }
            likedList.add(new News(title, description, url, category)); // Include category
        }

        like_table.setItems(likedList);
    }


    private List<Document> getReadArticlesFromDatabase(String username) {
        Document userRecord = readHistoryCollection.find(new Document("username", username)).first();
        if (userRecord == null) return Collections.emptyList();
        return userRecord.getList("readArticles", Document.class);
    }

    private List<Document> getLikedArticlesFromDatabase(String username) {
        Document userRecord = likedArticlesCollection.find(new Document("username", username)).first();
        if (userRecord == null || userRecord.getList("likedArticles", Document.class) == null) {
            return Collections.emptyList();
        }
        return userRecord.getList("likedArticles", Document.class);
    }







    public class RecommendationEngine {
        private final MongoDatabase database;

        public RecommendationEngine(MongoDatabase database) {
            this.database = database;
        }

        public List<Document> recommendArticles(String username, int limit) {
            List<Document> likedArticles = getLikedArticlesFromDatabase(username);

            // Debugging: Check liked articles
            System.out.println("Liked Articles for user " + username + ": " + likedArticles);
            System.out.println("Liked Articles: " + likedArticles);
            likedArticles.stream()
                    .filter(doc -> doc.getString("category") != null)
                    .forEach(doc -> System.out.println("Category: " + doc.getString("category")));


            // Ensure likedArticles is not null
            if (likedArticles == null || likedArticles.isEmpty()) {
                System.out.println("No liked articles found for user: " + username);
                return Collections.emptyList(); // Return an empty list or general recommendations
            }

            // Filter and collect categories
            Map<String, Long> categoryCounts = likedArticles.stream()
                    .filter(doc -> doc.getString("category") != null)
                    .collect(Collectors.groupingBy(doc -> doc.getString("category"), Collectors.counting()));

            // Debugging: Check category counts
            System.out.println("Category Counts: " + categoryCounts);

            // Get top categories
            List<String> topCategories = categoryCounts.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Debugging: Check top categories
            System.out.println("Top Categories: " + topCategories);

            // Fetch recommendations
            List<Document> recommendations = new ArrayList<>();
            for (String category : topCategories) {
                categorized_newsCollection.find(new Document("category", category))
                        .limit(limit)
                        .into(recommendations);
            }

            // Debugging: Check recommendations
            System.out.println("Recommendations: " + recommendations);

            return recommendations;
        }


        /*private List<Document> getArticlesFromCollection(MongoCollection<Document> collection, String username) {
            Document userRecord = collection.find(new Document("username", username)).first();
            if (userRecord == null) return Collections.emptyList();
            return userRecord.getList("readArticles", Document.class);
        }*/
    }

}