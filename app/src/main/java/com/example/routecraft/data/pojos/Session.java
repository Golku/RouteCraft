package com.example.routecraft.data.pojos;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private final String USER_ID = "USER_ID";
    private final String USER_NAME = "USER_NAME";
    private final String REMEMBER_USERNAME = "REMEMBER_USERNAME";
    private final String STAY_LOGGED_IN = "STAY_LOGGED_IN";
    private final String CURRENT_ROUTE = "CURRENT_ROUTE";
    private final String NEW_ROUTE_ID = "NEW_ROUTE_ID";

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

    public void setCurrentRoute(int routeId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CURRENT_ROUTE, routeId).apply();
    }

    public int getNewRouteId(){

        int newId = prefs.getInt(NEW_ROUTE_ID, 1) + 1;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NEW_ROUTE_ID, newId).apply();

        return newId;

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

    public int getCurrentRoute(){return prefs.getInt(CURRENT_ROUTE, 1);}
}