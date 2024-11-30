package com.example.cw_ood;

import javafx.scene.control.Button;

public class AdminUserControl {
    private String username; // Username of the user
    private String userHistory; // Placeholder for user history or summary
    private Button deleteButton; // Button for deleting the user
    private Button resetPasswordButton; // Button for resetting the user's password

    public AdminUserControl(String username, String userHistory, Button deleteButton, Button resetPasswordButton) {
        this.username = username;
        this.userHistory = userHistory;
        this.deleteButton = deleteButton;
        this.resetPasswordButton = resetPasswordButton;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserHistory() {
        return userHistory;
    }

    public void setUserHistory(String userHistory) {
        this.userHistory = userHistory;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Button getResetPasswordButton() {
        return resetPasswordButton;
    }

    public void setResetPasswordButton(Button resetPasswordButton) {
        this.resetPasswordButton = resetPasswordButton;
    }
}

