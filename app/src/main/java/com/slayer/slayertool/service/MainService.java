package com.slayer.slayertool.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.slayer.slayertool.R;
import com.slayer.slayertool.amap.RsLocation;
import com.slayer.slayertool.model.LocationVo;
import com.slayer.slayertool.receiver.StatusReceiver;
import com.slayer.slayertool.utils.JsonSender;
import com.slayer.slayertool.utils.PhoneInfoSender;

import static com.slayer.slayertool.utils.Constant.TAG;


public class MainService extends Service implements AMapLocationListener, StatusReceiver.PhoneInfoListener {
  // TODO: Rename actions, choose action names that describe tasks that this
  // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
  private static final String ACTION_FOO = "com.slayer.slayertool.action.FOO";

  // TODO: Rename parameters
  private static final String EXTRA_PARAM_SINGLE_LOCATION = "com.slayer.slayertool.extra.PARAM1";
  private static final String EXTRA_PARAM_INTERVAL = "com.slayer.slayertool.extra.PARAM2";
  private static final String EXTRA_PARAM_SERVERURL = "com.slayer.slayertool.extra.PARAM3";
  String channelId;
  private NotificationManager mNM;
  public static final int NOTIFICATION_ID = 101;

  public static Intent serviceIntent = null;
  private RsLocation rsLocation = null;
  private MyLocationListener locationListener;
  StatusReceiver screenStatusReceiver = null;
  StatusReceiver batteryStatusReceiver = null;
  String remoteServerUrl;

  public boolean isOnce = true;


  public MainService() {
    super();
  }

  /**
   * Starts this service to perform action Foo with the given parameters. If
   * the service is already performing a task this action will be queued.
   *
   * @see IntentService
   */
  // TODO: Customize helper method
  public static void startService(Context context, int interval, boolean isOnce) {
    Intent intent = new Intent(context, MainService.class);
    intent.putExtra(EXTRA_PARAM_SINGLE_LOCATION, isOnce);
    intent.putExtra(EXTRA_PARAM_INTERVAL, interval);
    context.startService(intent);
  }
  public static void startService(Context context) {
    Intent intent = new Intent(context, MainService.class);
    context.startService(intent);
  }
  public static void stopService(Context context) {
    Intent intent = new Intent(context, MainService.class);
    context.stopService(intent);
  }
  @Override
  public void onCreate() {
    super.onCreate();
    channelId = getString(R.string.app_name);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    serviceIntent = intent;
    initNotification(getString(R.string.app_name), "service ready");
    return START_NOT_STICKY;
  }

  public void startGetLocation(int interval) {
    if (serviceIntent == null) {
      Log.d(TAG, "startGetLocation: error start");
      return;
    }
    this.isOnce = false;

    rsLocation = new RsLocation(this);
    rsLocation.setOption(interval, false);
    rsLocation.setLocationListener(this);
    rsLocation.start();
    updateNotification(getString(R.string.app_name), "wait");
  }
  public void startGetOnceLocation() {
    if (serviceIntent == null) {
      Log.d(TAG, "startGetLocation: error start");
      return;
    }
    this.isOnce = true;

    rsLocation = new RsLocation(this);
    rsLocation.setOption(1000, true);
    rsLocation.setLocationListener(this);
    rsLocation.start();
    updateNotification(getString(R.string.app_name), "wait");
  }
  public void stopGetLocation() {
    if (null == rsLocation) {
      return;
    }
    rsLocation.onDestroy();
    updateNotification(getString(R.string.app_name), "stoped");
  }
  //返回屏幕监测器开关状态
  public boolean getStatusSwitchOfScreen() {
    return screenStatusReceiver != null;
  }
  public void startGetPhoneInfoOfScreen() {
    if (null == serviceIntent) {
      return;
    }
    screenStatusReceiver = new StatusReceiver();
    screenStatusReceiver.setPhoneInfoListener(this);

    IntentFilter recevierFilter = new IntentFilter();
    recevierFilter.addAction(Intent.ACTION_SCREEN_ON);
    recevierFilter.addAction(Intent.ACTION_SCREEN_OFF);
    registerReceiver(screenStatusReceiver, recevierFilter);
  }
  public void stopGetPhoneInfoOfScreen() {
    if (null == serviceIntent || null == screenStatusReceiver) {
      return;
    }
    unregisterReceiver(screenStatusReceiver);
    screenStatusReceiver = null;
  }
  //返回电池监测器开关状态
  public boolean getStatusSwitchOfBattery() {
    return batteryStatusReceiver != null;
  }
  public void startGetPhoneInfoOfBattery() {
    if (null == serviceIntent) {
      return;
    }
    batteryStatusReceiver = new StatusReceiver();
    batteryStatusReceiver.setPhoneInfoListener(this);

    IntentFilter recevierFilter = new IntentFilter();
    recevierFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
    registerReceiver(batteryStatusReceiver, recevierFilter);
  }
  public void stopGetPhoneInfoOfBattery() {
    if (null == serviceIntent || null == batteryStatusReceiver) {
      return;
    }
    unregisterReceiver(batteryStatusReceiver);
    batteryStatusReceiver = null;
  }



