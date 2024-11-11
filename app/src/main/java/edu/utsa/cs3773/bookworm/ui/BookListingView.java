package edu.utsa.cs3773.bookworm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Custom view extending <code>LinearLayout</code> that displays a book listing, with additional members in service of its role as a listing.
 *
 * @author Gavin C Wilson
 * @version %I% %G%
 * @see "res/layout/listing_book.xml"
 */
public class BookListingView extends LinearLayout {

    private long listingId;
    private long bookId;

    public BookListingView(Context context) {
        super(context);
    }
    public BookListingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BookListingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Gets the search-relevance-derived listing ID of this view.
     *
     * @return  a long equal to this view's listing ID
     */
    public long getListingId() {
        return listingId;
    }

    /**
     * Gets the database book ID associated with this view.
     *
     * @return  a long equal to this view's book ID
     */
    public long getBookId() {
        return bookId;
    }

    /**
     * Sets this view's listing ID to the provided search-relevance-derived value.
     *
     * @param id    the long to set this view's listing ID to
     */
    public void setListingId(long id) {
        listingId = id;
    }

    /**
     * Sets this view's book ID to the provided database primary key.
     *
     * @param id    the long to set this view's book ID to
     */
    public void setBookId(long id) {
        bookId = id;
    }
}
