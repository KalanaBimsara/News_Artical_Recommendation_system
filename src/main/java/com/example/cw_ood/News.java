package com.example.cw_ood;

import java.util.HashMap;
import java.util.Map;

public class News {
    private String title;
    private String description;
    private String url;
    private Map<String, Boolean> readStates; // Stores isRead for each user
    private Map<String, Boolean> likeStates; // Stores isLiked for each user

    // Constructor
    public News(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.readStates = new HashMap<>();
        this.likeStates = new HashMap<>();
    }

    // Getters and Setters
    public boolean isRead(String username) {
        return readStates.getOrDefault(username, false);
    }

    public void setRead(String username, boolean isRead) {
        readStates.put(username, isRead);
    }

    public boolean isLiked(String username) {
        return likeStates.getOrDefault(username, false);
    }

    public void setLiked(String username, boolean isLiked) {
        likeStates.put(username, isLiked);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Boolean> getReadStates() {
        return readStates;
    }

    public Map<String, Boolean> getLikeStates() {
        return likeStates;
    }
}
