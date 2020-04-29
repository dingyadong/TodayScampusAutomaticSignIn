package com.ycy.accessibilityservicetest;

/*
作者：丁亚东
编写日期：2020/4/17
考研加油鸭！
*/

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.ycy.accessibilityservicetest.Service.BackgroundAccess;
import com.ycy.accessibilityservicetest.Util.AlarManagerUtil;
import com.ycy.accessibilityservicetest.Util.IsServiceRunningUtil;
import com.ycy.accessibilityservicetest.Util.JumpPermissionManagement;
import com.ycy.accessibilityservicetest.Util.OpenAppUtil;

import java.util.Stack;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener {


    private TimePicker timePicker;
    private Switch cpdailySwitch;
    private Switch canBackground;
    private static Activity activity;
    private Switch accessbilitySwitch;
    private Button openSchool;
    private Button shutDownservice;
    private int hour;
    private int minute;
    private boolean b_accessblity;
    private Button btn_openS;
    private EditText text_openS;
    private TextView text_shellResult;
    private static Stack<Activity> activityStack;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //锁屏唤醒
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
        //加载控件UI和业务逻辑
        initWidget();
        isOpenService();
        isCanBackground();
    }

    //获得当前栈顶Activity

    public Activity currentActivity(){

        Activity activity=activityStack.lastElement();

        return activity;

    }

    public static void popActivity(Activity activity) {

        if (activity != null) {

            activity.finish();

            activityStack.remove(activity);

            activity = null;

        }
    }

    @Override
    public void onClick(View view) { //单击事件监听
        switch (view.getId()){
            case R.id.btn_openShell:
                new Thread(){
                    @Override
                    public void run() {
                        CommandResult result = Shell.SH.run(text_openS.getText().toString());
                        if (result.isSuccessful()) {
                            text_shellResult.setText(result.getStdout());
                        }else{
                            text_shellResult.setText(result.getStderr());
                        }
                    }
                }.start();


                break;
            case  R.id.btn_openSchool:
                    if (b_accessblity) {
                                try {
                                    OpenAppUtil.openAppByPackageName(MainActivity.this, MainActivity.this, "com.wisedu.cpdaily");
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                    }else{
                            Toasty.error(getApplicationContext(), "未打开无障碍服务", Toast.LENGTH_SHORT, true).show();
                        }
                break;
            case R.id.btn_shutDownService:
                try {
                    MyApplication.getInstance().setFlag(false);
                    Toasty.success(getApplicationContext(), "关闭脚本成功", Toast.LENGTH_SHORT, true).show();
                }catch (Exception e){
                    Toasty.error(getApplicationContext(), "关闭脚本失败", Toast.LENGTH_SHORT, true).show();
                }
                break;

        }
    }

//监听TimePicker时间变化
    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        hour = i;    //获取小时
        minute = i1; //获取分钟
    }

    //switch开关变化监听事件
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.sw_Accessbility:
                if (compoundButton.isPressed()) { //判断是否是人为单击
                    if (b_accessblity) {
                        openSetting();
                    } else {
                        openSetting();
                    }
                }
                break;
            case R.id.sw_cpdaily_forest:
                if (b){
                    if (b_accessblity) {
                        Toasty.success(getApplicationContext(), "今日校园于" + hour + ":" + minute + "每日自动签到", Toast.LENGTH_SHORT, true).show();
                        MyApplication.getInstance().setHour(hour);
                        MyApplication.getInstance().setMinute(minute);
                        AlarManagerUtil.timedTack(MainActivity.this,MyApplication.getInstance().getHour(),MyApplication.getInstance().getMinute());
                    }else {

                        Toasty.error(getApplicationContext(), "未打开无障碍服务", Toast.LENGTH_SHORT, true).show();
                        cpdailySwitch.setChecked(false);
                    }
                }else {
                    AlarManagerUtil.cancelTimetacker();

                }
                break;
            case R.id.sw_canBackGround:
                if (compoundButton.isPressed()) { //判断是否是人为单击
                    if (b) {
                        JumpPermissionManagement.GoToSetting(MainActivity.this);
                    } else {
                        JumpPermissionManagement.GoToSetting(MainActivity.this);
                    }
                }

        }

    }

    //手动开启辅助服务
    public void openSetting(){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    //重写onResume方法，优化卡顿
    @Override
    protected void onRestart() {
        isOpenService();
        isCanBackground();
        super.onRestart();

    }

    //判断后台弹出应用权限是否打开
    private void isCanBackground(){
        if (BackgroundAccess.canBackgroundStart(MainActivity.this)){
            canBackground.setChecked(true);
        }else {
            canBackground.setChecked(false);
            Toasty.error(getApplicationContext(), "请开启【后台弹出界面】权限", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void isOpenTimer(){

    }

    private void isOpenService(){
        //判断无障碍服务是否启动
        if (IsServiceRunningUtil.isAccessibilitySettingsOn(this,"com.ycy.accessibilityservicetest.Service.MyAccessibilityService") == false){
            accessbilitySwitch.setChecked(false);
            b_accessblity = false;     Toasty.error(getApplicationContext(), "未开启无障碍服务", Toast.LENGTH_SHORT, true).show();

        }else {
            accessbilitySwitch.setChecked(true);
            b_accessblity = true;
        }
    }


    private void initWidget(){
        text_openS = findViewById(R.id.text_openShell);
        btn_openS = findViewById(R.id.btn_openShell);
        text_shellResult = findViewById(R.id.shellResult);
        btn_openS.setOnClickListener(this);

        //加载时间选择器控件功能
        timePicker = (TimePicker) findViewById(R.id.timepick);
        openSchool = (Button)findViewById(R.id.btn_openSchool);
        shutDownservice = (Button)findViewById(R.id.btn_shutDownService);
        cpdailySwitch = (Switch) findViewById(R.id.sw_cpdaily_forest);
        accessbilitySwitch = (Switch)findViewById(R.id.sw_Accessbility);
        canBackground = findViewById(R.id.sw_canBackGround);
        //显示为24小时
        timePicker.setIs24HourView(true);
        //设置点击事件不弹键盘
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        //监听timePicker变化
        timePicker.setOnTimeChangedListener(this);
        //获取时间
        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        //监听switch开关
        cpdailySwitch.setOnCheckedChangeListener(this);
        accessbilitySwitch.setOnCheckedChangeListener(this);
        canBackground.setOnCheckedChangeListener(this);
        //自动签到按钮监听
        openSchool.setOnClickListener(this);
        //关闭服务
        shutDownservice.setOnClickListener(this);
    }

    public Activity  getMainActivity(){
         activity = MainActivity.this;
         return activity;
    }


    private void IsPopUpInterfaceAccess(){

    }

}
