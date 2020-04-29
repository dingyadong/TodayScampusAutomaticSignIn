package com.ycy.accessibilityservicetest.Util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.KEYGUARD_SERVICE;

public class OpenAppUtil {
    public static void openAppByPackageName (Activity app, Context context, String packageName)
            throws PackageManager.NameNotFoundException {

        PackageInfo pi;
        try {
            pi = app.getPackageManager().getPackageInfo(packageName, 0);//获取程序包信息
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = app.getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                //重点是加这个
                ComponentName cn = new ComponentName(packageName, className);
                Log.i(TAG, "openAppByPackageName: "+packageName+","+className);
                intent.setComponent(cn);
                context.startActivity(intent);

            }
        } catch (
                PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
