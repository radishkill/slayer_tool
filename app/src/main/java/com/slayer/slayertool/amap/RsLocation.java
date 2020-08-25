package com.slayer.slayertool.amap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.slayer.slayertool.utils.Constant;


import java.util.Objects;

public class RsLocation {
  //声明AMapLocationClient类对象
  public AMapLocationClient mLocationClient;
  //声明AMapLocationClientOption对象
  public AMapLocationClientOption mLocationOption;

  int interval = 20;

  private Intent alarmIntent = null;
  private PendingIntent alarmPi = null;
  private AlarmManager alarm = null;

  public RsLocation(Context context) {
    //初始化定位
    mLocationClient = new AMapLocationClient(context);

    //初始化AMapLocationClientOption对象
    mLocationOption = new AMapLocationClientOption();

    // 创建Intent对象，action为LOCATION
    alarmIntent = new Intent();
    alarmIntent.setAction(Constant.AUTO_LOCATION);
    IntentFilter ift = new IntentFilter();

    // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
    // 也就是发送了action 为"LOCATION"的intent
    alarmPi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
    // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
    alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    //动态注册一个广播
    IntentFilter filter = new IntentFilter();
    filter.addAction(Constant.AUTO_LOCATION);
//    context.registerReceiver(alarmReceiver, filter);

//    if (null != alarm) {
//      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
//        alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + interval * 1000, alarmPi);
//        Log.d(Constant.TAG, "onClick: test start");
//      } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP + interval * 1000, SystemClock.elapsedRealtime(),
//            alarmPi);
//      } else {
//        Log.d(Constant.TAG, "onClick: error system version");
//      }
//    }
  }
  public void setOption(int interval, boolean isOnce) {
    /**
     * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
     */
    mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);

    //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

    if (isOnce) {
      mLocationOption.setOnceLocation(true);
      mLocationOption.setOnceLocationLatest(true);
    } else {
      //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
      mLocationOption.setInterval(interval * 1000);
      mLocationOption.setOnceLocation(false);
      mLocationOption.setOnceLocationLatest(false);
    }

    //给定位客户端对象设置定位参数
    mLocationClient.setLocationOption(mLocationOption);
  }
  public void setLocationListener(AMapLocationListener listener) {
    mLocationClient.setLocationListener(listener);
  }
  public void stop() {
    if (mLocationClient.isStarted()) {
      mLocationClient.stopLocation();
    }
  }
  public void start() {
    if (!mLocationClient.isStarted()) {
      mLocationClient.startLocation();
    }
  }

  public void onDestroy() {
    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
  }


  private BroadcastReceiver alarmReceiver = new BroadcastReceiver(){
    @Override
    public void onReceive(Context context, Intent intent) {
      if(Objects.equals(intent.getAction(), Constant.AUTO_LOCATION)){
        if(null != mLocationClient){
          mLocationClient.startLocation();
        }
        if (null != alarm) {
          if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
            alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval * 1000, alarmPi);
            Log.d(Constant.TAG, "onClick: test start");
          } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP + interval * 1000, SystemClock.elapsedRealtime(),
                alarmPi);
          } else {
            Log.d(Constant.TAG, "onClick: error system version");
          }
        }
      }
    }
  };
}
