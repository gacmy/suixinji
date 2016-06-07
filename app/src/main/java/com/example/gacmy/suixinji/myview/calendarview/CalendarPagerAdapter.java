package com.example.gacmy.suixinji.myview.calendarview;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/3/19.
 */
public class CalendarPagerAdapter <V extends CalendarPagerView> extends PagerAdapter {

    private ArrayDeque<CalendarPagerView> currentViews;
    private CalendarDay today;
    private Integer color = null;
    private EventDecorator decorator;
    private Integer dateTextApperance = null;
    private Integer weekDayTextApperance = null;
    private CalendarDay minDate = null;
    private CalendarDay maxDate = null;
    private Monthly rangeIndex;
    private List<CalendarDay> selectedDates = new ArrayList<>();


    private int firstDayOfTheWeek = Calendar.MONDAY;
    private boolean selectionEnabled = true;
    private MaterialCalendarView mcv;
    CalendarPagerAdapter(MaterialCalendarView mcv) {
        this.mcv = mcv;
        this.today = CalendarDay.today();
        currentViews = new ArrayDeque<>();
        currentViews.iterator();
       setRangeDates(null, null);
    }



    public void setRangeDates(CalendarDay min, CalendarDay max) {
        this.minDate = min;
        this.maxDate = max;
        for (CalendarPagerView pagerView : currentViews) {
            pagerView.setMinimumDate(min);
            pagerView.setMaximumDate(max);
        }
         if (min == null) {
            min = CalendarDay.from(today.getYear() - 200, today.getMonth(), today.getDay());
        }
        if (max == null) {
            max = CalendarDay.from(today.getYear() + 200, today.getMonth(), today.getDay());
        }
        rangeIndex = createRangeIndex(min, max);
        notifyDataSetChanged();
        invalidateSelectedDates();
    }

    private void invalidateSelectedDates(){
        validateSelectedDates();
        for(CalendarPagerView pagerView : currentViews){
            pagerView.setSelectedDates(selectedDates);
        }
    }


    private Integer dateTextAppearance;
    public void setDateTextAppearance(int taId) {
        if (taId == 0) {
            return;
        }
        this.dateTextAppearance = taId;
        for (CalendarPagerView pagerView : currentViews) {
            pagerView.setDateTextAppearance(taId);
        }
    }

    private void validateSelectedDates() {
        for (int i = 0; i < selectedDates.size(); i++) {
            CalendarDay date = selectedDates.get(i);

            if ((minDate != null && minDate.isAfter(date)) || (maxDate != null && maxDate.isBefore(date))) {
                selectedDates.remove(i);
                mcv.onDateUnselected(date);
                i -= 1;
            }
        }
    }


