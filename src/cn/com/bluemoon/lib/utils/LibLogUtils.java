package cn.com.bluemoon.lib.utils;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;


public class LibLogUtils {
	
	/**********************************
	 *	Config for Test/UAT
	 **********************************/
	// set current log level
//    private static LogLevel currentLevel = LogLevel.DEBUG;
//    private static LogLevel currentLevel = LogLevel.VERBOSE;
    
    /**********************************
	 *	Config for Production
	 **********************************/
    // Set to LogLevel.OFF for Production
    private static LogLevel currentLevel = LogLevel.OFF;	
    
    private static final String TAG = "Blue Moon House";
    
    public static boolean DEBUG = true;
    
    public static void e(String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.ERROR.level)
        {
            Log.e(TAG, msg);
        }
    }
    
    public static void e(String tag, String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.ERROR.level)
        {
            Log.e(tag, msg);
        }
    }
    
    public static void e(String tag, String msg, Throwable throwable)
    {
        if (DEBUG && currentLevel.level >= LogLevel.ERROR.level)
        {
            Log.e(tag, msg, throwable);
        }
    }
    
    public static void i(String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.INFO.level)
        {
            Log.i(TAG, msg);
        }
    }
    
    public static void i(String tag, String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.INFO.level)
        {
            Log.i(tag, msg);
        }
    }
    
    public static void i(String tag, String msg, Throwable throwable)
    {
        if (DEBUG && currentLevel.level >= LogLevel.INFO.level)
        {
            Log.i(tag, msg, throwable);
        }
    }
    
    public static void d(String tag, String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.DEBUG.level)
        {
            Log.d(tag, msg );
        }
    }
    
    public static void d(String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.DEBUG.level)
        {
            Log.d(TAG, msg);
        }
    }
    
    public static void v(String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.VERBOSE.level)
            Log.v(TAG, msg);
    }
    
    public static void v(String tag, String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.VERBOSE.level)
        {
            Log.v(tag, msg);
        }
    }
    
    public static void w(String tag, String msg)
    {
        if (DEBUG && currentLevel.level >= LogLevel.WARN.level)
        {
            Log.e(tag, msg);
        }
    }
    
    public static void w(Throwable throwable)
    {
        if (DEBUG && currentLevel.level >= LogLevel.WARN.level)
        {
            Log.w(TAG, throwable);
        }
    }
    
    public static void w(String tag, Throwable throwable)
    {
        if (DEBUG && currentLevel.level >= LogLevel.WARN.level)
        {
            Log.w(tag, throwable);
        }
    }
    
	public enum LogLevel {

		OFF, ERROR, WARN, DEBUG, INFO, VERBOSE;
		public int level;
		private static final Map<String, LogLevel> mapping;
		static {
			mapping = new HashMap<String, LogLevel>();
			OFF.level = -1;
			ERROR.level = 0;
			WARN.level = 1;
			INFO.level = 2;
			DEBUG.level = 3;
			VERBOSE.level = 4;

			mapping.put("OFF", OFF);
			mapping.put("ERROR", ERROR);
			mapping.put("WARN", WARN);
			mapping.put("DEBUG", DEBUG);
			mapping.put("INFO", INFO);
			mapping.put("VERBOSE", VERBOSE);
		}
	}

}
