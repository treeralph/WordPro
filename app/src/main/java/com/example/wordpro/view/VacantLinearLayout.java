package com.example.wordpro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.wordpro.tool.Callback;

public class VacantLinearLayout extends LinearLayout {

    public String TAG = VacantLinearLayout.class.getName();

    Callback callback;

    public VacantLinearLayout(Context context) {
        super(context);
    }

    public VacantLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VacantLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VacantLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "MotionEvent: " + ev.toString());
        callback.OnCallback(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: " + ev.toString());
        return super.onInterceptTouchEvent(ev);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
