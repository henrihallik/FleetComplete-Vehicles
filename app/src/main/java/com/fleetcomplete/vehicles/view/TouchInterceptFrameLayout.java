package com.fleetcomplete.vehicles.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchInterceptFrameLayout extends FrameLayout {

    private GestureDetector detector;

    public TouchInterceptFrameLayout(Context context) {
        super(context);
    }

    public TouchInterceptFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchInterceptFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TouchInterceptFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (detector != null) {
            detector.onTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector != null) {
            detector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public void setDetector(GestureDetector detector) {
        this.detector = detector;
    }
}