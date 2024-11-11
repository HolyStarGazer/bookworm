package edu.utsa.cs3773.bookworm.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.color.MaterialColors;

import java.time.LocalDateTime;
import java.util.ArrayList;

import edu.utsa.cs3773.bookworm.R;
import edu.utsa.cs3773.bookworm.model.Review;

/**
 * Fragment that displays the reviews page.
 *
 * @author Gavin C Wilson
 * @version %I% %G%
 * @see "res/layout/fragment_reviews.xml"
 * @see "res/menu/review_dropdown.xml"
 */
public class ReviewsFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private static final int PER_PAGE = 10;

    private NavController navController;
    private FragmentManager fragmentManager;
    private long book;
    private LinearLayout listingLayout;
    private View prevButton, nextButton;
    private ReviewListingView firstListing, lastListing, selectedListing;
    private ArrayList<ReviewListingView> listings;
    private int prevButtonVisibility = View.VISIBLE, nextButtonVisibility = View.VISIBLE;

    /**
     * Class constructor.
     * Uses the resource ID of the reviews page layout.
     */
    public ReviewsFragment() {
        super(R.layout.fragment_reviews);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentManager = getParentFragmentManager();
        book = getArguments().getLong("book");
        prevButton = view.findViewById(R.id.reviews_prev_button);
        prevButton.setOnClickListener(this);
        nextButton = view.findViewById(R.id.reviews_next_button);
        nextButton.setOnClickListener(this);
        listingLayout = view.findViewById(R.id.reviews_listing_layout);
        if (listings == null) {
            int numReviews = PER_PAGE;
            Review[] reviews = new Review[PER_PAGE];
            Review beyond = null;
            //get the first* PER_PAGE reviews for the current book and store them in the reviews array, and get the next review after* that and store it in beyond
            //if there are fewer than PER_PAGE reviews, store them and update numReviews
            fetchPage(numReviews, reviews);
            prevButton.setVisibility(prevButtonVisibility = View.GONE);
            if (beyond == null) nextButton.setVisibility(nextButtonVisibility = View.GONE);
        }
        else {
            for (ReviewListingView listing : listings) {
                ((ViewGroup)listing.getParent()).removeView(listing);
                listingLayout.addView(listing);
            }
            prevButton.setVisibility(prevButtonVisibility);
            nextButton.setVisibility(nextButtonVisibility);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.review_listing_dropdown_button) {
            selectedListing = (ReviewListingView)view.getParent().getParent();
            PopupMenu dropdown = new PopupMenu(getContext(), view);
            dropdown.getMenuInflater().inflate(R.menu.review_dropdown, dropdown.getMenu());
            dropdown.show();
            dropdown.setOnMenuItemClickListener(this);
        }
        else if (view.getId() == R.id.reviews_prev_button) {
            Review[] reviews = new Review[PER_PAGE];
            Review beyond = null;
            //get the last* PER_PAGE reviews that come before* firstListing, and store the one before* that in beyond
            //if there are less than PER_PAGE reviews before* firstListing, get the first* PER_PAGE reviews
            fetchPage(PER_PAGE, reviews);
            if (beyond == null) prevButton.setVisibility(prevButtonVisibility = View.GONE);
            nextButton.setVisibility(nextButtonVisibility = View.VISIBLE);
        }
        else if (view.getId() == R.id.reviews_next_button) {
            Review[] reviews = new Review[PER_PAGE];
            Review beyond = null;
            //get the first* PER_PAGE reviews that come after* lastListing, and store the one after* that in beyond
            //if there are less than PER_PAGE reviews after* lastListing, get the last* PER_PAGE reviews
            fetchPage(PER_PAGE, reviews);
            prevButton.setVisibility(prevButtonVisibility = View.VISIBLE);
            if (beyond == null) nextButton.setVisibility(nextButtonVisibility = View.GONE);
        }
    }

    /**
     * Populates this fragment with a single page of review listings, generated from the provided array of review data.
     * Each element of the provided array is used to initialize a new review listing, which is then added to this fragment's freshly cleared <code>listingLayout</code>
     * view group and <code>listings</code> array list. This fragment's <code>firstListing</code> and <code>lastListing</code> review listing objects are set to
     * reference the first and last listings created, respectively.
     *
     * @param numReviews    the length of <code>reviews</code>
     * @param reviews       a review array containing the data to use in initializing review listings
     */
    private void fetchPage(int numReviews, Review[] reviews) {
        listingLayout.removeAllViews();
        listings = new ArrayList<ReviewListingView>();
        for (int i = 0; i < Math.min(PER_PAGE, numReviews); ++i) {
            ReviewListingView listing = (ReviewListingView)getLayoutInflater().inflate(R.layout.listing_review, listingLayout, false);
            listingLayout.addView(listing);
            listing.setListingId(LocalDateTime.now().minusSeconds(i * 10)); //set listing ID based on timestamp of reviews[i]
            listing.setUserId(PER_PAGE - i);    //set listing's user ID based on user ID of reviews[i]
            if (i == 0) firstListing = listing;
            else if (i == Math.min(PER_PAGE, numReviews) - 1) lastListing = listing;
            //populate listing fields with data from reviews[i]
            //if user is not admin, listing.findViewById(R.id.review_listing_dropdown_button).setVisibility(View.GONE);
            listing.findViewById(R.id.review_listing_dropdown_button).setOnClickListener(this);
            listings.add(listing);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.option_remove_review) {
            fragmentManager.setFragmentResultListener("ADMIN_REMOVE_REVIEW_DIALOG", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if (result.getBoolean("confirm")) {
                        //update backend
                        listingLayout.removeView(selectedListing);
                        listings.remove(selectedListing);
                    }
                }
            });
            ConfirmDialogFragment dialog = new ConfirmDialogFragment();
            Bundle args = new Bundle();
            args.putString("message", "Are you sure you want to completely remove this review?");
            dialog.setArguments(args);
            dialog.show(fragmentManager, "ADMIN_REMOVE_REVIEW_DIALOG");
            return true;
        }
        else if (menuItem.getItemId() == R.id.option_delete_review_contents) {
            fragmentManager.setFragmentResultListener("ADMIN_DELETE_REVIEW_CONTENTS_DIALOG", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if (result.getBoolean("confirm")) {
                        //update backend
                        TextView textField = selectedListing.findViewById(R.id.review_listing_contents);
                        textField.setText("    This review has no contents");
                        textField.setTextColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorAccent, 0xFF5F5F5F));
                        textField.setTypeface(null, Typeface.ITALIC);
                    }
                }
            });
            ConfirmDialogFragment dialog = new ConfirmDialogFragment();
            Bundle args = new Bundle();
            args.putString("message", "Are you sure you want to delete this review's contents?");
            dialog.setArguments(args);
            dialog.show(fragmentManager, "ADMIN_DELETE_REVIEW_CONTENTS_DIALOG");
            return true;
        }
        return false;
    }
}

//*when querying reviews, the resulting selection will be ordered from most to least recent, with user ID serving as a tiebreaker
// throughout these comments, "first" and "before" refer to items closer to the start of this ordering
// "last" and "after" refer to items closer to the end of this ordering