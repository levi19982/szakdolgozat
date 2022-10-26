package com.example.szakdolgozat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.JumpLine;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class atfogostatisztika extends AppCompatActivity {

    Spinner spinner;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atfogostatisztika);

        felugro2();

    }

    public void felugro2(){
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.valasztas, null);
        spinner = view.findViewById(R.id.melyikchart);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        String[] chartok1 = getResources().getStringArray(R.array.chartok);
        ArrayAdapter adapter1 = new ArrayAdapter(atfogostatisztika.this, android.R.layout.simple_spinner_item, chartok1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        //linechart();
                        alertDialog.dismiss();
                        break;
                    case 2:
                        //verticalchart();
                        alertDialog.dismiss();
                        break;
                    case 3:
                        proba();
                        alertDialog.dismiss();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void proba(){
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        //anyChartView.clear();
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Összes sport részvételei");

        List<DataEntry> seriesData = new ArrayList<>();



        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int a = 0;
                int b = 0;
                //int u = 0;
                String adat = null;
                Long szamokseged = Long.valueOf(1);
                Long szamokseged1 = Long.valueOf(2);
                Long szamokseged2 = Long.valueOf(3);
                for (DataSnapshot dataSnapshot4 : snapshot.child("Konditerem").getChildren()){
                    a++;
                }for (DataSnapshot dataSnapshot5 : snapshot.child("Tánc").getChildren()){
                    a++;
                }
                for (DataSnapshot dataSnapshot6 : snapshot.child("Íjászat").getChildren()){
                    a++;
                }
                for (int c = 0; c < 9;c++) {
                    Toast.makeText(atfogostatisztika.this, String.valueOf(a), Toast.LENGTH_SHORT).show();
                    for (DataSnapshot snapshot1 : snapshot.child("Konditerem").getChildren()) {
                        szamokseged = (snapshot1.getChildrenCount() - 1);
                    }
                        for (DataSnapshot snapshot2 : snapshot.child("Tánc").getChildren()) {
                            szamokseged1 = (snapshot2.getChildrenCount() - 1);
                        }
                            for (DataSnapshot snapshot3 : snapshot.child("Íjászat").getChildren()) {
                                adat = snapshot3.getKey();
                                szamokseged2 = (snapshot3.getChildrenCount() - 1);
                            }
                    //Toast.makeText(atfogostatisztika.this, "Lefut: " + u, Toast.LENGTH_SHORT).show();
                    seriesData.add(new CustomDataEntry(adat, szamokseged, szamokseged1, szamokseged2));
                    Toast.makeText(atfogostatisztika.this, String.valueOf(c), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Set set = Set.instantiate();
                set.data(seriesData);
                Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
                Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

                Line series1 = cartesian.line(series1Mapping);
                series1.name("Konditerem");
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);

                Line series2 = cartesian.line(series2Mapping);
                series2.name("Tánc");
                series2.hovered().markers().enabled(true);
                series2.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series2.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);

                Line series3 = cartesian.line(series3Mapping);
                series3.name("Íjászat");
                series3.hovered().markers().enabled(true);
                series3.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series3.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);


                cartesian.legend().enabled(true);
                cartesian.legend().fontSize(13d);
                cartesian.legend().padding(0d, 0d, 10d, 0d);

                anyChartView.setChart(cartesian);
            }
        }, 9000);
    }

    /*public void linechart(){
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Intent intent = getIntent();
        String kapottsport = intent.getStringExtra("kapottsportag");

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title(kapottsport + " részvételei");

        List<DataEntry> seriesData = new ArrayList<>();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsport);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String adat = snapshot1.getKey();
                    long szamokseged = (snapshot1.getChildrenCount() - 1);
                    seriesData.add(new CustomDataEntry(adat, szamokseged));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Set set = Set.instantiate();
                set.data(seriesData);
                Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

                Line series1 = cartesian.line(series1Mapping);
                series1.name(kapottsport);
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);


                cartesian.legend().enabled(true);
                cartesian.legend().fontSize(13d);
                cartesian.legend().padding(0d, 0d, 10d, 0d);

                anyChartView.setChart(cartesian);
            }
        }, 5000);
    }

    public void verticalchart(){
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Intent intent = getIntent();
        String kapottsport = intent.getStringExtra("kapottsportag");

        Cartesian cartesian = AnyChart.vertical();

        cartesian.animation(true).title(kapottsport + " részvételei");

        List<DataEntry> adatok = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(kapottsport);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String adat = snapshot1.getKey();
                    long szamokseged = (snapshot1.getChildrenCount() - 1);
                    adatok.add(new CustomDataEntry(adat, szamokseged));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        Set set = Set.instantiate();
        set.data(adatok);

        Mapping mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping jumpLineData = set.mapAs("{ x: 'x', value: 'jumpLine' }");

        Bar bar = cartesian.bar(mapping);
        bar.labels().format("Résztvevők száma");

        JumpLine jumpLine = cartesian.jumpLine(jumpLineData);
        jumpLine.stroke("2 #60727B");
        jumpLine.labels().enabled(false);

        cartesian.yScale().minimum(0d);

        cartesian.labels(true);

        cartesian.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .positionMode(TooltipPositionMode.POINT)
                .unionFormat(
                        "function() {\n" +
                                "      return 'Résztvevők száma: ' + this.points[1].value +\n" +
                                "        '\\n'" +
                                "    }");
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(true);
        cartesian.yAxis(true);
        cartesian.yAxis(0).labels().format("{%value}{type:number, decimalsCount:0}");

        anyChartView.setChart(cartesian);
            }
        }, 5000);

    }*/
    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }
}