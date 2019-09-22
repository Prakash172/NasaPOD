package com.androidcodeshop.obviousassigment.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends android.support.v4.view.ViewPager {

    float mStartDragX;
    OnSwipeOutListener mOnSwipeOutListener;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mOnSwipeOutListener = listener;
    }

    private void onSwipeOutAtStart() {
        if (mOnSwipeOutListener != null) {
            mOnSwipeOutListener.onSwipeOutAtStart();
        }
    }

    private void onSwipeOutAtEnd() {
        if (mOnSwipeOutListener != null) {
            mOnSwipeOutListener.onSwipeOutAtEnd();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if ((ev.getAction() & MotionEventCompat.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            mStartDragX = ev.getX();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getCurrentItem() == 0 || getCurrentItem() == getAdapter().getCount() - 1) {
            final int action = ev.getAction();
            float x = ev.getX();
            switch (action & MotionEventCompat.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (getCurrentItem() == 0 && x > mStartDragX) {
                        onSwipeOutAtStart();
                    }else if (getCurrentItem() == getAdapter().getCount() - 1 && x < mStartDragX) {
                        onSwipeOutAtEnd();
                    }
                    break;
            }
        } else {
            mStartDragX = 0;
        }
        return super.onTouchEvent(ev);

    }

    public interface OnSwipeOutListener {
        void onSwipeOutAtStart();
        void onSwipeOutAtEnd();
    }
}