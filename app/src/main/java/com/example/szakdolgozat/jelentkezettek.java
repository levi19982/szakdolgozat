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

    public String getBejelentkezesidopontja() {
        return bejelentkezesidopontja;
    }

    public void setBejelentkezesidopontja(String bejelentkezesidopontja) {
        this.bejelentkezesidopontja = bejelentkezesidopontja;
    }

    public String getKijelentkezesidopontja() {
        return kijelentkezesidopontja;
    }

    public void setKijelentkezesidopontja(String kijelentkezesidopontja) {
        this.kijelentkezesidopontja = kijelentkezesidopontja;
    }

    public String getEltottido() {
        return eltottido;
    }

    public void setEltottido(String eltottido) {
        this.eltottido = eltottido;
    }

    public String getKeplink() {
        return keplink;
    }

    public void setKeplink(String keplink) {
        this.keplink = keplink;
    }
}
