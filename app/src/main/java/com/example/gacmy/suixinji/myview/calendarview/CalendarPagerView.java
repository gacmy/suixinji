package com.example.gacmy.suixinji.myview.calendarview;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.gacmy.suixinji.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;



//一个月的显示页面
public class CalendarPagerView extends ViewGroup implements View.OnClickListener {
    private static final int DEFAULT_DAYS_IN_WEEK = 7;
    private static final int DEFAULT_MAX_WEEKS = 6;
    private boolean isEedit = true;
    private static final ArrayList<WeekDayView> weekDayViews = new ArrayList<>();//显示周一到周日
    private CalendarDay firstViewDay;
    private CalendarDay minDate = null;
    private CalendarDay maxDate = null;
    private int firstDayofWeek;//第一天是星期几
    private final Collection<DayView> dayViews = new ArrayList<>();
    private static final Calendar tempWorkingCalendar = CalendarUtils.getInstance();
    private  EventDecorator decoratorResults = null;
    private MaterialCalendarView mcv;
    private int currentMonth;
    public CalendarPagerView( MaterialCalendarView view, CalendarDay firstViewDay, int firstDayOfWeek) {
        super(view.getContext());
        this.mcv = view;
        this.firstViewDay = firstViewDay;
        this.firstDayofWeek = Calendar.MONDAY;
        setClipChildren(false);
        setClipToPadding(false);
        buildWeekDays(resetAndGetWorkingCalendar());
        buildLineView();
        buildDayViews(dayViews,resetAndGetWorkingCalendar());
    }

    private void buildLineView(){
        addView(new LineView(getContext()));
    }
    @Override
    public void onClick(View v) {
        if(v instanceof  DayView){
            DayView dayView = (DayView)v;
            //CalendarView 调用日期点击事件
            mcv.onDateClicked(dayView.getDate(),!dayView.isChecked());
        }
    }

    //填充一周的数据
    private void buildWeekDays(Calendar calendar){

        for(int i = 0 ; i < DEFAULT_DAYS_IN_WEEK;i++){
           // Log.e("gac","buildWeekDays:date:"+CalendarDay.from(calendar).toString());
            WeekDayView weekDayView = new WeekDayView(getContext(),CalendarUtils.getDayOfWeek(calendar));//CalendarUtils.getDayOfWeek(calendar));
            if(weekDayView.getText().toString().equals("六") || weekDayView.getText().toString().equals("日")){
                weekDayView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }else{
                weekDayView.setTextColor(getResources().getColor(R.color.calendartextcor0));
            }
            weekDayViews.add(weekDayView);
            addView(weekDayView,new LayoutParams0());
            calendar.add(Calendar.DATE,1);
        }
    }
    //建立四十二天的dayview视图
    protected void buildDayViews(Collection<DayView> dayViews,Calendar calendar){
        for(int r = 0; r < DEFAULT_MAX_WEEKS; r++){
            for(int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++){
                //Log.e("gac","buildDayViews.....");
                addDayView(dayViews, calendar);
            }
        }
    }

    //添加每一天的控件
    private void addDayView(Collection<DayView> dayViews,Calendar calendar){
        CalendarDay day = CalendarDay.from(calendar);
        DayView dayView = null;
      //  Log.e("gac","currentMonth:"+currentMonth);
        if(day.getMonth() != currentMonth){
             dayView = new DayView(getContext(),day,false);
        }else{
            dayView = new DayView(getContext(),day,true);
        }

        dayView.setOnClickListener(this);
        dayViews.add(dayView);
       //Log.e("gac", "build dayView dayView date " + dayView.getDate().toString());
        addView(dayView, new LayoutParams0());
        calendar.add(Calendar.DATE, 1);
    }

    //得到正在显示中的日期
    private Calendar resetAndGetWorkingCalendar(){
        getFirstViewDay().copyTo(tempWorkingCalendar);
        tempWorkingCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        currentMonth =  CalendarDay.from(tempWorkingCalendar).getMonth();
       // Log.e("gac", "tempWorkingCalendar:" + CalendarDay.from(tempWorkingCalendar).toString());
        int dow = CalendarUtils.getDayOfWeek(tempWorkingCalendar);
       // Log.e("gac","dow:"+dow+"");
        //Log.e("gac","getFirstDayOfWeek():"+getFirstDayofWeek());
        boolean removeRow = false;
       int delta = getFirstDayofWeek() - dow;
        if(delta > 0 ){
            removeRow  = true;
        }else{
            removeRow =false;
        }

       if(removeRow){
            delta -= DEFAULT_DAYS_IN_WEEK;
        }
        tempWorkingCalendar.add(Calendar.DATE, delta);
       // Log.e("gac","tempWorkingCalendar:"+CalendarDay.from(tempWorkingCalendar).toString());
        return  tempWorkingCalendar;
    }
    //
    private int getFirstDayofWeek(){
        return firstDayofWeek;
    }


    public CalendarDay getMonth(){
      return  getFirstViewDay();
    }




    void setDayViewDecoratorsGAC(EventDecorator results){
       // Log.e("gac", "pager set dayview decortor");
            this.decoratorResults = results;
            //第一次的数据靠谱其它时候不靠谱了
            invalidateDecoratorsGAC();


    }
    public void setWeekDayTextAppearance(int taId) {
        for (WeekDayView weekDayView : weekDayViews) {
            weekDayView.setTextAppearance(getContext(), taId);
        }
    }

