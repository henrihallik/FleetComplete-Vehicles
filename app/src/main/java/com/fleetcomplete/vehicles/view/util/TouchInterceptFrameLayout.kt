package com.fleetcomplete.vehicles.view.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout

class TouchInterceptFrameLayout : FrameLayout {
    private var detector: GestureDetector? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context!!, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (detector != null) {
            detector!!.onTouchEvent(ev)
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (detector != null) {
            detector!!.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    fun setDetector(detector: GestureDetector?) {
        this.detector = detector
    }
}