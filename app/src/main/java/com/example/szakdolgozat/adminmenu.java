package com.example.szakdolgozat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminmenu extends AppCompatActivity{

    Button esemenyletrehozasa, statisztikamegtekintese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmenu);

        esemenyletrehozasa = findViewById(R.id.idopontletrehozasagomb);
        statisztikamegtekintese = findViewById(R.id.statisztikamegtekintesegomb);

        esemenyletrehozasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminmenu.this, esemenyletrehozasa.class);
                startActivity(intent);
            }
        });

        statisztikamegtekintese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(adminmenu.this, statisztika.class);
                startActivity(intent1);
            }
        });

    }

}