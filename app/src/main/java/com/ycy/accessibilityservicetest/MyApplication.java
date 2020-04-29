package com.ycy.accessibilityservicetest;

import android.app.Application;



public class MyApplication extends Application {

    private boolean mFlag;
    private int hour;
    private int minute;
    private static MyApplication myApplication = null;
    public static MyApplication getInstance(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public boolean getFlag() {
        return mFlag;
    }

    public void setFlag(boolean mFlag) {
        this.mFlag = mFlag;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
