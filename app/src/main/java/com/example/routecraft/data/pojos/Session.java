package com.example.routecraft.data.pojos;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private final String USER_ID = "USER_ID";
    private final String USER_NAME = "USER_NAME";
    private final String REMEMBER_USERNAME = "REMEMBER_USERNAME";
    private final String STAY_LOGGED_IN = "STAY_LOGGED_IN";
    private final String CURRENT_ROUTE = "CURRENT_ROUTE";

    private SharedPreferences prefs;

    public Session(Context ctx) {
        prefs = ctx.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public void setUserId(int userId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(USER_ID, userId).apply();
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_NAME, username).apply();
    }

    public void setRememberUsername(boolean checked){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(REMEMBER_USERNAME, checked).apply();
    }

    public void setStayLoggedIn(boolean checked) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(STAY_LOGGED_IN, checked).apply();
    }

    public void setCurrentRoute(String routeName) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CURRENT_ROUTE, routeName).apply();
    }

    public int getUserId() {
        return prefs.getInt(USER_ID, 0);
    }

    public String getUsername() {
        return prefs.getString(USER_NAME, "");
    }

    public boolean getRememberUsername(){
        return prefs.getBoolean(REMEMBER_USERNAME, false);
    }

    public boolean getStayLoggedIn() {
        return prefs.getBoolean(STAY_LOGGED_IN, false);
    }

    public String getCurrentRoute(){return prefs.getString(CURRENT_ROUTE, "My first route");}
}