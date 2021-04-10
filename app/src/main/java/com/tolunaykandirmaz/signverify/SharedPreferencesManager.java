package com.tolunaykandirmaz.signverify;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SharedPreferencesManager {

    public static final String SETTINGS_KEY = "user_settings";

    private SharedPreferences sharedPreferences;

    private static SharedPreferencesManager manager;

    private SharedPreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static SharedPreferencesManager getInstance(Context context){
        if(manager == null){
            manager = new SharedPreferencesManager(context);
        }

        return manager;
    }

    public <S> void setObject(String key, S object){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonObject = gson.toJson(object);
        editor.putString(key, jsonObject);
        editor.apply();
    }

    public void setString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setLong(String key, Long value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void setBoolean(String key, boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public <S> S getObject(String key, Class<S> object){
        Gson gson = new Gson();
        String jsonObject = sharedPreferences.getString(key,"");
        return gson.fromJson(jsonObject, (Type) object);
    }

    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }

    public Long getLong(String key){
        return sharedPreferences.getLong(key,0L);
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public boolean isKeyExist(String key){
        return sharedPreferences.contains(key);
    }

    public <S> void clearObject(String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

}
