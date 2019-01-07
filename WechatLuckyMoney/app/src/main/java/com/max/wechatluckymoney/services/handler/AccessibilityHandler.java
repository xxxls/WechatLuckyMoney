package com.max.wechatluckymoney.services.handler;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.max.wechatluckymoney.utils.L;

/**
 * Created by max on 2018/2/9.
 * 辅助服务
 */
public abstract class AccessibilityHandler implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected final String TAG = this.getClass().getSimpleName();

    protected AccessibilityHandlerListener mListener;

    private Handler mHandler;

    /**
     * 是否处理完成
     */
    private boolean mIsHandlerComplete = true;

    public AccessibilityHandler(@NonNull AccessibilityHandlerListener listener) {
        this.mListener = listener;
        this.mHandler = new Handler();
    }

    /**
     * 处理类
     *
     * @return
     */
    protected abstract boolean onHandler();

    /**
     * 获取要拦截处理的Activity
     *
     * @return
     */
    protected abstract String getInterceptActivityName();

    /**
     * 处理
     *
     * @return
     */
    public boolean onExecute() {
        if (getCurActivity().contains(getInterceptActivityName())) {
            if (mIsHandlerComplete) {
                mIsHandlerComplete = false;
                boolean result = onHandler();
                mIsHandlerComplete = true;
                return result;
            }
        }
        return false;
    }

    private AccessibilityHandlerListener getListener() {
        return mListener;
    }

    /**
     * 获取 Service
     *
     * @return
     */
    protected AccessibilityService getService() {
        return getListener().getAccessibilityService();
    }

    /**
     * 获取当前 事件
     *
     * @return
     */
    protected AccessibilityEvent getEvent() {
        return getListener().getAccessibilityEvent();
    }

    protected SharedPreferences getSharedPreferences() {
        return getListener().getSharedPreferences();
    }

    /**
     * 获取 当前事件 Node
     *
     * @return
     */
    protected AccessibilityNodeInfo getRootNode() {
        return getEvent().getSource();
    }

    /**
     * 设置当前 事件 类名称
     */
    protected String getClassName() {
        if (getEvent().getClassName() == null) {
            return "";
        }
        return getEvent().getClassName().toString();
    }

    /**
     * 当前页面
     *
     * @return
     */
    protected String getCurActivity() {
        return mListener.getCurActivityClassName() == null ? "" : mListener.getCurActivityClassName();
    }

    /**
     * 开始一个延时任务
     *
     * @param runnable
     * @param time
     */
    protected void startDelayedTask(Runnable runnable, long time) {
        mHandler.postDelayed(runnable, time);
    }

    /**
     * 延时返回
     */
    protected void postDelayedBack() {
        startDelayedTask(
                () -> getService().performGlobalAction(getService().GLOBAL_ACTION_BACK), 300);
    }

    protected void toast(CharSequence sequence) {
        Toast.makeText(getService(), sequence, Toast.LENGTH_SHORT).show();
    }


    /**
     * 触发点击
     *
     * @param nodeInfo
     */
    protected void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    protected void log(String message) {
        L.e(TAG, message);
    }


}
