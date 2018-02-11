package com.max.wechatluckymoney.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.activitys.MainActivity;
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


    private WindowManager mWindowManager;

    //悬浮View
    private View mViewFloating;


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


    /**
     * 初始化 开关view
     */
    private void initSwitchView()
    {

    }


    private int mWinW, mWinH, mStartX, mStartY;
    private long mFirstTime, mDoubleTime;

    /**
     * 是否 show 悬浮按钮
     * @return
     */
    private boolean isShowFloating()
    {
        return getSharedPreferences().getBoolean("switch_float", true);
    }

    /**
     * 展现 悬浮菜单
     */
    private void showFloatingMenu()
    {
        if (mWindowManager == null)
        {
            mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        mWinW = mWindowManager.getDefaultDisplay().getWidth();
        mWinH = mWindowManager.getDefaultDisplay().getHeight();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.gravity = Gravity.RIGHT + Gravity.TOP;// 将重心位置设置为右上方,
        if (mViewFloating == null)
        {
            mViewFloating = View.inflate(this, R.layout.layout_floatingmenu, null);
            mLayoutSwitch = (LinearLayout) mViewFloating.findViewById(R.id.ll_switch);
            mTvSwitch = (TextView) mViewFloating.findViewById(R.id.tv_switch);
        }
        params.y = mWinH / 6;
        mWm.addView(mViewFloating, params);
        mViewFloating.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        mFirstTime = System.currentTimeMillis();
                        mStartX = (int) event.getRawX();
                        mStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endx = (int) event.getRawX();
                        int endy = (int) event.getRawY();

                        int dx = mStartX - endx;
                        int dy = endy - mStartY;

                        params.x += dx;
                        params.y += dy;

                        if (params.x > mWinW - v.getWidth())
                        {
                            params.x = mWinW - v.getWidth();
                        }
                        if (params.y > mWinH - v.getHeight())
                        {
                            params.y = mWinH - v.getHeight();
                        }
                        mWm.updateViewLayout(v, params);

                        mStartX = (int) event.getRawX();
                        mStartY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        long time = System.currentTimeMillis();
                        if (time - mFirstTime <= 150)
                        {  //单击
                            if (time - mDoubleTime <= 300)
                            {//双击 进入设置
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getBaseContext().startActivity(intent);
                                mState = false; // 强制改为 暂停状态
                                changeState();
                                return true;
                            }
                            mDoubleTime = time;
                            mState = ! mState;
                            changeState();
                        }
                        break;
                }
                return true;
            }
        });
    }
}
