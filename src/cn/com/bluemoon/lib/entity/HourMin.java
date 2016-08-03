package cn.com.bluemoon.lib.entity;

/**
 * Created by LIANGJIANGLI on 2016/7/28.
 */
public class HourMin {

    private String mHour;
    private String mMinute;

    public HourMin() {
    }

    public HourMin(String mHour, String mMinute) {
        this.mHour = mHour;
        this.mMinute = mMinute;
    }

    public String getmHour() {
        return mHour;
    }

    public void setmHour(String mHour) {
        this.mHour = mHour;
    }

    public String getmMinute() {
        return mMinute;
    }

    public void setmMinute(String mMinute) {
        this.mMinute = mMinute;
    }
}
