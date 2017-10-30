package com.efpro.bengkelmotor_01;

/**
 * Created by rzapalupi on 10/26/2017.
 */

public class Bengkel {

    private String bNama;
    private String bAlamat;
    private String bTelepon;
    private double bLatitude;
    private double bLongitude;
    private double bJarak;

    public Bengkel() {
    }

    public Bengkel(String bNama, String bAlamat, String bTelepon, double bLatitude, double bLongitude) {
        this.bNama = bNama;
        this.bAlamat = bAlamat;
        this.bTelepon = bTelepon;
        this.bLatitude = bLatitude;
        this.bLongitude = bLongitude;
    }

    public String getbNama() {
        return bNama;
    }

    public void setbNama(String bNama) {
        this.bNama = bNama;
    }

    public String getbAlamat() {
        return bAlamat;
    }

    public void setbAlamat(String bAlamat) {
        this.bAlamat = bAlamat;
    }

    public String getbTelepon() {
        return bTelepon;
    }

    public void setbTelepon(String bTelepon) {
        this.bTelepon = bTelepon;
    }

    public double getbLatitude() {
        return bLatitude;
    }

    public void setbLatitude(double bLatitude) {
        this.bLatitude = bLatitude;
    }

    public double getbLongitude() {
        return bLongitude;
    }

    public void setbLongitude(double bLongitude) {
        this.bLongitude = bLongitude;
    }

    public double getbJarak() {
        return bJarak;
    }

    public void setbJarak(double bJarak) {
        this.bJarak = bJarak;
    }


}


