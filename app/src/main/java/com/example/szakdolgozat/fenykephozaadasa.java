package com.example.szakdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class fenykephozaadasa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenykephozaadasa);

        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("nev");
        String kapottneptunkod = intent.getStringExtra("neptunkod");

        /*Toast.makeText(this, kapottnev, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, kapottneptunkod, Toast.LENGTH_SHORT).show();*/
        
    }
}