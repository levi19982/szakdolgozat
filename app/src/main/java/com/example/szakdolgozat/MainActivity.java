package com.example.szakdolgozat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.szakdolgozat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    String nev, neptunkod;
    TextView neptunkodszoveg, nevszoveg;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setNavigationViewListener();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        neptunkodszoveg = findViewById(R.id.neptunkodmezo);
        nevszoveg = findViewById(R.id.nevmezo);

        binding.fenykephozzaadasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nev = binding.nevmezo.getText().toString();
                neptunkod = binding.neptunkodmezo.getText().toString();
                String vanefenykepnev = nev;
                String atmenetitelefonszam = null;
                String atmenetikeplink = null;
                databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(vanefenykepnev)){
                            Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(nev, neptunkod, atmenetitelefonszam, atmenetikeplink);
                            databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
                            adatbazis = FirebaseDatabase.getInstance();
                            databaseReference = adatbazis.getReference("Felhasznalokepekkel");
                            databaseReference.child(vanefenykepnev).setValue(felhasznalotelefonszamokkal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent1 = new Intent(MainActivity.this, telefonszamhozzaadasa.class);
                                    intent1.putExtra("nev", nev);
                                    intent1.putExtra("neptunkod", neptunkod);
                                    startActivity(intent1);
                                }
                            });
                        }
                        if (snapshot.hasChild(vanefenykepnev)){
                            Intent intent = new Intent(MainActivity.this, marvantelefonszam.class);
                            intent.putExtra("nev", nev);
                            intent.putExtra("neptunkod", neptunkod);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        neptunkodszoveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nev2 = nevszoveg.getText().toString();
                if (nev2.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Kérjük írja be nevét!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalok").child(nev2);
                    databaseReference.child(nev2);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                String neptunkodszoveg2 = snapshot.child("neptunkod").getValue().toString();
                                neptunkodszoveg.setText(neptunkodszoveg2);
                            } else {
                                Toast.makeText(MainActivity.this, "Nincs ilyen név!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.adminisztrator: {
                Intent intent = new Intent(MainActivity.this, adminmenu.class);
                startActivity(intent);
                break;
            }
            case R.id.sajatalairsaok: {
                Intent intent = new Intent(MainActivity.this, sajatalairasok.class);
                startActivity(intent);
                intent.putExtra("nev", nev);
                intent.putExtra("neptunkod", neptunkod);
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
    }
}