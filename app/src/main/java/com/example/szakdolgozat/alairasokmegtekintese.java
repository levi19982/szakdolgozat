package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class alairasokmegtekintese extends ListActivity {

    ArrayList<String> elsokep = new ArrayList<>();
    ArrayList<String> masodikkep = new ArrayList<>();
    ArrayList<String> jelentkezettekneptun = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alairasokmegtekintese);

        Intent intent = getIntent();
        String idopont = intent.getStringExtra("kapottidopont2");
        String sport = intent.getStringExtra("kapottsportag2");

        ListView listView = getListView();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(sport).child(idopont);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChild("neptunkod")) {
                        String neptunkod = dataSnapshot.child("neptunkod").getValue().toString();
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    if (dataSnapshot1.hasChild("neptunkod")) {
                                        String neptunkod1 = dataSnapshot1.child("neptunkod").getValue().toString();
                                        if (neptunkod.equals(neptunkod1)) {
                                            elsokep.add(dataSnapshot.child("keplink").getValue().toString());
                                            masodikkep.add(dataSnapshot1.child("keplink").getValue().toString());
                                            jelentkezettekneptun.add(neptunkod);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listaadapter adapter = new listaadapter(this, elsokep, masodikkep, jelentkezettekneptun);
        listView.setAdapter(adapter);
        Toast.makeText(alairasokmegtekintese.this, elsokep.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(alairasokmegtekintese.this, masodikkep.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(alairasokmegtekintese.this, jelentkezettekneptun.toString(), Toast.LENGTH_SHORT).show();
    }
}