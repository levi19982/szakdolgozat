package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class jelentkezetteklista extends AppCompatActivity {

    TextView textView;
    ListView listView;
    AlertDialog.Builder builder, builder2;
    AlertDialog alertDialog, alertDialog2;
    ImageView eredetialairaskep, egyszerialairaskep;
    TextView bejelentkezes, kijelentkezes, eltoltott, nev, neptunkod, alairasok;
    TextView idopontment, sportagment, nevment;
    Button exportalas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jelentkezetteklista);

        Intent intent = getIntent();
        String kapottidopont1 = intent.getStringExtra("idopont");
        String kapottsportag1 = intent.getStringExtra("kapottsportag");

        textView = findViewById(R.id.kapottidopont);
        listView = findViewById(R.id.jelentkezetteknevszerint);
        idopontment = findViewById(R.id.idopontmentes);
        idopontment.setText(kapottidopont1);
        sportagment = findViewById(R.id.sportagmentes);
        sportagment.setText(kapottsportag1);
        nevment = findViewById(R.id.nevmentes);
        exportalas = findViewById(R.id.mentesgomb);

        exportalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Próba.csv");
                Toast.makeText(jelentkezetteklista.this, file.toString(), Toast.LENGTH_SHORT).show();
                try{
                    FileWriter fileWriter = new FileWriter(file);
                    CSVWriter csvWriter = new CSVWriter(fileWriter, '|', CSVWriter.NO_QUOTE_CHARACTER,
                                                                                 CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                                                                 CSVWriter.DEFAULT_LINE_END);
                    List<String[]> data = new ArrayList<String[]>();
                    data.add(new String[] { "Name", "Class", "Marks" });
                    data.add(new String[] { "Aman", "10", "620" });
                    data.add(new String[] { "Suraj", "10", "630" });
                    csvWriter.writeAll(data);
                    csvWriter.close();
                }
                catch(Exception e){}
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsportag1).child(kapottidopont1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText("Jelentkezők száma: " + (snapshot.getChildrenCount()-1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> jelentkezettek = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String adat = snapshot1.getKey();
                    if (!adat.equals("datum")){
                    jelentkezettek.add(adat);}
                }
                ArrayAdapter adapter = new ArrayAdapter(jelentkezetteklista.this, android.R.layout.simple_list_item_1, jelentkezettek);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        nevment.setText(jelentkezettek.get(i).toString());
                        felugroablak();
                        Toast.makeText(jelentkezetteklista.this, jelentkezettek.get(i).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void felugroablak(){
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.adatok, null);
        bejelentkezes = view.findViewById(R.id.bejelentkezesidopont);
        kijelentkezes = view.findViewById(R.id.kijelentkezesidopont);
        eltoltott = view.findViewById(R.id.eltoltottido);
        nev = view.findViewById(R.id.name);
        neptunkod = view.findViewById(R.id.neptuncode);
        alairasok = view.findViewById(R.id.alairas);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(sportagment.getText().toString()).child(idopontment.getText().toString()).child(nevment.getText().toString());
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    bejelentkezes.setText("Bejelentkezés időpontja: " + snapshot.child("bejelentkezesidopontja").getValue().toString());
                    nev.setText("Hallgató neve: " + snapshot.child("nev").getValue().toString());
                    neptunkod.setText("Hallgató Neptun kódja: " + snapshot.child("neptunkod").getValue().toString());
                    alairasok.setText("Kattints ide, hogy megnézd az aláírásokat!");
                    if (snapshot.hasChild("eltottido")){
                        eltoltott.setText("Eltöltött idő: " + snapshot.child("eltottido").getValue().toString());
                    }
                    else {
                        eltoltott.setText("Eltöltött idő: " + "Még nem fejezte be!");
                    }
                    if (snapshot.hasChild("kijelentkezesidopontja")){
                        kijelentkezes.setText("Kijelentkezés időpontja: " + snapshot.child("kijelentkezesidopontja").getValue().toString());
                    }
                    else {
                        kijelentkezes.setText("Kijelentkezés időpontja: " + "Még nem fejezte be!");
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        alairasok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                felugroablak2();
            }
        });

    }

    public void felugroablak2(){
        builder2 = new AlertDialog.Builder(this);
        View view2 = getLayoutInflater().inflate(R.layout.alairasok, null);
        eredetialairaskep = view2.findViewById(R.id.eredetialairas);
        egyszerialairaskep = view2.findViewById(R.id.egyszerialairas);

        builder2.setView(view2);
        alertDialog2 = builder2.create();
        alertDialog2.show();

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(sportagment.getText().toString()).child(idopontment.getText().toString()).child(nevment.getText().toString());
        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel").child(nevment.getText().toString());
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link1 = snapshot.child("keplink").getValue().toString();
                Picasso.get().load(link1).into(eredetialairaskep);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link2 = snapshot.child("keplink").getValue().toString();
                Picasso.get().load(link2).into(egyszerialairaskep);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}