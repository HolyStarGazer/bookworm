package edu.utsa.cs3773.bookworm.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BookCollections {
    public enum BookStatus {
        OWNED, TO_READ, CURRENTLY_READING, FAVORITES
    }

    private final Map<BookStatus, List<Book>> collections;

    public BookCollections() {
        collections = new EnumMap<>(BookStatus.class);
        for (BookStatus status : BookStatus.values()) {
            collections.put(status, new ArrayList<>());
        }
    }

    public void addBook(BookStatus status, Book book) {
        collections.get(status).add(book);
    }

    public void removeBook(BookStatus status, Book book) {
        collections.get(status).remove(book);
    }

    public List<Book> getBooksByCategory(BookStatus category) {
        return new ArrayList<>(collections.get(category));
    }

    public List<Book> getAllBooks() {
        List<Book> allBooks = new ArrayList<>();
        for (List<Book> books : collections.values())
            allBooks.addAll(books);

        return allBooks;
    }

    public boolean isBookInCategory(BookStatus category, Book book) {
        return collections.get(category).contains(book);
    }
}
