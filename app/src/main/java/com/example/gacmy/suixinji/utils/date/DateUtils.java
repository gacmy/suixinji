package com.example.gacmy.suixinji.utils.date;;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class DateUtils {
	private static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	//获取今天的日期字符串
	public static String getTodayDate(){
		return DATE.format(new Date());
	}

	//获取指定日期格式的日期
	public static String getDateStr(String datePattern,Date date){
		return new SimpleDateFormat(datePattern).format(date);
	}

	//将字符串解析成日期类型
	public static Date parse(String strDate, String pattern)
			throws ParseException  {
		return StrUtils.isBlank(strDate) ? null : new SimpleDateFormat(
				pattern).parse(strDate);
	}

	//使用默认格式解析日期字符串为日期类型
	public static Date parse(String strDate)
			throws ParseException  {
		return StrUtils.isBlank(strDate) ? null : DATE.parse(strDate);
	}

	//获取日期字符串
	public static String getDateStr(Date date){
		return DATE.format(date);
	}


	/**
	 * 在日期上增加数个整月
	 */
	public static Date addMonth(Date date, int n)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	//res = 1 date1 > date2 |  res = -1 date1 < date2 | res = 0 date1 = date1|res = -2 异常
	public static  int  compareDataTime(String date1,String date2){
		int res = -2;
		try {
			long s1 = DATE.parse(date1).getTime();//毫秒
			long s2 = DATE.parse(date2).getTime();
			if(s1 -s2 > 0){
				res = 1;
			}else if(s1 -s2 < 0){
				res = -1;
			}else {
				res = 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
	public static String getLastDayOfMonth(String year, String month)
	{
		Calendar cal = Calendar.getInstance();
		// 年
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		// 月，因为Calendar里的月是从0开始，所以要-1
		// cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		// 日，设为一号
		cal.set(Calendar.DATE, 1);
		// 月份加一，得到下个月的一号
		cal.add(Calendar.MONTH, 1);
		// 下一个月减一为本月最后一天
		cal.add(Calendar.DATE, -1);
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获得月末是几号
	}

	public static Date getDate(String year, String month, String day)
			throws ParseException{
		String result = year + "- "
				+ (month.length() == 1 ? ("0 " + month) : month) + "- "
				+ (day.length() == 1 ? ("0 " + day) : day);
		return parse(result);
	}

	public static Date getDate(int year,int month,int day)
			throws ParseException{
		String tempYear = year+"";
		String tempMonth = month+"";
		String tempDay = day+"";
		String result = tempYear + "- "
				+ (tempMonth.length() == 1 ? ("0 " + month) : month) + "- "
				+ (tempDay.length() == 1 ? ("0 " + day) : day);
		return parse(result);
	}


	public static void main(String[] args){
		print(getTodayDate());
		Date date = null;
		try {
			date = parse("2013-01-12");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		print(getDateStr(date));

	}

	public static void print(String str){
		System.out.println(str);
	}

	//字符串工具类
	private static class StrUtils{

		static boolean isBlank(String str){
			if(str == null || str.equals("")){
				return true;
			}
			return false;
		}
	}
}
