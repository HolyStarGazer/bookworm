package edu.utsa.cs3773.bookworm.model;

import java.util.ArrayList;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private ArrayList<Book> ownedBooks;
    private ArrayList<Book> toReadBooks;
    private ArrayList<Book> currentlyReadingBooks;
    private ArrayList<Book> favoriteBooks;

    /**
     * Constructor called upon signing up
     * @param username
     * @param password
     * @param email
     */
    public User (String username, String password, String email) {

    }
}
