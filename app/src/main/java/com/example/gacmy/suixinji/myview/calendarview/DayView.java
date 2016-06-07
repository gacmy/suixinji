package com.example.gacmy.suixinji.myview.calendarview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;

import com.example.gacmy.suixinji.R;


/**
 * Created by Administrator on 2016/3/18.
 */
public class DayView extends CheckedTextView {
    private CalendarDay date;//当前dayview需要显示的日期
    private int selectionColor = Color.GRAY;//选中文本需要设置的颜色
    private Drawable customBackground;
    private boolean isInMonth =true;
    private boolean isInRange = true;
    private Context mContext;
    public DayView(Context context,CalendarDay day,boolean visible){
        this(context, day);
        this.mContext = context;
        if(!visible){
            setTextColor(getResources().getColor(R.color.calendartextcor0));
            setEnabled(false);
        }

    }
    public DayView(Context context,CalendarDay day){
        super(context);
        this.mContext = context;
        setGravity( Gravity.CENTER_HORIZONTAL | Gravity.TOP);//设置内容居中

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }
        setDay(day);
        setTodayColor();
    }


    public void setDay(CalendarDay date){
        this.date = date;
        setText(getLabel());
    }

    public CalendarDay getDate(){
        return date;
    }

    public void setupSelection(boolean isRange,boolean inMonth){
        isInMonth = inMonth;
        isInRange = isRange;
        setEnabled();
    }

    private void setEnabled(){
        boolean enabled = true;
        super.setEnabled(true);
        setVisibility(View.VISIBLE);
    }
    public String getLabel(){


        return CalendarUtils.getDay(CalendarDay.from(date.getDate()).getCalendar())+"";
    }
    //设置选择单天时候的背景颜色
    public void setSelectionColor(int color){
        this.selectionColor = color;
        regenerateBackground();
    }

    private Drawable selectionDrawable;
    private void setTodayColor(){
        if(CalendarUtils.equal(CalendarDay.today().getCalendar(),date.getCalendar())){
            setTextColor(Color.RED);
        }
    }
    public void setSelectionDrawable(Drawable drawable) {
        if (drawable == null) {
            this.selectionDrawable = null;
        } else {
            this.selectionDrawable = drawable.getConstantState().newDrawable(getResources());
        }
        regenerateBackground();
    }

    void applyFacadeGAC(String mode,boolean visible){
      //  Log.e("gac", "DayView: applyFacadeGAC");
        //this.isDecoratedDisabled = facade.areDaysDisabled();

        setTodayColor();
        if(!visible){
            setTextColor(getResources().getColor(R.color.calendartextcor0));
            setEnabled(false);
        }
        //让大于当天的日期不可以点击
        if (CalendarUtils.compareDay(date, CalendarDay.today()) == 1){
            setEnabled(false);
        }
        if(mode.equals("0")  || mode.equals("1")){
            String label = getLabel();
            SpannableString formattedLabel = new SpannableString(getLabel());
            formattedLabel.setSpan(new TextSpan(mode), 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(formattedLabel);
        }else{
            setText(getLabel());
        }
    }

    //设置背景图片
    public void setCustomBackground(Drawable drawable){
        if(drawable == null){
            this.customBackground = null;
        }else{
            this.customBackground = drawable.getConstantState().newDrawable(getResources());
        }
        invalidate();
    }


    private final Rect tempRect = new Rect();
    @Override
    protected void onDraw(Canvas canvas) {

        setTextSize(18);
        if(customBackground != null){
            canvas.getClipBounds(tempRect);
            customBackground.setBounds(tempRect);
            customBackground.setState(getDrawableState());
            customBackground.draw(canvas);
        }
        super.onDraw(canvas);
    }

    //重新生成背景
    private void regenerateBackground(){
        setBackgroundDrawable(generateBackground(selectionColor));
    }
    private static Drawable generateBackground(int color){
        StateListDrawable drawable = new StateListDrawable();
        drawable.setExitFadeDuration(100);
        drawable.addState(new int[]{android.R.attr.state_checked},generateCircleDrawable(color) );
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            generateRippleDrawable(color);
        }else{
            drawable.addState(new int[]{android.R.attr.state_pressed},generateCircleDrawable(color) );
        }
        drawable.addState(new int[]{}, generateCircleDrawable(Color.TRANSPARENT));
        return drawable;
    }
    //生成圆的的drawable
    private static Drawable generateCircleDrawable(final int color){
        OvalShapeX o = new OvalShapeX();

        ShapeDrawable drawable = new ShapeDrawable(o);
       // Log.e("gac", "o heigth:"+o.getHeight());
      //  drawable.setIntrinsicWidth((int)o.getHeight());
        drawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {

            @Override
            public Shader resize(int width, int height) {
               // Log.e("gac","width:"+width+" heigth:"+height);
                return new LinearGradient(0, 0, 0, 0, color, color, Shader.TileMode.REPEAT);
            }
        });
        return drawable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable generateRippleDrawable(final int color){
        ColorStateList list = ColorStateList.valueOf(color);
        Drawable mask = generateCircleDrawable(Color.WHITE);
        return new RippleDrawable(list,null,mask);
    }


}


































