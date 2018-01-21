package com.efpro.bengkelmotor_01.Model;

/**
 * Created by rzapalupi on 11/30/2017.
 */

public class ReviewBengkel {

    private String username;
    private String comment;
    private int rate;
    private String date;
    private String photoUrl;

    public ReviewBengkel(){

    }

    public ReviewBengkel(String username, String comment, int rate, String date, String photoUrl) {
        this.username = username;
        this.comment = comment;
        this.rate = rate;
        this.date = date;
        this.photoUrl = photoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }



}
