package com.example.android.bookapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        final ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        /*Fill adapter with empty list*/
        adapt = new BookAdapter(this,new ArrayList<Book>());

        /*Find the ListView to set the adapter on*/
        ListView view = (ListView) findViewById(R.id.books_list);
        view.setAdapter(adapt);

        /*Set the empty textView on ListView*/
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        view.setEmptyView(mEmptyStateTextView);

        /*After pressing the book a link with more information is oppened*/
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*If a link to a book is available we sent an Intent to open the link in a browser*/
                if(!TextUtils.isEmpty(adapt.getItem(position).getmURL())) {
                    Uri webpage = Uri.parse(adapt.getItem(position).getmURL());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });

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

                /*If user typed anything we will search for matching books. If not we will display Toast msg*/
                if(TextUtils.isEmpty(usersInput)){
                    Toast.makeText(MainActivity.this,"You have to put some text",Toast.LENGTH_SHORT).show();
                }
                else{

                    // Get details on the currently active default data network
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    /*If there is an Internet connection*/
                    if (networkInfo != null && networkInfo.isConnected()) {
                        /*Format string so it can fit the query*/
                        usersInput = "\"" + usersInput;
                        usersInput = usersInput.replaceAll(" ","+");
                        usersInput+="\"";
                        customURL = GOOGLE_URL;

                        customURL = customURL.replace("q=","q="+usersInput);


                        getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                        // because this activity implements the LoaderCallbacks interface).
                        loaderManager.initLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                    }
                    /*If there is no Internet connection we display "No Internet Connection" msg*/
                    else{
                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet);
                    }

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

        //Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

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
