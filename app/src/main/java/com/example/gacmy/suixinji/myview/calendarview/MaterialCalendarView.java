package com.example.gacmy.suixinji.myview.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.gacmy.suixinji.R;

import java.util.Calendar;


/**
 * Created by Administrator on 2016/3/19.
 */
public class MaterialCalendarView extends ViewGroup {

    public static final int DEFAULT_TILE_SIZE_DP = 44;
    private static final int DEFAULT_DAYS_IN_WEEK = 7;
    private static final int DAY_NAMES_ROW = 1;

    private CalendarPager pager;
    private CalendarPagerAdapter<?> adapter;
    private CalendarDay currentMonth;

    private int titleSize = -1;
    private int accentColor;
    private OnDateSelectedListener listener;
    private OnMonthChangedListener monthListener;
    public void setCurrentDate(@Nullable CalendarDay day, boolean useSmoothScroll) {
        if (day == null) {
            return;
        }
        int index = adapter.getIndexForDay(day);
        pager.setCurrentItem(index, useSmoothScroll);

    }

    private final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            currentMonth = adapter.getItem(position);
            dispatchOnMonthChanged(currentMonth);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
    };



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return pager.dispatchTouchEvent(event);
    }

     public MaterialCalendarView(Context context) {
        this(context, null);
    }

    public MaterialCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setClipToPadding(false);
            setClipChildren(false);
        }else{
            setClipChildren(true);
            setClipToPadding(true);
        }
        pager = new CalendarPager(getContext());
        setupChildren();
        adapter = new CalendarPagerAdapter<>(this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(pageChangeListener);

        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MaterialCalendarView,0,0);

        try{

             setSelectionColor(a.getColor(R.styleable.MaterialCalendarView_mcv_selectionColor, getThemeAccentColor(context)));
             CharSequence[] array = a.getTextArray(R.styleable.MaterialCalendarView_mcv_weekDayLabels);



            setWeekDayTextAppearance(a.getResourceId(
                    R.styleable.MaterialCalendarView_mcv_weekDayTextAppearance,
                    R.style.TextAppearance_MaterialCalendarWidget_WeekDay
            ));
            setDateTextAppearance(a.getResourceId(
                    R.styleable.MaterialCalendarView_mcv_dateTextAppearance,
                    R.style.TextAppearance_MaterialCalendarWidget_Date
            ));
            setFirstDayOfWeek(Calendar.MONDAY);

        }catch (Exception e){

        }

        currentMonth = CalendarDay.today();
        setCurrentDate(currentMonth);

        if (isInEditMode()) {
            removeView(pager);
            CalendarPagerView monthView = new CalendarPagerView(this, currentMonth, getFirstDayOfWeek());
            monthView.setSelectionColor(getSelectionColor());

            addView(monthView, new LayoutParams(6 + DAY_NAMES_ROW));
        }
    }


    //设置固定的日期行数
    private int getWeekCountBasedOnMode() {
        int weekCount = 6;

        if (adapter != null && pager != null) {
            Calendar cal = (Calendar) adapter.getItem(pager.getCurrentItem()).getCalendar().clone();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            //noinspection ResourceType
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            weekCount = cal.get(Calendar.WEEK_OF_MONTH);
        }
       // Log.e("gac","weekCount:"+weekCount);
        return 5;
    }

    public int getFirstDayOfWeek() {
        return adapter.getFirstDayOfWeek();
    }

    public void setCurrentDate(@Nullable CalendarDay day) {
        setCurrentDate(day, true);

    }

    public void setFirstDayOfWeek(int day) {
        adapter.setFirstDayOfWeek(day);
    }
    public int getSelectionColor() {
        return accentColor;
    }







    public void addDecoratorGAC(EventDecorator decorator) {
        if (decorator == null) {
            return;
        }

        adapter.setDecoratorsGAC(decorator);
       // Log.e("gac", "MaterialCalendarView addDecoratorGAC");

    }


    public CalendarPagerAdapter getAdapter(){
        return  adapter;
    }
    public void setOnDateChangedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the listener to be notified upon month changes.
     *
     * @param listener thing to be notified
     */
    public void setOnMonthChangedListener(OnMonthChangedListener listener) {
        this.monthListener = listener;
    }





    /**
     * @param resourceId The text appearance resource id.
     */
    public void setDateTextAppearance(int resourceId) {
        adapter.setDateTextAppearance(resourceId);
    }

    /**
     * @param resourceId The text appearance resource id.
     */
    public void setWeekDayTextAppearance(int resourceId) {
        //adapter.setWeekDayTextAppearance(resourceId);
    }

    private static int getThemeAccentColor(Context context) {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }
    /**
     * @param color The selection color
     */
    public void setSelectionColor(int color) {
        if (color == 0) {
            if (!isInEditMode()) {
                return;
            } else {
                color = Color.GRAY;
            }
        }
        accentColor = color;
        adapter.setSelectionColor(color);
        invalidate();
    }





    public void setTitleSize(int size){
        this.titleSize = size;
        requestLayout();
    }


    @Override
    protected void onLayout(boolean changed, int left, int t, int right, int b) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentWidth = right - left - parentLeft - getPaddingRight();

        int childTop = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            int delta = (parentWidth - width) / 2;
            int childLeft = parentLeft + delta;

            child.layout(childLeft, childTop, childLeft + width, childTop + height);

            childTop += height;
        }
    }


    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Dispatch date change events to a listener, if set
     *
     * @param day      the day that was selected
     * @param selected true if the day is now currently selected, false otherwise
     */
    protected void dispatchOnDateSelected(final CalendarDay day, final boolean selected) {
        OnDateSelectedListener l = listener;
        if (l != null) {
            l.onDateSelected(MaterialCalendarView.this, day, selected);
        }
    }

    /**
     * Dispatch date change events to a listener, if set
     *
     * @param day first day of the new month
     */
    protected void dispatchOnMonthChanged(final CalendarDay day) {
        OnMonthChangedListener l = monthListener;
        if (l != null) {
            l.onMonthChanged(MaterialCalendarView.this, day);
        }
    }

    protected void onDateClicked( CalendarDay date, boolean nowSelected) {

                adapter.clearSelections();
                adapter.setDateSelected(date, true);
                dispatchOnDateSelected(date, true);

    }



    /**
     * Called by the adapter for cases when changes in state result in dates being unselected
     *
     * @param date date that should be de-selected
     */
    protected void onDateUnselected(CalendarDay date) {
        dispatchOnDateSelected(date, false);
    }

    private void setupChildren(){
        pager.setId(R.id.mcv_pager);
        pager.setOffscreenPageLimit(1);
        //调整总的行数
        addView(pager, new LayoutParams(8 + DAY_NAMES_ROW));
    }



    public boolean isPagingEnabled(){
        return pager.isPagingEnabled();
    }




    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(1);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(MaterialCalendarView.class.getName());
    }


    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(MaterialCalendarView.class.getName());
    }
    /*********************************************/

    /**
     * {@linkplain #setChildrenDrawingOrderEnabledCompat(boolean)} does some reflection that isn't needed.
     * And was making view creation time rather large. So lets override it and make it better!
     */
    class CalendarPager extends ViewPager {
        private boolean pagingEnabled = true;

        public CalendarPager(Context context) {
            super(context);
        }
        CalendarPager(Context context,AttributeSet attrs){
            super(context,attrs);
        }


        public void setChildrenDrawingOrderEnabledCompat(boolean enable) {
            setChildrenDrawingOrderEnabled(enable);
        }
        /**
         * enable disable viewpager scroll
         *
         * @param pagingEnabled false to disable paging, true for paging (default)
         */
        public void setPagingEnabled(boolean pagingEnabled) {
            this.pagingEnabled = pagingEnabled;
        }

        /**
         * @return is this viewpager allowed to page
         */
        public boolean isPagingEnabled() {
            return pagingEnabled;
        }


        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return pagingEnabled && super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return pagingEnabled && super.onTouchEvent(ev);
        }

        @Override
        public boolean canScrollVertically(int direction) {
            /**
             * disables scrolling vertically when paging disabled, fixes scrolling
             * for nested {@link ViewPager}
             */
            return pagingEnabled && super.canScrollVertically(direction);
        }

        @Override
        public boolean canScrollHorizontally(int direction) {
            /**
             * disables scrolling horizontally when paging disabled, fixes scrolling
             * for nested {@link ViewPager}
             */
            return pagingEnabled && super.canScrollHorizontally(direction);
        }



    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        //We need to disregard padding for a while. This will be added back later
        final int desiredWidth = specWidthSize - getPaddingLeft() - getPaddingRight();
        final int desiredHeight = specHeightSize - getPaddingTop() - getPaddingBottom();

        final int weekCount = getWeekCountBasedOnMode()+1;

        final int viewTileHeight = weekCount;//总共显示七行数据

        //Calculate independent tile sizes for later
        int desiredTileWidth = desiredWidth / DEFAULT_DAYS_IN_WEEK;
        int desiredTileHeight = desiredHeight / viewTileHeight;

        int measureTileSize = -1;


        if (this.titleSize > 0) {
            //We have a tileSize set, we should use that
            measureTileSize = this.titleSize;
        } else if (specWidthMode == MeasureSpec.EXACTLY) {
            if (specHeightMode == MeasureSpec.EXACTLY) {
                //Pick the larger of the two explicit sizes
                measureTileSize = Math.max(desiredTileWidth, desiredTileHeight);
            } else {
                //Be the width size the user wants
                measureTileSize = desiredTileWidth;
            }
        } else if (specHeightMode == MeasureSpec.EXACTLY) {
            //Be the height size the user wants
            measureTileSize = desiredTileHeight;
        }

        //Uh oh! We need to default to something, quick!
        if (measureTileSize <= 0) {
            measureTileSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TILE_SIZE_DP, getResources().getDisplayMetrics()
            );
        }

        //Calculate our size based off our measured tile size
        int measuredWidth = measureTileSize * DEFAULT_DAYS_IN_WEEK;
        int measuredHeight = measureTileSize * viewTileHeight;

        //Put padding back in from when we took it away
        measuredWidth += getPaddingLeft() + getPaddingRight();
        measuredHeight += getPaddingTop() + getPaddingBottom();

        //Contract fulfilled, setting out measurements
        setMeasuredDimension(
                //We clamp inline because we want to use un-clamped versions on the children
                clampSize(measuredWidth, widthMeasureSpec),
                clampSize(measuredHeight, heightMeasureSpec)
        );

        int count = getChildCount();
       // Log.e("gac", "childCount:" + count);
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            LayoutParams p = (LayoutParams) child.getLayoutParams();

            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    DEFAULT_DAYS_IN_WEEK * measureTileSize,
                    MeasureSpec.EXACTLY
            );

            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    p.height * measureTileSize,
                    MeasureSpec.EXACTLY
            );

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

    }

    private static int clampSize(int size, int spec) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);
        switch (specMode) {
            case MeasureSpec.EXACTLY: {
                return specSize;
            }
            case MeasureSpec.AT_MOST: {
                return Math.min(size, specSize);
            }
            case MeasureSpec.UNSPECIFIED:
            default: {
                return size;
            }
        }
    }


    /*******************************************/
      protected static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(int titleHeight){
            super(MATCH_PARENT,titleHeight);
        }
    }


    /*************************************/
    //public OnDate
    public interface OnDateSelectedListener {

        /**
         * Called when a user clicks on a day.
         * There is no logic to prevent multiple calls for the same date and state.
         *
         * @param widget   the view associated with this listener
         * @param date     the date that was selected or unselected
         * @param selected true if the day is now selected, false otherwise
         */
        void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected);
    }

    public interface OnMonthChangedListener {

        /**
         * Called upon change of the selected day
         *
         * @param widget the view associated with this listener
         * @param date   the month picked, as the first day of the month
         */
        void onMonthChanged(MaterialCalendarView widget, CalendarDay date);
    }


}
