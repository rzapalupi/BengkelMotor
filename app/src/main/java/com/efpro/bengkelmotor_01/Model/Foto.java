package com.efpro.bengkelmotor_01.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rzapalupi on 1/4/2018.
 */

public class Foto implements Parcelable {

    private String id;
    private byte[] foto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Foto(String id, byte[] foto) {
        this.id = id;
        this.foto = foto;
    }

    public Foto(){}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByteArray(this.foto);
    }

    protected Foto(Parcel in) {
        this.id = in.readString();
        this.foto = in.createByteArray();
    }

    public static final Creator<Foto> CREATOR = new Creator<Foto>() {
        @Override
        public Foto createFromParcel(Parcel source) {
            return new Foto(source);
        }

        @Override
        public Foto[] newArray(int size) {
            return new Foto[size];
        }
    };
}
