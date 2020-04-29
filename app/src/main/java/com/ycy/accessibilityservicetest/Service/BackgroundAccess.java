package com.ycy.accessibilityservicetest.Service;

import android.app.AppOpsManager;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

public class BackgroundAccess {
    public static boolean canBackgroundStart(Context context) {
        AppOpsManager ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021; // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]
                    {int.class, int.class, String.class}
            );
            //Binder.getCallingUid()
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            Log.e("mes", "not support", e);
        }
        return false;
    }
}
