package edu.utsa.cs3773.bookworm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RatingSystem {
    private HashMap<Integer, ArrayList<Review>> reviewsMap;

    public RatingSystem() {
        this.reviewsMap = new HashMap<>();
    }

    public void addReview(int bookID, Review review) {
        reviewsMap.computeIfAbsent(bookID, k -> new ArrayList<>()).add(review);
    }

    public double getAverageRating(int bookID) {
        List<Review> reviews = reviewsMap.get(bookID);
        if (reviews == null || reviews.isEmpty())
            return 0.0;

        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }

    public List<Review> getReviewsForBook(int bookID) {
        return reviewsMap.getOrDefault(bookID, new ArrayList<>());
    }
}
