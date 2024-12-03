package edu.utsa.cs3773.bookworm.model;

import java.util.ArrayList;

public class Book {
    private final int id;
    private String title;
    private String subtitle;
    private ArrayList<Author> authors;
    private String publisher;
    private ArrayList<Genre> genres;
    private String description;
    private int isbn10;
    private int isbn13;
    private double averageRating;
    private ArrayList<Review> reviews;
    private String releaseDate;
    private String coverURL;
    private double price;
    private int pages;
    private int words;
    private int chapters;

    // Reduced book class for previewing in explore fragment
    public static class Preview {
        private final int id;

        private final String title;
        private final String image;

        public Preview(int id, String title, String image) {
            this.id = id;
            this.title = title;
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public boolean equals(Object obj) {
            // If they have the same reference
            if (this == obj)
                return true;
            if (this.getClass() != obj.getClass())
                return false;
            Preview other = (Preview) obj;
            return this.id == other.id;
        }
    }

    public Book (int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Book (int id, String title, String subtitle) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
    }

    public void clearGenres() {
        this.genres.clear();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(int isbn10) {
        this.isbn10 = isbn10;
    }

    public int getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(int isbn13) {
        this.isbn13 = isbn13;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public String getCoverURL() {
        return coverURL;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getWords() {
        return words;
    }

    public boolean matchesSearch(String query, String genre, String ratingString, String price) {
        // Check if title matches the search query (case insensitive)
        boolean titleMatches = title.toLowerCase().contains(query);

        // Check if genre, rating, and price match the selected filter values
        boolean genreMatches = genre == null || this.genres.contains(genre);  // Compare Genre objects
        boolean ratingMatches = ratingString.equals("Any") || this.averageRating >= Double.parseDouble(ratingString);
        boolean priceMatches = price.equals("Any"); // TODO: Handle logic for price

        // Return true if all conditions are met
        return titleMatches && genreMatches && ratingMatches && priceMatches;
    }

    // An id is unique to a book, so it can be safely compared here
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;

        Book other = (Book) obj;
        return id == other.getId();
    }
}