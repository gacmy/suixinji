package com.example.gacmy.suixinji.myview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.myview.filpanimation.AnimationFactory;
import com.example.gacmy.suixinji.utils.ActivityUtils;


/**
 * Created by gac on 2016/6/7.
 */
public class CalendarSwitchView extends FrameLayout {
    View rootview;
    private TextView tv_year1;
    private TextView tv_year2;
    private TextView tv_year3;
    private TextView tv_year4;
    private TextView tv_month1;
    private TextView tv_month2;
    private TextView tv_day1;
    private TextView tv_day2;
    private ViewAnimator va_year1;
    private int int_cur_year1,int_cur_year2,int_cur_year3,int_cur_year4;
    private int int_cur_month1,int_cur_month2;
    private int int_cur_day1,int_cur_day2;
    private int int_pre_year1,int_pre_year2,int_pre_year3,int_pre_year4;
    private int int_pre_month1,int_pre_month2;
    private int int_pre_day1,int_pre_day2;


    public CalendarSwitchView(Context context) {
        super(context);
        init(context);
    }

    public CalendarSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        Log.e("gac","init");
        Activity activity = (Activity)context;
        rootview = LayoutInflater.from(context).inflate(R.layout.calendar_dateswitch_layout, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(rootview,layoutParams);
        tv_year1 = ActivityUtils.mGetView(rootview, R.id.year1);
         ActivityUtils.setViewShaow(activity,tv_year1);

        tv_year2 = ActivityUtils.mGetView(rootview,R.id.year2);
        ActivityUtils.setViewShaow(activity,tv_year2);
        tv_year3 = ActivityUtils.mGetView(rootview,R.id.year3);
        ActivityUtils.setViewShaow(activity, tv_year3);
        tv_year4 = ActivityUtils.mGetView(rootview, R.id.year4);
        ActivityUtils.setViewShaow(activity, tv_year4);
        tv_month1 = ActivityUtils.mGetView(rootview,R.id.month1);
        ActivityUtils.setViewShaow(activity, tv_month1);
        tv_month2 = ActivityUtils.mGetView(rootview,R.id.month2);
        ActivityUtils.setViewShaow(activity, tv_month2);
        tv_day1 = ActivityUtils.mGetView(rootview,R.id.day1);
        ActivityUtils.setViewShaow(activity, tv_day1);
        tv_day2 = ActivityUtils.mGetView(rootview,R.id.day2);
        ActivityUtils.setViewShaow(activity, tv_day2);
        va_year1 = ActivityUtils.mGetView(rootview,R.id.flipper_year1);
    }

    public void setDate(String date){

    }

    //当前某个日期中的一个数 不等于前一个数则 改变 设置动画效果
    private void setText(int cur,int pre,ViewAnimator viewAnimator,TextView textView){
        if(cur != pre){
            textView.setText(""+cur);
            AnimationFactory.flipTransition(viewAnimator, AnimationFactory.FlipDirection.TOP_BOTTOM);
        }
    }
    public void setTextYear1(){
        tv_year1.setText("3");
        AnimationFactory.flipTransition(va_year1, AnimationFactory.FlipDirection.TOP_BOTTOM);

    }
}
