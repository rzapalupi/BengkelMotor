package com.efpro.bengkelmotor_01;

/**
 * Created by rzapalupi on 10/26/2017.
 */

public class Bengkel {

    private String bNama;
    private String bAlamat;
    private String bTelepon;
    private Double bLatitude;
    private Double bLongitude;

    public Bengkel() {
    }

    public Bengkel(String bNama, String bAlamat, String bTelepon, Double bLatitude, Double bLongitude) {
        this.bNama = bNama;
        this.bAlamat = bAlamat;
        this.bTelepon = bTelepon;
        this.bLatitude = bLatitude;
        this.bLongitude = bLongitude;
    }


    public String getbNama() {
        return bNama;
    }

    public String getbAlamat() {
        return bAlamat;
    }

    public String getbTelepon() {
        return bTelepon;
    }

    public Double getbLatitude() {
        return bLatitude;
    }

    public Double getbLongitude() {
        return bLongitude;
    }




}


