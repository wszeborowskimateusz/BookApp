package com.example.android.bookapp;

/**
 * Class to represent a single book
 */

public class Book {

    /*Title of a single book*/
    private String mTitle;

    /*Author of a single book*/
    private String mAuthor;

    /*Description of a single book*/
    private String mDescription;

    /**
     * Public constructor to create a single book
     * @param mTitle is the title of a book
     * @param mAuthor is the author of a book
     * @param mDescription is a descriptionof a book
     */
    public Book(String mTitle, String mAuthor, String mDescription) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mDescription = mDescription;
    }

    /**
     * @return the title of a book
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * @return the author of a book
     */
    public String getmAuthor() {
        return mAuthor;
    }

    /**
     * @return the description of a book
     */
    public String getmDescription() {
        return mDescription;
    }
}
