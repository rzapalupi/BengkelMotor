package com.efpro.bengkelmotor_01.Helper;

/**
 * Created by rzapalupi on 10/28/2017.
 */

public class Haversine {

    public double Formula(double myLat, double myLong, double bLat, double bLong ){
        double R        = 6371; //Radius bumi
        double dLat     = toRadian (bLat - myLat);
        double dLong   = toRadian (bLong - myLong);
        myLat = toRadian(myLat);
        bLat  = toRadian(bLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLong / 2) * Math.sin(dLong / 2) * Math.cos(myLat) * Math.cos(bLat);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;

//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLong / 2) * Math.sin(dLong / 2) * Math.cos(myLat) * Math.cos(bLat);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        return R * c;

    }

    public double toRadian(double angle) {
        return Math.PI * angle / 180.0;
    }


}
