package com.slayer.slayertool.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.slayer.slayertool.R;

import static com.slayer.slayertool.utils.Constant.TAG;

public class LocationAdapter extends ArrayAdapter<AMapLocation> {
  private final LayoutInflater mInflater;
  private final int newResourceId;
  public LocationAdapter(Context context, int resourceId){
    super(context, resourceId);
    mInflater = LayoutInflater.from(context);
    newResourceId = resourceId;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    final View view;
    if (null == convertView) {
      view = mInflater.inflate(newResourceId, parent, false);
    } else {
      view = convertView;
    }
    AMapLocation aMapLocation = getItem(position);
    TextView textView = view.findViewById(R.id.item_content);
    textView.setText(aMapLocation.getLongitude() + "," + aMapLocation.getLatitude());
    return view;
  }
}
