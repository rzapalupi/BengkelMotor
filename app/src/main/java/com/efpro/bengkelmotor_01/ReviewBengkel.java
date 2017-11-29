package com.efpro.bengkelmotor_01;

/**
 * Created by rzapalupi on 11/30/2017.
 */

public class ReviewBengkel {


    private String uid;
    private String comment;
    private int rate;


    public ReviewBengkel(){

    }

    public ReviewBengkel(String uid, String comment, int rate) {
        this.uid = uid;
        this.comment = comment;
        this.rate = rate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
