package com.slayer.slayertool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amap.api.location.AMapLocation;

import static com.slayer.slayertool.utils.Constant.TAG;

public class StatusReceiver extends BroadcastReceiver {
  PhoneInfoListener listener = null;
  int lastBatteryLevel = 0;
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
      if (null != listener) {
        listener.onScreenChanged(false);
      }
    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
      if (null != listener) {
        listener.onScreenChanged(true);
      }
    } else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
      int batteryLevel = 0;
      batteryLevel = intent.getIntExtra("level", 0);    ///电池剩余电量
      if (lastBatteryLevel != batteryLevel) {
        listener.onBatteryLevelChanged(batteryLevel);
        lastBatteryLevel = batteryLevel;
      }
    }
  }
  public void setPhoneInfoListener(PhoneInfoListener listener) {
    this.listener = listener;
  }

  public interface PhoneInfoListener {
    void onScreenChanged(boolean status);
    void onBatteryLevelChanged(int batteryLevel);
  }
}
