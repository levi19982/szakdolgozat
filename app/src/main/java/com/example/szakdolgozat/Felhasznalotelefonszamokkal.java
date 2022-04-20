package com.example.szakdolgozat;

public class Felhasznalotelefonszamokkal {

    String nev, neptunkod,  telefonszam;

    public Felhasznalotelefonszamokkal() {
    }

    public Felhasznalotelefonszamokkal(String nev, String neptunkod, String telefonszam) {
        this.nev = nev;
        this.neptunkod = neptunkod;
        this.telefonszam = telefonszam;
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

    public String getTelefonszam() {
        return telefonszam;
    }

    public void setTelefonszam(String telefonszam) {
        this.telefonszam = telefonszam;
    }
}
