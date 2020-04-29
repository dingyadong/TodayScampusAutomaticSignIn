package com.ycy.accessibilityservicetest.Service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.KeyguardManager;
import android.app.VoiceInteractor;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Path;
import android.view.accessibility.AccessibilityWindowInfo;

import com.ycy.accessibilityservicetest.MainActivity;
import com.ycy.accessibilityservicetest.MyApplication;
import com.ycy.accessibilityservicetest.Util.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

/*
AccessibilityService服务
用于监听今日校园事件、自动签到行为
*/

public class MyAccessibilityService extends AccessibilityService{

    private static final String TAG = "MyAccessibilityService";
    //包名
    private String nowPackageName;
    //单击输入金额
    String className;
    String packages;
    String p_c;
    String inActivity;
    int x = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() != null) {
            nowPackageName = event.getPackageName().toString();
            //获取包名
            packages=event.getPackageName().toString();
            //获取类名
            className= event.getClassName().toString();
            if(className.contains("com."))
            {
                if(className.contains(packages))
                {
                    className=className.replace(packages,"");
                    className=className.replace("..","");
                    if(className.charAt(0)=='.')
                        className=className.substring(1);
                }
                p_c = packages+className;
                Log.i(TAG, "包名："+packages+"，"+"类名："+className);
            }
        }




        //判断当前包是否为今日校园
        if (nowPackageName!=null) {
            if (nowPackageName.equals("com.wisedu.cpdaily") && MyApplication.getInstance().getFlag()) {



                Log.i("running！", "AccessBility is running!");
                ArrayList<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
                nodes = getNodesFromWindows();

                if (nodes != null) {
                    for (AccessibilityNodeInfo node : nodes) {
                        executeOperation(node,event);
                    }
                }
            }
        }
    }
    //获取子节点窗口
    private ArrayList<AccessibilityNodeInfo> getNodesFromWindows() {
        List<AccessibilityWindowInfo> windows = getWindows();
        ArrayList<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
        if (windows.size() > 0) {
            for (AccessibilityWindowInfo window : windows) {
                nodes.add(window.getRoot());
            }
        }
        return nodes;
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    private boolean executeOperation(AccessibilityNodeInfo info,AccessibilityEvent event) {

        if (info == null) return false;
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                    if (p_c!=null) {
                        if (p_c.contains("com.wisedu.cpdaily")) {
                            if (!p_c.contains("WelcomeActivity")) {
                                if (!p_c.contains("BrowsePageActivity")) {
                                    if (!p_c.contains("HomeActivity")) {
                                        if (inActivity != p_c) {
                                            Log.i(TAG, "完整包名:" + p_c);
                                            clickBackKey();
                                            Log.i(TAG, "返回键单击！");
                                            inActivity = p_c;
                                        }

                                    }else if ("签到" !=info.getText().toString()){
                                        Log.i(TAG, "单击时间下的完整包名:" + p_c);
                                        Gesture();
                                    }
                                }
                            }
                        }
                    }

                Log.i(TAG, "马上进入签到！");
                if("跳过".equals(info.getText().toString())){
                    if ("android.widget.TextView".equals(info.getClassName())){
                        AccessibilityNodeInfo parent = info;
                        while(parent != null){
                            if(parent.isClickable()){
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                break;
                            }
                            parent = parent.getParent();
                        }
                    }
                }
                if("今选".equals(info.getText().toString())){
                    if ("android.widget.TextView".equals(info.getClassName())){
                        AccessibilityNodeInfo parent = info;
                        while(parent != null){
                            if(parent.isClickable()){
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                break;
                            }
                            parent = parent.getParent();
                        }
                    }
                }
                if("我知道了".equals(info.getText().toString())){
                    if ("android.widget.TextView".equals(info.getClassName())){
                        AccessibilityNodeInfo parent = info;
                        while(parent != null){
                            if(parent.isClickable()){
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                break;
                            }
                            parent = parent.getParent();
                        }
                    }
                }
                if ("签到".equals(info.getText().toString())) {
                    Log.i(TAG, "进入签到！");
                    if ("android.widget.TextView".equals(info.getClassName())){
                        AccessibilityNodeInfo parent = info;
                        while (parent != null) {
                            if (parent.isClickable()) {
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                break;
                            }
                            parent = parent.getParent();
                        }
                    }

                } else if("统计".equals(info.getText().toString())) {
                        Log.i(TAG, "进入统计。");
                        if ("android.view.View".equals(info.getClassName())) {
                            AccessibilityNodeInfo parent = info;
                            while (parent != null) {
                                if (parent.isClickable()) {
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                                    // 在Service中启动Activity，必须设置如下标志
                                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getApplicationContext().startActivity(i1);
                                    clickHomeKey();
                                    MyApplication.getInstance().setFlag(false);
                                    break;
                                }
                                parent = parent.getParent();
                            }
                        }
                    }

                   /*     while (parent != null) {
                             if (parent.isClickable()) {
                                boolean b = parent.performAction(AccessibilityNodeInfo
                                        .ACTION_CLICK);
                                if (b) {
                                    isClickSetMoney = true;
                                }
                                break;
                            }
                            parent = parent.getParent();
                        }*/

  /*              else if ("金额".equals(info.getText().toString())) {
                    if ("android.widget.TextView".equals(info.getClassName())) {
                        AccessibilityNodeInfo parent = info.getParent();
                        AccessibilityNodeInfo child = parent.getChild(1);
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService
                                (CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("scb", MyApplication.getInstance()
                                .getParams());
                        clipboardManager.setPrimaryClip(clipData);
                        boolean b = child.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                        isClickSetMoney = false;
                    }
                }*/

            }
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {

                if (info.getChild(i) != null) {
                    Log.i(TAG, "info.getChild(i) "+info.getChild(i).toString());
                    Log.i(TAG, "info.getChildCount() "+info.getChildCount());
                    executeOperation(info.getChild(i),event);
                }
            }
        }
        return false;
    }


    public Boolean clickBackKey() {
            return performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public Boolean clickHomeKey() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    @Override
    protected boolean onGesture(int gestureId) {
        return super.onGesture(gestureId);
    }

    private void Gesture(){

        Log.e(TAG, "屏幕高度"+ ScreenUtils.getScreenHeight(getApplicationContext()));
        Log.e(TAG, "屏幕宽度"+ScreenUtils.getScreenWidth(getApplicationContext()));
        int screenWidth = ScreenUtils.getScreenWidth(getApplicationContext());
        int screenHrigh=ScreenUtils.getScreenHeight(getApplicationContext());

        Path mPath = new Path();//线性的path代表手势路径,点代表按下,封闭的没用
        //x y坐标  下面例子是往下滑动界面
        mPath.moveTo(screenWidth/2,screenHrigh/8);//代表从哪个点开始滑动
        mPath.lineTo(screenWidth/2,screenHrigh-screenHrigh/6);//滑动到哪个点
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(mPath,20,20));
        GestureDescription gesture = builder.build();
        boolean isDispatched = dispatchGesture(gesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "onCompleted: 取消..........");
            }
        }, null);


    }





    @Override
    public void onInterrupt() {

    }
}
