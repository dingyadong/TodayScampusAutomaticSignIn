package com.ycy.accessibilityservicetest.Util;


import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
/*
无障碍服务启动检测
*/
import static android.content.ContentValues.TAG;

public  class IsServiceRunningUtil {
   public static boolean isAccessibilitySettingsOn(Context context, String accessibilityServiceName) {
        int accessibilityEnable = 0;
        String serviceName = context.getPackageName() + "/" +accessibilityServiceName;
        try {
            accessibilityEnable = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        } catch (Exception e) {
            Log.e(TAG, "get accessibility enable failed, the err:" + e.getMessage());
        }
        if (accessibilityEnable == 1) {
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(serviceName)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        }else {
            Log.d(TAG,"Accessibility service disable");
        }
        return false;
    }
}

