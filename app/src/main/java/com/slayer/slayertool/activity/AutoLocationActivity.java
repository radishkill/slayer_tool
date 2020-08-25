package com.slayer.slayertool.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amap.api.location.AMapLocation;
import com.slayer.slayertool.adapter.LocationAdapter;
import com.slayer.slayertool.service.MainService;
import com.slayer.slayertool.R;
import com.slayer.slayertool.utils.Constant;

import java.lang.ref.WeakReference;
import java.util.Objects;


public class AutoLocationActivity extends AppCompatActivity
    implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {
  Button serviceOpen;
  Button serviceClose;
  Button getOnceLocation;
  Button testBtn;
  EditText intervalEt;
  EditText serverUrlEt;
  DrawerLayout mainDrawerLayout;
  SwitchCompat mainServiceSwitch;
  SwitchCompat mainServiceBinderSwitch;
  ListView locationInfoLv;
  LocationAdapter locationInfoAdapter = null;
  MainService mainService = null;
  private Intent alarmIntent = null;
  private PendingIntent alarmPi = null;
  private AlarmManager alarm = null;
  public String serverUrl = null;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auto_location);
    initView();

    if (MainService.serviceIntent == null) {
      mainServiceSwitch.setChecked(false);
    } else {
      mainServiceSwitch.setChecked(true);
    }
    // 创建Intent对象，action为LOCATION
    alarmIntent = new Intent();
    alarmIntent.setAction(Constant.AUTO_LOCATION);
    IntentFilter ift = new IntentFilter();

    // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
    // 也就是发送了action 为"LOCATION"的intent
    alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
    // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
    alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

    //动态注册一个广播
    IntentFilter filter = new IntentFilter();
    filter.addAction(Constant.AUTO_LOCATION);
    registerReceiver(alarmReceiver, filter);
  }

  private void initView() {
    serviceOpen = findViewById(R.id.auto_location_service_open);
    serviceClose = findViewById(R.id.auto_location_service_close);
    getOnceLocation = findViewById(R.id.get_once_locaiton_btn);
    testBtn = findViewById(R.id.btn_auto_location_test);
    intervalEt = findViewById(R.id.et_location_interval);
    serverUrlEt = findViewById(R.id.et_url_of_data_server);
    mainDrawerLayout = findViewById(R.id.auto_location_drawer_layout);
    mainServiceSwitch = findViewById(R.id.switch_main_service);
    mainServiceBinderSwitch = findViewById(R.id.switch_main_service_binder);
    locationInfoLv = findViewById(R.id.lv_location_information);
    serviceOpen.setOnClickListener(this);
    serviceClose.setOnClickListener(this);
    getOnceLocation.setOnClickListener(this);
    testBtn.setOnClickListener(this);
    mainServiceSwitch.setOnCheckedChangeListener(this);
    mainServiceBinderSwitch.setOnClickListener(this);
    locationInfoAdapter = new LocationAdapter(this, R.layout.item_layout);
    locationInfoLv.setAdapter(locationInfoAdapter);
    locationInfoLv.setOnItemClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int interval = 20;
    //提取间隔数据
    String str = intervalEt.getText().toString();
    if(!TextUtils.isEmpty(str)){
      interval = Integer.parseInt(str);
    }
    //提取目标服务器地址数据
    serverUrl = serverUrlEt.getText().toString();

    if (v.getId() == R.id.auto_location_service_open) {
      mainService.setRemoteServerUrl(serverUrl);
      mainService.startGetLocation(interval);
    } else if (v.getId() == R.id.auto_location_service_close) {
      mainService.stopGetLocation();
    } else if (v.getId() == R.id.get_once_locaiton_btn) {
      mainService.setRemoteServerUrl(serverUrl);
      mainService.startGetOnceLocation();
    } else if (v.getId() == R.id.btn_auto_location_test) {
      Intent intent = new Intent(this, MainService.class);
      conn = new MyConn();
      bindService(intent, conn, BIND_AUTO_CREATE);
      //myBinder.startGetLocationOrder();
//      if(null != alarm) {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//          //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
//          alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//              SystemClock.elapsedRealtime() + 2*1000, alarmPi);
//          Log.d(Constant.TAG, "alarm test start");
//        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//          alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP + 2*1000, SystemClock.elapsedRealtime(),
//              alarmPi);
//        } else {
//          Log.d(Constant.TAG, "onClick: error system version");
//        }
//      }
    } else if (v.getId() == mainServiceBinderSwitch.getId()) {

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
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (buttonView.getId() == mainServiceSwitch.getId()) {
      if (isChecked) {
        MainService.startService(this);
      } else {
        MainService.stopService(this);
      }
    }
  }

  @Override
  protected void onDestroy() {
    if (mainServiceBinderSwitch.isChecked() && null != conn) {
      unbindService(conn);
    }
    super.onDestroy();
    Log.d(Constant.TAG, "onDestroy: AutoLocationActivity Destory");
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (parent.getId() == locationInfoLv.getId()) {
      AMapLocation location = locationInfoAdapter.getItem(position);
      if (location == null) {
        return;
      }
      String detailContent = "经度:" + location.getLongitude() + "\n"
          + "维度:" + location.getLatitude() + "\n"
          + "地址:" + location.getAddress() + "\n"
          + "精度:" + location.getAccuracy() + "m\n"
          + location.getProvider() + "\n";

      AlertDialog alertDialog1 = new AlertDialog.Builder(this)
          .setTitle("坐标详情")//标题
          .setMessage(detailContent)//内容
          .create();
      alertDialog1.show();
    }
  }

  private static class LocationHandler extends Handler {
    private final WeakReference<AutoLocationActivity> mTarget;
    LocationHandler(AutoLocationActivity target) {
      mTarget = new WeakReference<AutoLocationActivity>(target);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      AutoLocationActivity activity = mTarget.get();
      if (msg.what == 1 && activity != null) {
        AMapLocation aMapLocation = (AMapLocation) msg.obj;
      }
    }

    @Override
    public void dispatchMessage(@NonNull Message msg) {

    }
  }

  private BroadcastReceiver alarmReceiver = new BroadcastReceiver(){
    @Override
    public void onReceive(Context context, Intent intent) {
      if(Objects.equals(intent.getAction(), Constant.AUTO_LOCATION)) {
        Log.d(Constant.TAG, "alarm test end");
      }
    }
  };


  public LocationHandler handler = new LocationHandler(this);
  public Message obtainMessage() {
    return  handler.obtainMessage();
  }
  public boolean sendMessage(Message msg) {
    return handler.sendMessage(msg);
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
      mainService.setLocationListener(new MainService.MyLocationListener() {
        @Override
        public void onLocationGet(AMapLocation aMapLocation) {
          if (locationInfoAdapter.getCount() > 10) {
            locationInfoAdapter.remove(locationInfoAdapter.getItem(0));
          }
          locationInfoAdapter.add(aMapLocation);
        }
      });
      mainServiceBinderSwitch.setChecked(true);
    }
    //失去连接
    @Override
    public void onServiceDisconnected(ComponentName name) {
      mainServiceBinderSwitch.setChecked(false);
      myBinder = null;
    }}


  public native String GetThreadId();
  static {
    System.loadLibrary("native-lib");
  }
}
