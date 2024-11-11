package edu.utsa.cs3773.bookworm.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.color.MaterialColors;

import edu.utsa.cs3773.bookworm.R;
import edu.utsa.cs3773.bookworm.model.Author;
import edu.utsa.cs3773.bookworm.model.Book;
import edu.utsa.cs3773.bookworm.model.Genre;

/**
 * Fragment that displays the book details page.
 *
 * @author Gavin C Wilson
 * @version %I% %G%
 * @see "res/layout/fragment_book_details.xml"
 */
public class BookDetailsFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FragmentManager fragmentManager;
    private Bundle instanceState;
    private Book book;
    private boolean optionsVisible;
    private int bottomNavigationVisibility;
    private MenuItem navSearch, navFavorites;
    private View bottomNavigation;
    private TextView reviewCount, userContents;
    private RatingBar avgRating, userRating;
    private Button leaveReview;
    private ViewGroup userReview;
    private boolean isFavorite;

    /**
     * Class constructor.
     * Uses the resource ID of the book details page layout.
     */
    public BookDetailsFragment() {
        super(R.layout.fragment_book_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentManager = getParentFragmentManager();
        if (instanceState == null) instanceState = new Bundle();
        book = new Book((int)getArguments().getLong("book"), "title");    //set book based on database info retrieved using "book" argument, or based on book object retrieved from parent fragment
        optionsVisible = getArguments().getBoolean("optionsVisible");
        bottomNavigationVisibility = getArguments().getInt("bottomNavigationVisibility");
        navSearch = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_search);
        navSearch.setVisible(false);
        navFavorites = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_favorites);
        navFavorites.setVisible(false);
        bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.GONE);
        view.findViewById(R.id.book_details_favorite_button).setOnClickListener(this);
        leaveReview = view.findViewById(R.id.book_details_leave_review_button);
        leaveReview.setOnClickListener(this);
        view.findViewById(R.id.book_details_edit_review_button).setOnClickListener(this);
        view.findViewById(R.id.book_details_delete_review_button).setOnClickListener(this);
        view.findViewById(R.id.book_details_see_reviews_button).setOnClickListener(this);
        isFavorite = false;
        reviewCount = view.findViewById(R.id.book_details_review_count);
        avgRating = view.findViewById(R.id.book_details_avg_rating);
        userReview = view.findViewById(R.id.book_details_user_review);
        userRating = view.findViewById(R.id.book_details_user_rating);
        userContents = view.findViewById(R.id.book_details_user_contents);
        if (book.getTitle() != null) setText(view.findViewById(R.id.book_details_title), book.getTitle());
        if (book.getAuthors() != null) {
            String authors = "";
            for (Author a : book.getAuthors()) {
                //revise this if/when we change the naming storage scheme
                if ((a.getFirstName() == null || a.getFirstName().isEmpty()) && (a.getLastName() == null || a.getLastName().isEmpty())) continue;
                if (!authors.isEmpty()) authors += ", ";
                authors += a.getFirstName() + " " + a.getLastName();
            }
            if (!authors.isEmpty()) setText(view.findViewById(R.id.book_details_authors), "by " + authors);
        }
        //FIXME set the cover image here
        if (book.getPublisher() != null) setText(view.findViewById(R.id.book_details_publisher), book.getPublisher());
        if (book.getReleaseDate() != null) setText(view.findViewById(R.id.book_details_date), book.getReleaseDate());
        if (book.getIsbn13() >= 0)  //check if an ISBN was found for this book
            setText(view.findViewById(R.id.book_details_isbn), "ISBN: " + Long.toString(book.getIsbn13()));
        else if (book.getIsbn10() >= 0)
            setText(view.findViewById(R.id.book_details_isbn), "ISBN: " + Long.toString(book.getIsbn10()));
        if (book.getWords() >= 0) setText(view.findViewById(R.id.book_details_length_words), Integer.toString(book.getWords()));
        if (book.getPages() >= 0) setText(view.findViewById(R.id.book_details_length_pages), Integer.toString(book.getPages()));
        if (book.getChapters() >= 0) setText(view.findViewById(R.id.book_details_length_chapters), Integer.toString(book.getChapters()));
        if (book.getGenres() != null) {
            String genres = "";
            for (Genre g : book.getGenres()) {
                if (g.getName() == null || g.getName().isEmpty()) continue;
                if (!genres.isEmpty()) genres += ", ";
                genres += g.getName();
            }
            if (!genres.isEmpty()) setText(view.findViewById(R.id.book_details_genres), "Genres: " + genres);
        }
        if (book.getDescription() != null) setText(view.findViewById(R.id.book_details_description), "    " + book.getDescription());
        if (book.getPrice() >= 0) setText(view.findViewById(R.id.book_details_price), Double.toString(book.getPrice()));
        String instanceStateString = instanceState.getString("reviewCount", "");
        float instanceStateFloat = instanceState.getFloat("avgRating", -1);
        if (!(instanceStateString.isEmpty() || instanceStateFloat < 0)) {
            setText(reviewCount, instanceStateString);
            avgRating.setRating(instanceStateFloat);
        }
        //else {
        //  fetch review count and average rating from the database
        //  if (review count > 0) {
        //      instanceState.putString("reviewCount", instanceStateString = "Overall Rating from " + /*review count*/ + " reviews:");
        //      setText(reviewCount, instanceStateString);
        //      instanceState.putFloat("avgRating", instanceStateFloat = /*average rating*/);
        //      avgRating.setRating(instanceStateFloat);
        //  }
        //}
        instanceStateFloat = instanceState.getFloat("userRating", -1);
        instanceStateString = instanceState.getString("userContents", "");
        if (instanceState.getBoolean("userReview")) {
            leaveReview.setVisibility(View.GONE);
            userRating.setRating(instanceStateFloat);
            if (!instanceStateString.isEmpty()) setText(userContents, instanceStateString);
            userReview.setVisibility(View.VISIBLE);
        }
        //else if (!instanceState.containsKey("userReview")) {
        //  fetch logged in user's review for this book from the database
        //  if (it exists) {
        //      leaveReview.setVisibility(View.GONE);
        //      instanceState.putBoolean("userReview", true);
        //      instanceState.putFloat("userRating", instanceStateFloat = /*user review rating*/);
        //      userRating.setRating(instanceStateFloat);
        //      if (user review contents == null || (user review contents).isEmpty()) instanceState.putString("userContents", "");
        //      else {
        //          instanceState.putString("userContents", instanceStateString = "    " + /*user review contents*/);
        //          setText(userContents, instanceStateString);
        //      }
        //      userReview.setVisibility(View.VISIBLE);
        //  }
        //}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navSearch.setVisible(optionsVisible);
        navFavorites.setVisible(optionsVisible);
        bottomNavigation.setVisibility(bottomNavigationVisibility);
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
                        //String instanceStateString;
                        //float instanceStateFloat;
                        //fetch review count and average rating from the database
                        //if (review count > 0) {
                        //  instanceState.putString("reviewCount", instanceStateString = "Overall Rating from " + /*review count*/ + " reviews:");
                        //  setText(reviewCount, instanceStateString);
                        //  instanceState.putFloat("avgRating", instanceStateFloat = /*average rating*/);
                        //  avgRating.setRating(instanceStateFloat);
                        //}
                        //else {
                        //  instanceState.putString("reviewCount", "");
                        //  reviewCount.setText("This book has no reviews");
                        //  reviewCount.setTextColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorAccent, 0xFF5F5F5F));
                        //  reviewCount.setTypeface(null, Typeface.ITALIC);
                        //  instanceState.putFloat("avgRating", -1);
                        //  avgRating.setRating(0);
                        //}
                        //fetch logged in user's review for this book from the database
                        //if (it exists) {
                        leaveReview.setVisibility(View.GONE);
                        instanceState.putBoolean("userReview", true);
                        //  instanceState.putFloat("userRating", instanceStateFloat = /*user review rating*/);
                        //  userRating.setRating(instanceStateFloat);
                        //  if (user review contents == null || (user review contents).isEmpty()) {
                        //      instanceState.putString("userContents", "");
                        //      userContents.setText("    This review has no contents");
                        //      userContents.setTextColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorAccent, 0xFF5F5F5F));
                        //      userContents.setTypeface(null, Typeface.ITALIC);
                        //  }
                        //  else {
                        //      instanceState.putString("userContents", instanceStateString = "    " + /*user review contents*/);
                        //      setText(userContents, instanceStateString);
                        //  }
                        userReview.setVisibility(View.VISIBLE);
                        //}
                        //else {
                        //  userReview.setVisibility(View.GONE);
                        //  instanceState.putBoolean("userReview", false);
                        //  leaveReview.setVisibility(View.VISIBLE);
                        //}
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
                        userReview.setVisibility(View.GONE);
                        //update backend
                        //String instanceStateString;
                        //float instanceStateFloat;
                        //fetch review count and average rating from the database
                        //if (review count > 0) {
                        //  instanceState.putString("reviewCount", instanceStateString = "Overall Rating from " + /*review count*/ + " reviews:");
                        //  setText(reviewCount, instanceStateString);
                        //  instanceState.putFloat("avgRating", instanceStateFloat = /*average rating*/);
                        //  avgRating.setRating(instanceStateFloat);
                        //}
                        //else {
                        //  instanceState.putString("reviewCount", "");
                        //  reviewCount.setText("This book has no reviews");
                        //  reviewCount.setTextColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorAccent, 0xFF5F5F5F));
                        //  reviewCount.setTypeface(null, Typeface.ITALIC);
                        //  instanceState.putFloat("avgRating", -1);
                        //  avgRating.setRating(0);
                        //}
                        instanceState.putBoolean("userReview", false);
                        leaveReview.setVisibility(View.VISIBLE);
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
            args.putLong("book", book.getId());
            navController.navigate(R.id.nav_reviews, args);
        }
    }

    /**
     * Sets the text, text color, and text style of the provided <code>TextView</code>.
     * The provided <code>TextView</code> has its text set to the provided string, and its text color and style are set to the appropriate values for non-placeholder
     * text in order to distinguish it from default placeholder text.
     *
     * @param tv    the text view to modify
     * @param s     the string to set <code>tv</code>'s text to
     */
    private void setText(TextView tv, String s) {
        tv.setText(s);
        tv.setTextColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorOnBackground, 0xFF000000));
        tv.setTypeface(null, Typeface.NORMAL);
    }
}