package com.termproject;

public class AuthenticatedUserManager {
    private AuthenticatedUser mUser;

    private static AuthenticatedUserManager mInstance;

    private AuthenticatedUserManager(){

    }

    /**
     * Get singleton instance.
     * @return - the singleton instance.
     * @throws - IllegalStateException if not initialized.
     */
    public static AuthenticatedUserManager getInstance(){
        if (mInstance == null){
            mInstance = new AuthenticatedUserManager();
        }

        return mInstance;
    }

    /**
     * Get the authenticated user.
     * @return the @AuthenticatedUser
     */
    public AuthenticatedUser getUser(){
        return mUser;
    }

    /**
     * Set the authenticated user.
     * @param user - the @AuthenticatedUser
     */
    public void setUser(AuthenticatedUser user){
        mUser = user;
    }

}
