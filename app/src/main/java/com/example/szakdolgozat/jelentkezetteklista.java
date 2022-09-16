package com.example.szakdolgozat;

import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jelentkezetteklista);

        Intent intent = getIntent();
        String kapottidopont1 = intent.getStringExtra("idopont");
        String kapottsportag1 = intent.getStringExtra("kapottsportag");

        textView = findViewById(R.id.kapottidopont);
        listView = findViewById(R.id.jelentkezetteknevszerint);

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
                        Toast.makeText(jelentkezetteklista.this, jelentkezettek.get(i).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}