package com.termproject;

import java.util.Date;

public class Entry {
    private long id;
    private String date;
    private float weight;
    private String username;


    public Entry(){

    }

    public Entry(long id, String date, float weight, String username) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.username = username;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setWeight(float weight){
        this.weight = weight;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public long getId(){
        return id;
    }

    public String getDate(){
        return date;
    }

    public float getWeight(){
        return weight;
    }

    public String getUsername(){
        return username;
    }
}
