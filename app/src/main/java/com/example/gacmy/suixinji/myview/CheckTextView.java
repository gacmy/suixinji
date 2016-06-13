package com.example.gacmy.suixinji.myview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.gacmy.suixinji.R;

/**
 * Created by Administrator on 2016/6/8.
 */
public class CheckTextView extends TextView {
    public boolean isChecked = false;
    public CheckTextView(Context context) {
        super(context);
        init();
    }

    public CheckTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        setUncheckedBg();
        setOnClickListener();
    }
    public void setUncheckedBg(){
        setBackgroundResource(R.drawable.circle);
        setTextColor(Color.GRAY);
        isChecked = false;
    }
    public void setCheckedBg(){
        setBackgroundResource(R.drawable.circle_red);
        setTextColor(Color.WHITE);
        isChecked = true;
    }
    private void setOnClickListener(){
        setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isChecked == false){

                    setCheckedBg();
                }else{

                    setUncheckedBg();
                }
            }
        });
    }
}
