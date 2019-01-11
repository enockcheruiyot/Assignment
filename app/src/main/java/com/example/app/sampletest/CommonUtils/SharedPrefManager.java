/*
 * Created by Prabhakaran on 10/1/2019
 */
package com.example.app.sampletest.CommonUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.example.app.sampletest.CommonUtils.Constants.PREF_NAME;


public class SharedPrefManager {

    private SharedPreferences sharedPreferences;
    private Context mContext;
    private int PRIVATE_MODE = 0;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefManager(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void saveIsLoggedIn(Context context, Boolean isLoggedIn){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean (Constants.IS_LOGGED_IN, isLoggedIn);
        editor.apply();

    }
    public boolean getISLogged_IN() {
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false);
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }
}
