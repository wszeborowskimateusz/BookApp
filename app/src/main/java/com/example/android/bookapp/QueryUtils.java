package com.example.android.bookapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 17/09/2017.
 * Class to download data from a server and return List<Book> with downloaded books
 */

public class QueryUtils {

    public static final String LOG_TAG = MainActivity.class.getName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Google dataset
     * @return an {@link List<Book>} object to represent a single book.
     * @param requestUrl is a String url to perform a HTTP Request
     */
    public static List<Book> fetchBooksData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link List<Book>} object
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    /**
     * @return  a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     * @param bookJSON is a JSON String from witch we will create a list of books
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items (or books).
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            // For each earthquake in the earthquakeArray, create an {@link Book} object
            for (int i = 0; i < booksArray.length(); i++) {


                // Get a single book at position i within the list of books
                JSONObject currentBookInfo = booksArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all properties
                // for that book.
                JSONObject currentBook = currentBookInfo.getJSONObject("volumeInfo");

                String title = "There is no title for this book";
                if(currentBook.has("title")){
                    // Extract the value for the key called "title"
                    title = currentBook.getString("title");
                }

                String description = "There is no description for this book.";
                if(currentBook.has("description")){
                    // Extract the value for the key called "description"
                    description = currentBook.getString("description");
                }

                String author="There are no author for this book";
                if(currentBook.has("authors")) {
                    author = "";
                    JSONArray authors = currentBook.getJSONArray("authors");
                    for (int j = 0; j < authors.length(); j++) {
                        // Extract the value for the key called "authors"
                        author += authors.get(j).toString();
                        author += " ";
                    }
                }

                String url = "";
                if(currentBook.has("previewLink")) {
                    // Extract the value for the key called "url"
                     url = currentBook.getString("previewLink");
                }

                // Create a new {@link Book} object with the title, authors, description,
                // and url from the JSON response.
                Book book = new Book(title,author,description,url);

                // Add the new {@link Book} to the list of books.
                books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of books
        return books;
    }

    /**
     * @return new URL object from the given string URL.
     * @param stringUrl is an String url to be converted into {@link URL} Object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     * @return JSON String file to extract information about books
     * @param url is an URL to perform an HTTP Request
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If a url is null do not send request
        if(url == null)return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if(urlConnection.getResponseCode()>=200 && urlConnection.getResponseCode()<400) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else throw new IOException();
        } catch (IOException e) {
            Log.e(LOG_TAG,"IOException",e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     * @return The JSON String built from InputStream
     * @param inputStream the ImputStream from witch we will create a String object
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
