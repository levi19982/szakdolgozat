package com.example.szakdolgozat;

public class Felhasznalotelefonszamokkal {

    String nev, neptunkod, telefonszam, keplink;

    public Felhasznalotelefonszamokkal(){

    }

    public Felhasznalotelefonszamokkal(String nev, String neptunkod, String telefonszam, String keplink) {
        this.nev = nev;
        this.neptunkod = neptunkod;
        this.telefonszam = telefonszam;
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

    public String getTelefonszam() {
        return telefonszam;
    }

    public void setTelefonszam(String telefonszam) {
        this.telefonszam = telefonszam;
    }

    public String getKeplink() {
        return keplink;
    }

    public void setKeplink(String keplink) {
        this.keplink = keplink;
    }
}
