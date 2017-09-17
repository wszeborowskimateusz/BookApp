package com.example.android.bookapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String usersInput;
    private static String GOOGLE_URL = "https://www.googleapis.com/books/v1/volumes?q=&maxResults=10";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Book> books = new ArrayList<Book>();



        BookAdapter adapt = new BookAdapter(this,books);

        /*Find the ListView to set the adapter on*/
        ListView view = (ListView) findViewById(R.id.books_list);
        view.setAdapter(adapt);

        /*Finding the search button*/
        Button searchButton = (Button)findViewById(R.id.search_button);
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

                }
            }
        });

    }
}
