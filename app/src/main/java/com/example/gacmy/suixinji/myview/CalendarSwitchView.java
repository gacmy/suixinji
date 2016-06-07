package com.example.gacmy.suixinji.myview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.myview.calendarview.CalendarDay;
import com.example.gacmy.suixinji.myview.filpanimation.AnimationFactory;
import com.example.gacmy.suixinji.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by gac on 2016/6/7.
 */
public class CalendarSwitchView extends FrameLayout {
    View rootview;
    private TextView tv_year1,tv_year2,tv_year3,tv_year4;
    private TextView tv_month1,tv_month2;
    private TextView tv_day1,tv_day2;
    private ViewAnimator va_year1,va_year2,va_year3,va_year4;
    private ViewAnimator va_month1,va_month2;
    private ViewAnimator va_day1,va_day2;
    private List<TextView> tv_yearlist = new ArrayList<>();
    private List<TextView> tv_monthList = new ArrayList<>();
    private List<TextView> tv_dayList = new ArrayList<>();
    private List<ViewAnimator> va_yearList = new ArrayList<>();
    private List<ViewAnimator> va_monthList = new ArrayList<>();
    private List<ViewAnimator> va_dayList = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_yearlist.add(tv_year1);
            va_yearList.add(va_year1);
            tv_yearlist.add(tv_year2);
            va_yearList.add(va_year2);
            tv_yearlist.add(tv_year3);
            va_yearList.add(va_year3);
            tv_yearlist.add(tv_year4);
            va_yearList.add(va_year4);
            tv_monthList.add(tv_month1);
            va_monthList.add(va_month1);
            tv_monthList.add(tv_month2);
            va_monthList.add(va_month2);
            tv_dayList.add(tv_day1);
            va_dayList.add(va_day1);
            tv_dayList.add(tv_day2);
            va_dayList.add(va_day2);
            tv_year1.setText(cur_year[0]+"");
            tv_year2.setText(cur_year[1] +"");
            tv_year3.setText(cur_year[2]+"");
            tv_year4.setText(cur_year[3]+"");
            tv_month1.setText(cur_month[0]+"");
            tv_month2.setText(cur_month[1]+"");
            tv_day1.setText(cur_day[0]+"");
            tv_day2.setText(cur_day[1]+"");
        }
    };

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
        va_year1 = ActivityUtils.mGetView(rootview,R.id.flipper_year1);


        tv_year2 = ActivityUtils.mGetView(rootview,R.id.year2);
        ActivityUtils.setViewShaow(activity,tv_year2);
        va_year2 = ActivityUtils.mGetView(rootview,R.id.flipper_year2);


        tv_year3 = ActivityUtils.mGetView(rootview,R.id.year3);
        ActivityUtils.setViewShaow(activity, tv_year3);
        va_year3 = ActivityUtils.mGetView(rootview,R.id.flipper_year3);


        tv_year4 = ActivityUtils.mGetView(rootview, R.id.year4);
        ActivityUtils.setViewShaow(activity, tv_year4);
        va_year4 = ActivityUtils.mGetView(rootview,R.id.flipper_year4);

         tv_month1 = ActivityUtils.mGetView(rootview,R.id.month1);
        ActivityUtils.setViewShaow(activity, tv_month1);
        va_month1 = ActivityUtils.mGetView(rootview,R.id.flipper_month1);


         tv_month2 = ActivityUtils.mGetView(rootview,R.id.month2);
         ActivityUtils.setViewShaow(activity, tv_month2);
        va_month2 = ActivityUtils.mGetView(rootview,R.id.flipper_month2);


         tv_day1 = ActivityUtils.mGetView(rootview,R.id.day1);
         ActivityUtils.setViewShaow(activity, tv_day1);
        va_day1 = ActivityUtils.mGetView(rootview,R.id.flipper_day1);


         tv_day2 = ActivityUtils.mGetView(rootview,R.id.day2);
         ActivityUtils.setViewShaow(activity, tv_day2);
        va_day2 = ActivityUtils.mGetView(rootview,R.id.flipper_day2);

         new Thread(new Runnable() {
             @Override
             public void run() {
                 initDate();
                 mHandler.sendEmptyMessage(0);
             }
         }).start();


    }
    private int[] pre_year = new int[4];
    private int[] pre_month = new int[2];
    private int[] pre_day = new int[2];
    private void initDate(){
        int year = CalendarDay.today().getYear();
        int month = CalendarDay.today().getMonth()+1;
        int day = CalendarDay.today().getDay();
        Log.e("gac","year:"+year+" month:"+month+" day:"+day);
        int i = 3;
        while (year > 0){
            pre_year[i] = year %10;
            year = year/10;
            i--;
        }
        for(int j = 0; j < pre_year.length;j++){
            Log.e("gac","year:"+pre_year[j]);
        }
        if(month >= 10){
            pre_month[0] = 1;
            pre_month[1] = month %10;
        }else{
            pre_month[0] = 0;
            pre_month[1] = month;
        }

        if(day >=  10){
            pre_day[0] = day/10;
            pre_day[1] = day % 10;
        }else{
            pre_day[0] = 0;
            pre_day[1] = day;
        }
        System.arraycopy(pre_year,0,cur_year,0,pre_year.length);
        System.arraycopy(pre_day,0,cur_day,0,pre_day.length);
        System.arraycopy(pre_month,0,cur_month,0,pre_month.length);
    }
    private int[] cur_year = new int[4];
    private int[] cur_month = new int[2];
    private int[] cur_day = new int[2];
    private void setDate(int year,int month,int day){

        int i = 3;
        while (year > 0){
            cur_year[i] = year %10;
            year = year/10;
            i--;
        }
       // Log.e("gac","i:"+i);

        if(month >= 10){
            cur_month[0] = 1;
            cur_month[1] = month %10;
        }else{
            cur_month[0] = 0;
            cur_month[1] = month;
        }

        if(day >=  10){
            cur_day[0] = day/10;
            cur_day[1] = day % 10;
        }else{
            cur_day[0] = 0;
            cur_day[1] = day;
        }
    }
    //设置日期改变时候的动画效果
    public void setDateChanged(int year,int month,int day){
        setDate(year,month,day);
        //Log.e("gac","preyearLen:"+pre_year.length+" curyearLen:"+cur_year.length);
        for(int i = 0;i < cur_year.length; i++){
            setText(cur_year[i],pre_year[i],va_yearList.get(i),tv_yearlist.get(i));
        }
        for(int i = 0; i < cur_month.length; i++){
            setText(cur_month[i],pre_month[i],va_monthList.get
                    (i),tv_monthList.get(i));
        }
        for(int i = 0; i < cur_day.length; i++){
            setText(cur_day[i],pre_day[i],va_dayList.get(i),tv_dayList.get(i));
        }
        for(int i =0; i < cur_day.length;i++){
            Log.e("gac","day:"+cur_day[i]);
        }
        for(int i = 0; i < pre_day.length;i++){
            Log.e("gac","preday:"+pre_day[i]);
        }
        pre_year = cur_year.clone();
        pre_month = cur_month.clone();
        pre_day = cur_day.clone();
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
