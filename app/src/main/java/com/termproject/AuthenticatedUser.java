package com.termproject;

public class AuthenticatedUser {

    private String mUsername;

    public AuthenticatedUser(String username){
        mUsername = username;
    }

    public String getUsername(){
        return mUsername;
    }
}
