package com.example.szakdolgozat;

public class Felhasznalok   {

    String nev, neptunkod;

    public Felhasznalok() {
    }

    public Felhasznalok(String nev, String neptunkod) {
        this.nev = nev;
        this.neptunkod = neptunkod;
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
}
