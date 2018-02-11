package com.max.wechatluckymoney.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.ChatDetailsHandler;
import com.max.wechatluckymoney.services.handler.ChatListHandler;
import com.max.wechatluckymoney.services.handler.LuckyMoneyDetailsHandler;
import com.max.wechatluckymoney.services.handler.LuckyMoneyReceiveHandler;
import com.max.wechatluckymoney.utils.L;

import java.util.ArrayList;

/**
 * Created by max on 2018/2/9.
 * 红包 辅助服务
 */
public class LuckyMoneyService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener, OnAccessibilityHandlerListener
{

    private static final String WECHAT_ACTIVITY_GENERAL = "LauncherUI";

    //处理类
    private ArrayList<BaseAccessibilityHandler> mHandlers;

    //event
    private AccessibilityEvent mEvent;

    //配置
    private SharedPreferences mSharedPreferences;


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
        L.e("onAccessibilityEvent() -> ");
        L.e("onAccessibilityEvent() -> ClassName : " + event.getClassName());
        L.e("onAccessibilityEvent() -> toString() : " + event.toString());

        mEvent = event;

        if (event.getSource() == null)
        {
            return;
        }

        if (isHandler())
        {
            onHandler();
        }
    }


    /**
     * 是否处理
     *
     * @return
     */
    private boolean isHandler()
    {
        //开关
        return getSharedPreferences().getBoolean("switch_app", true);
    }

    /**
     * 处理
     */
    private void onHandler()
    {
        for (BaseAccessibilityHandler handler : getHandlers())
        {
            if (handler.onExecute())
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
        L.e("onInterrupt() ->");
    }

    /**
     * 按键事件
     *
     * @param event
     * @return
     */
    @Override
    protected boolean onKeyEvent(KeyEvent event)
    {
        return super.onKeyEvent(event);
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
            mHandlers.add(new ChatListHandler(this));
            mHandlers.add(new ChatDetailsHandler(this));
            mHandlers.add(new LuckyMoneyReceiveHandler(this));
            mHandlers.add(new LuckyMoneyDetailsHandler(this));
        }
    }


    @Override
    public AccessibilityService getAccessibilityService()
    {
        return this;
    }

    @Override
    public AccessibilityEvent getAccessibilityEvent()
    {
        return mEvent;
    }

    @Override
    public SharedPreferences getSharedPreferences()
    {
        if (mSharedPreferences == null)
        {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
        return mSharedPreferences;
    }


}
