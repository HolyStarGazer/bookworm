package edu.utsa.cs3773.bookworm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String password; // Need to learn how to store this as a hash value
    private String email;
    private boolean isAdmin;
    private BookCollections bookCollections;

    public User (String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = false;
        this.bookCollections = new BookCollections();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Book> getBooksByCategory(BookCollections.BookCategory category) {
        return bookCollections.getBooksByCategory(category);
    }
}
