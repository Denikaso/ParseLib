package model;

public class Article {
    private String title;
    private String text;
    private String imageUrl;

    public Article(String title, String text, String imageUrl) {
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
