package com.project.labtest;

import android.content.Context;
import android.content.SharedPreferences;


import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by admin on 05/03/2019.
 */

public class SharedPreference {

    public static String My_Prefrences = "Prefrences";
    static SharedPreferences mPref;
    SharedPreferences.Editor editor;

    public SharedPreference(Context mContext) {
        mPref = mContext.getSharedPreferences(My_Prefrences, Context.MODE_PRIVATE);
        editor = mPref.edit();
    }



    public static boolean isFirstTime() {
        return mPref.getBoolean("IS_FIRST_TIME",false);
    }

    public static void setFirstTime() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean("IS_FIRST_TIME", true);
        editor.commit();
    }



}
