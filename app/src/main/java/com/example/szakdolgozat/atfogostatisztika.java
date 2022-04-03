package com.example.szakdolgozat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
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

        PieChart pieChart = findViewById(R.id.idpieChart);


        Intent intent = getIntent();
        String kapottsportag = intent.getStringExtra("kapottsportag");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsportag);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                valami(snapshot);
                valami2(snapshot);
                ArrayList<PieEntry> barEntries = new ArrayList<>();
                        for (int i = 0; i < ertekek.size(); i++){
                                String datum = ertekek.get(i);
                                int jelentkezok = ertekek2.get(i);
                            barEntries.add(new PieEntry(jelentkezok, datum));
                }
                PieDataSet pieDataSet = new PieDataSet(barEntries, "Résztvevők");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(16f);

                    PieData pieData = new PieData(pieDataSet);

                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setCenterText("Résztvevők");
                    pieChart.animate();
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