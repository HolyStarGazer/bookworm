package edu.utsa.cs3773.bookworm.model.api;

public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }

    public AuthException() {
        super("Authentication failed");
    }
}
