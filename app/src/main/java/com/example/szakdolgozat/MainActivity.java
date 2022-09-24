package com.example.szakdolgozat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    String nev, neptunkod;
    Button kijelentkezes;
    TextView neptunkodszoveg, nevszoveg, udvozles;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setNavigationViewListener();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        kijelentkezes = findViewById(R.id.kijelentkezesgomb);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        neptunkodszoveg = findViewById(R.id.neptunkodmezo);
        nevszoveg = findViewById(R.id.nevmezo);
        udvozles = findViewById(R.id.udvozloszoveg);

        udvozles.setTranslationY(300);
        neptunkodszoveg.setTranslationY(300);
        nevszoveg.setTranslationY(300);
        kijelentkezes.setTranslationY(300);
        binding.fenykephozzaadasa.setTranslationY(300);

        udvozles.setAlpha(v);
        neptunkodszoveg.setAlpha(v);
        nevszoveg.setAlpha(v);
        kijelentkezes.setAlpha(v);
        binding.fenykephozzaadasa.setAlpha(v);

        udvozles.animate().translationY(450).alpha(1).setDuration(1000).setStartDelay(400).start();
        neptunkodszoveg.animate().translationY(700).alpha(1).setDuration(1000).setStartDelay(400).start();
        nevszoveg.animate().translationY(600).alpha(1).setDuration(1000).setStartDelay(600).start();
        kijelentkezes.animate().translationY(1000).alpha(1).setDuration(1000).setStartDelay(600).start();
        binding.fenykephozzaadasa.animate().translationY(800).alpha(1).setDuration(1000).setStartDelay(600).start();

        kijelentkezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-M-dd");
                String felhasznaloidopont = simpleDateFormat1.format(calendar.getTime());
                adatbazis = FirebaseDatabase.getInstance();
                nev = binding.nevmezo.getText().toString();
                neptunkod = binding.neptunkodmezo.getText().toString();
                if ((!nevszoveg.getText().toString().isEmpty()) && (!neptunkodszoveg.getText().toString().isEmpty())) {
                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if ((snapshot.child("Íjászat").child(felhasznaloidopont).hasChild(nev))) {
                                    if (!snapshot.child("Íjászat").child(felhasznaloidopont).child(nev).hasChild("kijelentkezesidopontja")){
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setTitle("Kijelentkezés");
                                    alertDialog.setMessage("Biztos ki szeretnél jelentkezni?");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Igen", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                                            databaseReference2 = adatbazis.getReference("Sportok").child("Íjászat").child(felhasznaloidopont);
                                            databaseReference2.child(nev).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String idopont = snapshot.child("bejelentkezesidopontja").getValue().toString();
                                                    String keplink = snapshot.child("keplink").getValue().toString();
                                                    String kijelentkezesiidopont = simpleDateFormat.format(calendar.getTime());
                                                    try {
                                                        Date date = simpleDateFormat.parse(idopont);
                                                        Date masikdate = simpleDateFormat.parse(kijelentkezesiidopont);
                                                        long percek = masikdate.getTime() - date.getTime();
                                                        long masodperc = percek / 1000;
                                                        long perckulonbseg = masodperc / 60;
                                                        long hatravan = 30 - perckulonbseg;
                                                        String hatravanido = Long.toString(hatravan);
                                                        if (masodperc > 0) {
                                                            //if (perckulonbseg >= 30) {
                                                            String eltottiido = Long.toString(perckulonbseg) + " " + "perc";
                                                            jelentkezettek jelentkezettek = new jelentkezettek(nev, neptunkod, idopont, kijelentkezesiidopont, eltottiido, keplink);
                                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                                                            databaseReference2 = adatbazis.getReference("Sportok").child("Íjászat").child(felhasznaloidopont);
                                                            databaseReference2.child(nev).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(MainActivity.this, "Sikeres!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "Már csak " + hatravanido + " perc van hátra!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nem", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(MainActivity.this, "Nem jelentkezett ki!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    alertDialog.show();
                                    } else Toast.makeText(MainActivity.this, "Már kijelentkezett!", Toast.LENGTH_SHORT).show();
                                } else if ((snapshot.child("Konditerem").child(felhasznaloidopont).hasChild(nev))) {
                                    if (!snapshot.child("Konditerem").child(felhasznaloidopont).child(nev).hasChild("kijelentkezesidopontja")){
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setTitle("Kijelentkezés");
                                    alertDialog.setMessage("Biztos ki szeretnél jelentkezni?");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Igen", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                                            databaseReference2 = adatbazis.getReference("Sportok").child("Konditerem").child(felhasznaloidopont);
                                            databaseReference2.child(nev).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String idopont = snapshot.child("bejelentkezesidopontja").getValue().toString();
                                                    String keplink = snapshot.child("keplink").getValue().toString();
                                                    String kijelentkezesiidopont = simpleDateFormat.format(calendar.getTime());
                                                    try {
                                                        Date date = simpleDateFormat.parse(idopont);
                                                        Date masikdate = simpleDateFormat.parse(kijelentkezesiidopont);
                                                        long percek = masikdate.getTime() - date.getTime();
                                                        long masodperc = percek / 1000;
                                                        long perckulonbseg = masodperc / 60;
                                                        long hatravan = 30 - perckulonbseg;
                                                        String hatravanido = Long.toString(hatravan);
                                                        if (masodperc > 0) {
                                                            //if (perckulonbseg >= 30) {
                                                            String eltottiido = Long.toString(perckulonbseg) + " " + "perc";
                                                            jelentkezettek jelentkezettek = new jelentkezettek(nev, neptunkod, idopont, kijelentkezesiidopont, eltottiido, keplink);
                                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                                                            databaseReference2 = adatbazis.getReference("Sportok").child("Konditerem").child(felhasznaloidopont);
                                                            databaseReference2.child(nev).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(MainActivity.this, "Sikeres!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "Már csak " + hatravanido + " perc van hátra!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nem", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(MainActivity.this, "Nem jelentkezett ki!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    alertDialog.show();
                                } else Toast.makeText(MainActivity.this, "Már kijelentkezett!", Toast.LENGTH_SHORT).show();
                            } else if ((snapshot.child("Tánc").child(felhasznaloidopont).hasChild(nev))) {
                                if (!snapshot.child("Tánc").child(felhasznaloidopont).child(nev).hasChild("kijelentkezesidopontja")){
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setTitle("Kijelentkezés");
                                    alertDialog.setMessage("Biztos ki szeretnél jelentkezni?");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Igen", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                                            databaseReference2 = adatbazis.getReference("Sportok").child("Tánc").child(felhasznaloidopont);
                                            databaseReference2.child(nev).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String idopont = snapshot.child("bejelentkezesidopontja").getValue().toString();
                                                    String keplink = snapshot.child("keplink").getValue().toString();
                                                    String kijelentkezesiidopont = simpleDateFormat.format(calendar.getTime());
                                                    try {
                                                        Date date = simpleDateFormat.parse(idopont);
                                                        Date masikdate = simpleDateFormat.parse(kijelentkezesiidopont);
                                                        long percek = masikdate.getTime() - date.getTime();
                                                        long masodperc = percek / 1000;
                                                        long perckulonbseg = masodperc / 60;
                                                        long hatravan = 30 - perckulonbseg;
                                                        String hatravanido = Long.toString(hatravan);
                                                        if (masodperc > 0) {
                                                            //if (perckulonbseg >= 30) {
                                                            String eltottiido = Long.toString(perckulonbseg) + " " + "perc";
                                                            jelentkezettek jelentkezettek = new jelentkezettek(nev, neptunkod, idopont, kijelentkezesiidopont, eltottiido, keplink);
                                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                                                            databaseReference2 = adatbazis.getReference("Sportok").child("Tánc").child(felhasznaloidopont);
                                                            databaseReference2.child(nev).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(MainActivity.this, "Sikeres!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(MainActivity.this, "Már csak " + hatravanido + " perc van hátra!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nem", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(MainActivity.this, "Nem jelentkezett ki!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    alertDialog.show();
                                } else Toast.makeText(MainActivity.this, "Már kijelentkezett!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Nincs ilyen név!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Kérjük írja be a nevét és/vagy Neptun kódját!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.fenykephozzaadasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nev = binding.nevmezo.getText().toString();
                neptunkod = binding.neptunkodmezo.getText().toString();
                String vanefenykepnev = nev;
                String atmenetitelefonszam = null;
                String atmenetikeplink = null;
                if ((!nevszoveg.getText().toString().isEmpty()) && (!neptunkodszoveg.getText().toString().isEmpty())) {
                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChild(vanefenykepnev)) {
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
                            if (snapshot.hasChild(vanefenykepnev)) {
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
                else if ((nevszoveg.getText().toString().isEmpty()) && (neptunkodszoveg.getText().toString().isEmpty())){
                    Toast.makeText(MainActivity.this, "Kérjük írja be nevét és Neptun kódját!", Toast.LENGTH_SHORT).show();
                }
                else if (neptunkodszoveg.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Kérjük írja be Neptun kódját!", Toast.LENGTH_SHORT).show();
                }
                else if (nevszoveg.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Kérjük írja be a nevét!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        neptunkodszoveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nev2 = nevszoveg.getText().toString();
                if (nev2.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Kérjük írja be nevét!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(nev2)) {
                                String neptunkodszoveg2 = snapshot.child(nev2).child("neptunkod").getValue().toString();
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