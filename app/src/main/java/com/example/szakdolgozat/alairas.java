package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class alairas extends AppCompatActivity {

    SignaturePad signaturePad;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    ImageView imageView1;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app/");
    Uri kepuri;
    private final int PICK_IMAGE_REQUEST = 22;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alairas);
        Button button = findViewById(R.id.alairasfeltoltes);
        signaturePad = findViewById(R.id.alairashelye);
        imageView1 = findViewById(R.id.imageView);

        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("kapottnev1");
        String kapottneptunkod = intent.getStringExtra("kapottneptunkod1");
        String kapottelefonszam = intent.getStringExtra("kapotttelefonszam");

        SelectImage();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = signaturePad.getSignatureBitmap();
                //imageView1.setImageBitmap(bitmap);
                signaturePad.clear();
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, kapottnev + " " + kapottneptunkod, "");
                /*String keplink = "példaaaaaaaa";
                adatbazis = FirebaseDatabase.getInstance();
                databaseReference = adatbazis.getReference("Felhasznalokepekkel");
                StorageReference storageReference2 = storageReference.child("Felhasznalokepekkel").child(kapottnev);
                Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(kapottnev, kapottneptunkod, kapottelefonszam, keplink);
                storageReference2.putFile()*/
        }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

}
