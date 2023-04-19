package com.example.wordpro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.tool.Callback;

public class BottomMenuRecyclerView extends RecyclerView{

    Callback callback;

    public BottomMenuRecyclerView(@NonNull Context context) {
        super(context);
    }
    public BottomMenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public BottomMenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        if(callback != null) {
            if (isVacant(ev, layoutManager)) {
                MotionEvent refinedMotionEvent = MotionEvent.obtain(
                        ev.getDownTime(),
                        ev.getEventTime(),
                        ev.getAction(),
                        ev.getRawX(),
                        ev.getRawY(),
                        ev.getMetaState()
                );
                callback.OnCallback(refinedMotionEvent);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isVacant(MotionEvent motionEvent, LinearLayoutManager linearLayoutManager){
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        if(firstVisibleItemPosition >= 1){
            return false;
        }
        if(motionEvent.getY() < linearLayoutManager.getChildAt(1).getTop()){
            return true;
        }else{
            return false;
        }
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    /**
     * Recycler view와 bottom menu의 layout parameter와 동일하게 한다.
     *
     * */
}
