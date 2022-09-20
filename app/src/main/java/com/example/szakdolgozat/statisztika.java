package com.example.szakdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class statisztika extends AppCompatActivity {

    private Spinner spinner;
    private TextView textView, textView2, sportag;
    Button statisztika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statisztika);

        spinner = findViewById(R.id.sportvalaszto2);
        textView = findViewById(R.id.valasztottsportag2);
        statisztika = findViewById(R.id.statisztikalekerese);
        String[] sportok = getResources().getStringArray(R.array.sportok);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sportok);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        textView.setText(spinner.getSelectedItem().toString());
                        break;
                    case 1:
                        textView.setText(spinner.getSelectedItem().toString());
                        break;
                    case 2:
                        textView.setText(spinner.getSelectedItem().toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        statisztika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sportagatvitel = textView.getText().toString();
                Intent intent = new Intent(statisztika.this, sportagstatisztika.class);
                intent.putExtra("sportag", sportagatvitel);
                startActivity(intent);
            }
        });

    }
}