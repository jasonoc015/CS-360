package com.termproject;

public class Entry {
    private long id;
    private String date;
    private float weight;
    private String username;

    /**
     * Default constructor.
     */
    public Entry(){

    }

    /**
     * Overloaded constructor.
     * @param id - the id number of the entry.
     * @param date - the date of the entry.
     * @param weight - the weight of the entry.
     * @param username - the username of the entry.
     */
    public Entry(long id, String date, float weight, String username) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.username = username;
    }

    /**
     * Sets the id.
     * @param id - the new id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets the date.
     * @param date - the new date.
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Sets the weight.
     * @param weight - the new weight.
     */
    public void setWeight(float weight){
        this.weight = weight;
    }

    /**
     * Sets the username.
     * @param username - the new username.
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Gets the username.
     * @return - the username.
     */
    public long getId(){
        return id;
    }

    /**
     * Gets the date.
     * @return - the date
     */
    public String getDate(){
        return date;
    }

    /**
     * Gets the weight
     * @return - the weight.
     */
    public float getWeight(){
        return weight;
    }

    /**
     * Gets the username.
     * @return - the username.
     */
    public String getUsername(){
        return username;
    }
}