  public void setRemoteServerUrl(String url) {
    this.remoteServerUrl = url;
  }
  public void initNotification(String titile, String content) {
    mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
      notificationChannel.setDescription(channelId);
      notificationChannel.setSound(null, null);
      mNM.createNotificationChannel(notificationChannel);
      Notification notification = new Notification.Builder(this, channelId)
          .setContentTitle(titile)
          .setContentText(content)
          .setSmallIcon(R.drawable.ic_launcher_foreground)
          .build();
      startForeground(NOTIFICATION_ID, notification);
    } else {
      Notification.Builder builder = new Notification.Builder(this);
      builder.setSmallIcon(R.drawable.ic_launcher_foreground)
          .setContentTitle(titile)
          .setContentText(content);
      Notification notification = builder.build();
      startForeground(NOTIFICATION_ID, notification);
    }
  }
  public void updateNotification(String titile, String content) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
      notificationChannel.setDescription(channelId);
      notificationChannel.setSound(null, null);
      mNM.createNotificationChannel(notificationChannel);
      Notification notification = new Notification.Builder(this, channelId)
          .setContentTitle(titile)
          .setContentText(content)
          .setSmallIcon(R.drawable.ic_launcher_foreground)
          .build();
      mNM.notify(NOTIFICATION_ID, notification);
    } else {
      Notification.Builder builder = new Notification.Builder(this);
      builder.setSmallIcon(R.drawable.ic_launcher_foreground)
          .setContentTitle(titile)
          .setContentText(content);
      Notification notification = builder.build();
      mNM.notify(NOTIFICATION_ID, notification);
    }
  }
  public void setLocationListener(MyLocationListener locationListener) {
    this.locationListener = locationListener;
  }
  @Override
  public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind: ");
    return new LocalBinder();
    // TODO: Return the communication channel to the service.
    //throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void onRebind(Intent intent) {
    Log.d(TAG, "onRebind: ");
    //super.onRebind(intent);
  }

  @Override
  public boolean onUnbind(Intent intent) {
    Log.d(TAG, "onUnbind: ");
    //return super.onUnbind(intent);
    return true;
  }

  @Override
  public void onDestroy() {
    serviceIntent = null;
    if (null != rsLocation) {
      rsLocation.onDestroy();
    }
    super.onDestroy();
  }

  private LocationVo locationVo;
  @Override
  public void onLocationChanged(AMapLocation aMapLocation) {
    if (null == aMapLocation) {
      return;
    }
    if (aMapLocation.getErrorCode() != 0) {
      //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
      Log.d("rstest", "location Error, ErrCode:"
          + aMapLocation.getErrorCode() + ", errInfo:"
          + aMapLocation.getErrorInfo());
      return;
    }
    locationVo = new LocationVo();
    locationVo.setLongitude(aMapLocation.getLongitude());
    locationVo.setLatitude(aMapLocation.getLatitude());
    locationVo.setAltitude(aMapLocation.getAltitude());
    locationVo.setAccuracy(aMapLocation.getAccuracy());
    locationVo.setSpeed(aMapLocation.getSpeed());
    locationVo.setBearing(aMapLocation.getBearing());
    locationVo.setProvider(aMapLocation.getProvider());
    new Thread() {
      @Override
      public void run() {
        super.run();
        if (remoteServerUrl == null || remoteServerUrl.isEmpty())
          return;
        try {
          Gson gson = new Gson();
          String reponse = JsonSender.sendPost(remoteServerUrl, gson.toJson(locationVo));
          Log.d("test", "post: " + reponse);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
    updateNotification(getString(R.string.app_name),
        "longitude: " + aMapLocation.getLongitude() + " latitude:" + aMapLocation.getLatitude());
    if (null != locationListener) {
      locationListener.onLocationGet(aMapLocation);
    }

    Log.d(TAG, "onLocationChanged: " + aMapLocation.getLatitude() + " " + aMapLocation.getLongitude());
    //返回一次就退出
    if (isOnce) {
      stopGetLocation();
    }
  }

  @Override
  public void onScreenChanged(boolean status) {
    Log.d(TAG, "onScreenChanged: " + status);
    if (status) {
      PhoneInfoSender.sendScreenStatus("http://slayer.top:9999/status/insert", "On");
    } else {
      PhoneInfoSender.sendScreenStatus("http://slayer.top:9999/status/insert", "Off");
    }
  }

  @Override
  public void onBatteryLevelChanged(int batteryLevel) {
    Log.d(TAG, "onBatteryLevelChanged: " + batteryLevel);
    PhoneInfoSender.sendBatteryLevel("http://slayer.top:9999/status/insert", batteryLevel);
  }

  public class LocalBinder extends Binder {
    public MainService getService() {
      return MainService.this;
    }
  }
  public interface MyLocationListener {
    void onLocationGet(AMapLocation aMapLocation);
  }

  public native String GetThreadId();
  static {
    System.loadLibrary("native-lib");
  }
}
