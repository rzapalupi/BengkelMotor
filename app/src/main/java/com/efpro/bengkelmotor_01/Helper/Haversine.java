package com.efpro.bengkelmotor_01.Helper;

/**
 * Created by rzapalupi on 10/28/2017.
 */

public class Haversine {

    public double Formula(double myLat, double myLng, double bLat, double bLng ){
        double R        = 6371; //Radius bumi
        double dLat     = toRadian (bLat - myLat);
        double dLng     = toRadian (bLng - myLng);
        myLat           = toRadian(myLat);
        bLat            = toRadian(bLat);

        double a    = Math.sin(dLat / 2) * Math.sin(dLat / 2);
        double c    = Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(myLat) * Math.cos(bLat);
        return R * 2 * Math.asin(Math.sqrt(a + c));
    }

    private double toRadian(double angle) {
        return Math.PI * angle / 180.0;
    }
}


