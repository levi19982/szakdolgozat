package com.example.szakdolgozat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.core.Tag;
import com.google.firebase.database.snapshot.Index;

import org.apache.commons.collections4.trie.AbstractBitwiseTrie;
import org.checkerframework.checker.units.qual.C;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class atfogostatisztika extends AppCompatActivity {

    LineChart lineChart;
    Spinner spinner;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atfogostatisztika);

        lineChart = findViewById(R.id.linechart);





        felugroablak();



    }

    public void barchart(){

    }

    public void linechart(){
        ArrayList<Integer> szamok = new ArrayList<>();
        ArrayList<String> datumok = new ArrayList<>();
        String[] datumo1k = {"Január", "Február"};
        Intent intent = getIntent();
        String kapottsport = intent.getStringExtra("kapottsportag");
        ArrayList<Entry> vegso = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsport);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String adat = snapshot1.getKey();
                    datumok.add(adat);
                    String[] ertekek = adat.split("-");
                    int nap = Integer.parseInt(ertekek[2]);
                    int honap = Integer.parseInt(ertekek[1]);
                    int ev = Integer.parseInt(ertekek[0]);
                    int egyesitett = Integer.parseInt(adat.replace("-",""));
                    szamok.add(egyesitett);
                    int szamokseged = (int) (snapshot1.getChildrenCount() - 1);
                        vegso.add(new Entry(egyesitett, szamokseged));
                        //Toast.makeText(atfogostatisztika.this, String.valueOf(egyesitett), Toast.LENGTH_SHORT).show();
                }
                LineDataSet lineDataSet = new LineDataSet(vegso, "Próba");
                ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                iLineDataSets.add(lineDataSet);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(datumok));

                //xAxis.setGranularity(2f);
                YAxis yAxisleft = lineChart.getAxisLeft();
                yAxisleft.setGranularity(1f);
                YAxis yAxisright = lineChart.getAxisRight();
                yAxisright.setGranularity(1f);

                xAxis.setTextColor(Color.RED);
                yAxisleft.setTextColor(Color.RED);


                LineData lineData = new LineData(iLineDataSets);
                lineChart.setData(lineData);
                lineChart.invalidate();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void felugroablak() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.valasztas, null);
        spinner = view.findViewById(R.id.melyikchart);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        String[] sportok = getResources().getStringArray(R.array.chartok);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sportok);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        linechart();
                        break;
                    case 1:
                        barchart();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private class ctengelyertekek extends ValueFormatter implements IAxisValueFormatter{

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return "Időpont" + value;
        }
    }
}