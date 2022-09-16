package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class jelentkezetteklista extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jelentkezetteklista);

        Intent intent = getIntent();
        String kapottidopont1 = intent.getStringExtra("idopont");
        String kapottsportag1 = intent.getStringExtra("kapottsportag");

        textView = findViewById(R.id.kapottidopont);

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

    }
}