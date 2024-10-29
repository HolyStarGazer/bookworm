package edu.utsa.cs3773.bookworm.model;

public class Review {
    private int id;
    private int bookID;
    private User reviewer;
    private String content;
    private double rating;

    public Review(User reviewer, int bookID, String content, double rating) {
        this.reviewer = reviewer;
        this.bookID = bookID;
        this.content = content;
        this.rating = rating;
    }

    public Review(User reviewer, int bookID, String content) {
        this.reviewer = reviewer;
        this.bookID = bookID;
        this.content = content;
    }

    public Review(User reviewer, int bookID, double rating) {
        this.reviewer = reviewer;
        this.bookID = bookID;
        this.rating = rating;
    }

    public Review() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
