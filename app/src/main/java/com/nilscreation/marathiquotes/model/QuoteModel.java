package com.nilscreation.marathiquotes.model;

public class QuoteModel {
    String title;
    String category;
    boolean isLiked = false;

    public QuoteModel() {

    }

    public QuoteModel(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public QuoteModel(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
