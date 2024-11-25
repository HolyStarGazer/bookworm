package edu.utsa.cs3773.bookworm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class User {
    private final int id;
    private String username;
    private String password;
    /* FOR PASSWORD HASHING (REPLACE password)
    private String passwordHashed;
     */
    private String email;
    private boolean isAdmin;
    private BookCollections bookCollections;

    public User (int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        /* FOR PASSWORD HASHING
        this.salt = generateSalt();
        this.passwordHashed = hashPassword(password, this.salt);
         */
        this.email = email;
        this.isAdmin = false;
        this.bookCollections = new BookCollections();
    }

    /* FOR PASSWORD HASHING
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Unable to hash password", e);
        }
    }

    public boolean verifyPassword(String password) {
        String hashedAttempt = hashPassword(password, this.salt);
        return hashedAttempt.equals(this.passwordHashed);
    }
     */

    public int getId() {
        return id;
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
