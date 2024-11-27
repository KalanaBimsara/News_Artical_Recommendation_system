package com.example.cw_ood;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

import java.util.Map;


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
    private Button logout_button;
    @FXML
    private ImageView logout_img;
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
    private TableColumn<News, Void> readColumn;
    @FXML
    private TableColumn<News, Void> likeColumn;

    private MongoCollection<Document> newsCollection;

    public void setDatabase(MongoDatabase database) {
        this.newsCollection = database.getCollection("News");
        fetchNewsFromDatabase(); // Call this method after initializing the collection
        System.out.println("newsCollection initialized: " + (this.newsCollection != null));

    }

    private UserService userService;


    public News_Controller() {
        // Initialize optional defaults if necessary
        this.userService = null; // Will be set later by setter
    }


    public void Login_to_system(ActionEvent actionEvent) {
        String username = Login_username_field.getText();
        String password = login_password_field.getText();
        System.out.println("Username: " + username + ", Password: " + password);

        if (userService.validateLogin(username, password)) {
            // Successful login
            Log_in_panel.setVisible(false);
            Login_field.setVisible(false);
            Sign_up_panel.setVisible(false);
            Password_reset.setVisible(false);
            User_dashboard.setVisible(true);
            HI_user.setText("Hi, " + username);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText("Welcome, " + username + "!");
            alert.showAndWait();
        } else {
            // Failed login
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid username or password");
            alert.showAndWait();
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
        // Set up TableColumn bindings
        headline_discover.setCellValueFactory(new PropertyValueFactory<>("title"));
        desc_discover.setCellValueFactory(new PropertyValueFactory<>("description"));
        learn_more.setCellValueFactory(new PropertyValueFactory<>("url"));

        // Add buttons for actions
        addActionButtons();

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
                text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10)); // Adjust for padding
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
                text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10)); // Adjust for padding
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
        if (newsCollection == null) {
            throw new IllegalStateException("newsCollection is not initialized!");
        }

        ObservableList<News> newsList = FXCollections.observableArrayList();

        for (Document doc : newsCollection.find()) {
            String title = doc.getString("title");
            String description = doc.getString("description");
            String url = doc.getString("url");
            Document readStatesDoc = doc.get("readStates", Document.class);
            Document likeStatesDoc = doc.get("likeStates", Document.class);

            News news = new News(title, description, url);

            // Populate user-specific states
            if (readStatesDoc != null) {
                for (String user : readStatesDoc.keySet()) {
                    news.setRead(user, readStatesDoc.getBoolean(user));
                }
            }

            if (likeStatesDoc != null) {
                for (String user : likeStatesDoc.keySet()) {
                    news.setLiked(user, likeStatesDoc.getBoolean(user));
                }
            }

            newsList.add(news);
        }

        Discover_panel.setItems(newsList);
    }



    //add button

    private void addActionButtons() {
        String currentUser = HI_user.getText().replace("Hi, ", "").trim(); // Get the current username

        // "Mark as Read" Button Column
        readColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button readButton = new Button("Mark as Read");

            {
                readButton.setOnAction(event -> {
                    News news = getTableView().getItems().get(getIndex());
                    news.setRead(currentUser, true);
                    updateNewsInDatabase(currentUser, news); // Update for current user in MongoDB
                    refreshTable(); // Refresh the table
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    News news = getTableView().getItems().get(getIndex());
                    setGraphic(readButton);
                    readButton.setDisable(news.isRead(currentUser));
                    readButton.setStyle(news.isRead(currentUser) ? "-fx-background-color: green;" : "");
                }
            }
        });

        // "Like" Button Column
        likeColumn.setCellFactory(tc -> new TableCell<>() {
            private final Button likeButton = new Button("Like");

            {
                likeButton.setOnAction(event -> {
                    News news = getTableView().getItems().get(getIndex());
                    news.setLiked(currentUser, !news.isLiked(currentUser));
                    updateNewsInDatabase(currentUser, news); // Update for current user in MongoDB
                    refreshTable(); // Refresh the table
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    News news = getTableView().getItems().get(getIndex());
                    setGraphic(likeButton);
                    likeButton.setText(news.isLiked(currentUser) ? "Unlike" : "Like");
                    likeButton.setStyle(news.isLiked(currentUser) ? "-fx-background-color: red;" : "");
                }
            }
        });
    }



    private void updateNewsInDatabase(String username, News news) {
        // Update the user-specific read/like states in MongoDB
        Document readStatesDoc = new Document();
        for (Map.Entry<String, Boolean> entry : news.getReadStates().entrySet()) {
            readStatesDoc.append(entry.getKey(), entry.getValue());
        }

        Document likeStatesDoc = new Document();
        for (Map.Entry<String, Boolean> entry : news.getLikeStates().entrySet()) {
            likeStatesDoc.append(entry.getKey(), entry.getValue());
        }

        Document updatedDocument = new Document("title", news.getTitle())
                .append("description", news.getDescription())
                .append("url", news.getUrl())
                .append("readStates", readStatesDoc)
                .append("likeStates", likeStatesDoc);

        newsCollection.replaceOne(new Document("title", news.getTitle()), updatedDocument);
    }


    private void refreshTable() {
        fetchNewsFromDatabase(); // Re-fetch data to update the TableView
    }



}