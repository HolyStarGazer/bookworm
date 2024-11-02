package edu.utsa.cs3773.bookworm.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BookCollections {
    public enum BookCategory {
        OWNED, TO_READ, CURRENTLY_READING, FAVORITES
    }

    private final Map<BookCategory, List<Book>> collections;

    public BookCollections() {
        collections = new EnumMap<>(BookCategory.class);
        for (BookCategory category : BookCategory.values()) {
            collections.put(category, new ArrayList<>());
        }
    }

    public void addBook(BookCategory category, Book book) {
        collections.get(category).add(book);
    }

    public void removeBook(BookCategory category, Book book) {
        collections.get(category).remove(book);
    }

    public List<Book> getBooksByCategory(BookCategory category) {
        return new ArrayList<>(collections.get(category));
    }

    public List<Book> getAllBooks() {
        List<Book> allBooks = new ArrayList<>();
        for (List<Book> books : collections.values())
            allBooks.addAll(books);

        return allBooks;
    }

    public boolean isBookInCategory(BookCategory category, Book book) {
        return collections.get(category).contains(book);
    }
}
