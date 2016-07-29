package cn.com.bluemoon.lib.utils;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.com.bluemoon.lib.entity.HourMin;
import cn.com.bluemoon.lib.entity.YearMonDay;
import cn.com.bluemoon.lib.view.CommonDatePickerDialog;
import cn.com.bluemoon.lib.view.CommonTimePickerDialog;

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


	//show datePicker and timePicker, added by liangjiangli on 2016.7.29
	private static List<YearMonDay> ymds;
	private static List<HourMin> hms;
	private static List<TextView> txtDates;
	private static List<TextView> txtTimes;
	private static CommonDatePickerDialog datePicker;
	private static CommonTimePickerDialog timePicker;
	private static int selectIndex;
	private static String format = null;

	public static void setDateTime(long date, int index) {
		String mYear = getTime(date, "yyyy");
		String mMon = String.valueOf(Integer.valueOf(getTime(date, "MM"))-1);
		String mMonth = getTime(date, "MM");
		String mDay = getTime(date, "dd");
		if (isExistYMD(index)) {
			YearMonDay ymd = ymds.get(index);
			ymd.setmYear(mYear);
			ymd.setmMonth(mMonth);
			ymd.setmMon(mMon);
			ymd.setmDay(mDay);
			ymds.set(index,ymd);
		}
	}

	public static void setHourTime(long time, int index) {
		String mHour = getTime(time, "HH");
		String mMinute = getTime(time, "mm");

		if (isExistHM(index)) {
			HourMin hm = hms.get(index);
			hm.setmHour(mHour);
			hm.setmMinute(mMinute);
			hms.set(index,hm);
		}
	}

	public static void addDatePicker(TextView... tvs) {
		ymds = new ArrayList<>();
		txtDates = new ArrayList<>();
		Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
		for (int i = 0; i < tvs.length; i++) {
			String mYear = String.valueOf(dateAndTime.get(Calendar.YEAR));
			String mMon = String.valueOf(dateAndTime.get(Calendar.MONTH));
			String mMonth = String.valueOf(dateAndTime.get(Calendar.MONTH) + 1);
			String mDay = String.valueOf(dateAndTime.get(Calendar.DAY_OF_MONTH));
			YearMonDay ymd = new YearMonDay();
			ymd.setmYear(mYear);
			ymd.setmMon(mMon);
			ymd.setmMonth(mMonth);
			ymd.setmDay(mDay);
			ymds.add(ymd);
			txtDates.add(tvs[i]);
		}
	}

	public static void addTimePicker(TextView... tvs) {
		hms = new ArrayList<>();
		txtTimes = new ArrayList<>();
		Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
		for (int i = 0; i < tvs.length; i++) {
			String mHour = String.valueOf(dateAndTime.get(Calendar.HOUR_OF_DAY));
			String mMinute = String.valueOf(dateAndTime.get(Calendar.MINUTE));
			HourMin hm = new HourMin(mHour, mMinute);
			hms.add(hm);
			txtTimes.add(tvs[i]);
		}
	}


	private static boolean isExistHM(int index) {
		if (hms != null && hms.size() > index) {
			return true;
		}
		return false;
	}

	private static boolean isExistYMD(int index) {
		if (ymds != null && ymds.size() > index) {
			return true;
		}
		return false;
	}

	public static void showDatePickerDialog(Context context, int index, String formatStr) {
		selectIndex = index;
		format = formatStr;
		String mYear = ymds.get(index).getmYear();
		String mMon = ymds.get(index).getmMon();
		String mDay = ymds.get(index).getmDay();
		String mMonth = ymds.get(index).getmMonth();

		if (datePicker == null) {
			datePicker = new CommonDatePickerDialog(context, mDateSetListener,
					Integer.valueOf(mYear), Integer.valueOf(mMon),
					Integer.valueOf(mDay));
			datePicker.show();
		} else {
			if (!datePicker.isShowing()) {
				datePicker.updateDate(Integer.valueOf(mYear),
						Integer.valueOf(mMonth) - 1, Integer.valueOf(mDay));
				datePicker.show();
			}
		}
	}

	private static CommonDatePickerDialog.OnDateSetListener mDateSetListener = new CommonDatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			YearMonDay ymd = ymds.get(selectIndex);
			ymd.setmYear(String.valueOf(year));
			ymd.setmMon(String.valueOf(monthOfYear));
			String mMonth;
			if (monthOfYear <= 8) {
				mMonth = "0" + (monthOfYear + 1);
			} else {
				mMonth = String.valueOf(monthOfYear + 1);
			}
			ymd.setmMonth(mMonth);
			String mDay;
			if (dayOfMonth <= 9) {
				mDay = String.valueOf("0" + dayOfMonth);
			} else {
				mDay = String.valueOf(dayOfMonth);
			}
			ymd.setmDay(mDay);
			String date = getTime(view.getCalendarView().getDate(), StringUtils.isNotBlank(format) ? format : "yyyy-MM-dd");
			txtDates.get(selectIndex).setText(date);
		}
	};

	public static void showTimePickerDialog(Context context, int index) {
		selectIndex = index;
		String hour = hms.get(index).getmHour();
		String minute = hms.get(index).getmMinute();

		if (timePicker == null) {
			timePicker = new CommonTimePickerDialog(context, mTimeSetListener,
					Integer.valueOf(hour), Integer.valueOf(minute), true);
		} else {
			if (!timePicker.isShowing()) {
				timePicker.updateTime(Integer.valueOf(hour),
						Integer.valueOf(minute));
			}
		}
		timePicker.show();
	}

	private static CommonTimePickerDialog.OnTimeSetListener mTimeSetListener = new CommonTimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String hour = String.valueOf(hourOfDay);
			if (hourOfDay <= 9) {
				hour = "0" + hour;
			}

			HourMin hm = hms.get(selectIndex);
			hm.setmHour(hour);

			String m = String.valueOf(minute);
			if (minute <= 9) {
				m = "0" + minute;
			}
			hm.setmMinute(m);
			txtTimes.get(selectIndex).setText(hour+":"+m);
		}
	};

	public static void onDestroy() {
		datePicker = null;
		timePicker = null;
	}

}
