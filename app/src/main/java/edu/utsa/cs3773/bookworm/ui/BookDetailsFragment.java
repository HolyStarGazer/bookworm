package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.R;
import edu.utsa.cs3773.bookworm.model.Book;

public class BookDetailsFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FragmentManager fragmentManager;
    private long book;
    private boolean isFavorite;

    public BookDetailsFragment() {
        super(R.layout.fragment_book_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentManager = getParentFragmentManager();
        book = getArguments().getLong("book");
        view.findViewById(R.id.book_details_favorite_button).setOnClickListener(this);
        view.findViewById(R.id.book_details_leave_review_button).setOnClickListener(this);
        view.findViewById(R.id.book_details_edit_review_button).setOnClickListener(this);
        view.findViewById(R.id.book_details_delete_review_button).setOnClickListener(this);
        view.findViewById(R.id.book_details_see_reviews_button).setOnClickListener(this);
        isFavorite = false;
        //populate fields with appropriate data
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.book_details_favorite_button) {
            //update backend
            if (isFavorite) ((ImageButton) view).setImageResource(R.drawable.favorite);
            else ((ImageButton) view).setImageResource(R.drawable.unfavorite);
            isFavorite = !isFavorite;
        }
        else if (view.getId() == R.id.book_details_leave_review_button || view.getId() == R.id.book_details_edit_review_button) {
            fragmentManager.setFragmentResultListener("updateReview", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if (result.getBoolean("changed")) {
                        getView().findViewById(R.id.book_details_leave_review_button).setVisibility(View.GONE);
                        //populate average rating, user rating, and user review contents fields with values from database
                        getView().findViewById(R.id.book_details_user_review).setVisibility(View.VISIBLE);
                    }
                }
            });
            navController.navigate(R.id.nav_review_editor);
        }
        else if (view.getId() == R.id.book_details_delete_review_button) {
            fragmentManager.setFragmentResultListener("USER_DELETE_REVIEW_DIALOG", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if (result.getBoolean("confirm")) {
                        getView().findViewById(R.id.book_details_user_review).setVisibility(View.GONE);
                        //update backend
                        //populate average rating fields with values from database
                        getView().findViewById(R.id.book_details_leave_review_button).setVisibility(View.VISIBLE);
                    }
                }
            });
            ConfirmDialogFragment dialog = new ConfirmDialogFragment();
            Bundle args = new Bundle();
            args.putString("message", "Are you sure you want to delete this review?");
            dialog.setArguments(args);
            dialog.show(fragmentManager, "USER_DELETE_REVIEW_DIALOG");
        }
        else if (view.getId() == R.id.book_details_see_reviews_button) {
            Bundle args = new Bundle();
            args.putLong("book", book);
            navController.navigate(R.id.nav_reviews, args);
        }
    }
}
