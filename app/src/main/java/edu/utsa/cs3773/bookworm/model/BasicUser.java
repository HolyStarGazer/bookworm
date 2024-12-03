package edu.utsa.cs3773.bookworm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BasicUser extends User {
    private BookCollections bookCollections;

    public BasicUser(int id, String username, String password, String email) {
        super(id, username, password, email);
        setAdmin(false);
        this.bookCollections = new BookCollections();
    }

    public List<Book> getBooksByCategory(BookCollections.BookStatus category) {
        return bookCollections.getBooksByCategory(category);
    }
}
