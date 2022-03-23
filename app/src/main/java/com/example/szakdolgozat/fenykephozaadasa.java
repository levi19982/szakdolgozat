package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class fenykephozaadasa extends AppCompatActivity {

    Button feltoltes;
    ImageView feltoltottkep;
    ProgressBar toltes;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenykephozaadasa);

        feltoltes = findViewById(R.id.feltoltes);
        feltoltottkep = findViewById(R.id.feltoltenikivantkep);
        toltes = findViewById(R.id.tolteskep);
        toltes.setVisibility(View.INVISIBLE);



        feltoltottkep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kepintent = new Intent();
                kepintent.setAction(Intent.ACTION_GET_CONTENT);
                kepintent.setType("image/*");
                startActivityForResult(kepintent, 2);
            }
        });

        feltoltes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null){
                    kepfeltolteseadatbazisba(uri);
                }else {
                     Toast.makeText(fenykephozaadasa.this, "Nincs kiválasztva kép!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void kepfeltolteseadatbazisba(Uri kepuri) {

        StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(kepuri));
        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("nev");
        String kapottneptunkod = intent.getStringExtra("neptunkod");
        storageReference2.putFile(kepuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Felhasznalokepekkel felhasznalokepekkel = new Felhasznalokepekkel(kapottnev, kapottneptunkod, uri.toString());
                        adatbazis = FirebaseDatabase.getInstance();
                        //String felhasznalokeppel = databaseReference.push().getKey();
                        databaseReference = adatbazis.getReference("Felhasznalokepekkel");
                        databaseReference.child(kapottnev).setValue(felhasznalokepekkel);

                        Toast.makeText(fenykephozaadasa.this, "Fénykép hozzáadása sikerült!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(fenykephozaadasa.this, "Fénykép feltöltése nem sikerült", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri2){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri2));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            feltoltottkep.setImageURI(uri);

        }
    }
}