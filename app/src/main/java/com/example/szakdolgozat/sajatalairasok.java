package com.example.szakdolgozat;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.JumpLine;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class sajatalairasok extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    DatabaseReference databaseReference1, databaseReference2, databaseReference3;
    TextView alairasok,ijaszat, konditerem, tanc, osszesalairasseged;
    EditText emailmegadasa;
    Button alairasokszam, alairasokletoltese, kuldesgomb;
    ArrayList<String> nevek = new ArrayList<>();
    ArrayList<String> nevek2 = new ArrayList<>();
    ArrayList<String> nevek3 = new ArrayList<>();
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    AutoCompleteTextView nev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sajatalairasok);

        alairasok = findViewById(R.id.osszesalairas);
        osszesalairasseged = findViewById(R.id.textView8);
        konditerem = findViewById(R.id.textView4);
        tanc = findViewById(R.id.textView6);
        nev = findViewById(R.id.kapottnev);
        alairasokszam = findViewById(R.id.alairasokszama);
        ijaszat = findViewById(R.id.ijaszatalairas);
        alairasokletoltese = findViewById(R.id.sajatalairasokletoltsegomb);
        databaseReference1 = databaseReference.child("Sportok").child("Íjászat");
        databaseReference2 = databaseReference.child("Sportok").child("Konditerem");
        databaseReference3 = databaseReference.child("Sportok").child("Tánc");

        databaseReference.child("Felhasznalokepekkel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> proba = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String nevproba = dataSnapshot.child("neptunkod").getValue().toString();
                    proba.add(nevproba);
                }
                nev.setAdapter(new ArrayAdapter<String>(sajatalairasok.this, android.R.layout.simple_list_item_1, proba));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        alairasokletoltese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailkuldese();
            }
        });


        alairasokszam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nev.getText().toString().isEmpty()) {
                    Toast.makeText(sajatalairasok.this, "Kérjük írja be nevét!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int elofordulasok = 0;
                            ijaszat.setText("");
                            nevek.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        String valami = dataSnapshot1.getKey().toString();
                                        if (!valami.equals("datum")) {
                                            String valami1 = dataSnapshot1.child("neptunkod").getValue().toString();
                                            if (valami1.equals(nev.getText().toString())){
                                                elofordulasok++;
                                            }
                                        }
                                }
                            }
                            ijaszat.setText(Integer.toString(elofordulasok));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int elofordulasok2 = 0;
                            konditerem.setText("");
                            nevek2.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    String valami = dataSnapshot1.getKey().toString();
                                    if (!valami.equals("datum")) {
                                        String valami1 = dataSnapshot1.child("neptunkod").getValue().toString();
                                        if (valami1.equals(nev.getText().toString())){
                                            elofordulasok2++;
                                        }
                                    }
                                }
                            }
                            konditerem.setText(Integer.toString(elofordulasok2));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int elofordulasok3 = 0;
                            tanc.setText("");
                            nevek3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    String valami = dataSnapshot1.getKey().toString();
                                    if (!valami.equals("datum")) {
                                            String valami1 = dataSnapshot1.child("neptunkod").getValue().toString();
                                        if (valami1.equals(nev.getText().toString())){
                                            elofordulasok3++;
                                        }
                                    }
                                }
                            }
                            tanc.setText(Integer.toString(elofordulasok3));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int osszesalairas = 0;
                            if (!ijaszat.getText().toString().equals("")) {
                                osszesalairas += Integer.parseInt(ijaszat.getText().toString());
                            }
                            if (!konditerem.getText().toString().equals("")) {
                                osszesalairas += Integer.parseInt(konditerem.getText().toString());
                            }
                            if (!tanc.getText().toString().equals("")) {
                                osszesalairas += Integer.parseInt(tanc.getText().toString());
                            }
                            alairasok.setText("Összes aláírásainak száma: " + String.valueOf(osszesalairas));
                            osszesalairasseged.setText(String.valueOf(osszesalairas));
                        }
                    }, 500);
                }
            }
        });
    }
    public void emailkuldese(){
        if (!nev.getText().toString().isEmpty()) {
            if (!alairasok.getText().toString().isEmpty()) {
                builder = new AlertDialog.Builder(this);
                View view2 = getLayoutInflater().inflate(R.layout.emailmegadasa, null);
                emailmegadasa = view2.findViewById(R.id.emailkuldeseidegomb);
                kuldesgomb = view2.findViewById(R.id.button);
                builder.setView(view2);
                alertDialog = builder.create();
                alertDialog.show();
                kuldesgomb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        emailkuldesemasik();
                    }
                });
            }
            else {
                Toast.makeText(sajatalairasok.this, "Kérjük először futtassa le az Aláírások számát!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(sajatalairasok.this, "Kérjük írja be a nevét!", Toast.LENGTH_SHORT).show();
        }
    }
    public void emailkuldesemasik(){
        File file = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        StringBuilder stringBuilder = new StringBuilder();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int seged = Integer.parseInt(osszesalairasseged.getText().toString());
                if (seged < 1){
                    Toast.makeText(sajatalairasok.this, "Nincs egy aláírása sem!", Toast.LENGTH_SHORT).show();
                } else {
                    /*for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            String valami = dataSnapshot1.getKey().toString();
                            if (!valami.equals("datum")) {
                                String valami1 = dataSnapshot1.child("neptunkod").getValue().toString();
                                if (valami1.equals(nev.getText().toString())){
                                    elofordulasok3++;
                                }
                            }
                        }
                    }*/
                    stringBuilder.append("Ijaszat idopontjai" + "\n");
                    for (DataSnapshot dataSnapshot : snapshot.child("Íjászat").getChildren()){
                        for (DataSnapshot dataSnapshot5 : dataSnapshot.getChildren()){
                            String valami = dataSnapshot5.getKey().toString();
                            if (!valami.equals("datum")){
                                String valami1 = dataSnapshot5.child("neptunkod").getValue().toString();
                                if (valami1.equals(nev.getText().toString())){
                                    String ijaszatidopont = dataSnapshot.getKey();
                                    stringBuilder.append(ijaszatidopont + ", ");
                                }
                            }
                        }
                    }
                    stringBuilder.append("\n" + "Konditerem idopontjai" + "\n");
                    for (DataSnapshot dataSnapshot : snapshot.child("Konditerem").getChildren()){
                        for (DataSnapshot dataSnapshot5 : dataSnapshot.getChildren()){
                            String valami = dataSnapshot5.getKey().toString();
                            if (!valami.equals("datum")){
                                String valami1 = dataSnapshot5.child("neptunkod").getValue().toString();
                                if (valami1.equals(nev.getText().toString())){
                                    String kondiidopont = dataSnapshot.getKey();
                                    stringBuilder.append(kondiidopont + ", ");
                                }
                            }
                        }
                    }
                    stringBuilder.append("\n" + "Tanc idopontjai" + "\n");
                    for (DataSnapshot dataSnapshot : snapshot.child("Tánc").getChildren()){
                        for (DataSnapshot dataSnapshot5 : dataSnapshot.getChildren()){
                            String valami = dataSnapshot5.getKey().toString();
                            if (!valami.equals("datum")){
                                String valami1 = dataSnapshot5.child("neptunkod").getValue().toString();
                                if (valami1.equals(nev.getText().toString())){
                                    String tancidopont = dataSnapshot.getKey();
                                    stringBuilder.append(tancidopont + ", ");
                                }
                            }
                        }
                    }
                    String fajlnev = nev.getText().toString() + ".csv";
                    File textfajl = new File(file, fajlnev);
                    try{
                        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(textfajl), StandardCharsets.UTF_8);
                        fileWriter.append(stringBuilder);
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (Exception e){}
                    Uri uri = FileProvider.getUriForFile(sajatalairasok.this, getPackageName()+ ".provider", textfajl);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("vnd.android.cursor.dir/email");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailmegadasa.getText().toString()});
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT, nev.getText().toString() + " aláírásai");
                    startActivity(Intent.createChooser(intent , "Send email..."));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}