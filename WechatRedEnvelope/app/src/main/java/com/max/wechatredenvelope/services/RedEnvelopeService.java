package com.max.wechatredenvelope.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatredenvelope.services.handler.BaseAccessibilityHandler;
import com.max.wechatredenvelope.services.handler.ChatDetailsHandler;

import java.util.ArrayList;

/**
 * Created by max on 2018/2/9.
 * 红包 辅助服务
 */
public class RedEnvelopeService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener
{

    private ArrayList<BaseAccessibilityHandler> mHandlers;

    /**
     * 系统会在成功连接上你的服务的时候调用这个方法，在这个方法里你可以做一下初始化工作
     */
    @Override
    protected void onServiceConnected()
    {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null)
        {
            return;
        }
        for (BaseAccessibilityHandler handler : getHandlers())
        {
            if (handler.onHandler(root))
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
        if (mHandlers == null)
        {
            mHandlers = new ArrayList<>();
        }
        mHandlers.add(new ChatDetailsHandler());
        return mHandlers;
    }
}
