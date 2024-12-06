package com.example.cw_ood;

import javafx.scene.control.Button;

public class AdminUserControl {
    private String username;
    private String userHistory;
    private Button deleteButton;

    public AdminUserControl(String username, Button deleteButton) {
        this.username = username;
        this.deleteButton = deleteButton;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

}

