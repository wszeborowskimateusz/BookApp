package com.example.android.bookapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Book> books = new ArrayList<Book>();
        books.add(new Book("Kubus Puchatek","Jan Brzechwa","Bardzo fajna ksiazka z wieloma zwrotami akcji.Bardzo fajna ksiazka z wieloma zwrotami akcji"));
        books.add(new Book("Kubus Prawdziwek","Andrzej Wajda","Ksiazka taka srednia"));
        books.add(new Book("Marsz mieczy przez pustkowie","Nicol Kidman","Niby ksiazka niby nie"));
        books.add(new Book("Kubus Prawdziwek","Andrzej Wajda","Ksiazka taka srednia"));
        books.add(new Book("Marsz mieczy przez pustkowie","Nicol Kidman","Niby ksiazka niby nie"));

        BookAdapter adapt = new BookAdapter(this,books);

        ListView view = (ListView) findViewById(R.id.books_list);
        view.setAdapter(adapt);
    }
}
