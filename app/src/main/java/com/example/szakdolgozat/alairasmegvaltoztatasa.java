package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class alairasmegvaltoztatasa extends AppCompatActivity {

    SignaturePad signaturePad;
    FirebaseDatabase adatbazis;
    ImageView imageView1;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app/");
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 23;
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alairasmegvaltoztatasa);

        Button button = findViewById(R.id.alairasfeltoltess);
        signaturePad = findViewById(R.id.alairashelye);
        imageView1 = findViewById(R.id.imageView);
        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("nev");
        String kapottneptunkod = intent.getStringExtra("neptunkod");

        AlertDialog alertDialog1 = new AlertDialog.Builder(alairasmegvaltoztatasa.this).create();
        alertDialog1.setTitle("Aláírás megváltoztatása");
        alertDialog1.setMessage("A folytatáshoz kérjük változtassa meg az aláírását!");
        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Rendben!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog1.dismiss();
            }
        });
        alertDialog1.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alairasmegerosites = new AlertDialog.Builder(alairasmegvaltoztatasa.this).create();
                alairasmegerosites.setTitle("Megerősítés");
                alairasmegerosites.setMessage("Biztos szeretnéd ezzel az aláírással folytatni? Később nem lesz lehetőség a módosításra!");
                alairasmegerosites.setButton(AlertDialog.BUTTON_POSITIVE, "Igen!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bitmap bitmap = signaturePad.getSignatureBitmap();
                        signaturePad.clear();
                        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, kapottnev + " " + kapottneptunkod, "");
                        SelectImage();
                    }
                });
                alairasmegerosites.setButton(AlertDialog.BUTTON_NEGATIVE, "Nem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alairasmegerosites.dismiss();
                        signaturePad.clear();
                    }
                });
                alairasmegerosites.show();
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
        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getPath() + File.separator + "Screenshots"), "image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void kepfeltoltes(){
        Intent intent = getIntent();
        String kapottneptunkod = intent.getStringExtra("neptunkod");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel").child(kapottneptunkod);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nev;
                String neptunkod = kapottneptunkod;
                String telefonszam;
                nev = snapshot.child("nev").getValue().toString();
                telefonszam = snapshot.child("telefonszam").getValue().toString();
                StorageReference storageReference = reference.child("Aláírások").child(nev + " " + neptunkod);
                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String keplink = uri.toString();
                                Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(nev, neptunkod, telefonszam, keplink);
                                adatbazis = FirebaseDatabase.getInstance();
                                databaseReference1 = adatbazis.getReference("Felhasznalokepekkel");
                                databaseReference1.child(neptunkod).setValue(felhasznalotelefonszamokkal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Sikerült feltölteni", Toast.LENGTH_SHORT).show();
                                        Intent intent13 = new Intent(alairasmegvaltoztatasa.this, MainActivity.class);
                                        startActivity(intent13);
                                    }
                                });
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data){
            imageUri = data.getData();
            imageView1.setImageURI(imageUri);
            kepfeltoltes();
        }
    }
}