    public void invalidateDecoratorsGAC(){
        // Log.e("gac","CalendarPagerAdapter invalidateDecoratorsGAC currentViews size:"+currentViews.size());
        for (CalendarPagerView pagerView : currentViews) {
            pagerView.setDayViewDecoratorsGAC(decorator);
        }
        // Log.e("gac", "CalendarPagerAdapter:invalidateDecoratorsGAC");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }



    public int getFirstDayOfWeek() {
        return firstDayOfTheWeek;
    }
    protected CalendarPagerView createView(int position) {
        //Log.e("gac","CalendarPagerView fristDayOfWeek:"+getFirstDayOfWeek());
        //Log.e("gac","getItemPosition:"+getItem(position).toString());
        return new CalendarPagerView(mcv, getItem(position), Calendar.MONDAY);
    }

    public CalendarDay getItem(int position) {
        return rangeIndex.getItem(position);
    }
    public Monthly getRangeIndex() {
        return rangeIndex;
    }

    protected int indexOf(CalendarPagerView view) {
        CalendarDay month = view.getMonth();
        return getRangeIndex().indexOf(month);
    }

    public void clearSelections() {
        selectedDates.clear();
        invalidateSelectedDates();
    }

    public void setDateSelected(CalendarDay day, boolean selected) {
        if (selected) {
            if (!selectedDates.contains(day)) {
                selectedDates.add(day);
                invalidateSelectedDates();
            }
        } else {
            if (selectedDates.contains(day)) {
                selectedDates.remove(day);
                invalidateSelectedDates();
            }
        }
    }

    protected boolean isInstanceOfView(Object object) {
        return object instanceof CalendarPagerView;
    }


    protected Monthly createRangeIndex(CalendarDay min, CalendarDay max) {
        return new Monthly(min, max);
    }

    public int getIndexForDay(CalendarDay day) {
        if (day == null) {
            return getCount() / 2;
        }
        if (minDate != null && day.isBefore(minDate)) {
            return 0;
        }
        if (maxDate != null && day.isAfter(maxDate)) {
            return getCount() - 1;
        }
        return rangeIndex.indexOf(day);
    }
    @Override
    public int getCount() {
       // Log.e("gac","CalendarPagerAdapter count:"+rangeIndex.getCount());
        return   rangeIndex.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }







    public void setDecoratorsGAC(EventDecorator decorators){
        this.decorator = decorators;
        invalidateDecoratorsGAC();
       // Log.e("gac", "CalendarPagerAdapter:setDecoratorsGAC");
    }



    @Override
    public int getItemPosition(Object object) {
        if (!(isInstanceOfView(object))) {
            return POSITION_NONE;
        }
        CalendarPagerView monthView = (CalendarPagerView) object;
        CalendarDay month = monthView.getMonth();
        if (month == null) {
            return POSITION_NONE;
        }
        int index = indexOf((V) object);
        if (index < 0) {
            return POSITION_NONE;
        }
        return index;
    }
  private Integer weekDayTextAppearance;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       // Log.e("gac","instaniateItem:position"+position);
        CalendarPagerView pagerView = createView(position);
        pagerView.setAlpha(0);
        pagerView.setSelectionEnabled(selectionEnabled);

        //pagerView.setWeekDayFormatter(weekDayFormatter);
        //pagerView.setDayFormatter(dayFormatter);
        if (color != null) {
            pagerView.setSelectionColor(color);
        }
       if (dateTextAppearance != null) {
           pagerView.setDateTextAppearance(dateTextAppearance);
       }
       if (weekDayTextAppearance != null) {
            pagerView.setWeekDayTextAppearance(weekDayTextAppearance);
        }
//      pagerView.setShowOtherDates(showOtherDates);
        pagerView.setMinimumDate(minDate);
        pagerView.setMaximumDate(maxDate);
        pagerView.setSelectedDates(selectedDates);

        container.addView(pagerView);
        currentViews.add(pagerView);
       // Log.e("gac", "insitate");
        // pagerView.setDayViewDecorators(decoratorResults);
       invalidateDecoratorsGAC();
        return pagerView;
    }

    public void setFirstDayOfWeek(int day) {
        firstDayOfTheWeek = day;
        for (CalendarPagerView pagerView : currentViews) {
            pagerView.setFirstDayOfWeek(firstDayOfTheWeek);
        }
    }

    public void setSelectionEnabled(boolean enabled) {
        selectionEnabled = enabled;
        for (CalendarPagerView pagerView : currentViews) {
            pagerView.setSelectionEnabled(selectionEnabled);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        CalendarPagerView pagerView = (CalendarPagerView) object;
        currentViews.remove(pagerView);
        container.removeView(pagerView);
    }



    public void setSelectionColor(int color) {
        this.color = color;
        for (CalendarPagerView pagerView : currentViews) {
            pagerView.setSelectionColor(color);
        }
    }


    /****************************************************/
    public static class Monthly {

        private final CalendarDay min;
        private final int count;

        private SparseArrayCompat<CalendarDay> dayCache = new SparseArrayCompat<>();

        public Monthly(@NonNull CalendarDay min, @NonNull CalendarDay max) {
            this.min = CalendarDay.from(min.getYear(), min.getMonth(), 1);
            max = CalendarDay.from(max.getYear(), max.getMonth(), 1);
            this.count = indexOf(max) + 1;
        }

        public int getCount() {
            return count;
        }

        public int indexOf(CalendarDay day) {
            int yDiff = day.getYear() - min.getYear();
            int mDiff = day.getMonth() - min.getMonth();

            return (yDiff * 12) + mDiff;
        }

        public CalendarDay getItem(int position) {

            CalendarDay re = dayCache.get(position);
            if (re != null) {
                return re;
            }
          // Log.e("gac", "position:" + position);
            int numY = position / 12;
            //Log.e("gac","numY:"+numY);
            int numM = position % 12;
            //Log.e("gac","numM"+numM);
            int year = min.getYear() + numY;
            //Log.e("gac","year"+year);
            int month = min.getMonth() + numM;
            //Log.e("gac","month:"+month);
            if (month >= 12) {
                year += 1;
                month -= 12;
            }

            re = CalendarDay.from(year, month, 1);
            dayCache.put(position, re);
            return re;
        }
    }
}
