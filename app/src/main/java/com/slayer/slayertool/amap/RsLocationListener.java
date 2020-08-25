package com.slayer.slayertool.amap;


import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.slayer.slayertool.model.LocationVo;

import java.security.MessageDigest;

import static com.slayer.slayertool.utils.Constant.TAG;


public class RsLocationListener implements AMapLocationListener {
  private LocationVo locationVo;
  RsLocation rsLocation;
  public RsLocationListener(RsLocation l) {
    rsLocation = l;
  }

  @Override
  public void onLocationChanged(AMapLocation aMapLocation) {
  }
}
