package com.example.cw_ood;

public class News {
    private String title;
    private String description;
    private String url;
    private String category; // Added category field

    public News(String title, String description, String url, String category) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.category = category;
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

    public String getCategory() {
        return category; // Getter for category
    }
}
