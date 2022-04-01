package com.example.szakdolgozat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.database.snapshot.Index;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class atfogostatisztika extends AppCompatActivity {

    ArrayList<Integer> ertekek2;
    ArrayList<String> ertekek;

    BarChart barChart;

    BarData barData;

    BarDataSet barDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atfogostatisztika);

        barChart = findViewById(R.id.idBarChart);


        Intent intent = getIntent();
        String kapottsportag = intent.getStringExtra("kapottsportag");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsportag);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                valami(snapshot);
                valami2(snapshot);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Log.v(TAG, ertekek.get(0));
                Log.v(TAG, ertekek2.get(0).toString());
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                for (int i = 0; i < ertekek.size(); i++){
                    for (int a = 0; a < ertekek2.size(); a++){
                        String datum = ertekek.get(i);
                        int datumszam = Integer.parseInt(datum);
                        int jelentkezok = ertekek2.get(a);
                        //String jelentkezokszama = String.valueOf(jelentkezok);
                        barEntries.add(new BarEntry(datumszam, jelentkezok));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public ArrayList valami2(DataSnapshot snapshot) {
        ertekek2 = new ArrayList<>();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
            int a = (int) (dataSnapshot.getChildrenCount()-1);
            ertekek2.add(a);
        }
        return ertekek2;
    }

    public ArrayList valami(DataSnapshot snapshot) {
        ertekek = new ArrayList<>();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            String adat = dataSnapshot.getKey();
            ertekek.add(adat);
        }
        return ertekek;
    }
}