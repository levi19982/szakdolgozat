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
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class marvantelefonszam extends AppCompatActivity {

    TextView nev, neptunkod, sport;
    StorageReference storageReference;
    Button  jelentkezes, verify, kepfeltoltes;
    private Spinner spinner;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    FirebaseAuth firebaseAuth;
    EditText editText2;
    TextView editText;
    String verificationId;
    SignaturePad signaturePad;
    private final int PICK_IMAGE_REQUEST = 22;
    ImageView alairas;
    Uri imageUri;
    StorageReference reference = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marvantelefonszam);
        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("nev");
        String kapottneptunkod = intent.getStringExtra("neptunkod");
        String kapottnev2 = kapottnev;

        firebaseAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.idEdtPhoneNumber);
        editText2 = findViewById(R.id.idEdtOtp);
        verify = findViewById(R.id.idBtnVerify);
        kepfeltoltes = findViewById(R.id.alairasfeltoltes);
        signaturePad = findViewById(R.id.alairashelyee);
        alairas = findViewById(R.id.imageView);
        kepfeltoltes.setEnabled(false);

                databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel").child(kapottnev2);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phone = snapshot.child("telefonszam").getValue().toString();
                        editText.setText(phone);
                        sendVerificationCode(phone);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText2.getText().toString())) {
                    Toast.makeText(marvantelefonszam.this, "Kód:", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(editText2.getText().toString());
                }
            }
        });


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference[] databaseReference = {firebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel").child(kapottnev)};
        DatabaseReference kepbetoltes = databaseReference[0].child("keplink");


        nev = findViewById(R.id.nevszoveg);
        jelentkezes = findViewById(R.id.jelentkezesgomb);
        jelentkezes.setEnabled(false);
        sport = findViewById(R.id.valasztottsportagszoveg);
        neptunkod = findViewById(R.id.neptunmkodszoveg);
        storageReference = FirebaseStorage.getInstance().getReference().child(kapottnev);
        spinner = findViewById(R.id.sportagvalaszto);
        String[] sportok = getResources().getStringArray(R.array.sportok);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sportok);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        sport.setText(spinner.getSelectedItem().toString());
                        break;
                    case 1:
                        sport.setText(spinner.getSelectedItem().toString());
                        break;
                    case 2:
                        sport.setText(spinner.getSelectedItem().toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kepfeltoltes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alairasmegerosites = new AlertDialog.Builder(marvantelefonszam.this).create();
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

        nev.setText("Hallgató neve: " + kapottnev);
        neptunkod.setText("Hallgató Neptun kódja: " + kapottneptunkod);
        jelentkezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String felhasznaloidopont = simpleDateFormat.format(calendar.getTime());
                String minta = "yyyy-MM-dd";
                String eltottiido = null;
                String kijelentkezesiidopont = null;
                String keplink = null;
                DateFormat dateFormat = new SimpleDateFormat(minta);
                Date mainap = Calendar.getInstance().getTime();
                String maidatum = dateFormat.format(mainap);
                String sportagstring = sport.getText().toString();
                jelentkezettek jelentkezettek = new jelentkezettek(kapottnev, kapottneptunkod, felhasznaloidopont, kijelentkezesiidopont, eltottiido, keplink);
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(sportagstring).child(maidatum);
                adatbazis = FirebaseDatabase.getInstance();
                databaseReference2 = adatbazis.getReference("Sportok").child(sportagstring).child(maidatum);
                databaseReference2.child(kapottnev).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
                StorageReference storageReference = reference.child("Sportok").child(sportagstring).child(maidatum).child(kapottnev + " " + kapottneptunkod);
                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String keplink = uri.toString();
                                jelentkezettek jelentkezettek = new jelentkezettek(kapottnev, kapottneptunkod, felhasznaloidopont, kijelentkezesiidopont, eltottiido, keplink);
                                adatbazis = firebaseDatabase.getInstance();
                                databaseReference1 = adatbazis.getReference("Sportok").child(sportagstring).child(maidatum);
                                databaseReference1.child(kapottnev).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(marvantelefonszam.this, "Sikeresen hozzáadva az időpont a következő sportághoz: " + sportagstring, Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(marvantelefonszam.this, MainActivity.class);
                                        startActivity(intent1);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    private void SelectImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getPath() + File.separator + "Screenshots"), "image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data){
            imageUri = data.getData();
            alairas.setImageURI(imageUri);
            jelentkezes.setEnabled(true);
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            kepfeltoltes.setEnabled(true);
                            Toast.makeText(marvantelefonszam.this, "Sikeres, már csak az aláírás hiányzik!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(marvantelefonszam.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(number)
                        .setTimeout(2L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                editText.setText(code);

                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(marvantelefonszam.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

        private void verifyCode(String code) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            signInWithCredential(credential);
        }
    }
