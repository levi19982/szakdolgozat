package com.example.szakdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class alairasokmegtekintese extends ListActivity {

    ArrayList<String> elsokep = new ArrayList<>();
    ArrayList<String> masodikkep = new ArrayList<>();
    ArrayList<String> jelentkezettekneptun = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alairasokmegtekintese);

        ListView listView = (ListView) findViewById(R.id.jelentkezettekalairas);

        listaadapter adapter = new listaadapter(this, elsokep, masodikkep, jelentkezettekneptun);
        listView.setAdapter(adapter);

    }
}