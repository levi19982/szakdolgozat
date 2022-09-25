package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class
sportagstatisztika extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase adatbazis;
    Button atfogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportagstatisztika);

        listView = findViewById(R.id.sportagidopontlista);
        atfogo = findViewById(R.id.atfogostatisztika);
        Intent intent = getIntent();
        String kapottsportag = intent.getStringExtra("sportag");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsportag);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> idopontok = new ArrayList<>();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String adat = snapshot1.getKey();
                    idopontok.add(adat);
                }
                ArrayAdapter adapter = new ArrayAdapter(sportagstatisztika.this, android.R.layout.simple_list_item_1, idopontok);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsportag).child(idopontok.get(i).toString());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String idopont = idopontok.get(i).toString();
                                Intent intent2 = new Intent(sportagstatisztika.this, jelentkezetteklista.class);
                                intent2.putExtra("idopont", idopont);
                                intent2.putExtra("kapottsportag", kapottsportag);
                                startActivity(intent2);
                                //Toast.makeText(sportagstatisztika.this, "Jelentkezők száma: " + (snapshot.getChildrenCount()-1), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        atfogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sportagstatisztika.this, atfogostatisztika.class);
                intent.putExtra("kapottsportag", kapottsportag);
                startActivity(intent);
            }
        });

    }
}