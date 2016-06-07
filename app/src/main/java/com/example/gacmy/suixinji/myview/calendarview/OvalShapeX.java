package com.example.gacmy.suixinji.myview.calendarview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

/**
 * Created by Administrator on 2016/5/23.
 */
public class OvalShapeX extends RectShape {
    public OvalShapeX() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        final RectF rect = rect();
        float rate = rect.bottom/93;
       Log.e("gac", "rect left：" + rect.left + " top:" + rect.top + " right:" + rect.right + " bottom:" + rect.bottom);
        rect.left = 16*rate;
        rect.right = rect.bottom+16*rate;
        rect.top = 0;

        canvas.drawOval(rect, paint);
    }
//
//    @Override
//    public void getOutline(Outline outline) {
//        final RectF rect = rect();
//
//
//        rect.left = 25;
//        rect.right = rect.bottom+25;
//        rect.top = 0;
//
//       // rect.offset(10.0f,10.0f);
//       // outline.
//       // Log.e("gac","rect left："+rect.left+" top:"+rect.top+" right:"+rect.right+" bottom:"+rect.bottom);
//        outline.setOval((int) Math.ceil(rect.left), (int) Math.ceil(rect.right),
//                (int) Math.floor(rect.right), (int) Math.floor(rect.bottom));
//    }

}
