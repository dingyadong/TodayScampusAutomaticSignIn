package com.ycy.accessibilityservicetest.Broad;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.ycy.accessibilityservicetest.MainActivity;
import com.ycy.accessibilityservicetest.MyApplication;
import com.ycy.accessibilityservicetest.Util.AlarManagerUtil;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;
import static android.content.Context.KEYGUARD_SERVICE;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("timer");
        int hour = bundle.getInt("hour");
        int minute = bundle.getInt("minute");
        Log.v("dingding","I am AlarmReceiver,I receive the message");
        Toasty.success(context,"回到主页", Toast.LENGTH_SHORT,true).show();
        wakeUpAndUnlock(context);
        Intent i1 = new Intent(context, MainActivity.class);
        // 在Service中启动Activity，必须设置如下标志
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i1);

        String packageName = "com.wisedu.cpdaily";
        String className = "com.wisorg.wisedu.login.WelcomeActivity";
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        //重点是加这个
        ComponentName cn = new ComponentName(packageName, className);
        Log.i(TAG, "openAppByPackageName: "+packageName+","+className);
        i.setComponent(cn);
        context.startActivity(i);
        MyApplication.getInstance().setFlag(true);

        try {
            Activity activity = (Activity) context;
            AlarManagerUtil.timedTack(activity,hour,minute);
            Log.e(TAG, "定时成功！");
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "定时失败！");
        }

    }


/*     // 唤醒手机屏幕并解锁
    public static void wakeUpAndUnlock(Context context) {

        PowerManager pm = (PowerManager)
                context.getSystemService(Context.POWER_SERVICE);
        if (pm == null)
            return;PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK        |     PowerManager.ACQUIRE_CAUSES_WAKEUP, "messagelisenter:bright");
        wl.acquire(10000);
        wl.release();

    }*/

   public static void wakeUpAndUnlock(Context context){

        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright:");
        //点亮屏幕
        wl.acquire(10000);
        //释放
        wl.release();
       //屏锁管理器
       KeyguardManager km= (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
       KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
       //解锁
       kl.disableKeyguard();
    }




}
