package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class sajatalairasok extends AppCompatActivity {

    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    DatabaseReference databaseReference1, databaseReference2, databaseReference3;
    TextView alairasok;
    EditText nev;
    Button alairasokszam;
    int elofordulasok = 0;
    ArrayList<String> nevek = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sajatalairasok);

        alairasok = findViewById(R.id.osszesalairas);
        nev = findViewById(R.id.kapottnev);
        alairasokszam = findViewById(R.id.alairasokszama);
        databaseReference1 = databaseReference.child("Sportok").child("Íjászat");


        alairasokszam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nevek.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if (dataSnapshot.child(nev.getText().toString()).exists()){
                                nevek.add(dataSnapshot.child(nev.getText().toString()).child("nev").getValue().toString());
                                elofordulasok++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}