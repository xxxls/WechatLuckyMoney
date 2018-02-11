package com.max.wechatluckymoney.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

/**
 * Created by max on 2018/2/9.
 * 红包 辅助服务
 */
public class LuckyMoneyService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener, OnAccessibilityHandlerListener, View.OnTouchListener
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
        initSwitchView();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {

        if (event == null)
        {
            return;
        }

        if (event.getSource() == null)
        {
            return;
        }

        mEvent = event;

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

        if (mWindowManager != null)
        {
            mWindowManager = null;
        }

        if (mViewFloating != null)
        {
            mViewFloating = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        showFloatingMenu();
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

    /**
     * 初始化 处理类
     */
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

    /**
     * 初始化 开关view
     */
    private void initSwitchView()
    {
        showFloatingMenu();
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

    private int mWinW, mWinH, mStartX, mStartY;
    private long mFirstTime, mDoubleTime;
    private WindowManager.LayoutParams mParams;

    /**
     * 是否 show 悬浮按钮
     *
     * @return
     */
    private boolean isFloatingEnabled()
    {
        return getSharedPreferences().getBoolean("switch_float", true);
    }

    /**
     * 展现 悬浮菜单
     */
    private void showFloatingMenu()
    {
        if (! isFloatingEnabled())
        {
            if (mWindowManager != null && mViewFloating != null)
            {
                mWindowManager.removeView(mViewFloating);
                mViewFloating = null;
                mWindowManager = null;
            }
            return;
        }

        if (mWindowManager == null)
        {
            mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
            mParams = new WindowManager.LayoutParams();
            mWinW = mWindowManager.getDefaultDisplay().getWidth();
            mWinH = mWindowManager.getDefaultDisplay().getHeight();
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mParams.gravity = Gravity.RIGHT + Gravity.TOP;// 将重心位置设置为右上方,
            if (mViewFloating == null)
            {
                mViewFloating = View.inflate(this, R.layout.layout_floating, null);
            }
            mParams.y = mWinH / 6;
            mWindowManager.addView(mViewFloating, mParams);
            mViewFloating.setOnTouchListener(this);
        }

        //改变状态
        changeState(! isHandler());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (mWindowManager == null)
        {
            return false;
        }

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

                mParams.x += dx;
                mParams.y += dy;

                if (mParams.x > mWinW - v.getWidth())
                {
                    mParams.x = mWinW - v.getWidth();
                }
                if (mParams.y > mWinH - v.getHeight())
                {
                    mParams.y = mWinH - v.getHeight();
                }
                mWindowManager.updateViewLayout(v, mParams);

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
                        return true;
                    }
                    mDoubleTime = time;
                    setSwitch(! isHandler());
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 改变状态显示
     */
    public void changeState(boolean state)
    {
        if (mViewFloating == null)
        {
            return;
        }

        LinearLayout llSwitch = (LinearLayout) mViewFloating.findViewById(R.id.ll_switch);
        TextView tvSwitch = (TextView) mViewFloating.findViewById(R.id.tv_switch);
        if (state)
        {
            llSwitch.setBackgroundResource(R.drawable.bg_circle_switch_on);
            tvSwitch.setTextColor(Color.BLACK);
            tvSwitch.setText("开启");

        } else
        {
            llSwitch.setBackgroundResource(R.drawable.bg_circle_switch_off);
            tvSwitch.setText("关闭");
            tvSwitch.setTextColor(Color.WHITE);
        }
    }

    /**
     * 设置开关
     *
     * @param sw
     */
    private void setSwitch(boolean sw)
    {

        if (sw && ! isServiceEnabled())
        {
            jumpAccessibilitySetting();
        }
        getSharedPreferences().edit().putBoolean("switch_app", sw).commit();
    }


    /**
     * 获取 Service 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled()
    {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices)
        {
            if (info.getId().equals(getPackageName() + "/.services.LuckyMoneyService"))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * 跳转 辅助服务 设置页面
     */
    private void jumpAccessibilitySetting()
    {
        Intent intent = new Intent(
                android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "找到" + getBaseContext().getString(R.string.app_name) + ", 打开即可", Toast.LENGTH_LONG).show();
    }

}
