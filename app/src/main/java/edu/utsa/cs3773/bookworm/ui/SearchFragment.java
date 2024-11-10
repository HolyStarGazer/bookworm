package edu.utsa.cs3773.bookworm.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.color.MaterialColors;

import edu.utsa.cs3773.bookworm.R;
import edu.utsa.cs3773.bookworm.model.Author;
import edu.utsa.cs3773.bookworm.model.Book;
import edu.utsa.cs3773.bookworm.model.Genre;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private static final int PER_PAGE = 10;

    private NavController navController;
    private FragmentManager fragmentManager;
    private MenuItem navSearch, navFavorites;
    private RadioGroup searchBy;
    private EditText searchBar, filterList, filterMin, filterMax;
    private Button genreButton, publisherButton, ratingButton, priceButton;
    private View bottomNavigation, genreNotch, publisherNotch, ratingNotch, priceNotch, genrePublisherFilter, ratingPriceFilter, currency1, currency2, range1, range2, star1, star2, prevButton, nextButton;
    private Spinner listInclusivity;
    private int currentFilter = 0, genreInclusivitySelection = 1, publisherInclusivitySelection = 1;
    private String genreList = "", publisherList = "";
    private float ratingMin = Float.NEGATIVE_INFINITY, ratingMax = Float.POSITIVE_INFINITY, priceMin = Float.NEGATIVE_INFINITY, priceMax = Float.POSITIVE_INFINITY;
    private LinearLayout listingLayout;
    private BookListingView firstListing, lastListing;
    private BookListingView[] listings;
    private int prevButtonVisibility = View.GONE, nextButtonVisibility = View.GONE;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentManager = getParentFragmentManager();
        navSearch = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_search);
        navSearch.setVisible(false);
        navFavorites = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_favorites);
        navFavorites.setVisible(false);
        bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.GONE);
        view.findViewById(R.id.search_submit_button).setOnClickListener(this);
        genreButton = view.findViewById(R.id.search_genre_button);
        genreButton.setOnClickListener(this);
        publisherButton = view.findViewById(R.id.search_publisher_button);
        publisherButton.setOnClickListener(this);
        ratingButton = view.findViewById(R.id.search_rating_button);
        ratingButton.setOnClickListener(this);
        priceButton = view.findViewById(R.id.search_price_button);
        priceButton.setOnClickListener(this);
        view.findViewById(R.id.search_list_apply_button).setOnClickListener(this);
        view.findViewById(R.id.search_list_clear_button).setOnClickListener(this);
        view.findViewById(R.id.search_range_apply_button).setOnClickListener(this);
        view.findViewById(R.id.search_range_clear_button).setOnClickListener(this);
        view.findViewById(R.id.search_prev_button).setOnClickListener(this);
        view.findViewById(R.id.search_next_button).setOnClickListener(this);
        prevButton = view.findViewById(R.id.search_prev_button);
        prevButton.setOnClickListener(this);
        nextButton = view.findViewById(R.id.search_next_button);
        nextButton.setOnClickListener(this);
        searchBy = view.findViewById(R.id.search_by_radio);
        searchBar = view.findViewById(R.id.search_bar);
        if (!genreList.isEmpty()) genreButton.setBackgroundColor(MaterialColors.getColor(view, com.google.android.material.R.attr.colorPrimary, 0xFF6200EE));
        if (!publisherList.isEmpty()) publisherButton.setBackgroundColor(MaterialColors.getColor(view, com.google.android.material.R.attr.colorPrimary, 0xFF6200EE));
        if (ratingMin >= 1 || ratingMax <= 5) ratingButton.setBackgroundColor(MaterialColors.getColor(view, com.google.android.material.R.attr.colorPrimary, 0xFF6200EE));
        if (priceMin >= 0 || priceMax < Float.POSITIVE_INFINITY) priceButton.setBackgroundColor(MaterialColors.getColor(view, com.google.android.material.R.attr.colorPrimary, 0xFF6200EE));
        genreNotch = view.findViewById(R.id.search_genre_notch);
        publisherNotch = view.findViewById(R.id.search_publisher_notch);
        ratingNotch = view.findViewById(R.id.search_rating_notch);
        priceNotch = view.findViewById(R.id.search_price_notch);
        genrePublisherFilter = view.findViewById(R.id.search_filter_genre_publisher);
        ratingPriceFilter = view.findViewById(R.id.search_filter_rating_price);
        listInclusivity = view.findViewById(R.id.search_filter_spinner);
        filterList = view.findViewById(R.id.search_filter_list);
        currency1 = view.findViewById(R.id.search_filter_currency1);
        currency2 = view.findViewById(R.id.search_filter_currency2);
        filterMin = view.findViewById(R.id.search_filter_min);
        filterMax = view.findViewById(R.id.search_filter_max);
        range1 = view.findViewById(R.id.search_filter_range1);
        range2 = view.findViewById(R.id.search_filter_range2);
        star1 = view.findViewById(R.id.search_filter_star1);
        star2 = view.findViewById(R.id.search_filter_star2);
        if (currentFilter == 1) {
            genreNotch.setVisibility(View.VISIBLE);
            genrePublisherFilter.setVisibility(View.VISIBLE);
        }
        else if (currentFilter == 2) {
            publisherNotch.setVisibility(View.VISIBLE);
            genrePublisherFilter.setVisibility(View.VISIBLE);
        }
        else if (currentFilter == 3) {
            currency1.setVisibility(View.GONE);
            currency2.setVisibility(View.GONE);
            range1.setVisibility(View.VISIBLE);
            range2.setVisibility(View.VISIBLE);
            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.VISIBLE);
            ratingNotch.setVisibility(View.VISIBLE);
            ratingPriceFilter.setVisibility(View.VISIBLE);
        }
        else if (currentFilter == 4) {
            priceNotch.setVisibility(View.VISIBLE);
            ratingPriceFilter.setVisibility(View.VISIBLE);
        }
        listingLayout = view.findViewById(R.id.search_listing_layout);
        if (listings != null) {
            for (int i = 0; i < listings.length; ++i) {
                ((ViewGroup)listings[i].getParent()).removeView(listings[i]);
                listingLayout.addView(listings[i]);
            }
        }
        prevButton.setVisibility(prevButtonVisibility);
        nextButton.setVisibility(nextButtonVisibility);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navSearch.setVisible(true);
        navFavorites.setVisible(true);
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_submit_button) {
            currentFilter = 0;
            genreNotch.setVisibility(View.INVISIBLE);
            publisherNotch.setVisibility(View.INVISIBLE);
            ratingNotch.setVisibility(View.INVISIBLE);
            priceNotch.setVisibility(View.INVISIBLE);
            genrePublisherFilter.setVisibility(View.GONE);
            ratingPriceFilter.setVisibility(View.GONE);
            int numBooks = PER_PAGE;
            Book[] books = new Book[PER_PAGE];
            Book beyond = null;
            //use search type, terms, and filters to generate a database query
            //get the first* PER_PAGE books for the generated query and store them in the books array, and get the next book after* that and store it in beyond
            //if there are fewer than PER_PAGE books, store them and update numBooks
            fetchPage(numBooks, books);
            prevButton.setVisibility(prevButtonVisibility = View.GONE);
            nextButton.setVisibility(nextButtonVisibility = (beyond == null ? View.GONE : View.VISIBLE));
        }
        else if (view == genreButton || view == publisherButton || view == ratingButton || view == priceButton) {
            genreNotch.setVisibility(View.INVISIBLE);
            publisherNotch.setVisibility(View.INVISIBLE);
            ratingNotch.setVisibility(View.INVISIBLE);
            priceNotch.setVisibility(View.INVISIBLE);
            currency1.setVisibility(View.GONE);
            currency2.setVisibility(View.GONE);
            range1.setVisibility(View.GONE);
            range2.setVisibility(View.GONE);
            star1.setVisibility(View.GONE);
            star2.setVisibility(View.GONE);
            genrePublisherFilter.setVisibility(View.GONE);
            ratingPriceFilter.setVisibility(View.GONE);
            if (currentFilter != 1 && view == genreButton) {
                currentFilter = 1;
                listInclusivity.setSelection(genreInclusivitySelection);
                filterList.setText(genreList);
                genreNotch.setVisibility(View.VISIBLE);
                genrePublisherFilter.setVisibility(View.VISIBLE);
            }
            else if (currentFilter != 2 && view == publisherButton) {
                currentFilter = 2;
                listInclusivity.setSelection(publisherInclusivitySelection);
                filterList.setText(publisherList);
                publisherNotch.setVisibility(View.VISIBLE);
                genrePublisherFilter.setVisibility(View.VISIBLE);
            }
            else if (currentFilter != 3 && view == ratingButton) {
                currentFilter = 3;
                filterMin.setText(ratingMin < 1 ? "" : String.format("%02.1f", ratingMin));
                filterMax.setText(ratingMax > 5 ? "" : String.format("%02.1f", ratingMax));
                range1.setVisibility(View.VISIBLE);
                range2.setVisibility(View.VISIBLE);
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                ratingNotch.setVisibility(View.VISIBLE);
                ratingPriceFilter.setVisibility(View.VISIBLE);
            }
            else if (currentFilter != 4 && view == priceButton) {
                currentFilter = 4;
                filterMin.setText(priceMin < 0 ? "" : String.format("%03.2f", priceMin));
                filterMax.setText(priceMax == Float.POSITIVE_INFINITY ? "" : String.format("%03.2f", priceMax));
                currency1.setVisibility(View.VISIBLE);
                currency2.setVisibility(View.VISIBLE);
                priceNotch.setVisibility(View.VISIBLE);
                ratingPriceFilter.setVisibility(View.VISIBLE);
            }
            else currentFilter = 0;
        }
        else if (view.getId() == R.id.search_list_apply_button || view.getId() == R.id.search_list_clear_button) {
            if (view.getId() == R.id.search_list_clear_button) filterList.setText("");
            if (currentFilter == 1) {
                genreInclusivitySelection = listInclusivity.getSelectedItemPosition();
                if (filterList.getText() == null) filterList.setText("");
                genreList = filterList.getText().toString();
                if (genreList.isEmpty()) genreButton.setBackgroundColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorAccent, 0xFF5F5F5F));
                else genreButton.setBackgroundColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorPrimary, 0xFF6200EE));
            }
            else if (currentFilter == 2) {
                publisherInclusivitySelection = listInclusivity.getSelectedItemPosition();
                if (filterList.getText() == null) filterList.setText("");
                publisherList = filterList.getText().toString();
                if (publisherList.isEmpty()) publisherButton.setBackgroundColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorAccent, 0xFF5F5F5F));
                else publisherButton.setBackgroundColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorPrimary, 0xFF6200EE));
            }
        }
        else if (view.getId() == R.id.search_range_apply_button || view.getId() == R.id.search_range_clear_button) {
            if (view.getId() == R.id.search_range_clear_button) {
                filterMin.setText("");
                filterMax.setText("");
            }
            if (filterMin.getText() == null) filterMin.setText("");
            if (filterMax.getText() == null) filterMax.setText("");
            if (currentFilter == 3) {
                try {
                    if ((ratingMin = Float.parseFloat(filterMin.getText().toString())) < 1) ratingMin = 1;
                    filterMin.setText(String.format("%02.1f", ratingMin));
                } catch (NumberFormatException e) {
                    filterMin.setText("");
                    ratingMin = Float.NEGATIVE_INFINITY;
                }
                try {
                    if ((ratingMax = Float.parseFloat(filterMax.getText().toString())) > 5) ratingMax = 5;
                    filterMax.setText(String.format("%02.1f", ratingMax));
                } catch (NumberFormatException e) {
                    filterMax.setText("");
                    ratingMax = Float.POSITIVE_INFINITY;
                }
            }
            if (currentFilter == 4) {
                try {
                    if ((priceMin = Float.parseFloat(filterMin.getText().toString())) < 0) priceMin = 0;
                    filterMin.setText(String.format("%03.2f", priceMin));
                } catch (NumberFormatException e) {
                    filterMin.setText("");
                    priceMin = Float.NEGATIVE_INFINITY;
                }
                try {
                    if ((priceMax = Float.parseFloat(filterMax.getText().toString())) == Float.POSITIVE_INFINITY) filterMax.setText("");
                    else filterMax.setText(String.format("%03.2f", priceMax));
                } catch (NumberFormatException e) {
                    filterMax.setText("");
                    priceMax = Float.POSITIVE_INFINITY;
                }
            }
            if ((currentFilter == 3 && ratingMin < 1 && ratingMax > 5) || (currentFilter == 4 && ratingMin < 0 && ratingMax == Float.POSITIVE_INFINITY))
                ratingButton.setBackgroundColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorAccent, 0xFF5F5F5F));
            else ratingButton.setBackgroundColor(MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorPrimary, 0xFF6200EE));
        }
        else if (view.getId() == R.id.book_listing) {
            Bundle args = new Bundle();
            args.putLong("book", ((BookListingView)view).getBookId());
            args.putBoolean("optionsVisible", false);
            args.putInt("bottomNavigationVisibility", View.GONE);
            navController.navigate(R.id.nav_book_details, args);
        }
        else if (view.getId() == R.id.search_prev_button) {
            Book[] books = new Book[PER_PAGE];
            Book beyond = null;
            //get the last* PER_PAGE books that come before* firstListing, and store the one before* that in beyond
            //if there are less than PER_PAGE books before* firstListing, get the first* PER_PAGE books
            fetchPage(PER_PAGE, books);
            if (beyond == null) prevButton.setVisibility(prevButtonVisibility = View.GONE);
            nextButton.setVisibility(nextButtonVisibility = View.VISIBLE);
        }
        else if (view.getId() == R.id.search_next_button) {
            Book[] books = new Book[PER_PAGE];
            Book beyond = null;
            //get the first* PER_PAGE books that come after* lastListing, and store the one after* that in beyond
            //if there are less than PER_PAGE books after* lastListing, get the last* PER_PAGE books
            fetchPage(PER_PAGE, books);
            prevButton.setVisibility(prevButtonVisibility = View.VISIBLE);
            if (beyond == null) nextButton.setVisibility(nextButtonVisibility = View.GONE);
        }
    }

    private void fetchPage(int numBooks, Book[] books) {
        listingLayout.removeAllViews();
        listings = new BookListingView[numBooks];
        for (int i = 0; i < Math.min(PER_PAGE, numBooks); ++i) {
            BookListingView listing = (BookListingView)getLayoutInflater().inflate(R.layout.listing_book, listingLayout, false);
            listingLayout.addView(listing);
            listing.setListingId(PER_PAGE - i); //set listing ID based on relevance of books[i]
            listing.setBookId(PER_PAGE - i);    //set listing's book ID based on book ID of books[i]
            if (i == 0) firstListing = listing;
            else if (i == Math.min(PER_PAGE, numBooks) - 1) lastListing = listing;
            //populate listing fields with data from books[i]
            listing.setOnClickListener(this);
            listings[i] = listing;
        }
    }
}
