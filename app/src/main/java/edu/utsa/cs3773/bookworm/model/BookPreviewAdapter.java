package edu.utsa.cs3773.bookworm.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.utsa.cs3773.bookworm.R;

// Enhanced adapter that only updates changes in the list without completely refreshing
public class BookPreviewAdapter extends ListAdapter<Book.Preview, BookPreviewAdapter.BookViewHolder> {

    private List<Book.Preview> books;
    private OnItemClickListener onItemClickListener; // Custom listener

    public BookPreviewAdapter(List<Book.Preview> books) {
        // Used for comparing changes in the old and new list
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Book.Preview oldItem, @NonNull Book.Preview newItem) {
                return (oldItem.getTitle().equals(newItem.getTitle())) && (oldItem.getImage().equals(newItem.getImage()));
            }

            @Override
            public boolean areContentsTheSame(@NonNull Book.Preview oldItem, @NonNull Book.Preview newItem) {
                return (oldItem.getTitle().equals(newItem.getTitle())) && (oldItem.getImage().equals(newItem.getImage()));
            }
        });

        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book.Preview book = books.get(position);
        holder.bookTitle.setText(book.getTitle());

        // Use Glide to load book cover image
        Glide.with(holder.itemView.getContext())
            .load(book.getImage()) // Assuming image is a URL
            .placeholder(R.drawable.no_cover_image_found) // placeholder
            .into(holder.bookCover);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(book); // Trigger click event
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    // Set the listener directly (without unnecessary cast)
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Custom listener interface
    public interface OnItemClickListener {
        void onItemClick(Book.Preview book); // Define click behavior
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
        }
    }

    public void updateBooks(List<Book.Preview> newBooks) {
        this.books = newBooks;
        submitList(newBooks); // Notify the adapter to update only new changes
    }
}
