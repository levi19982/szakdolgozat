package com.example.szakdolgozat;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

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
import java.util.concurrent.TimeUnit;

public class sajatalairasok extends AppCompatActivity {

    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    DatabaseReference databaseReference1, databaseReference2, databaseReference3;
    TextView alairasok,ijaszat, konditerem, tanc;
    EditText nev;
    Button alairasokszam;
    ArrayList<String> nevek = new ArrayList<>();
    ArrayList<String> nevek2 = new ArrayList<>();
    ArrayList<String> nevek3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sajatalairasok);

        alairasok = findViewById(R.id.osszesalairas);
        konditerem = findViewById(R.id.textView4);
        tanc = findViewById(R.id.textView6);
        nev = findViewById(R.id.kapottnev);
        alairasokszam = findViewById(R.id.alairasokszama);
        ijaszat = findViewById(R.id.ijaszatalairas);
        databaseReference1 = databaseReference.child("Sportok").child("Íjászat");
        databaseReference2 = databaseReference.child("Sportok").child("Konditerem");
        databaseReference3 = databaseReference.child("Sportok").child("Tánc");


        alairasokszam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nev.getText().toString().isEmpty()) {
                    Toast.makeText(sajatalairasok.this, "Kérjük írja be nevét!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int elofordulasok = 0;
                            ijaszat.setText("");
                            nevek.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child(nev.getText().toString()).exists()) {
                                    nevek.add(dataSnapshot.child(nev.getText().toString()).child("nev").getValue().toString());
                                    elofordulasok++;
                                }
                            }
                            ijaszat.setText(Integer.toString(elofordulasok));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int elofordulasok2 = 0;
                            konditerem.setText("");
                            nevek2.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child(nev.getText().toString()).exists()) {
                                    nevek2.add(dataSnapshot.child(nev.getText().toString()).child("nev").getValue().toString());
                                    elofordulasok2++;
                                }
                            }
                            konditerem.setText(Integer.toString(elofordulasok2));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int elofordulasok3 = 0;
                            tanc.setText("");
                            nevek3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child(nev.getText().toString()).exists()) {
                                    nevek3.add(dataSnapshot.child(nev.getText().toString()).child("nev").getValue().toString());
                                    elofordulasok3++;
                                }
                            }
                            tanc.setText(Integer.toString(elofordulasok3));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    int osszesalairas = 0;
                    if (!ijaszat.getText().toString().equals("")) {
                            osszesalairas += Integer.parseInt(ijaszat.getText().toString());
                        }
                        if (!konditerem.getText().toString().equals("")) {
                            osszesalairas += Integer.parseInt(konditerem.getText().toString());
                        }
                        if (!tanc.getText().toString().equals("")) {
                            osszesalairas += Integer.parseInt(tanc.getText().toString());
                        }
                    alairasok.setText(String.valueOf(osszesalairas));
                }
            }
        });
    }
}