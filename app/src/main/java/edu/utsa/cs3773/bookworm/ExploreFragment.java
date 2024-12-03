package edu.utsa.cs3773.bookworm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.utsa.cs3773.bookworm.model.Book;
import edu.utsa.cs3773.bookworm.model.BookPreviewAdapter;
import edu.utsa.cs3773.bookworm.model.api.APIHandler;
import edu.utsa.cs3773.bookworm.model.api.MainHandler;

public class ExploreFragment extends Fragment {
    private List<Book.Preview> bookList = new ArrayList<>(); // Initialize here to avoid null reference

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        // Initialize views
        RecyclerView recyclerViewBooks = root.findViewById(R.id.recycler_view_books);

        // Set up RecyclerView with GridLayoutManager
        int numColumns = 2; // Number of columns in the grid
        recyclerViewBooks.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Assumes you have a BookAdapter for RecyclerView
        BookPreviewAdapter bookPreviewAdapter = new BookPreviewAdapter(bookList); // Use initialized list
        recyclerViewBooks.setAdapter(bookPreviewAdapter);

        try {
            bookList = MainHandler.getSavedBooks().get();
            bookPreviewAdapter.updateBooks(bookList);
        } catch (Exception e) {
            APIHandler.Message
                .createMessage(e.getMessage())
                .displayToast(getContext());
        }

        return root;
    }
}
