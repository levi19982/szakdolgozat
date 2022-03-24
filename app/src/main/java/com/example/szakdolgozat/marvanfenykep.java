package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class marvanfenykep extends AppCompatActivity {

    TextView nev, neptunkod;
    StorageReference storageReference;
    ImageView imageView, mostanikep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marvanfenykep);

        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("nev");
        String kapottneptunkod = intent.getStringExtra("neptunkod");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel").child(kapottnev);
        DatabaseReference kepbetoltes = databaseReference.child("keplink");

        nev = findViewById(R.id.nevszoveg);
        neptunkod = findViewById(R.id.neptunmkodszoveg);
        storageReference = FirebaseStorage.getInstance().getReference().child(kapottnev);
        imageView = findViewById(R.id.fenykep);
        mostanikep = findViewById(R.id.mostkeszitett);

        nev.setText(kapottnev);
        neptunkod.setText(kapottneptunkod);

        kepbetoltes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                Picasso.get().load(link).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(marvanfenykep.this, "Nem sikerült betölteni a képet!", Toast.LENGTH_SHORT).show();
            }
        });

        mostanikep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kepintent = new Intent();
                kepintent.setAction(Intent.ACTION_GET_CONTENT);
                kepintent.setType("image/*");
                startActivityForResult(kepintent, 2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            Uri kep = data.getData();
            mostanikep.setImageURI(kep);
        }
    }
}