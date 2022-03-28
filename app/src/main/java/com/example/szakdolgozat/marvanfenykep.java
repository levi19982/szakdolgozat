package com.example.szakdolgozat;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class marvanfenykep extends AppCompatActivity {

    TextView nev, neptunkod, sport;
    StorageReference storageReference;
    ImageView imageView, mostanikep;
    Button osszehasonlit, jelentkezes;
    private Spinner spinner;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();

    FaceDetectorOptions detectorOptions;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marvanfenykep);

        Intent intent = getIntent();
        String kapottnev = intent.getStringExtra("nev");
        String kapottneptunkod = intent.getStringExtra("neptunkod");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference[] databaseReference = {firebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel").child(kapottnev)};
        DatabaseReference kepbetoltes = databaseReference[0].child("keplink");


        nev = findViewById(R.id.nevszoveg);
        jelentkezes = findViewById(R.id.jelentkezesgomb);
        sport = findViewById(R.id.valasztottsportagszoveg);
        neptunkod = findViewById(R.id.neptunmkodszoveg);
        storageReference = FirebaseStorage.getInstance().getReference().child(kapottnev);
        imageView = findViewById(R.id.fenykep);
        mostanikep = findViewById(R.id.mostkeszitett);
        osszehasonlit = findViewById(R.id.osszehasonlitgomb);
        osszehasonlit.setEnabled(false);
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

        nev.setText(kapottnev);
        neptunkod.setText(kapottneptunkod);

        detectorOptions = new FaceDetectorOptions.Builder().setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

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
                mGetContent.launch("image/*");
            }
        });

        jelentkezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minta = "yyyy-M-dd";
                DateFormat dateFormat = new SimpleDateFormat(minta);
                Date mainap = Calendar.getInstance().getTime();
                String maidatum = dateFormat.format(mainap);
                String sportagstring = sport.getText().toString();
                jelentkezettek jelentkezettek = new jelentkezettek(kapottnev, kapottneptunkod);
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(sportagstring).child(maidatum);
                adatbazis = FirebaseDatabase.getInstance();
                databaseReference2 = adatbazis.getReference("Sportok").child(sportagstring).child(maidatum);
                databaseReference2.child(kapottnev).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(marvanfenykep.this, "Sikeresen hozzáadva az időpont a következő sportághoz: " + sportagstring, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void processImage(InputImage image){
        FaceDetector detector = FaceDetection.getClient();

        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        displayFaces(faces);
                                    }
                                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
    }

    public void displayFaces(List<Face> faces){
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap, 0, 0, null);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3.0f);

        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            canvas.drawRect(bounds, paint);
        }
        mostanikep.setImageBitmap(bitmap2);
        if (faces.size() == 1) {
            Toast.makeText(marvanfenykep.this, "Sikeresen felismertem az arcát a képen!", Toast.LENGTH_SHORT).show();
            osszehasonlit.setEnabled(true);
        }
        else if (faces.size() < 1)
        {
            Toast.makeText(marvanfenykep.this, "Kérem próbálkozzon egy másik fényképpel!", Toast.LENGTH_SHORT).show();
        }
        else if (faces.size() > 1)
        {
            Toast.makeText(marvanfenykep.this, "Túl sok arcot találtam a képen!", Toast.LENGTH_SHORT).show();
        }
    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        bitmap = getBitmapFromUri(uri);
                        InputImage inputImage = InputImage.fromFilePath(getApplicationContext(), uri);

                        processImage(inputImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            Uri kep = data.getData();
            mostanikep.setImageURI(kep);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}