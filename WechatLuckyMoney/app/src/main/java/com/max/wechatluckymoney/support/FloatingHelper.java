package com.max.wechatluckymoney.support;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import com.max.wechatluckymoney.activitys.MainActivity;
import com.max.wechatluckymoney.utils.Utils;
import com.max.wechatluckymoney.view.FloatingView;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 悬浮视图
 * Created by Max on 2019/1/5.
 */
public class FloatingHelper implements FloatingView.OnFloatingListener {

    private Context mContext;

    private int mWinW, mWinH;

    private WindowManager.LayoutParams mParams;

    private WindowManager mWindowManager;

    private FloatingView mFloatingView;

    private OnFloatingHelperListener mListener;

    public FloatingHelper(Context context, OnFloatingHelperListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    /**
     * 检查加载
     */
    public void checkLoad() {
        if (isFloatingEnabled()) {
            show();
        }else{
            hide();
        }
    }

    private void show() {
        if (mFloatingView == null) {
            mFloatingView = new FloatingView(mContext, this);
        }

        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
            mParams = new WindowManager.LayoutParams();
            mWinW = mWindowManager.getDefaultDisplay().getWidth();
            mWinH = mWindowManager.getDefaultDisplay().getHeight();
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            mParams.format = PixelFormat.TRANSLUCENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            mParams.gravity = Gravity.RIGHT + Gravity.TOP;// 将重心位置设置为右上方,
            mParams.y = mWinH / 6;
            mWindowManager.addView(mFloatingView, mParams);
        }

        //改变状态
        changeState(!isHandler());
    }

    private void hide() {
        if (mWindowManager != null && mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
            mFloatingView = null;
            mWindowManager = null;
        }
    }

    /**
     * 解绑
     */
    public void onUnbind() {
        hide();
    }

    /**
     * 改变状态显示
     */
    private void changeState(boolean state) {
        if (mFloatingView == null) {
            return;
        }
        mFloatingView.changeState(state);
    }

    /**
     * 是否处理
     *
     * @return
     */
    private boolean isHandler() {
        //开关
        return mListener.getSharedPreferences().getBoolean("switch_app", true);
    }


    /**
     * 设置开关
     *
     * @param sw
     */
    private void setSwitch(boolean sw) {
        mListener.getSharedPreferences().edit().putBoolean("switch_app", sw).commit();
    }

    /**
     * 是否 show 悬浮按钮
     *
     * @return
     */
    private boolean isFloatingEnabled() {
        return mListener.getSharedPreferences().getBoolean("switch_float", true);
    }

    @Override
    public void onMove(int offsetX, int offsetY) {
        mParams.x += offsetX;
        mParams.y += offsetY;

        if (mParams.x > mWinW - mFloatingView.getWidth()) {
            mParams.x = mWinW - mFloatingView.getWidth();
        }
        if (mParams.y > mWinH - mFloatingView.getHeight()) {
            mParams.y = mWinH - mFloatingView.getHeight();
        }

        mWindowManager.updateViewLayout(mFloatingView, mParams);
    }

    @Override
    public void onClick() {
        Utils.vibrate(mContext, 50);
        setSwitch(!isHandler());
    }

    @Override
    public void onLongClick() {
        Utils.vibrate(mContext, 150);
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        checkLoad();
    }


    public interface OnFloatingHelperListener {
        SharedPreferences getSharedPreferences();
    }
}
