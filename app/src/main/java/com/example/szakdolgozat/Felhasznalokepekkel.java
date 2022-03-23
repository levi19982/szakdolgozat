package com.example.szakdolgozat;

public class Felhasznalokepekkel {

    String nev, neptunkod,  keplink;

    public Felhasznalokepekkel() {
    }

    public Felhasznalokepekkel(String nev, String neptunkod, String keplink) {
        this.nev = nev;
        this.neptunkod = neptunkod;
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

    public String getKeplink() {
        return keplink;
    }

    public void setKeplink(String keplink) {
        this.keplink = keplink;
    }
}
