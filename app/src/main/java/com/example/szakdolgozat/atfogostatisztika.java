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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.core.Tag;
import com.google.firebase.database.snapshot.Index;

import org.apache.commons.collections4.trie.AbstractBitwiseTrie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class atfogostatisztika extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atfogostatisztika);

        lineChart = findViewById(R.id.linechart);


        Intent intent = getIntent();
        String kapottsport = intent.getStringExtra("kapottsportag");
        ArrayList<String> datumok = new ArrayList<>();
        ArrayList<Integer> szamok = new ArrayList<>();
        ArrayList<Entry> vegso = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsport);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        String adat = snapshot1.getKey();
                        String[] ertekek = adat.split("-");
                        int nap = Integer.parseInt(ertekek[2]);
                        int honap = Integer.parseInt(ertekek[1]);
                        int ev = Integer.parseInt(ertekek[0]);
                        //datumok.add(adat);
                        //Toast.makeText(atfogostatisztika.this, adat, Toast.LENGTH_SHORT).show();
                        int szamokseged = (int) (snapshot1.getChildrenCount() - 1);
                        vegso.add(new Entry(nap, szamokseged));

                        //szamok.add(szamokseged);

                }
                Toast.makeText(atfogostatisztika.this, String.valueOf(vegso), Toast.LENGTH_SHORT).show();
                LineDataSet lineDataSet = new LineDataSet(vegso, "Próba");
                ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                iLineDataSets.add(lineDataSet);

                LineData lineData = new LineData(iLineDataSets);
                lineChart.setData(lineData);
                lineChart.invalidate();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}