    public void setDateTextAppearance(int taId) {
        for (DayView dayView : dayViews) {
            dayView.setTextAppearance(getContext(), taId);
        }
    }


    public void setMinimumDate(CalendarDay minDate) {
        this.minDate = minDate;
        updateUi();
    }

    public void setMaximumDate(CalendarDay maxDate) {
        this.maxDate = maxDate;
        updateUi();
    }

    public void setSelectedDates(Collection<CalendarDay> dates) {
        for (DayView dayView : dayViews) {
            CalendarDay day = dayView.getDate();
            dayView.setChecked(dates != null && dates.contains(day));
        }
        postInvalidate();
    }
    public void setSelectionColor(int color) {
        for (DayView dayView : dayViews) {
            dayView.setSelectionColor(color);
        }
    }


    private int firstDayOfWeek;
    public void setFirstDayOfWeek(int dayOfWeek) {
        this.firstDayOfWeek = dayOfWeek;

        Calendar calendar = resetAndGetWorkingCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        for (WeekDayView dayView : weekDayViews) {
            dayView.setDayofWeek(calendar);
            calendar.add(Calendar.DATE, 1);
        }

        calendar = resetAndGetWorkingCalendar();
        for (DayView dayView : dayViews) {
            CalendarDay day = CalendarDay.from(calendar);
            dayView.setDay(day);
            calendar.add(Calendar.DATE, 1);
        }

        updateUi();
    }

    public void setSelectionEnabled(boolean selectionEnabled) {
        for (DayView dayView : dayViews) {
            dayView.setOnClickListener(selectionEnabled ? this : null);
            dayView.setClickable(selectionEnabled);
        }
    }

    public void updateUi(){
        for(DayView dayView: dayViews){
            CalendarDay day = dayView.getDate();
           dayView.setupSelection(day.isInRange(minDate,maxDate),isDayEnabled(day));
        }
        postInvalidate();
    }
    private CalendarDay getFirstViewDay(){
        return firstViewDay;
    }

    protected  boolean isDayEnabled(CalendarDay day){
        return day.getMonth() == getFirstViewDay().getMonth();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(specHeightMode == MeasureSpec.UNSPECIFIED || specWidthMode == MeasureSpec.UNSPECIFIED){
            throw new IllegalStateException("CalendarPagerView should never be left to decide it's size");
        }
        final int measureTitleSize = specWidthSize/DEFAULT_DAYS_IN_WEEK;

        setMeasuredDimension(specWidthSize, specHeightSize);

        int count = getChildCount();
        //Log.e("gac","onMeasure:count:"+count);
        for(int i = 0; i < count; i++){
            final View child = getChildAt(i);

            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measureTitleSize,
                    MeasureSpec.EXACTLY);
            int childHeightMeasureSpec;
            if(i < 7){
                 childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measureTitleSize / 2,
                         MeasureSpec.EXACTLY);
            }else{
                 childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(3 * measureTitleSize / 4,
                         MeasureSpec.EXACTLY);
            }

            child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
        }
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        //Log.e("gac","child count:"+getChildCount());
        final int parentLeft = 0;
        int childTop = 0;
        int childLeft = parentLeft;
        int k = 0;
        for(int i = 0; i < count; i++){
            final View child = getChildAt(i);

            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            child.layout(childLeft,childTop,childLeft+width,childTop+height);
            childLeft+=width;
            if(i == 7){
                childLeft = 0;
            }
            if(i >= 8){
                //0-6 七天 每次到6时候的就需要换行 重新去排列位置
                if( (i-8) % DEFAULT_DAYS_IN_WEEK == (DEFAULT_DAYS_IN_WEEK - 1)){
                    //Log.e("gac","***********i********"+k++);
                    childLeft = parentLeft;//换行的时候左边的位置置0
                    childTop+=height;//换行的时候高度的位置切换到下一行
                }
            }else{
                //0-6 七天 每次到6时候的就需要换行 重新去排列位置
                if( i % DEFAULT_DAYS_IN_WEEK == (DEFAULT_DAYS_IN_WEEK - 1)){
                    //Log.e("gac","***********i********"+k++);
                    childLeft = parentLeft;//换行的时候左边的位置置0
                    childTop+=height;//换行的时候高度的位置切换到下一行
                }
            }

        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams();
    }

    //是否延迟子元素的按下的状态

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof  LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CalendarPagerView.class.getName());
    }

    public void invalidateDecoratorsGAC(){
        String currentmode = "";
        //Log.e("gac","invalidateDecortorsGac: size:"+dayViews.size());
        if(decoratorResults == null){
            return;
        }
        for (DayView dayView : dayViews) {
            currentmode = decoratorResults.shouldDecorateGAC(dayView.getDate());
            boolean visible = dayView.getDate().getMonth()==currentMonth;
            dayView.applyFacadeGAC(currentmode,visible);
        }
    }


    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CalendarPagerView.class.getName());
    }




    protected static class LayoutParams extends MarginLayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams() {
            super(WRAP_CONTENT, WRAP_CONTENT);
        }
    }
    protected static class LayoutParams0 extends MarginLayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams0() {
            super(WRAP_CONTENT, WRAP_CONTENT);
            setMargins(0,0,0,20);

            //setMarginEnd(0);
        }
    }
}
