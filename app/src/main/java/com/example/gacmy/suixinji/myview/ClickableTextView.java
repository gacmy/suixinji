package com.example.gacmy.suixinji.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.gacmy.suixinji.R;

/**
 * Created by Administrator on 2016/6/8.
 */
public class ClickableTextView extends TextView implements View.OnTouchListener{
    private int color;
    private Context mContext;
    public ClickableTextView(Context context) {
        super(context);
        init(context);
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        setOnTouchListener(this);
        setClickable(true);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.round_rect_selected);
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundResource(R.drawable.round_rect);
                break;
        }
        return false;
    }
}
