package com.max.wechatluckymoney.activitys;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AccessibilityManager.AccessibilityStateChangeListener
{

    private AccessibilityManager mAccessibilityManager;

    @Bind(R.id.btn_state)
    Button mBtnState;

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
        updateState();
        super.onResume();
    }

    @OnClick({R.id.btn_state,R.id.btn_setting})
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.btn_setting:
                startActivity(SettingActivity.getInstance(this));
                break;
            case R.id.btn_state:
                jumpAccessibilitySetting();
                break;
        }
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
     * 更新 状态
     */
    private void updateState()
    {

        if (mBtnState == null)
        {
            return;
        }

        if (isServiceEnabled())
        {
            mBtnState.setText("正在运行");
        } else
        {
            mBtnState.setText("启动");
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

    @Override
    public void onAccessibilityStateChanged(boolean enabled)
    {
        updateState();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
