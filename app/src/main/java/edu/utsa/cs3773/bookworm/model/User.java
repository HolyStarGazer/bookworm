package edu.utsa.cs3773.bookworm.model;

import androidx.annotation.NonNull;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public abstract class User {
    private final int id;
    private String username;
    // Stores the HASHED PASSWORD
    private String password;
    private String email;
    private boolean isAdmin;

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
        this.isAdmin = false;
    }

    // For receiving responses from the backend
    public static class Response {
        public int id;
        public String username;
        public String email;
        public boolean isAdmin;
    }

    // FOR PASSWORD HASHING
    private String hashPassword(@NonNull String plain) {
        Argon2 argon2 = Argon2Factory.create();
        char[] plainCharArr = plain.toCharArray();
        String hash = argon2.hash(3, 65536, 1, plainCharArr);

        // Immediately wipe plaintext array for an extra layer of security
        argon2.wipeArray(plainCharArr);

        return hash;
    }

    public boolean verifyPassword(String hashed, @NonNull String plain) {
        Argon2 argon2 = Argon2Factory.create();
        return argon2.verify(hashed, plain.toCharArray());
    }

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

    // Password is ALREADY HASHED
    public String getPassword() {
        return password;
    }

    // Assuming that the password is not hashed
    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
