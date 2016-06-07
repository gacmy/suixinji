package com.example.gacmy.suixinji.myview.calendarview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	private static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	

	public static String getTodayDate(){
		return DATE.format(new Date());
	}
	

	public static String getDateStr(String datePattern,Date date){
		return new SimpleDateFormat(datePattern).format(date);
	}
	

	public static Date parse(String strDate, String pattern)
            throws ParseException {
        return StrUtils.isBlank(strDate) ? null : new SimpleDateFormat(
                pattern).parse(strDate);  
    }  
	

	public static Date parse(String strDate)
            throws ParseException {
        return StrUtils.isBlank(strDate) ? null : DATE.parse(strDate);  
    }  

	public static String getDateStr(Date date){
		return DATE.format(date);
	}
	
	

    public static Date addMonth(Date date, int n)
    {  
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);  
        cal.add(Calendar.MONTH, n);
        return cal.getTime();  
    }  
    
    
    public static String getLastDayOfMonth(String year, String month)
    {  
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, Integer.parseInt(year));

        cal.set(Calendar.DATE, 1);

        cal.add(Calendar.MONTH, 1);

        cal.add(Calendar.DATE, -1);
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// �����ĩ�Ǽ���
    }  
  
    public static Date getDate(String year, String month, String day)
            throws ParseException {
        String result = year + "- "
                + (month.length() == 1 ? ("0 " + month) : month) + "- "  
                + (day.length() == 1 ? ("0 " + day) : day);  
        return parse(result);  
    }  
    
    public static Date getDate(int year,int month,int day)
    		throws ParseException {
    	String tempYear = year+"";
    	String tempMonth = month+"";
    	String tempDay = day+"";
    	String result = tempYear + "- "
                 + (tempMonth.length() == 1 ? ("0 " + month) : month) + "- "  
                 + (tempDay.length() == 1 ? ("0 " + day) : day);  
         return parse(result);
    }
	private static class StrUtils{

		static boolean isBlank(String str){
			if(str == null || str.equals("")){
				return true;
			}
			return false;
		}
	}
}
