package com.example.szakdolgozat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class telefonszamhozzaadasa extends AppCompatActivity {

    Button feltoltes;
    EditText telefonszam;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefonszamhozzaadasa);

        Intent telefonszamhozzaadasaseged = getIntent();
        String kapottnev = telefonszamhozzaadasaseged.getStringExtra("nev");
        String kapottneptunkod = telefonszamhozzaadasaseged.getStringExtra("neptunkod");

        feltoltes = findViewById(R.id.telefonszamhozzaadsagomb);
        telefonszam = findViewById(R.id.telefonszamtext);

        feltoltes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(kapottnev, kapottneptunkod, telefonszam.getText().toString(), null);
                int hossz = telefonszam.length();
                if (hossz == 16) {
                    databaseReference.child("Felhasznalokepekkel").child(kapottneptunkod).setValue(felhasznalotelefonszamokkal).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(telefonszamhozzaadasa.this, "Telefonszám sikeresen hozzáadva!", Toast.LENGTH_SHORT).show();
                            Intent alairashoz = new Intent(telefonszamhozzaadasa.this, alairas.class);
                            alairashoz.putExtra("kapottnev1", kapottnev);
                            alairashoz.putExtra("kapottneptunkod1", kapottneptunkod);
                            alairashoz.putExtra("kapotttelefonszam", telefonszam.getText().toString());
                            startActivity(alairashoz);
                        }
                    });
                } else {
                    Toast.makeText(telefonszamhozzaadasa.this, "Kérjük helyesen adja meg telefonszámát!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}