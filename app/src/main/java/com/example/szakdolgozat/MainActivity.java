package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.szakdolgozat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String nev, neptunkod;
    TextView neptunkodszoveg, nevszoveg;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        neptunkodszoveg = findViewById(R.id.neptunkodmezo);
        nevszoveg = findViewById(R.id.nevmezo);


        binding.fenykephozzaadasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nev = binding.nevmezo.getText().toString();
                neptunkod = binding.neptunkodmezo.getText().toString();
                String vanefenykepnev = nev;
                databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel").child(vanefenykepnev);
                databaseReference.child(vanefenykepnev);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!nev.isEmpty() && !neptunkod.isEmpty() && snapshot.getValue() == null){
                            Felhasznalok felhasznalok = new Felhasznalok(nev, neptunkod);
                            databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
                            adatbazis = FirebaseDatabase.getInstance();
                            databaseReference = adatbazis.getReference("Felhasznalok");
                            databaseReference.child(nev).setValue(felhasznalok).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(MainActivity.this, fenykephozaadasa.class);
                                    intent.putExtra("nev", nev);
                                    intent.putExtra("neptunkod", neptunkod);
                                    startActivity(intent);
                                    binding.nevmezo.setText("");
                                    binding.neptunkodmezo.setText("");
                                }
                            });

                        } else if  (!nev.isEmpty() && neptunkod.isEmpty()){
                            Toast.makeText(MainActivity.this, "Kérjük írja be Neptun kódját!", Toast.LENGTH_SHORT).show();
                        } else if  (nev.isEmpty() && !neptunkod.isEmpty()){
                            Toast.makeText(MainActivity.this, "Kérjük írja be nevét!", Toast.LENGTH_SHORT).show();
                        } else if  (nev.isEmpty() && neptunkod.isEmpty()){
                            Toast.makeText(MainActivity.this, "Kérjük töltse ki a mezőket!", Toast.LENGTH_SHORT).show();
                        } else if (!nev.isEmpty() && !neptunkod.isEmpty() && snapshot.getValue() != null) {
                            Intent intent = new Intent(MainActivity.this, marvanfenykep.class);
                            intent.putExtra("nev", nev);
                            intent.putExtra("neptunkod", neptunkod);
                            startActivity(intent);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        neptunkodszoveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nev2 = nevszoveg.getText().toString();
                if (nev2.isEmpty()){
                    Toast.makeText(MainActivity.this, "Kérjük írja be nevét!", Toast.LENGTH_SHORT).show();
                } else {
                databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalok").child(nev2);
                databaseReference.child(nev2);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null){
                        String neptunkodszoveg2 = snapshot.child("neptunkod").getValue().toString();
                        neptunkodszoveg.setText(neptunkodszoveg2);}
                        else {
                            Toast.makeText(MainActivity.this, "Nincs ilyen név!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }}
        });

    }
}