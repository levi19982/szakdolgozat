package com.example.szakdolgozat;

public class jelentkezettek {
    String nev, neptunkod, bejelentkezesidopontja;

    public jelentkezettek() {
    }

    public jelentkezettek(String nev, String neptunkod, String idopont) {
        this.nev = nev;
        this.neptunkod = neptunkod;
        this.bejelentkezesidopontja = idopont;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getNeptunkod() {
        return neptunkod;
    }

    public void setNeptunkod(String neptunkod) {
        this.neptunkod = neptunkod;
    }

    public String getBejelentkezesidopontja() {return bejelentkezesidopontja;}

    public void setBejelentkezesidopontja(String bejelentkezesidopontja) {this.bejelentkezesidopontja = bejelentkezesidopontja;}
}
