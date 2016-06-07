package com.example.gacmy.suixinji.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.gacmy.suixinji.myview.shadowhelper.ShadowProperty;
import com.example.gacmy.suixinji.myview.shadowhelper.ShadowViewHelper;

/**
 * Created by gacmy on 2016/5/31.
 */
public class ActivityUtils {

    public static  <T extends View> T mGetView(View view,int resid){
        return (T)view.findViewById(resid);
    }
    public static <T extends View> T mGetView(Activity context,int resid){
        return (T)context.findViewById(resid);
    }
    public static String[] getArray(Context context,int id){
        if(context == null){
            return null;
        }
       return context.getResources().getStringArray(id);

    }

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        //int height = wm.getDefaultDisplay().getHeight();
        return width;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void setViewShaow(Activity context,TextView shadowView){
        ShadowViewHelper.bindShadowHelper(
                new ShadowProperty()
                        .setShadowColor(0x77000000)
                        .setShadowRadius(dip2px(context, 1))
                , shadowView);
    }
}
