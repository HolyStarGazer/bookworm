package edu.utsa.cs3773.bookworm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ListingView extends LinearLayout {

    private long listingId;
    private long userId;

    public ListingView(Context context) {
        super(context);
    }
    public ListingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public long getListingId() {
        return this.listingId;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setListingId(long id) {
        this.listingId = id;
    }
    public void setUserId(long id) {
        this.userId = id;
    }
}
