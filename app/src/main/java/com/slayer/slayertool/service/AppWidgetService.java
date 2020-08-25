package com.slayer.slayertool.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AppWidgetService extends Service {
  public static final String TAG = "test";
  public AppWidgetService() {
    super();
  }

  //初始化
  @Override
  public void onCreate() {
    super.onCreate();
  }
  //重复触发
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
