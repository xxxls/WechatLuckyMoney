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
import com.max.wechatluckymoney.utils.L;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AccessibilityManager.AccessibilityStateChangeListener, SharedPreferences.OnSharedPreferenceChangeListener
{

    private AccessibilityManager mAccessibilityManager;
    private SharedPreferences mSharedPreferences;

    @BindView(R.id.iv_switch)
    ImageView mIvSwitch;
    @BindView(R.id.tv_switch)
    TextView mTvSwitch;

    @Override
    protected void onInitialize()
    {
        initView();
        initData();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_main;
    }

    private void initView()
    {
    }

    private void initData()
    {
        mAccessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        mAccessibilityManager.addAccessibilityStateChangeListener(this);
    }

    @Override
    protected void onResume()
    {
        updateSwitchUIState();
        super.onResume();
    }

    @OnClick({R.id.ll_setting, R.id.ll_switch, R.id.ll_github, R.id.ll_github_star,
            R.id.tv_app_name})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_app_name:
                Toast.makeText(this, R.string.str_blessing, Toast.LENGTH_LONG).show();
                break;

            case R.id.ll_setting:
                startActivity(SettingActivity.getInstance(this));
                break;

            case R.id.ll_switch:
                switchApp();
                break;

            case R.id.ll_github_star:
            case R.id.ll_github:
                startActivity(WebViewActivity.getInstance(this, getString(R.string.str_github_index)
                        , getString(R.string.url_github_index)));
                break;
            default:
        }
    }

    /**
     * 开关
     */
    private void switchApp()
    {
        if (isSwitchApp() && isServiceEnabled())
        {
            //关闭
            getSharedPreferences().edit().putBoolean("switch_app", false).apply();
        } else
        {
            if (isServiceEnabled())
            {
                //关闭
                Toast.makeText(this, "红包助手启动成功.", Toast.LENGTH_LONG).show();
                getSharedPreferences().edit().putBoolean("switch_app", true).apply();
            } else
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
        if (mIvSwitch != null && mTvSwitch != null)
        {
            if (isSwitchApp() && isServiceEnabled())
            {
                mTvSwitch.setText(R.string.str_stop);
                mIvSwitch.setImageResource(R.mipmap.ic_stop);
            } else
            {
                mIvSwitch.setImageResource(R.mipmap.ic_start);
                mTvSwitch.setText(R.string.str_start);
            }
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
        return getSharedPreferences().getBoolean("switch_app", true);
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
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
        return mSharedPreferences;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        updateSwitchUIState();
    }
}
