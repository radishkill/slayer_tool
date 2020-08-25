package com.slayer.slayertool.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.amap.api.location.AMapLocation;
import com.slayer.slayertool.R;
import com.slayer.slayertool.service.MainService;

public class PhoneStatusActivity extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

  SwitchCompat mainServiceSwitch;
  SwitchCompat mainServiceBinderSwitch;
  //开关控制是否监测屏幕开启状态和电池电量变化
  SwitchCompat phoneScreenSwitch;
  SwitchCompat phoneBatterySwitch;


  MainService mainService;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_phone_status);
    initView();

    if (MainService.serviceIntent == null) {
      mainServiceSwitch.setChecked(false);
    } else {
      mainServiceSwitch.setChecked(true);
    }
  }

  public void initView() {
    mainServiceSwitch = findViewById(R.id.switch_main_service);
    mainServiceBinderSwitch = findViewById(R.id.switch_main_service_binder);
    phoneScreenSwitch = findViewById(R.id.switch_phone_screen_monitor);
    phoneBatterySwitch = findViewById(R.id.switch_phone_battery_monitor);

    mainServiceSwitch.setOnCheckedChangeListener(this);
    mainServiceBinderSwitch.setOnClickListener(this);
    phoneScreenSwitch.setOnCheckedChangeListener(this);
    phoneBatterySwitch.setOnCheckedChangeListener(this);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    //规避错误判断
    if (!buttonView.isPressed())
      return;

    if (buttonView.getId() == mainServiceSwitch.getId()) {
      if (isChecked) {
        MainService.startService(this);
      } else {
        MainService.stopService(this);
      }
    } else if (buttonView.getId() == phoneScreenSwitch.getId()) {
      if (isChecked) {
        mainService.startGetPhoneInfoOfScreen();
      } else {
        mainService.stopGetPhoneInfoOfScreen();
      }
    } else if (buttonView.getId() == phoneBatterySwitch.getId()) {
      if (isChecked) {
        mainService.startGetPhoneInfoOfBattery();
      } else {
        mainService.stopGetPhoneInfoOfBattery();
      }
    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == mainServiceBinderSwitch.getId()) {
      //这个时候是反着的
      if (!mainServiceBinderSwitch.isChecked() && null != conn) {
        unbindService(conn);
      } else {
        mainServiceBinderSwitch.setChecked(false);
        Intent intent = new Intent(this, MainService.class);
        conn = new MyConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
      }
    }
  }

  @Override
  protected void onDestroy() {
    if (mainServiceBinderSwitch.isChecked())
      unbindService(conn);
    super.onDestroy();
  }

  private MainService.LocalBinder myBinder = null;
  private MyConn conn = null;
  //监视服务的状态
  private class MyConn implements ServiceConnection {

    //当服务连接成功调用
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      //获取中间人对象
      myBinder = (MainService.LocalBinder) service;
      mainService = myBinder.getService();
      mainServiceBinderSwitch.setChecked(true);
      if (mainService.getStatusSwitchOfScreen()) {
        phoneScreenSwitch.setChecked(true);
      }
      if (mainService.getStatusSwitchOfBattery()) {
        phoneBatterySwitch.setChecked(true);
      }
    }
    //失去连接
    @Override
    public void onServiceDisconnected(ComponentName name) {
      mainServiceBinderSwitch.setChecked(false);
      myBinder = null;
    }}
}
