package com.max.wechatluckymoney.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.max.wechatluckymoney.R;

/**
 * 悬浮视图
 * Created by Max on 2019/1/5.
 */
public class FloatingView extends FrameLayout implements View.OnTouchListener {

    private long mFirstTime;

    //悬浮按钮相关
    private int mStartX, mStartY, mFirstX, mFirstY;

    private OnFloatingListener mListener;

    public FloatingView(Context context, OnFloatingListener listener) {
        super(context);
        this.mListener = listener;
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_floating, this, true);

        setOnTouchListener(this);
    }

    /**
     * 改变状态显示
     */
    public void changeState(boolean state) {
        LinearLayout llSwitch = (LinearLayout) findViewById(R.id.ll_switch);
        TextView tvSwitch = (TextView) findViewById(R.id.tv_switch);
        if (state) {
            llSwitch.setBackgroundResource(R.drawable.bg_circle_switch_on);
            tvSwitch.setTextColor(Color.BLACK);
            tvSwitch.setText(R.string.str_open);

        } else {
            llSwitch.setBackgroundResource(R.drawable.bg_circle_switch_off);
            tvSwitch.setText(R.string.str_close);
            tvSwitch.setTextColor(Color.WHITE);
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstTime = System.currentTimeMillis();
                mFirstX = mStartX = (int) motionEvent.getRawX();
                mFirstY = mStartY = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endx = (int) motionEvent.getRawX();
                int endy = (int) motionEvent.getRawY();

                int dx = mStartX - endx;
                int dy = endy - mStartY;

                if (mListener != null) {
                    mListener.onMove(dx, dy);
                }

                mStartX = (int) motionEvent.getRawX();
                mStartY = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                long timeDiffer = System.currentTimeMillis() - mFirstTime;
                if (timeDiffer >= 500) {
                    if (Math.abs(mFirstX - motionEvent.getRawX()) <= 10
                            && Math.abs(mFirstY - motionEvent.getRawY()) <= 10) {
                        if (mListener != null) {
                            mListener.onLongClick();
                        }
                    }
                } else if (timeDiffer <= 300) {
                    if (mListener != null) {
                        mListener.onClick();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    public interface OnFloatingListener {

        /**
         * 移动
         *
         * @param offsetX 偏移X
         * @param offsetY 偏移Y
         */
        void onMove(int offsetX, int offsetY);

        void onClick();

        void onLongClick();
    }
}
