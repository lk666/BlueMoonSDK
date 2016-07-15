package cn.com.bluemoon.lib.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LibDateUtil {

	public static String getTime(long t, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		if(String.valueOf(t).length()==10){
			t=t*1000;
		}
		Date curDate = new Date(t);
		String time = formatter.format(curDate);
		return time;
	}

	public static String getTimeByTimestamp(Timestamp t, String format) {
		return getTime(t.getTime(), format);
	}


	public static String getTimeByTimestamp(Timestamp t) {
		return getTime(t.getTime(), "yyyy-MM-dd");
	}


	public static String getTimeStringByCustTime(long t, String format) {
		try {
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(String
					.valueOf(t));
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} catch (ParseException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	public static long getTimeByCustTime(long t) {
		try {
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(String
					.valueOf(t));
			return date.getTime();
		} catch (ParseException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0l;
		}

	}

	public static long getTimeToLong(long t) {
		return Long.parseLong(getTime(t, "yyyyMMddHHmmss"));
	}

	public Date setDelayDate(int year, int moth, int day) {
		Calendar ca = Calendar.getInstance(Locale.CHINA);
		ca.add(Calendar.YEAR, year);
		ca.add(Calendar.MONTH, moth);
		ca.add(Calendar.DAY_OF_MONTH, day);
		return ca.getTime();
	}

	public static long getDayTimetoLong(long timestamp){
		try {
			return Long.parseLong(getTime(timestamp, "yyyyMMdd"));
		} catch (Exception e) {
			return 0;
		}
	}

	public static boolean isSameDay(long oldtime,long newtime){
		return getDayTimetoLong(oldtime)==getDayTimetoLong(newtime);
	}

	public static boolean isToday(long timestamp){
		return isSameDay(timestamp, System.currentTimeMillis());
	}

	public static long getTimeOffsetMonth(long time,int month){
		Calendar c = Calendar.getInstance();
		if(time!=0) c.setTimeInMillis(time);
		c.add(Calendar.MONTH, month);
		return c.getTimeInMillis();
	}

	public static long getTimeOffsetDay(long time,int day){
		Calendar c = Calendar.getInstance();
		if(time!=0) c.setTimeInMillis(time);
		c.add(Calendar.DAY_OF_YEAR, day);
		return c.getTimeInMillis();
	}

	public static long getTimeOffsetMonth(int month){
		return getTimeOffsetMonth(0,month);
	}

	public static long getTimeOffsetDay(int day){
		return getTimeOffsetDay(0,day);
	}

}
