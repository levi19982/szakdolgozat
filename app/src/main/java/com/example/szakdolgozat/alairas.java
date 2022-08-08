package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class alairas extends AppCompatActivity {

    SignaturePad signaturePad;
    FirebaseDatabase adatbazis;
    ImageView imageView1;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app/");
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 22;
    Button feltolto;
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alairas);
        Button button = findViewById(R.id.alairasfeltoltes);
        signaturePad = findViewById(R.id.alairashelye);
        imageView1 = findViewById(R.id.imageView);
        feltolto = findViewById(R.id.feltoltesgomb);
        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("kapottnev1");
        String kapottneptunkod = intent.getStringExtra("kapottneptunkod1");
        String kapottelefonszam = intent.getStringExtra("kapotttelefonszam");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = signaturePad.getSignatureBitmap();
                signaturePad.clear();
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, kapottnev + " " + kapottneptunkod, "");
                SelectImage();
                /*String keplink = "példaaaaaaaa";
                adatbazis = FirebaseDatabase.getInstance();
                databaseReference = adatbazis.getReference("Felhasznalokepekkel");
                StorageReference storageReference2 = storageReference.child("Felhasznalokepekkel").child(kapottnev);
                Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(kapottnev, kapottneptunkod, kapottelefonszam, keplink);
                storageReference2.putFile()*/
        }
        });
        feltolto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kepfeltoltes();
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void kepfeltoltes(){
        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("kapottnev1");
        String kapottneptunkod = intent.getStringExtra("kapottneptunkod1");
        String kapottelefonszam = intent.getStringExtra("kapotttelefonszam");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference("Felhasznalokepekkel").child(kapottnev);
            StorageReference storageReference = reference.child("Aláírások").child(kapottnev + " " + kapottneptunkod);
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String keplink = uri.toString();
                            Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(kapottnev, kapottneptunkod, kapottelefonszam, keplink);
                            adatbazis = FirebaseDatabase.getInstance();
                            databaseReference1 = adatbazis.getReference("Felhasznalokepekkel");
                            databaseReference1.child(kapottnev).setValue(felhasznalotelefonszamokkal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Sikerült feltölteni", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data){
                imageUri = data.getData();
                imageView1.setImageURI(imageUri);
        }
    }
}
