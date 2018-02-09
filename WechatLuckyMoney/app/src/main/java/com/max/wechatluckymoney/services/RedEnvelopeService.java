package com.max.wechatluckymoney.services;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.ChatPageHandler;
import com.max.wechatluckymoney.services.handler.LuckyMoneyDetailsHandler;
import com.max.wechatluckymoney.utils.L;

import java.util.ArrayList;

/**
 * Created by max on 2018/2/9.
 * 红包 辅助服务
 */
public class RedEnvelopeService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener, OnAccessibilityHandlerListener
{

    private static final String WECHAT_ACTIVITY_GENERAL = "LauncherUI";

    private ArrayList<BaseAccessibilityHandler> mHandlers;


    //当前活动页面名称
    private String mCurActivityName;

    //event
    private AccessibilityEvent mEvent;

    /**
     * 系统会在成功连接上你的服务的时候调用这个方法，在这个方法里你可以做一下初始化工作
     */
    @Override
    protected void onServiceConnected()
    {
        initHandlers();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {

        setCurrentActivityName(event);

        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null)
        {
            return;
        }

        mEvent = event;

        onHandler(root);
    }

    /**
     * 处理
     *
     * @param root
     */
    private void onHandler(AccessibilityNodeInfo root)
    {
        for (BaseAccessibilityHandler handler : getHandlers())
        {
            if (handler.onExecute(root))
            {
                break;
            }
        }
    }


    /**
     * 这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用
     */
    @Override
    public void onInterrupt()
    {

    }

    /**
     * 在系统将要关闭这个AccessibilityService会被调用。在这个方法中进行一些释放资源的工作。
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent)
    {
        if (mHandlers != null)
        {
            mHandlers.clear();
            mHandlers = null;
        }

        if (mEvent != null)
        {
            mEvent = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {

    }

    /**
     * 获取 处理类
     *
     * @return
     */
    private ArrayList<BaseAccessibilityHandler> getHandlers()
    {
        initHandlers();
        return mHandlers;
    }

    private void initHandlers()
    {
        if (mHandlers == null)
        {
            mHandlers = new ArrayList<>();
            mHandlers.add(new ChatPageHandler(this));
            mHandlers.add(new LuckyMoneyDetailsHandler(this));
        }
    }

    /**
     * 设置当前 页面名称
     *
     * @param event
     */
    private void setCurrentActivityName(AccessibilityEvent event)
    {
        String name = event.getClassName().toString();
        if (name.contains("com.tencent.mm"))
        {
            mCurActivityName = name;
            L.e("NAMEMAX  event = " + event.getEventType());
            L.e("NAMEMAX  name = " + name);
        }

        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                || event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
        {
            return;
        }

        try
        {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            getPackageManager().getActivityInfo(componentName, 0);
            mCurActivityName = componentName.flattenToShortString();

        } catch (PackageManager.NameNotFoundException e)
        {
            mCurActivityName = WECHAT_ACTIVITY_GENERAL;
        }

        L.e("mCurActivityName  = " + mCurActivityName);
    }


    @Override
    public AccessibilityService getAccessibilityService()
    {
        return this;
    }

    @Override
    public String getCurrentActivityName()
    {
        if (mCurActivityName == null)
        {
            return "";
        }
        return mCurActivityName;
    }

    @Override
    public AccessibilityEvent getAccessibilityEvent()
    {
        return mEvent;
    }
}
