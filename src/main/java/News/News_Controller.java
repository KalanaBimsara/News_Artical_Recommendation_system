package News;

import com.example.cw_ood.AdminControl;
import com.example.cw_ood.AdminUserControl;
import com.example.cw_ood.UserService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TableView<News> Discover_panel;
    @FXML
    public TableColumn<News, String> headline_discover;
    @FXML
    public TableColumn<News, String> desc_discover;
    @FXML
    public TableColumn<News, String> learn_more;
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
    public TableColumn <News, String>Cat_column_admin;
    @FXML
    public TableColumn<News, String> headline_admin;
    @FXML
    public TableColumn<News, String> desc_admin;
    @FXML
    public TableColumn <News, String>learn_more_admin;
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
    public TableColumn<News, Void> delete_news;
    @FXML
    public Button submit_button;
    @FXML
    public Button clear_button;
    @FXML
    public TableColumn cat_like;
    @FXML
    public TableColumn cat_history;
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
    @FXML
    private TextArea title_field, description_field, url_field;


    public static MongoCollection<Document> categorized_newsCollection;
    private MongoCollection<Document> readHistoryCollection;
    private MongoCollection<Document> likedArticlesCollection;
    ObservableList<News> recommendations = FXCollections.observableArrayList();

    public void setDatabase(MongoDatabase database) {
        this.database = database;
        this.categorized_newsCollection = database.getCollection("categorized_news");
        this.readHistoryCollection = database.getCollection("ReadHistory");
        this.likedArticlesCollection = database.getCollection("LikedArticles");
        fetchNewsFromDatabase();
    }

    private String currentUser;
    private UserService userService;
    private MongoDatabase database;
    private AdminControl adminControl;
    private ObservableList<News> likedList;
    private boolean isAdminMode = false;

    public void setAdminControl(AdminControl adminControl) {
        this.adminControl = adminControl;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public News_Controller() {
        this.userService = null;
    }
//login and panel showing functions-------------------------------------------------------------------------------
    public void setUserMode(ActionEvent actionEvent) {
        isAdminMode = false;
        Admin_button.setStyle("-fx-background-color: #3c3f41;");
        user_button.setStyle("-fx-background-color: #272727;");
        signUp.setVisible(true);
        forgot_password.setVisible(true);
    }

    public void setAdminMode(ActionEvent actionEvent) {
        isAdminMode = true;
        Admin_button.setStyle("-fx-background-color: #272727;");
        user_button.setStyle("-fx-background-color: #3c3f41;");
        signUp.setVisible(false);
        forgot_password.setVisible(false);
    }

    public void Login_to_system(ActionEvent actionEvent) {
        String username = Login_username_field.getText();
        String password = login_password_field.getText();

        if (isAdminMode) {
            // Admin
            if (userService.validateAdminLogin(username, password)) {
                currentUser = username;
                admin_dashboard.setVisible(true);
                Log_in_panel.setVisible(false);
                Login_field.setVisible(false);
                HI_admin.setText("Hi, " + username);
                loadNewsTable();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Admin Login Successful");
                alert.setHeaderText("Welcome, Admin " + username + "!");
                alert.showAndWait();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Admin Credentials", "Please check your username and password.");
            }
        } else {
            // User
            if (userService.validateLogin(username, password)) {
                currentUser = username;
                User_dashboard.setVisible(true);
                Log_in_panel.setVisible(false);
                Login_field.setVisible(false);
                HI_user.setText("Hi, " + username);
                Discover_panel.getItems().clear();
                fetchNewsFromDatabase();
                addActionButtons(Discover_panel, currentUser);

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
        fetchNewsFromDatabase();

    }
    @FXML
    public void Show_recommendations(ActionEvent actionEvent) {
        if (database == null) {
            throw new IllegalStateException("Database is not initialized!");
        }

        recommendations_table.setVisible(true);
        Discover_panel.setVisible(false);
        history_table.setVisible(false);
        like_table.setVisible(false);

        recommendations.clear();

        RecommendationEngine recommendationEngine = new RecommendationEngine(database);

        // Create the async for fetching recommendations
        Task<List<Document>> recommendationTask = recommendationEngine.recommendArticlesAsync(currentUser, 10);

        // Set up the handler when the task is completed
        recommendationTask.setOnSucceeded(event -> {
            List<Document> recommendedDocs = recommendationTask.getValue();

            // Process the recommended documents and update the UI
            for (Document doc : recommendedDocs) {
                String category = doc.getString("category");
                String title = doc.getString("title");
                String description = doc.getString("description");
                String url = doc.getString("url");

                recommendations.add(new News(category, title, description, url));
            }

            recommendations_table.setItems(recommendations);
            recommendations_cat.setCellValueFactory(new PropertyValueFactory<>("category"));
            recommendations_headline.setCellValueFactory(new PropertyValueFactory<>("title"));
            recommendations_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            recommendations_learnmore.setCellValueFactory(new PropertyValueFactory<>("url"));

            // Add action buttons
            addActionButtons(recommendations_table, currentUser);
        });

        // handle errors
        recommendationTask.setOnFailed(event -> {
            System.err.println("Recommendation task failed: " + recommendationTask.getException().getMessage());
        });

        // Start the task in the background
        new Thread(recommendationTask).start();
    }




    //admin panel
    @FXML
    public void showAdminNews(ActionEvent actionEvent) {
        News_Admin.setVisible(true);
        user_control_table.setVisible(false);
        add_article_panel.setVisible(false);
        loadNewsTable();
        News_Admin.refresh();
    }

    @FXML
    public void show_user_control_table(ActionEvent actionEvent) {
        News_Admin.setVisible(false);
        user_control_table.setVisible(true);
        add_article_panel.setVisible(false);
        loadUserControlTable();
        user_control_table.refresh();
    }

    @FXML
    public void show_add_article_panel(ActionEvent actionEvent) {
        News_Admin.setVisible(false);
        user_control_table.setVisible(false);
        add_article_panel.setVisible(true);
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

    // show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Reset Password
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
//login and panel showing functions-------------------------------------------------------------------------------

    //load news from db and showing in tables------------------------------------------------------------------------
    @FXML
    public void initialize() {
        headline_discover.setCellValueFactory(new PropertyValueFactory<>("title"));
        desc_discover.setCellValueFactory(new PropertyValueFactory<>("description"));
        learn_more.setCellValueFactory(new PropertyValueFactory<>("url"));
        Cat_column.setCellValueFactory(new PropertyValueFactory<>("category"));


        headline_history.setCellValueFactory(new PropertyValueFactory<>("title"));
        description_history.setCellValueFactory(new PropertyValueFactory<>("description"));
        learnmore_history.setCellValueFactory(new PropertyValueFactory<>("url"));
        cat_history.setCellValueFactory(new PropertyValueFactory<>("category"));


        // Initialize liked table columns
        like_table.setItems(likedList);
        headline_like.setCellValueFactory(new PropertyValueFactory<>("title"));
        desciption_like.setCellValueFactory(new PropertyValueFactory<>("description"));
        learnmore_like.setCellValueFactory(new PropertyValueFactory<>("url"));
        cat_like.setCellValueFactory(new PropertyValueFactory<>("category"));

        if (currentUser != null) {
            addActionButtons(Discover_panel, currentUser);
            loadHistoryTable();
            loadLikedTable();
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
        recommendations_learnmore.setCellFactory(tc -> new TableCell<>() {
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

        recommendations_headline.setCellFactory(tc -> new TableCell<>() {
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
        recommendations_description.setCellFactory(tc -> new TableCell<>() {
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
            throw new IllegalStateException("categorized_newsCollection is not initialized!");
        }

        ObservableList<News> newsList = FXCollections.observableArrayList();

        // Iterate over the documents in the database collection
        for (Document doc : categorized_newsCollection.find()) {
            String category = doc.getString("category");
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");

            News news = new News(category, title, description, url);


            newsList.add(news);
        }

        Discover_panel.setItems(newsList);
    }

    private final Set<String> readArticles = new HashSet<>();
    private final Set<String> likedArticles = new HashSet<>();


    //adding buttons to discover panel-----------------------------------------------------------------------------
    private Button createReadButton(String currentUser, News news) {
        Button readButton = new Button();

        // Check if the article is already marked as read
        boolean isRead = readArticles.contains(news.getUrl()); // Use URL or a unique identifier


        readButton.setText(isRead ? "Read" : "Mark as Read");
        readButton.setStyle(isRead ? "-fx-background-color: green;" : "-fx-background-color: gray;");
        readButton.setDisable(isRead);

        readButton.setOnAction(event -> {
            if (!isRead) {
                markAsRead(currentUser, news);
                readArticles.add(news.getUrl());
                readButton.setText("Read");
                readButton.setStyle("-fx-background-color: green;");
                readButton.setDisable(true);
            }
        });

        return readButton;
    }

    private Button createLikeButton(String currentUser, News news) {
        Button likeButton = new Button();

        // Check if the article is already liked
        boolean isLiked = likedArticles.contains(news.getUrl());


        likeButton.setText(isLiked ? "Unlike" : "Like");
        likeButton.setStyle(isLiked ? "-fx-background-color: red;" : "-fx-background-color: gray;");

        likeButton.setOnAction(event -> {
            if (isLiked) {
                unlikeArticle(currentUser, news);
                likedArticles.remove(news.getUrl());
                likeButton.setText("Like");
                likeButton.setStyle("-fx-background-color: gray;");
            } else {
                likeArticle(currentUser, news);
                likedArticles.add(news.getUrl());
                likeButton.setText("Unlike");
                likeButton.setStyle("-fx-background-color: red;");
            }
        });

        return likeButton;
    }

    private void addActionButtons(TableView<News> table, String currentUser) {
        boolean hasReadColumn = table.getColumns().stream().anyMatch(column -> "Read".equals(column.getText()));
        if (!hasReadColumn) {
            TableColumn<News, Void> readColumn = new TableColumn<>("Read");
            readColumn.setCellFactory(tc -> new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        News news = getTableRow().getItem();
                        Button readButton = createReadButton(currentUser, news);
                        setGraphic(readButton);
                    }
                }
            });
            table.getColumns().add(readColumn);
        }

        boolean hasLikeColumn = table.getColumns().stream().anyMatch(column -> "Like".equals(column.getText()));
        if (!hasLikeColumn) {
            TableColumn<News, Void> likeColumn = new TableColumn<>("Like");
            likeColumn.setCellFactory(tc -> new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setGraphic(null);
                    } else {
                        News news = getTableRow().getItem();
                        Button likeButton = createLikeButton(currentUser, news);
                        setGraphic(likeButton);
                    }
                }
            });
            table.getColumns().add(likeColumn);
        }
    }

    // Mark article as read - working-----------------------------------------------------------------------------
    private void markAsRead(String username, News news) {
        // Fetch the user record or create a new one
        Document userRecord = readHistoryCollection.find(new Document("username", username)).first();
        if (userRecord == null) {
            userRecord = new Document("username", username).append("readArticles", new ArrayList<>());
        }

        List<Document> readArticles = userRecord.getList("readArticles", Document.class, new ArrayList<>());

        // Avoid duplicates
        boolean alreadyRead = readArticles.stream()
                .anyMatch(article -> article.getString("url").equals(news.getUrl()));

        if (!alreadyRead) {
            readArticles.add(new Document("title", news.getTitle())
                    .append("description", news.getDescription())
                    .append("url", news.getUrl())
                    .append("category", news.getCategory())
                    .append("timestamp", java.time.Instant.now().toString()));
        }

        // Update the database
        userRecord.put("readArticles", readArticles);
        readHistoryCollection.replaceOne(new Document("username", username), userRecord, new ReplaceOptions().upsert(true));
    }

    private void loadHistoryTable() {
        ObservableList<News> historyList = FXCollections.observableArrayList();
        List<Document> readArticles = getReadArticlesFromDatabase(currentUser);

        for (Document doc : readArticles) {
            String category = doc.getString("category"); // Fetch category first
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");


            if (category == null) {
                category = "Unknown";
            }
            historyList.add(new News(category, title, description, url));
        }
        history_table.setItems(historyList);
    }

    private List<Document> getReadArticlesFromDatabase(String username) {
        Document userRecord = readHistoryCollection.find(new Document("username", username)).first();
        if (userRecord == null) return Collections.emptyList();
        return userRecord.getList("readArticles", Document.class);
    }

//like table function---------------------------------------------------------------
    private void likeArticle(String username, News news) {
        Document userRecord = likedArticlesCollection.find(new Document("username", username)).first();
        if (userRecord == null) {
         userRecord = new Document("username", username).append("likedArticles", new ArrayList<>());
     }

        List<Document> likedArticles = userRecord.getList("likedArticles", Document.class, new ArrayList<>());

        // Check for duplicates
        boolean alreadyLiked = likedArticles.stream()
                .anyMatch(article -> article.getString("url").equals(news.getUrl()));

        if (!alreadyLiked) {
            likedArticles.add(new Document("title", news.getTitle())
                    .append("description", news.getDescription())
                    .append("url", news.getUrl())
                    .append("category", news.getCategory())
                    .append("timestamp", java.time.Instant.now().toString()));
        }

        userRecord.put("likedArticles", likedArticles);
        likedArticlesCollection.replaceOne(new Document("username", username), userRecord, new ReplaceOptions().upsert(true));
    }

    private void unlikeArticle(String username, News news) {
        Document userRecord = likedArticlesCollection.find(new Document("username", username)).first();
        if (userRecord == null) return;

        List<Document> likedArticles = userRecord.getList("likedArticles", Document.class, new ArrayList<>());

        likedArticles.removeIf(article -> article.getString("url").equals(news.getUrl()));

        userRecord.put("likedArticles", likedArticles); // Replace with updated list
        likedArticlesCollection.replaceOne(new Document("username", username), userRecord);
    }

    @FXML
    private void loadLikedTable() {
        ObservableList<News> likedList = FXCollections.observableArrayList();
        List<Document> likedArticles = getLikedArticlesFromDatabase(currentUser);

        for (Document doc : likedArticles) {
            String category = doc.getString("category");
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");

            if (category == null) {
                category = "Unknown";
            }

            likedList.add(new News(category, title, description, url));
        }
        like_table.setItems(likedList);
    }

    private List<Document> getLikedArticlesFromDatabase(String username) {
        Document userRecord = likedArticlesCollection.find(new Document("username", username)).first();
        if (userRecord == null) return Collections.emptyList();
        return userRecord.getList("likedArticles", Document.class);
    }


    //------------------------------------------------------------------------------------------------------------------------
    public class RecommendationEngine {
        private final MongoDatabase database;

        public RecommendationEngine(MongoDatabase database) {
            this.database = database;
        }

        // fetch recommendations asynchronously
        public Task<List<Document>> recommendArticlesAsync(String username, int limit) {
            return new Task<List<Document>>() {
                @Override
                protected List<Document> call() throws Exception {
                    return recommendArticles(username, limit); // Perform the recommendations logic in background
                }
            };
        }

        public List<Document> recommendArticles(String username, int limit) {
            // Fetch liked and read articles
            List<Document> likedArticles = getLikedArticlesFromDatabase(username);
            List<Document> readArticles = getReadArticlesFromDatabase(username);

            // Combine liked and read articles
            List<Document> allArticles = new ArrayList<>();
            if (likedArticles != null) {
                allArticles.addAll(likedArticles);
            }
            if (readArticles != null) {
                allArticles.addAll(readArticles);
            }

            // Ensure no duplication
            Map<String, Document> uniqueArticles = allArticles.stream()
                    .collect(Collectors.toMap(
                            article -> article.getString("url"), // Key: URL
                            article -> article,                 // Value: Article Document
                            (existing, replacement) -> existing // Resolve conflicts by keeping the existing article
                    ));


            Map<String, Long> categoryCounts = uniqueArticles.values().stream()
                    .filter(doc -> doc.getString("category") != null)
                    .collect(Collectors.groupingBy(doc -> doc.getString("category"), Collectors.counting()));


            List<String> topCategories = categoryCounts.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // Sort descending by count
                    .limit(3) // Limit to top 3 categories
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());


            List<Document> recommendations = new ArrayList<>();
            for (String category : topCategories) {
                database.getCollection("categorized_news")
                        .find(new Document("category", category))
                        .limit(limit)
                        .into(recommendations);
            }
            return recommendations;
        }

        private List<Document> getLikedArticlesFromDatabase(String username) {
            Document userRecord = database.getCollection("LikedArticles")
                    .find(new Document("username", username))
                    .first();
            return userRecord != null ? userRecord.getList("likedArticles", Document.class) : Collections.emptyList();
        }

        private List<Document> getReadArticlesFromDatabase(String username) {
            Document userRecord = database.getCollection("ReadHistory")
                    .find(new Document("username", username))
                    .first();
            return userRecord != null ? userRecord.getList("readArticles", Document.class) : Collections.emptyList();
        }
    }

    //admin functions-----------------------------------------------------------------------------------------------------
    @FXML
    public void loadNewsTable() {
        if (adminControl == null) {
            System.err.println("AdminControl is not initialized!");
            return;
        }

        // Fetch news
        List<News> newsList = adminControl.fetchAllNews();
        ObservableList<News> observableList = FXCollections.observableArrayList(newsList);


        Cat_column_admin.setCellValueFactory(new PropertyValueFactory<>("category"));
        headline_admin.setCellValueFactory(new PropertyValueFactory<>("title"));
        desc_admin.setCellValueFactory(new PropertyValueFactory<>("description"));
        learn_more_admin.setCellValueFactory(new PropertyValueFactory<>("url"));

        // Add a Delete button to each row
        delete_news.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void unused, boolean empty) {
                super.updateItem(unused, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    News news = getTableRow().getItem();
                    deleteButton.setOnAction(event -> {
                        boolean isDeleted = adminControl.deleteNews(news.getId());
                        if (isDeleted) {
                            getTableView().getItems().remove(news);
                        } else {
                            System.err.println("Failed to delete news!");
                        }
                    });
                    setGraphic(deleteButton);
                }
            }
        });

        News_Admin.setItems(observableList);
    }

    private void loadUserControlTable() {
        ObservableList<AdminUserControl> usersList = FXCollections.observableArrayList();

        // Fetch users from database
        MongoCollection<Document> usersCollection = database.getCollection("Users");
        for (Document userDoc : usersCollection.find()) {
            String username = userDoc.getString("username");
            String userHistory = "Summary or Stats"; // Placeholder, replace with actual history

            // Create delete buttons
            Button deleteButton = new Button("Delete");

            deleteButton.setOnAction(event -> deleteUser(username));

            // Add user to the list
            usersList.add(new AdminUserControl(username, deleteButton));
        }


        user_control_table.setItems(usersList);
        user_name_admin.setCellValueFactory(new PropertyValueFactory<>("username"));
        delete_user.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
    }

    private void deleteUser(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete user: " + username + "?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Remove the user from the database
            database.getCollection("Users").deleteOne(new Document("username", username));
            loadUserControlTable(); // Refresh the table
            showAlert(Alert.AlertType.INFORMATION, "User Deleted", "The user '" + username + "' has been deleted.");
        }
    }

    @FXML
    private void handleSubmitArticle(ActionEvent actionEvent) {
        String title = title_field.getText().trim();
        String description = description_field.getText().trim();
        String url = url_field.getText().trim();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || url.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields are required.");
            return;
        }

        // Define categories
        String[] categories = {"Sports", "Technology", "Health", "AI", "Business", "Entertainment", "General", "Science", "Politics"};

        // Use the NewsCategorizer class asynchronously
        NewsCategorizer.categorizeAsync(title + " " + description, categories).setOnSucceeded(event -> {
            String category = NewsCategorizer.categorizeAsync(title + " " + description, categories).getValue(); // Get the category from the background task

            // Prepare the article document
            Document article = new Document("title", title)
                    .append("description", description)
                    .append("url", url)
                    .append("category", category) // Add category here
                    .append("timestamp", java.time.Instant.now().toString())
                    .append("readCount", 0)
                    .append("likeCount", 0);

            // Insert into database
            try {
                categorized_newsCollection.insertOne(article);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Article added successfully!");
                clearFields(); // Clear fields after successful submission
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add the article. Please try again.");
            }
        });
    }


    @FXML
    private void handleClearFields(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        title_field.clear();
        description_field.clear();
        url_field.clear();
    }

}