package com.example.szakdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class esemenyletrehozasa extends AppCompatActivity {

    private static final String TAG = "Esemenyletrahozasa";
    private TextView textView, textView2;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esemenyletrehozasa);
        textView = findViewById(R.id.datum);
        textView2 = findViewById(R.id.datummutatasa);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int ev = calendar.get(Calendar.YEAR);
                int honap = calendar.get(Calendar.MONTH);
                int nap = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(esemenyletrehozasa.this,
                        android.R.style.Theme_Holo_Dialog,
                        dateSetListener,
                        ev, honap, nap);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ev, int honap, int nap) {
                honap = honap + 1;
                String datum = ev + "/" + honap + "/" + nap;
                textView2.setText(datum);
            }
        };

    }
}