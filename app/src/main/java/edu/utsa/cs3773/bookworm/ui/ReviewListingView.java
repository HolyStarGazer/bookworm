package edu.utsa.cs3773.bookworm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.time.LocalDateTime;

/**
 * Custom view extending <code>LinearLayout</code> that displays a review listing, with additional members in service of its role as a listing.
 *
 * @author Gavin C Wilson
 * @version %I% %G%
 * @see "res/layout/listing_review.xml"
 */
public class ReviewListingView extends LinearLayout {

    private LocalDateTime listingId;
    private long userId;

    public ReviewListingView(Context context) {
        super(context);
    }
    public ReviewListingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ReviewListingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Gets the timestamp-derived listing ID of this view.
     *
     * @return  a local date/time storing this view's listing ID
     */
    public LocalDateTime getListingId() {
        return this.listingId;
    }

    /**
     * Gets the database user ID associated with this view.
     *
     * @return  a long equal to this view's user ID
     */
    public long getUserId() {
        return this.userId;
    }

    /**
     * Sets this view's listing ID to the provided timestamp.
     *
     * @param id    the local date/time to set this view's listing ID to
     */
    public void setListingId(LocalDateTime id) {
        this.listingId = id;
    }

    /**
     * Sets this view's user ID to the provided database primary key.
     *
     * @param id    the long to set this view's user ID to
     */
    public void setUserId(long id) {
        this.userId = id;
    }
}