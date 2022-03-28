package com.example.szakdolgozat;

public class jelentkezettek {
    String nev, neptunkod;

    public jelentkezettek() {
    }

    public jelentkezettek(String nev, String neptunkod) {
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
