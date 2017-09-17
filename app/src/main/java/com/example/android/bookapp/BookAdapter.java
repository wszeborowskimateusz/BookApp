package com.example.android.bookapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mateusz on 17/09/2017.
 * Class to headle a single book to a [@ListView]
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View bookItemView = convertView;

        /*If there is no view to reuse we inflate a new one from layout book_item*/
        if(bookItemView == null) {
            bookItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item, parent, false);
        }

        /*The current book to display*/
        Book currentBook = getItem(position);

        /*Find the TextView to fill it with the author*/
        TextView authorView = (TextView)bookItemView.findViewById(R.id.author);

        /*Fill the authorTextView with proper author*/
        authorView.setText(currentBook.getmAuthor());

        /*Find the TextView to fill it with the title*/
        TextView titleView = (TextView)bookItemView.findViewById(R.id.title);

        /*Fill the titleTextView with proper title*/
        titleView.setText(currentBook.getmTitle());

        /*Find the TextView to fill it with the description*/
        TextView descriptionView = (TextView)bookItemView.findViewById(R.id.description);

        /*Fill the @descriptionTextView with proper description*/
        descriptionView.setText(currentBook.getmDescription());

        return bookItemView;
    }
}
