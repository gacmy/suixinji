package com.example.gacmy.suixinji.myview.calendarview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

/**
 * Created by Administrator on 2016/3/17.
 */
public class TextSpan implements LineBackgroundSpan {
    private  int color ;
    private String mText;
    public static String EXCEPTION = "1";//
    public static String NORMAL = "0";
    private String mode = "-1";
    public TextSpan(int color, String text){
        this.color = color;
        this.mText = text;
    }
    public TextSpan(){
        this.color = Color.RED;
        this.mText = "正常";
    }
    public TextSpan(String mode){
        this.mode = mode;
        if(this.mode.equals(EXCEPTION)){
            this.color = Color.parseColor("#fe8604") ;
            this.mText = "未考勤";
        }else if(this.mode.equals(NORMAL)){
            this.color = Color.BLACK;
            this.mText = "考勤";
        }
    }
    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        int oldColor = p.getColor();
        float oldTextSize = p.getTextSize();
        float rate = oldTextSize/54;//以42为标准进行适配
        //textsize 42 bottom 57  45
        //textsize 21 bottom 29 basline 23
        //27 37   29
        //54 73 58
        Log.e("gac", "oldTextSize:" + oldTextSize);
        float newtextSize = p.getTextSize()/2;//sp
        Log.e("gac", "bottom:" + bottom + " basline:" + baseline);
        p.setColor(color);
        p.setTextSize(newtextSize);
        float center = (right+left)/2;
        float textLength = this.mText.length()*newtextSize;
        c.drawText(mText,center-textLength/2,bottom+baseline-45*rate,p);
        p.setColor(oldColor);
        p.setTextSize(oldTextSize);
    }
}
