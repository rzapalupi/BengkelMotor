package com.efpro.bengkelmotor_01;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rzapalupi on 10/26/2017.
 */

public class Bengkel implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bNama);
        dest.writeString(this.bAlamat);
        dest.writeString(this.bTelepon);
        dest.writeDouble(this.bLatitude);
        dest.writeDouble(this.bLongitude);
        dest.writeDouble(this.bJarak);
    }

    protected Bengkel(Parcel in) {
        this.bNama = in.readString();
        this.bAlamat = in.readString();
        this.bTelepon = in.readString();
        this.bLatitude = in.readDouble();
        this.bLongitude = in.readDouble();
        this.bJarak = in.readDouble();
    }

    public static final Parcelable.Creator<Bengkel> CREATOR = new Parcelable.Creator<Bengkel>() {
        @Override
        public Bengkel createFromParcel(Parcel source) {
            return new Bengkel(source);
        }

        @Override
        public Bengkel[] newArray(int size) {
            return new Bengkel[size];
        }
    };
}


