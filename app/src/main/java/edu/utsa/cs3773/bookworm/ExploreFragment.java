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

    private RecyclerView recyclerViewBooks;
    private BookAdapter bookAdapter; // Assumes you have a BookAdapter for RecyclerView
    private List<Book> bookList = new ArrayList<>(); // Initialize here to avoid null reference

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        // Initialize views
        recyclerViewBooks = root.findViewById(R.id.recycler_view_books);

        // Set up RecyclerView with GridLayoutManager
        int numColumns = 3; // Number of columns in the grid
        recyclerViewBooks.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        bookAdapter = new BookAdapter(bookList); // Use initialized list
        recyclerViewBooks.setAdapter(bookAdapter);

        // FIXME: This is to populate the list of books for testing, delete later
        bookList.add(new Book(1, "Book 1"));
        bookList.add(new Book(2, "Book 2"));
        bookList.add(new Book(3, "Book 3"));
        bookList.add(new Book(4, "Book 4"));
        bookList.add(new Book(5, "Book 5"));
        bookList.add(new Book(6, "Book 6"));
        bookList.add(new Book(7, "Book 7"));
        bookList.add(new Book(8, "Book 8"));

        return root;
    }
}
