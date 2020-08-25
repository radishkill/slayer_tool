package com.slayer.slayertool.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.slayer.slayertool.R;
import com.slayer.slayertool.widget.AnimationButton;

import static com.slayer.slayertool.utils.Constant.TAG;

public class CustomUiActivity extends AppCompatActivity {

  AnimationButton animationButton;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_ui);

    animationButton = findViewById(R.id.custom_ui_animation_button1);
    animationButton.setAnimationButtonListener(new AnimationButton.AnimationButtonListener() {
      @Override
      public void onClickListener() {
        animationButton.start();
      }

      @Override
      public void animationFinish() {
        animationButton.reset();
      }
    });
  }
}
