package com.example.gacmy.suixinji.myview.calendarview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Utilities for Calendar
 */
public class CalendarUtils {

    /**
     * @param date {@linkplain Date} to pull date information from
     * @return a new Calendar instance with the date set to the provided date. Time set to zero.
     */
    public static Calendar getInstance(@Nullable Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        copyDateTo(calendar, calendar);
        return calendar;
    }

    public static boolean equal(Calendar day1,Calendar day2){
        if((day1.get(YEAR) == day2.get(YEAR)) && (day1.get(MONTH)==day2.get(MONTH)) &&(day1.get(Calendar.DAY_OF_MONTH)==day2.get(Calendar.DAY_OF_MONTH))){
            return true;
        }

        return false;
    }
    //比较两个日期精确到天 比较两个日期 daystart > dayend 返回1  = 返回0 <返回-1
    public static int compareDay(CalendarDay daystart,CalendarDay dayend){
        int flag = compare(daystart,dayend);
        if(flag == 1){
            return 1;
        }else if(flag == 0){
            if (daystart.getDay() > dayend.getDay()){
                return 1;
            }else if(daystart.getDay() == dayend.getDay()){
                return  0;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }
    //比较两个日期 daystart > dayend 返回1  = 返回0 <返回-1 精确到月份
    public static int compare(CalendarDay daystart,CalendarDay dayend){
        int flag = -2;
        if(daystart.getYear() > dayend.getYear()){
            flag=  1;
        }else if(daystart.getYear() == dayend.getYear()){
            if(daystart.getMonth() > dayend.getMonth()){
                flag =  1;
            }else if(daystart.getMonth() == dayend.getMonth()){
                flag = 0;
            }else{
                flag =  -1;
            }

        }else{
            flag =  -1;
        }
        return flag;
    }
    public static Calendar getNextDay(Calendar day){
         day.add(Calendar.DAY_OF_MONTH,1);
        return day;
    }

    /**
     * @return a new Calendar instance with the date set to today. Time set to zero.
     */
    @NonNull
    public static Calendar getInstance() {
        Calendar calendar = Calendar.getInstance();
        copyDateTo(calendar, calendar);
        return calendar;
    }

    /**
     * Set the provided calendar to the first day of the month. Also clears all time information.
     *
     * @param calendar {@linkplain Calendar} to modify to be at the first fay of the month
     */
    public static void setToFirstDay(Calendar calendar) {
        int year = getYear(calendar);
        int month = getMonth(calendar);
        calendar.clear();
        calendar.set(year, month, 1);
    }

    /**
     * Copy <i>only</i> date information to a new calendar.
     *
     * @param from calendar to copy from
     * @param to   calendar to copy to
     */
    public static void copyDateTo(Calendar from, Calendar to) {
        int year = getYear(from);
        int month = getMonth(from);
        int day = getDay(from);
        to.clear();
        to.set(year, month, day);
    }

    public static int getYear(Calendar calendar) {
        return calendar.get(YEAR);
    }

    public static int getMonth(Calendar calendar) {
        return calendar.get(MONTH);
    }

    public static int getDay(Calendar calendar) {
        return calendar.get(DATE);
    }

    public static int getDayOfWeek(Calendar calendar) {
        return calendar.get(DAY_OF_WEEK);
    }
}
