package com.max.wechatluckymoney.view;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.max.wechatluckymoney.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 悬浮视图
 * Created by Max on 2019/1/5.
 */
public class FloatingView extends FrameLayout
{
    private boolean mScrolling;
    private float touchDownX, mStartX, mStartY;

    private AVLoadingIndicatorView mIndicatorViewForeground;
    private OnFloatingListener mListener;

    public FloatingView(Context context, OnFloatingListener listener)
    {
        super(context);
        this.mListener = listener;
        init();
    }


    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_floating, this, true);

        mIndicatorViewForeground = findViewById(R.id.avi_foreground);
        mIndicatorViewForeground.show();

        findViewById(R.id.ll_switch).setOnClickListener(view -> {
            if (mListener != null)
            {
                mListener.onClick();
            }
        });

        findViewById(R.id.ll_switch).setOnLongClickListener(view ->
        {
            if (mListener != null)
            {
                mListener.onLongClick();
            }
            return true;
        });
    }

    /**
     * 改变状态显示
     */
    public void changeState(boolean state)
    {
        if (state)
        {
            findViewById(R.id.ll_switch).setBackgroundResource(R.drawable.bg_circle_switch_on);
            findViewById(R.id.iv_start).setVisibility(VISIBLE);
            mIndicatorViewForeground.setVisibility(GONE);
            mIndicatorViewForeground.hide();
        } else
        {
            findViewById(R.id.ll_switch).setBackgroundResource(R.drawable.bg_circle_switch_off);
            findViewById(R.id.iv_start).setVisibility(GONE);
            mIndicatorViewForeground.setVisibility(VISIBLE);
            mIndicatorViewForeground.show();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getRawX();
                mStartY = (int) event.getRawY();
                touchDownX = event.getX();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop())
                {
                    mScrolling = true;
                } else
                {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                float dx = mStartX - motionEvent.getRawX();
                float dy = motionEvent.getRawY() - mStartY;

                mStartX = motionEvent.getRawX();
                mStartY = motionEvent.getRawY();

                if (mListener != null)
                {
                    mListener.onMove(dx, dy);
                }
                break;
        }
        return super.onTouchEvent(motionEvent);
    }


    public interface OnFloatingListener
    {

        /**
         * 移动
         *
         * @param offsetX 偏移X
         * @param offsetY 偏移Y
         */
        void onMove(float offsetX, float offsetY);

        void onClick();

        void onLongClick();
    }
}
