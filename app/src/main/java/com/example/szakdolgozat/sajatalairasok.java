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

public class sajatalairasok extends AppCompatActivity {

    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    TextView alairasok;
    EditText nev;
    Button alairasokszam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sajatalairasok);

        alairasok = findViewById(R.id.osszesalairas);
        nev = findViewById(R.id.kapottnev);
        alairasokszam = findViewById(R.id.alairasokszama);

        alairasokszam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = databaseReference.orderByChild("nev").equalTo(nev.getText().toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int alairasszam = (int) snapshot.getChildrenCount();
                        alairasok.setText(String.valueOf(alairasszam));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}