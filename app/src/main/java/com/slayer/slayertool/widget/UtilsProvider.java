package com.slayer.slayertool.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.slayer.slayertool.R;
import com.slayer.slayertool.service.MainService;
import com.slayer.slayertool.utils.Constant;

import java.util.Objects;

import static com.slayer.slayertool.utils.Constant.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class UtilsProvider extends AppWidgetProvider {

  private static final String EXTRA_PARAM1 = "com.slayer.widget.utilsprovider.buttontype";
  private static final String BUTTON_TYPE1 = "getlocation";
  private static final String BUTTON_TYPE2 = "closelocationservice";

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    Log.d(TAG, "onReceive:" + intent.getAction());
    if (Objects.equals(intent.getAction(), Constant.CLICK_ACTION)) {
      String buttonType = intent.getStringExtra(EXTRA_PARAM1);
      Log.d(TAG, "onReceive: " + buttonType);
      if (Objects.equals(buttonType, BUTTON_TYPE1)) {
        MainService.startService(context, 10000, true);
      } else if (Objects.equals(buttonType, BUTTON_TYPE2)){
        MainService.stopService(context);
      }
    }
  }

  private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                               int appWidgetId) {
    Log.d(TAG, "onEnabled: onUpdate");
//    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.utils_provider);
    views.setTextViewText(R.id.appwidget_utils_get_location, context.getString(R.string.location_text));
//    Intent utils_intent = new Intent(context, AppWidgetService.class);
//    PendingIntent pi = PendingIntent.getService(context, 200, utils_intent, PendingIntent.FLAG_CANCEL_CURRENT);
//    views.setOnClickPendingIntent(R.id.appwidget_utils_get_location, pi);
//    Intent utils_intent = new Intent(context, MainActivity.class);
//    Intent utils_intent = new Intent();
//    utils_intent.setAction(Constant.CLICK_ACTION);
//    utils_intent.setComponent(new ComponentName(context, UtilsProvider.class));
//    utils_intent.putExtra(EXTRA_PARAM1, BUTTON_TYPE1);
//
//    PendingIntent pi = PendingIntent.getBroadcast(context, 0, utils_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    views.setOnClickPendingIntent(R.id.appwidget_utils_get_location, pi);

    Intent intent_close_service = new Intent();
    intent_close_service.setAction(Constant.CLICK_ACTION);
    intent_close_service.setComponent(new ComponentName(context, UtilsProvider.class));
    intent_close_service.putExtra(EXTRA_PARAM1, BUTTON_TYPE2);
    PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent_close_service, PendingIntent.FLAG_UPDATE_CURRENT);
    views.setOnClickPendingIntent(R.id.appwidget_utils_close_location_service, pi);
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    Log.d(TAG, "onEnabled: onEnabled");
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    Log.d(TAG, "onEnabled: onDisabled");
    // Enter relevant functionality for when the last widget is disabled
  }
}

