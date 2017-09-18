package com.example.android.bookapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>>{

    /*String containing text the user put into the EditView*/
    private String usersInput;

    /*The URL we will edit in order to search for specific books*/
    private static String GOOGLE_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";

    /*The specific URL with specific books to be find*/
    private String customURL;

    /*Adapter to hand in books for ListView*/
    private BookAdapter adapt;

    /*ID of books loader we will use to load books from server*/
    private static final int BOOKS_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapt = new BookAdapter(this,new ArrayList<Book>());

        /*Find the ListView to set the adapter on*/
        ListView view = (ListView) findViewById(R.id.books_list);
        view.setAdapter(adapt);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        final LoaderManager loaderManager = getLoaderManager();

        /*Finding the search button*/
        Button searchButton = (Button)findViewById(R.id.search_button);
        /*Add effect on click in order to fetch specific data(books) from server*/
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Finding the EditView to read users Input*/
                EditText input = (EditText)v.getRootView().findViewById(R.id.search_bar);
                usersInput = input.getText().toString();
                if(TextUtils.isEmpty(usersInput)){
                    Toast.makeText(MainActivity.this,"You have to put some text",Toast.LENGTH_SHORT).show();
                }
                else{
                    /*Format string so it can fit the query*/
                    usersInput = "\"" + usersInput;
                    usersInput = usersInput.replaceAll(" ","+");
                    usersInput+="\"";
                    customURL = GOOGLE_URL;

                    customURL = customURL.replace("q=","q="+usersInput);
                    Log.v("Main Activity","customURL = "+customURL);


                    getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.initLoader(BOOKS_LOADER_ID, null, MainActivity.this);

                }
            }
        });



    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BooksLoader(this, customURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        //ProgressBar bar = (ProgressBar)findViewById(R.id.progress_bar);
        //bar.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        //mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous book data
        adapt.clear();
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            adapt.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapt.clear();
    }
}
