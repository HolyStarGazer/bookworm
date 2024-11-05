package edu.utsa.cs3773.bookworm.model;

import java.util.List;

public class Admin extends User {
    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email);
        setAdmin(true);
    }

    public void deleteUser(List<User> userList, int userID) {
        User userToDelete = null;
        for (User user : userList) {
            if (user.getId() == userID) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            userList.remove(userToDelete);
            System.out.println("User deleted: " + userToDelete.getUsername());
        } else {
            System.out.println("User not found.");
        }
    }

    public void modifyBook(List<Book> bookList, int bookID, String newTitle, String newDescription) {
        Book bookToModify = null;
        for (Book book : bookList) {
            if (book.getId() == bookID) {
                bookToModify = book;
                break;
            }
        }

        if (bookToModify != null) {
            bookToModify.setTitle(newTitle);
            bookToModify.setDescription(newDescription);
            System.out.println("Book modified: " + newTitle);
        } else {
            System.out.println("Book not found.");
        }
    }

    public void deleteBook(List<Book> bookList, int bookID) {
        Book bookToDelete = null;
        for (Book book : bookList) {
            if (book.getId() == bookID) {
                bookToDelete = book;
                break;
            }
        }

        if (bookToDelete != null) {
            bookList.remove(bookToDelete);
            System.out.println("Book deleted: " + bookToDelete.getTitle());
        } else {
            System.out.println("Book not found.");
        }
    }

    public void deleteReview(List<Review> reviewList, int reviewID) {
        Review reviewToDelete = null;
        for (Review review : reviewList) {
            if (review.getId() == reviewID) {
                reviewToDelete = review;
                break;
            }
        }

        if (reviewToDelete != null) {
            reviewList.remove(reviewToDelete);
            System.out.println("Review deleted.");
        } else {
            System.out.println("Review not found.");
        }
    }
}
