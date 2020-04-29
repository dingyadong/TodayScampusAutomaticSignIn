package com.ycy.accessibilityservicetest.Util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.ycy.accessibilityservicetest.MyApplication;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;


public class AlarManagerUtil{
    public static AlarmManager alarmManager;
    private static  PendingIntent pendingIntent;
    private static Activity activityA;

    public static void timedTack(Activity activity,int hour,int minute){
        activityA = activity;
        alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,00);
        Intent alarmIntent = new Intent("dingding");
        Bundle bundle=new Bundle();
        bundle.putInt("hour",MyApplication.getInstance().getHour());
        bundle.putInt("minute",MyApplication.getInstance().getMinute());
        alarmIntent.putExtra("timer",bundle);
        alarmIntent.setPackage(activity.getPackageName());
        pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        long TaskTimer = calendar.getTimeInMillis();
        long autoTime = calendar.getTimeInMillis()-System.currentTimeMillis();
        if(autoTime<0){
            TaskTimer += 1000*60*60*24;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, TaskTimer, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//  4.4
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,TaskTimer, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,TaskTimer, pendingIntent);
        }
    }
    public static void cancelTimetacker(){
        try {
            alarmManager.cancel(pendingIntent);
            Toasty.info(activityA, "今日校园自动签到已关闭", Toast.LENGTH_SHORT, true).show();
        }catch (Exception e){
            e.printStackTrace();
            Toasty.error(activityA,"取消失败！",Toasty.LENGTH_SHORT,true).show();
        }


    }


}
