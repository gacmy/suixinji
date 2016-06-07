package com.example.gacmy.suixinji.myview.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.WindowManager;

import com.example.gacmy.suixinji.R;


/**
 * Created by Administrator on 2016/4/14.
 */
public class LineView  extends View {
    private Paint mPaint;
    private int width;
    private int pading = 20;
    public LineView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.kaoqin_gray));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(pading,0,width - pading,0,mPaint);
    }
}
