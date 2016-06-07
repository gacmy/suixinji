package com.example.gacmy.suixinji.myview.calendarview;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;

import java.util.Calendar;

//显示一周的视图
public class WeekDayView extends TextView {
    //private int dayofWeek;//在一个星期中排第几个位置

    public WeekDayView(Context context,int dayofWeek) {
        super(context);
        setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }
        //setPadding(0,0,0,0);

        setTextSize(14);
        init();
        setDayofWeek(dayofWeek);
    }
    private void init(){


    }

    public void setDayofWeek(int dayofWeek){
        setText(getDayofWeek(dayofWeek));
    }

    public void setDayofWeek(Calendar calendar){
        setDayofWeek(CalendarUtils.getDayOfWeek(calendar));
    }

    private CharSequence[] weekDayLabels = {"日","一","二","三","四","五","六"};//星期几的标签序列
    //由一个星期的第几天参数 结果返回的是星期几
    private CharSequence getDayofWeek(int dayofWeek){
        return weekDayLabels[dayofWeek-1];
    };


}
