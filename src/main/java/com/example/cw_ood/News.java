package com.example.cw_ood;

public class News {
    private String title;
    private String description;
    private String url;
    private boolean isRead; // Track if the news is marked as read
    private boolean isLiked; // Track if the news is liked

    public News(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.isRead = false;
        this.isLiked = false;
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

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}

