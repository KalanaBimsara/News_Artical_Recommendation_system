package com.example.cw_ood;

public class News {
    private String category;
    private String title;
    private String description;
    private String url;

    // Constructor
    public News(String category, String title, String description, String url) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    // Getters
    public String getCategory() {
        return category;
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

    // Setters (if needed)
    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
