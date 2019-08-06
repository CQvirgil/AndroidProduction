package com.virgil.study.cqplayer.mView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

//圆形进度条
public class CircularProgress extends View {
    private final String TAG = "CircularProgress";
    private float width, height;
    private double percentage = 0;
    private int offset = 6;
    private double max = 0;
    private double value = 0;

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        postInvalidate();
    }

    public CircularProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public CircularProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#e2e2e2"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dip2px(1));
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawCircle(width / 2, height / 2, width / 2 - px2dip(offset), paint);

        if (max != 0 && value != 0) {
            percentage = value / max * 100;
        }
        Log.i(TAG, "onDraw: max" + max);
        Log.i(TAG, "onDraw: value" + value);
        Log.i(TAG, "onDraw: percentage" + percentage);
        paint.setColor(Color.parseColor("#e7af00"));
        RectF oval = new RectF(px2dip(offset), px2dip(offset), width - px2dip(offset), height - px2dip(offset));
        canvas.drawArc(oval, -90, (float) (percentage * 3.6f), false, paint);
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
