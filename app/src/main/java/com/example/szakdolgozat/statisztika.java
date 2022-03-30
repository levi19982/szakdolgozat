package com.example.szakdolgozat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class statisztika extends AppCompatActivity {

    private Spinner spinner;
    private TextView textView, textView2, sportag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statisztika);

        spinner = findViewById(R.id.sportvalaszto2);
        textView = findViewById(R.id.valasztottsportag2);
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
                        String sportagatvitel = textView.getText().toString();
                        Intent intent = new Intent(statisztika.this, sportagstatisztika.class);
                        intent.putExtra("sportag", sportagatvitel);
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(statisztika.this, "Valami", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}