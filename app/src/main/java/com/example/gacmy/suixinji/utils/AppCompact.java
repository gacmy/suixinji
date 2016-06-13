package com.example.gacmy.suixinji.utils;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.example.gacmy.suixinji.R;

/**
 * Created by Administrator on 2016/6/7.
 */
public class AppCompact {


    ///兼容6.0以下设置 tint selector
    public static void setTint(Activity context,ImageView iv,int resId,int normalColorId,int pressedColorId){
        Drawable drawable = ContextCompat.getDrawable(context,resId);
        int[] colors = new int[]{ContextCompat.getColor(context, pressedColorId),ContextCompat.getColor(context,normalColorId)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states,colors);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0],drawable);
        stateListDrawable.addState(states[1],drawable);
        Drawable.ConstantState state = stateListDrawable.getConstantState();
        drawable = DrawableCompat.wrap(state==null?stateListDrawable:state.newDrawable()).mutate();
        DrawableCompat.setTintList(drawable,colorList);
        iv.setImageDrawable(drawable);

    }
}
