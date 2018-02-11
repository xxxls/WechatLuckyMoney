package com.max.wechatluckymoney.activitys;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AccessibilityManager.AccessibilityStateChangeListener
{

    private AccessibilityManager mAccessibilityManager;
    private SharedPreferences mSharedPreferences;

    @Bind(R.id.iv_switch)
    ImageView mIvSwitch;
    @Bind(R.id.tv_switch)
    TextView mTvSwitch;

    @Override
    protected void onInitialize()
    {
        initView();
        initData();
        initEvnet();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_main;
    }

    private void initView()
    {
        setStatusBarColor(R.color.theme);
    }

    private void initData()
    {
        mAccessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        mAccessibilityManager.addAccessibilityStateChangeListener(this);
    }

    private void initEvnet()
    {

    }

    @Override
    protected void onResume()
    {
        updateSwitchUIState();
        super.onResume();
    }

    @OnClick({R.id.ll_setting, R.id.ll_switch})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_setting:
                startActivity(SettingActivity.getInstance(this));
                break;
            case R.id.ll_switch:
                switchApp();
                break;
            default:
        }
    }

    /**
     * 开关
     */
    private void switchApp()
    {
        if (isSwitchApp())
        {
            //关闭
            getSharedPreferences().edit().putBoolean("switch_app", false).commit();
        } else
        {
            //打开
            getSharedPreferences().edit().putBoolean("switch_app", true).commit();
            if (! isServiceEnabled())
            {
                jumpAccessibilitySetting();
            }
        }

        updateSwitchUIState();
    }

    /**
     * 跳转 辅助服务 设置页面
     */
    private void jumpAccessibilitySetting()
    {
        Intent intent = new Intent(
                android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "找到" + getBaseContext().getString(R.string.app_name) + ", 打开即可", Toast.LENGTH_LONG).show();
    }

    /**
     * 更新 switch UI 状态
     */
    private void updateSwitchUIState()
    {
        if (isSwitchApp())
        {
            if (isServiceEnabled())
            {
                mTvSwitch.setText(R.string.str_stop);
                mIvSwitch.setImageResource(R.mipmap.ic_stop);
            } else
            {
                mIvSwitch.setImageResource(R.mipmap.ic_start);
                mTvSwitch.setText(R.string.str_start);
            }
        } else
        {
            mIvSwitch.setImageResource(R.mipmap.ic_start);
            mTvSwitch.setText(R.string.str_start);
        }
    }


    /**
     * 获取 Service 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled()
    {
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
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
     * 是否启动APP
     *
     * @return
     */
    private boolean isSwitchApp()
    {
        return getSharedPreferences().getBoolean("switch_app", false);
    }


    @Override
    public void onAccessibilityStateChanged(boolean enabled)
    {
        updateSwitchUIState();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public SharedPreferences getSharedPreferences()
    {
        if (mSharedPreferences == null)
        {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return mSharedPreferences;
    }

}
