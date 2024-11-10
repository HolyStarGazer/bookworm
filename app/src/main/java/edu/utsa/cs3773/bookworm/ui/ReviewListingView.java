package edu.utsa.cs3773.bookworm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.time.LocalDateTime;

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

    public LocalDateTime getListingId() {
        return this.listingId;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setListingId(LocalDateTime id) {
        this.listingId = id;
    }
    public void setUserId(long id) {
        this.userId = id;
    }
}