package com.example.gacmy.suixinji.utils;

import android.content.Context;

/**
 * Created by gacmy on 2016/5/31.
 */
public class ActivityUtils {
    public static String[] getArray(Context context,int id){
        if(context == null){
            return null;
        }
       return context.getResources().getStringArray(id);

    }
}
