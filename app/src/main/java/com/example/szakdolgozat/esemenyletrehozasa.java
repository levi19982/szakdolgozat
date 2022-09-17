package com.example.szakdolgozat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class esemenyletrehozasa extends AppCompatActivity{

    private static final String TAG = "Esemenyletrahozasa";
    private TextView textView, textView2, sportag;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Spinner spinner;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    Button esemenyhozzaadasa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esemenyletrehozasa);
        textView = findViewById(R.id.datum);
        textView2 = findViewById(R.id.datummutatasa);
        spinner = findViewById(R.id.sportvalaszto);
        sportag = findViewById(R.id.valasztottsportag);
        esemenyhozzaadasa = findViewById(R.id.esemenyhozzaadasagomb);
        String[] sportok = getResources().getStringArray(R.array.sportok);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sportok);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                            sportag.setText(spinner.getSelectedItem().toString());
                        break;
                    case 1:
                            sportag.setText(spinner.getSelectedItem().toString());
                        break;
                    case 2:
                            sportag.setText(spinner.getSelectedItem().toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                String datum = ev + "-" + honap + "-" + nap;
                textView2.setText(datum);
            }
        };

        esemenyhozzaadasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datum = textView2.getText().toString();
                String sportagszoveg = sportag.getText().toString();
                sportok sportok1 = new sportok(datum);
                databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok").child(sportagszoveg);
                adatbazis = FirebaseDatabase.getInstance();
                databaseReference = adatbazis.getReference("Sportok").child(sportagszoveg);
                databaseReference.child(datum).setValue(sportok1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(esemenyletrehozasa.this, "Sikeresen hozzáadva az időpont a következő sportághoz: " + sportagszoveg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}