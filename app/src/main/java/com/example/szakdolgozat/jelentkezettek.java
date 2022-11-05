package com.example.szakdolgozat;

public class jelentkezettek {
    String nev, neptunkod, bejelentkezesidopontja, kijelentkezesidopontja, eltottido, keplink;
    

    public jelentkezettek(String nev, String neptunkod, String idopont, String kijelentkezesidopontja, String eltoltottido, String keplink) {
        this.nev = nev;
        this.neptunkod = neptunkod;
        this.bejelentkezesidopontja = idopont;
        this.kijelentkezesidopontja = kijelentkezesidopontja;
        this.eltottido = eltoltottido;
        this.keplink = keplink;
    }
}
