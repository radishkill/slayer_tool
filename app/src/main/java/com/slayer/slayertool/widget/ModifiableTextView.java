package com.slayer.slayertool.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ModifiableTextView extends View {
  public ModifiableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Rect textRect = new Rect();
    textRect.left = 0;
    textRect.top = 0;
    textRect.right = 100;
    textRect.bottom = 100;
//    Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
//    int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
    //文字绘制到整个布局的中心位置
//    canvas.drawText(buttonString, textRect.centerX(), baseline, textPaint);
  }
}
