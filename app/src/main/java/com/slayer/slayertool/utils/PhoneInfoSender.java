package com.slayer.slayertool.utils;

import android.os.Build;

import com.google.gson.Gson;
import com.slayer.slayertool.model.PhoneInfoVo;

import java.util.Date;

public class PhoneInfoSender {
  public static void sendScreenStatus(final String url, String status) {
    final PhoneInfoVo phoneInfoVo = new PhoneInfoVo();
    phoneInfoVo.setCreateTime(new Date().getTime());
    phoneInfoVo.setStatusType("ScreenStatus");
    phoneInfoVo.setDevice(Build.DEVICE);
    phoneInfoVo.setStatus(status);

    final Gson gson = new Gson();

    new Thread() {
      @Override
      public void run() {
        super.run();
        try {
          String ret = JsonSender.sendPost(url, gson.toJson(phoneInfoVo));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }
  public static void sendBatteryLevel(final String url, int level) {
    final PhoneInfoVo phoneInfoVo = new PhoneInfoVo();
    phoneInfoVo.setCreateTime(new Date().getTime());
    phoneInfoVo.setStatusType("BatteryLevel");
    phoneInfoVo.setDevice(Build.DEVICE);
    phoneInfoVo.setStatus(String.valueOf(level));
    final Gson gson = new Gson();
    new Thread() {
      @Override
      public void run() {
        super.run();
        try {
          String ret = JsonSender.sendPost(url, gson.toJson(phoneInfoVo));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }
  public static void sendServiceStatus(final String url, String status) {
    final PhoneInfoVo phoneInfoVo = new PhoneInfoVo();
    phoneInfoVo.setCreateTime(new Date().getTime());
    phoneInfoVo.setStatusType("ServiceStatus");
    phoneInfoVo.setDevice(Build.DEVICE);
    phoneInfoVo.setStatus(status);
    final Gson gson = new Gson();
    new Thread() {
      @Override
      public void run() {
        super.run();
        try {
          String ret = JsonSender.sendPost(url, gson.toJson(phoneInfoVo));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }
}
