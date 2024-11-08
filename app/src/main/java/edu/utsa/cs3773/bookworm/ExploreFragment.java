package edu.utsa.cs3773.bookworm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.cs3773.bookworm.model.Book;
import edu.utsa.cs3773.bookworm.model.BookAdapter;

public class ExploreFragment extends Fragment {

    private EditText searchBar;
    private Spinner spinnerGenre, spinnerRating, spinnerPrice;
    private RecyclerView recyclerViewBooks;
    private BookAdapter bookAdapter; // Assumes you have a BookAdapter for RecyclerView
    private List<Book> bookList = new ArrayList<>(); // Initialize here to avoid null reference

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        // Initialize views
        searchBar = root.findViewById(R.id.search_bar);
        spinnerGenre = root.findViewById(R.id.spinner_genre);
        spinnerRating = root.findViewById(R.id.spinner_rating);
        spinnerPrice = root.findViewById(R.id.spinner_price);
        recyclerViewBooks = root.findViewById(R.id.recycler_view_books);

        // Set up RecyclerView with GridLayoutManager
        int numColumns = 3; // Number of columns in the grid
        recyclerViewBooks.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        bookAdapter = new BookAdapter(bookList); // Use initialized list
        recyclerViewBooks.setAdapter(bookAdapter);

        // FIXME: This is to populate the list of books for testing
        bookList.add(new Book(1, "Book 1"));
        bookList.add(new Book(2, "Book 2"));
        bookList.add(new Book(3, "Book 3"));
        bookList.add(new Book(4, "Book 4"));
        bookList.add(new Book(5, "Book 5"));
        bookList.add(new Book(6, "Book 6"));
        bookList.add(new Book(7, "Book 7"));
        bookList.add(new Book(8, "Book 8"));

        // Set up Spinners with ArrayAdapter for sample data (replace with actual data as needed)
        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genres_array, android.R.layout.simple_spinner_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(genreAdapter);

        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.ratings_array, android.R.layout.simple_spinner_item);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRating.setAdapter(ratingAdapter);

        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.price_range_array, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(priceAdapter);

        // Setup search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Add listeners for spinners to filter books on selection change
        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterBooks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterBooks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterBooks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return root;
    }

    private void filterBooks() {
        if (bookList == null) {
            bookList = new ArrayList<>(); // Initialize if null
        }

        String query = searchBar.getText().toString().toLowerCase();
        String genre = spinnerGenre.getSelectedItem().toString();
        String rating = spinnerRating.getSelectedItem().toString();
        String price = spinnerPrice.getSelectedItem().toString();

        List<Book> filteredBooks = new ArrayList<>();

        for (Book book : bookList) {
            boolean matches = true;

            // Check if book title matches the search query
            if (!book.getTitle().toLowerCase().contains(query)) {
                matches = false;
            }

            // TODO: Add filtering for genre, rating, and price

            if (matches) {
                filteredBooks.add(book);
            }
        }

        // Update the adapter with the filtered list
        bookAdapter.updateBooks(filteredBooks);

        // Show or hide the "Books not found" message
        TextView tvBooksNotFound = getView().findViewById(R.id.tv_books_not_found);
        RecyclerView recyclerViewBooks = getView().findViewById(R.id.recycler_view_books);

        if (filteredBooks.isEmpty()) {
            recyclerViewBooks.setVisibility(View.GONE); // Hide RecyclerView
            tvBooksNotFound.setVisibility(View.VISIBLE); // Show "Books not found" message
        } else {
            recyclerViewBooks.setVisibility(View.VISIBLE); // Show RecyclerView
            tvBooksNotFound.setVisibility(View.GONE); // Hide "Books not found" message
        }
    }

}
