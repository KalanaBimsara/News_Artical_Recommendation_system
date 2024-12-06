package News;

public class News {
    private String id;
    private String category;
    private String title;
    private String description;
    private String url;

    // Constructor polymorphism
    public News(String category, String title, String description, String url) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.url = url;
    }
    public News(String id, String title, String description, String url, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.category = category;
    }

    // Getters setters
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
