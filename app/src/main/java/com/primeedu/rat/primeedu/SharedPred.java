package com.primeedu.rat.primeedu;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPred {
    private static Context context;
    private static SharedPred mInstance;
    private static final String SHARED_PREF_NAME = "demo";
    private static final String SHARED_PREF_TOKEN = "token";
    private SharedPred(Context context){
        this.context = context;
    }
    public static synchronized SharedPred getmInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPred(context);

        }
        return mInstance;
    }
    public boolean storeToken(String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_TOKEN,token);
        editor.apply();
        return  true;
    }
    public String getToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_TOKEN,null);
    }
}
