package edu.utsa.cs3773.bookworm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.utsa.cs3773.bookworm.model.Book;
import edu.utsa.cs3773.bookworm.model.BookCollections;
import edu.utsa.cs3773.bookworm.model.BookAdapter;

import java.util.List;

public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList;
    private BookCollections bookCollections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        // Initialize RecyclerView and its layout manager
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 columns for grid

        // Initialize BookCollections (use real data in your app)
        bookCollections = new BookCollections();
        populateBookCollections(); // Add books to the collection for testing

        // Create and set the adapter with books from the collection
        bookList = bookCollections.getAllBooks(); // Get all books
        adapter = new BookAdapter(bookList);
        recyclerView.setAdapter(adapter);

        // Item click listener to open BookDetailFragment
        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                // Navigate to the BookDetailsFragment
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", book.getId());
                Navigation.findNavController(rootView).navigate(R.id.action_libraryFragment_to_bookDetailsFragment, bundle);
            }
        });

        return rootView;
    }

    private void populateBookCollections() {
        // Add some sample books to the collections
        bookCollections.addBook(BookCollections.BookCategory.OWNED,
                new Book(1, "Book 1"));
        bookCollections.addBook(BookCollections.BookCategory.TO_READ,
                new Book(2, "Book 2"));
        bookCollections.addBook(BookCollections.BookCategory.CURRENTLY_READING,
                new Book(3, "Book 3"));
        bookCollections.addBook(BookCollections.BookCategory.FAVORITES,
                new Book(4, "Book 4"));
    }
}
