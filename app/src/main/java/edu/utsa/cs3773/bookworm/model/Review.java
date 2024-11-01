package edu.utsa.cs3773.bookworm.model;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class Review {
    private int id;
    private Book book;
    private User reviewer;
    private String content;
    private double rating;
    private LocalDateTime timestamp;

    public Review(User reviewer, Book book, String content, double rating) {
        this.reviewer = reviewer;
        this.book = book;
        this.content = content;
        this.rating = rating;
    }

    public Review(User reviewer, Book book, String content) {
        this.reviewer = reviewer;
        this.book = book;
        this.content = content;
    }

    public Review(User reviewer, Book book, double rating) {
        this.reviewer = reviewer;
        this.book = book;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
        if (rating < 0.0)
            this.rating = 0.0;
        else
            this.rating = Math.min(rating, 5.0);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Review))
            return false;

        Review review = (Review) o;
        return id == review.id &&
                Double.compare(review.rating, rating) == 0 &&
                Objects.equals(book, review.book) &&
                Objects.equals(reviewer, review.reviewer) &&
                Objects.equals(content, review.content) &&
                Objects.equals(timestamp, review.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, reviewer, content, rating, timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", book=" + book +
                ", reviewer=" + reviewer.getUsername() +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}
