package edu.utsa.cs3773.bookworm.model.api;

public class AuthException extends RuntimeException {
  public AuthException(String message) {
    super(message);
  }
}
