package com.foo.bar.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.ViewGroup

class DimmingView(context: Context) : ViewGroup(context) {
    private val maxAlpha: Float = 0.3f

    // This view has no children
    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) = Unit

    init {
        setBackgroundColor(Color.BLACK)
        alpha = maxAlpha
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // We do not have children, but let's block them anyway :D
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Block interaction with anything underneath
        return true
    }
}