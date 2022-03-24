package com.example.szakdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class marvanfenykep extends AppCompatActivity {

    Intent intent = getIntent();
    String kapottnev = intent.getStringExtra("nev");
    String kapottneptunkod = intent.getStringExtra("neptunkod");
    TextView nev, neptunkod;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marvanfenykep);

        nev = findViewById(R.id.nevszoveg);
        neptunkod = findViewById(R.id.neptunmkodszoveg);
        storageReference = FirebaseStorage.getInstance().getReference().child(kapottnev);

        nev.setText(kapottnev);
        neptunkod.setText(kapottneptunkod);

    }
}