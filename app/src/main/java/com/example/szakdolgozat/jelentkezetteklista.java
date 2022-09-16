package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class jelentkezetteklista extends AppCompatActivity {

    TextView textView;
    ListView listView;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    TextView bejelentkezes, kijelentkezes, eltoltott, nev, neptunkod, alairasok;
    TextView idopontment, sportagment, nevment;

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
        sportagment = findViewById(R.id.sportagmentes);
        nevment = findViewById(R.id.nevmentes);

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
                    bejelentkezes.setText(snapshot.child("bejelentkezesidopontja").getValue().toString());
                    nev.setText(snapshot.child("nev").getValue().toString());
                    neptunkod.setText(snapshot.child("neptunkod").getValue().toString());
                    alairasok.setText("Kattints ide, hogy megnézd az aláírásokat!");
                    //if (snapshot.hasChild())
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}