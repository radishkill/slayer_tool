package com.slayer.slayertool.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.slayer.slayertool.R;
import com.slayer.slayertool.amap.RsLocation;
import com.slayer.slayertool.utils.Constant;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

  private ListView mainListView = null;
  private ArrayAdapter<String> adapter = null;
  private RsLocation rsLocation;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initPermission();

    mainListView = findViewById(R.id.main_menu_list);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
        new String[] {"PositionActivity", "CustomUi", "PhoneStatusActivity", "3"});
    mainListView.setAdapter(adapter);
    mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
          case 0: {
            Intent intent = new Intent(MainActivity.this, AutoLocationActivity.class);
            startActivity(intent);
            break;
          }
          case 1: {
            Intent intent = new Intent(MainActivity.this, CustomUiActivity.class);
            startActivity(intent);
            break;
          }
          case 2: {
            Intent intent = new Intent(MainActivity.this, PhoneStatusActivity.class);
            startActivity(intent);
            break;
          }
          default:
        }
      }
    });
  }

  private void initPermission() {
    List<String> permissions = new ArrayList<String>();
    permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
    permissions.add(Manifest.permission.INTERNET);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
      return;
    //android10以上需要
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
      permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);

    ArrayList<String> toApplyList = new ArrayList<String>();
    for (String p : permissions) {
      if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, p)) {
        toApplyList.add(p);
      }
    }
    String[] tmpList = new String[toApplyList.size()];
    if (!toApplyList.isEmpty()) {
      ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
    }
  }

  /**
   * A native method that is implemented by the 'native-lib' native library,
   * which is packaged with this application.
   */
  public native String stringFromJNI();
  public native String GetThreadId();
  // Used to load the 'native-lib' library on application startup.
  static {
    System.loadLibrary("native-lib");
  }
}
