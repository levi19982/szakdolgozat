package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
                String keplink = null;
                Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(kapottnev, kapottneptunkod, telefonszam.getText().toString(), keplink);
                adatbazis = FirebaseDatabase.getInstance();
                databaseReference = adatbazis.getReference("Felhasznalokepekkel");
                int hossz = telefonszam.length();
                if (hossz == 16) {
                    databaseReference.child(kapottnev).setValue(felhasznalotelefonszamokkal).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(telefonszamhozzaadasa.this, "Telefonszám sikeresen hozzáadva!", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(telefonszamhozzaadasa.this, alairas.class);
                            intent1.putExtra("kapottnev1", kapottnev);
                            intent1.putExtra("kapottneptunkod1", kapottneptunkod);
                            intent1.putExtra("kapotttelefonszam", telefonszam.getText().toString());
                            startActivity(intent1);
                        }
                    });
                } else {
                    Toast.makeText(telefonszamhozzaadasa.this, "Kérjük helyesen adja meg telefonszámát!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}