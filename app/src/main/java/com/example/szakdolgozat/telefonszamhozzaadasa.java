package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class telefonszamhozzaadasa extends AppCompatActivity {

    Button feltoltes;
    EditText telefonszam;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefonszamhozzaadasa);
        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("nev");
        String kapottneptunkod = intent.getStringExtra("neptunkod");

        feltoltes = findViewById(R.id.telefonszamhozzaadsagomb);
        telefonszam = findViewById(R.id.telefonszamtext);

        feltoltes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(kapottnev, kapottneptunkod, telefonszam.getText().toString());
                adatbazis = FirebaseDatabase.getInstance();
                databaseReference = adatbazis.getReference("Felhasznalokepekkel");
                databaseReference.child(kapottnev).setValue(felhasznalotelefonszamokkal).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(telefonszamhozzaadasa.this, "Telefonszám hozzáadása sikerült!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}