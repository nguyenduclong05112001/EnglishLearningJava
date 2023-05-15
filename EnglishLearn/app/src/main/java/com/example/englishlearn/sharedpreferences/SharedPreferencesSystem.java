package com.example.englishlearn.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesSystem {
    public static final String SP_NAME = "MySharedPreferences";
    private Context mContext;

    public SharedPreferencesSystem(Context mContext) {
        this.mContext = mContext;
    }

    public void pubIsLogin(String key,boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getIsLogin(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public void pubIsCheckedRemember(String key,boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getIsCheckedRemember(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public void pubUsername(String key,String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getUsername(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void pubPassword(String key,String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPassword(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void pubUsernametoCheck(String key,String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getUsernametoCheck(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void pubPointoday(String key,String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPointoday(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void pubIsFirstCompleteOfDay(String key,boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getIsFirstCompleteOfDay(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public void pubFirstInApp(String key,boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getFirstInApp(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
