package edu.utsa.cs3773.bookworm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

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

    public long getListingId() {
        return listingId;
    }
    public long getBookId() {
        return bookId;
    }
    public void setListingId(long id) {
        listingId = id;
    }
    public void setBookId(long id) {
        bookId = id;
    }
}
