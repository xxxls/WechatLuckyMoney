package com.max.wechatluckymoney.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.support.FloatingHelper;
import com.max.wechatluckymoney.utils.L;
import com.max.wechatluckymoney.utils.Utils;

import java.util.ArrayList;

/**
 * Created by max on 2018/2/9.
 * 红包 辅助服务
 */
public class LuckyMoneyService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener,
        AccessibilityHandlerListener, FloatingHelper.OnFloatingHelperListener {

    //处理类
    private ArrayList<AccessibilityHandler> mHandlers;

    //event
    private AccessibilityEvent mEvent;

    //配置
    private SharedPreferences mSharedPreferences;

    //悬浮窗
    private FloatingHelper mFloatingHelper;

    /**
     * 当前UI页面
     */
    private String mCurActivityClassName;

    /**
     * 开关
     */
    private Boolean mSwitchService;

    /**
     * 系统会在成功连接上你的服务的时候调用这个方法，在这个方法里你可以做一下初始化工作
     */
    @Override
    protected void onServiceConnected() {
        mFloatingHelper = new FloatingHelper(this, this);
        mFloatingHelper.checkLoad();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event == null || event.getSource() == null) {
            return;
        }

        mEvent = event;

        String className = event.getClassName().toString();
        if (className.startsWith("com.tencent.mm")) {
            mCurActivityClassName = className;
        }

        L.e("onAccessibilityEvent -> EventClassName:" + event.getClassName());
        L.e("onAccessibilityEvent -> CurActivityClassName:" + mCurActivityClassName);

        if (isHandler()) {
            onHandler();
        }
    }


    /**
     * 是否处理
     *
     * @return
     */
    private boolean isHandler() {

        if (mSwitchService == null) {
            mSwitchService = getSharedPreferences().getBoolean("switch_app", true);
        }
        return mSwitchService;
    }

    /**
     * 处理
     */
    private void onHandler() {
        if (mHandlers == null) {
            initHandlers();
        }

        for (AccessibilityHandler handler : mHandlers) {
            if (handler.onExecute()) {
                break;
            }
        }
    }

    /**
     * 这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用
     */
    @Override
    public void onInterrupt() {
        L.e("onInterrupt() ->");
    }

    /**
     * 按键事件
     *
     * @param event
     * @return
     */
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    /**
     * 在系统将要关闭这个AccessibilityService会被调用。在这个方法中进行一些释放资源的工作。
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        if (mHandlers != null) {
            mHandlers.clear();
            mHandlers = null;
        }

        if (mEvent != null) {
            mEvent = null;
        }

        if (mFloatingHelper != null) {
            mFloatingHelper.onUnbind();
        }

        return super.onUnbind(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //开关
        mSwitchService = getSharedPreferences().getBoolean("switch_app", true);
        if (mFloatingHelper != null) {
            mFloatingHelper.onSharedPreferenceChanged(sharedPreferences, key);
        }

        if (mHandlers != null) {
            for (AccessibilityHandler handler : mHandlers) {
                handler.onSharedPreferenceChanged(sharedPreferences, key);
            }
        }
    }

    /**
     * 初始化 处理类
     */
    private void initHandlers() {
        mHandlers = new HandlerHelper(this).getHandlers(Utils.getWeChatVersion(this));
    }

    @Override
    public AccessibilityService getAccessibilityService() {
        return this;
    }

    @Override
    public AccessibilityEvent getAccessibilityEvent() {
        return mEvent;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
        return mSharedPreferences;
    }

    @Override
    public String getCurActivityClassName() {
        return mCurActivityClassName;
    }

}
