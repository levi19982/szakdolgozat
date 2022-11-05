package com.example.szakdolgozat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.szakdolgozat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String nev, neptunkod;
    Button kijelentkezes, sajatalairasok, adatvaltoztatas, alairasvaltoztatas, neptunkodvaltoztatas, nevvaltoztatas, telefonszamvaltoztatas, megerositesgomb;
    TextView nevszoveg, udvozles;
    AutoCompleteTextView neptunkodszoveg;
    FirebaseDatabase adatbazis;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    float v = 0;
    AlertDialog.Builder builder, builder2;
    AlertDialog alertDialog, alertDialog2;
    EditText seged;
    int segedvaltozo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        kijelentkezes = findViewById(R.id.kijelentkezesgomb);
        sajatalairasok = findViewById(R.id.sajatalairasokgomb);
        neptunkodszoveg = findViewById(R.id.neptunkodmezo);
        nevszoveg = findViewById(R.id.nevmezo);
        udvozles = findViewById(R.id.udvozloszoveg);
        adatvaltoztatas = findViewById(R.id.adatokvaltoztatasagomb);

        databaseReference.child("Felhasznalokepekkel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> proba = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String nevproba = dataSnapshot.child("neptunkod").getValue().toString();
                    proba.add(nevproba);
                }
                neptunkodszoveg.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, proba));
                neptunkodszoveg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (neptunkodszoveg.getText().toString().equals(dataSnapshot.getKey())) {
                                        String beilleszto = dataSnapshot.child("nev").getValue().toString();
                                        nevszoveg.setText(beilleszto);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        udvozles.setTranslationY(300);
        neptunkodszoveg.setTranslationY(300);
        nevszoveg.setTranslationY(300);
        kijelentkezes.setTranslationY(300);
        binding.fenykephozzaadasa.setTranslationY(300);
        sajatalairasok.setTranslationY(300);
        adatvaltoztatas.setTranslationY(300);

        udvozles.setAlpha(v);
        neptunkodszoveg.setAlpha(v);
        nevszoveg.setAlpha(v);
        kijelentkezes.setAlpha(v);
        binding.fenykephozzaadasa.setAlpha(v);
        sajatalairasok.setAlpha(v);
        adatvaltoztatas.setAlpha(v);

        udvozles.animate().translationY(250).alpha(1).setDuration(1000).setStartDelay(400).start();
        neptunkodszoveg.animate().translationY(300).alpha(1).setDuration(1000).setStartDelay(400).start();
        nevszoveg.animate().translationY(550).alpha(1).setDuration(1000).setStartDelay(600).start();
        binding.fenykephozzaadasa.animate().translationY(600).alpha(1).setDuration(1000).setStartDelay(600).start();
        kijelentkezes.animate().translationY(700).alpha(1).setDuration(1000).setStartDelay(600).start();
        sajatalairasok.animate().translationY(800).alpha(1).setDuration(1000).setStartDelay(600).start();
        adatvaltoztatas.animate().translationY(900).alpha(1).setDuration(1000).setStartDelay(600).start();

        adatvaltoztatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((nevszoveg.getText().toString().isEmpty()) || (neptunkodszoveg.getText().toString().isEmpty())) {
                    Toast.makeText(MainActivity.this, "Kérjük töltse ki mind a két mezőt!", Toast.LENGTH_SHORT).show();
                } else if (!(nevszoveg.getText().toString().isEmpty()) && !(neptunkodszoveg.getText().toString().isEmpty())) {
                    felugroablak2();
                }
            }
        });

        sajatalairasok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sajatalairasokhoz = new Intent(MainActivity.this, sajatalairasok.class);
                startActivity(sajatalairasokhoz);
            }
        });

        kijelentkezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                String felhasznaloidopont = simpleDateFormat1.format(calendar.getTime());
                adatbazis = FirebaseDatabase.getInstance();
                nev = binding.nevmezo.getText().toString();
                neptunkod = binding.neptunkodmezo.getText().toString();
                if ((!nevszoveg.getText().toString().isEmpty()) && (!neptunkodszoveg.getText().toString().isEmpty())) {
                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Sportok");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if ((snapshot.child("Íjászat").child(felhasznaloidopont).hasChild(neptunkod)) && (!snapshot.child("Íjászat").child(felhasznaloidopont).child(neptunkod).hasChild("kijelentkezesidopontja"))) {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Kijelentkezés");
                                alertDialog.setMessage("Biztos ki szeretnél jelentkezni?");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Igen", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        databaseReference.child("Íjászat").child(felhasznaloidopont).child(neptunkod).addValueEventListener(new ValueEventListener() {
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
                                                    if (perckulonbseg >= 30) {
                                                        String eltottiido = (perckulonbseg) + " " + "perc";
                                                        jelentkezettek jelentkezettek = new jelentkezettek(nev, neptunkod, idopont, kijelentkezesiidopont, eltottiido, keplink);
                                                        databaseReference.child("Íjászat").child(felhasznaloidopont).child(neptunkod).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            } else if ((snapshot.child("Konditerem").child(felhasznaloidopont).hasChild(neptunkod)) && (!snapshot.child("Konditerem").child(felhasznaloidopont).child(neptunkod).hasChild("kijelentkezesidopontja"))) {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Kijelentkezés");
                                alertDialog.setMessage("Biztos ki szeretnél jelentkezni?");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Igen", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        databaseReference.child("Konditerem").child(felhasznaloidopont).child(neptunkod).addValueEventListener(new ValueEventListener() {
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
                                                    if (perckulonbseg >= 30) {
                                                        String eltottiido = (perckulonbseg) + " " + "perc";
                                                        jelentkezettek jelentkezettek = new jelentkezettek(nev, neptunkod, idopont, kijelentkezesiidopont, eltottiido, keplink);
                                                        databaseReference.child("Konditerem").child(felhasznaloidopont).child(neptunkod).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            } else if ((snapshot.child("Tánc").child(felhasznaloidopont).hasChild(neptunkod)) && (!snapshot.child("Tánc").child(felhasznaloidopont).child(neptunkod).hasChild("kijelentkezesidopontja"))) {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Kijelentkezés");
                                alertDialog.setMessage("Biztos ki szeretnél jelentkezni?");
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Igen", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        databaseReference.child("Tánc").child(felhasznaloidopont).child(neptunkod).addValueEventListener(new ValueEventListener() {
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
                                                    if (perckulonbseg >= 30) {
                                                        String eltottiido = (perckulonbseg) + " " + "perc";
                                                        jelentkezettek jelentkezettek = new jelentkezettek(nev, neptunkod, idopont, kijelentkezesiidopont, eltottiido, keplink);
                                                        databaseReference.child("Sportok").child("Tánc").child(felhasznaloidopont).child(neptunkod).setValue(jelentkezettek).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            } else {
                                Toast.makeText(MainActivity.this, "Nincs ilyen név!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Kérjük írja be a nevét és/vagy Neptun kódját!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.fenykephozzaadasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nev = binding.nevmezo.getText().toString();
                neptunkod = binding.neptunkodmezo.getText().toString().toUpperCase();
                if ((!nevszoveg.getText().toString().isEmpty()) && (!neptunkodszoveg.getText().toString().isEmpty())) {
                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Felhasznalokepekkel");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChild(neptunkod)) {
                                Felhasznalotelefonszamokkal felhasznalotelefonszamokkal = new Felhasznalotelefonszamokkal(nev, neptunkod, null, null);
                                databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference();
                                adatbazis = FirebaseDatabase.getInstance();
                                databaseReference = adatbazis.getReference("Felhasznalokepekkel");
                                databaseReference.child(neptunkod).setValue(felhasznalotelefonszamokkal).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent telefonszamhozzaadasahoz = new Intent(MainActivity.this, telefonszamhozzaadasa.class);
                                        telefonszamhozzaadasahoz.putExtra("nev", nev);
                                        telefonszamhozzaadasahoz.putExtra("neptunkod", neptunkod);
                                        startActivity(telefonszamhozzaadasahoz);
                                    }
                                });
                            } else {
                                if (snapshot.child(neptunkod).child("keplink").getValue().toString().equals("Megváltoztatni")) {
                                    Intent alairasmegvaltoztatasahoz = new Intent(MainActivity.this, alairasmegvaltoztatasa.class);
                                    alairasmegvaltoztatasahoz.putExtra("nev", nev);
                                    alairasmegvaltoztatasahoz.putExtra("neptunkod", neptunkod);
                                    startActivity(alairasmegvaltoztatasahoz);
                                } else {
                                    Intent marvantelefonszamhoz = new Intent(MainActivity.this, marvantelefonszam.class);
                                    marvantelefonszamhoz.putExtra("nev", nev);
                                    marvantelefonszamhoz.putExtra("neptunkod", neptunkod);
                                    startActivity(marvantelefonszamhoz);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if ((nevszoveg.getText().toString().isEmpty()) && (neptunkodszoveg.getText().toString().isEmpty())) {
                    Toast.makeText(MainActivity.this, "Kérjük írja be nevét és Neptun kódját!", Toast.LENGTH_SHORT).show();
                } else if (neptunkodszoveg.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Kérjük írja be Neptun kódját!", Toast.LENGTH_SHORT).show();
                } else if (nevszoveg.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Kérjük írja be a nevét!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void felugroablak2() {
        builder = new AlertDialog.Builder(MainActivity.this);
        View view5 = getLayoutInflater().inflate(R.layout.valtoztatas, null);
        alairasvaltoztatas = view5.findViewById(R.id.alairasvaltoztatasagomb);
        neptunkodvaltoztatas = view5.findViewById(R.id.neptunkodvaltoztatasagomb);
        nevvaltoztatas = view5.findViewById(R.id.nevvaltoztatasagomb);
        telefonszamvaltoztatas = view5.findViewById(R.id.telefonszamvaltoztatasagomb);

        String minta = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(minta);
        Date mainap = Calendar.getInstance().getTime();
        String maidatum = dateFormat.format(mainap);

        builder.setView(view5);
        alertDialog = builder.create();
        alertDialog.show();

        alairasvaltoztatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nev = binding.nevmezo.getText().toString();
                neptunkod = binding.neptunkodmezo.getText().toString().toUpperCase();
                String alairasvaltoztatsstring = neptunkod + " szeretné megváltoztatni az aláírását!";
                databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Változtatásikérelmek");
                valtoztatass valtoztatass = new valtoztatass(alairasvaltoztatsstring);
                databaseReference.child(maidatum + " " + neptunkod).setValue(valtoztatass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Sikeres!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        neptunkodvaltoztatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segedvaltozo = 1;
                felugroablak3();
            }
        });

        nevvaltoztatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segedvaltozo = 2;
                felugroablak3();
            }
        });

        telefonszamvaltoztatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segedvaltozo = 3;
                felugroablak3();
            }
        });
    }

    public void felugroablak3() {
        builder2 = new AlertDialog.Builder(MainActivity.this);
        View view6 = getLayoutInflater().inflate(R.layout.valtoztatasseged, null);
        seged = view6.findViewById(R.id.valtoztatassegedgomb);
        megerositesgomb = view6.findViewById(R.id.megerosites);

        builder2.setView(view6);
        alertDialog2 = builder2.create();
        alertDialog2.show();

        String minta = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(minta);
        Date mainap = Calendar.getInstance().getTime();
        String maidatum = dateFormat.format(mainap);
        nev = binding.nevmezo.getText().toString();
        neptunkod = binding.neptunkodmezo.getText().toString().toUpperCase();
        String maidatumesneptunkod = maidatum + " " + neptunkod;

        megerositesgomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seged.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nem írt be semmit!", Toast.LENGTH_SHORT).show();
                    alertDialog2.dismiss();
                } else {
                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Változtatásikérelmek");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild((maidatumesneptunkod))) {
                                Toast.makeText(MainActivity.this, "A mai napra már adott le változtatási kérelmet!", Toast.LENGTH_SHORT).show();
                                alertDialog2.dismiss();
                            } else {
                                if (segedvaltozo == 1) {
                                    String valtoztatas = neptunkod + " szeretné megváltoztatni a Neptun kódját a következőre: " + seged.getText().toString();
                                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Változtatásikérelmek");
                                    valtoztatass valtoztatass = new valtoztatass(valtoztatas);
                                    databaseReference.child(maidatumesneptunkod).setValue(valtoztatass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity.this, "Sikeresen kérelmezte Neptun kódjának megváltoztatását!", Toast.LENGTH_SHORT).show();
                                            alertDialog2.dismiss();
                                        }
                                    });
                                }
                                if (segedvaltozo == 2) {
                                    String valtoztatas = neptunkod + " szeretné megváltoztatni a nevét a következőre: " + seged.getText().toString();
                                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Változtatásikérelmek");
                                    valtoztatass valtoztatass = new valtoztatass(valtoztatas);
                                    databaseReference.child(maidatumesneptunkod).setValue(valtoztatass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity.this, "Sikeresen kérelmezte nevének megváltoztatását!", Toast.LENGTH_SHORT).show();
                                            alertDialog2.dismiss();
                                        }
                                    });
                                }
                                if (segedvaltozo == 3) {
                                    String valtoztatas = neptunkod + " szeretné megváltoztatni a telefonszámát a következőre: " + seged.getText().toString();
                                    databaseReference = FirebaseDatabase.getInstance("https://szakdolgozat-9d551-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Változtatásikérelmek");
                                    valtoztatass valtoztatass = new valtoztatass(valtoztatas);
                                    databaseReference.child(maidatumesneptunkod).setValue(valtoztatass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity.this, "Sikeresen kérelmezte telefonszámának megváltoztatását!", Toast.LENGTH_SHORT).show();
                                            alertDialog2.dismiss();
                                        }
                                    });
                                }
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
}