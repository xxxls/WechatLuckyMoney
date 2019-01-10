package com.max.wechatluckymoney.activitys;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.base.BaseActivity;
import com.max.wechatluckymoney.services.HandlerHelper;
import com.max.wechatluckymoney.utils.AppUtils;
import com.max.wechatluckymoney.utils.L;
import com.max.wechatluckymoney.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AccessibilityManager.AccessibilityStateChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private AccessibilityManager mAccessibilityManager;
    private SharedPreferences mSharedPreferences;

    @BindView(R.id.iv_switch)
    ImageView mIvSwitch;
    @BindView(R.id.tv_switch)
    TextView mTvSwitch;
    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @Override
    protected void onInitialize() {
        initView();
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initView() {
        mTvVersion.setText("v" + AppUtils.getLocalVersionName(this));
        initPermission();
    }

    private void initData() {
        mAccessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        mAccessibilityManager.addAccessibilityStateChangeListener(this);
    }

    @Override
    protected void onResume() {
        updateSwitchUIState();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initPermission();
    }

    @OnClick({R.id.ll_setting, R.id.ll_switch, R.id.ll_github, R.id.ll_github_star,
            R.id.tv_app_name, R.id.tv_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_app_name:
                Toast.makeText(this, R.string.str_blessing, Toast.LENGTH_LONG).show();
                break;

            case R.id.tv_version:
                checkAdapter();
                break;

            case R.id.ll_setting:
                startActivity(SettingActivity.getInstance(this));
                break;

            case R.id.ll_switch:
                getSharedPreferences().edit().putBoolean("is_first", false).apply();
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
     * 检查适配
     */
    private void checkAdapter() {
        String[] arr = HandlerHelper.getAdapterVersion();
        if (Utils.isArrContains(arr, Utils.getWeChatVersion(this))) {
            Toast.makeText(this, "当前微信版本已适配", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "未适配当前微信版本,敬请期待~", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 开关
     */
    private void switchApp() {
        if (isSwitchApp() && isServiceEnabled()) {
            //关闭
            getSharedPreferences().edit().putBoolean("switch_app", false).apply();
        } else {
            if (isServiceEnabled()) {
                //关闭
                Toast.makeText(this, "红包助手启动成功.", Toast.LENGTH_LONG).show();
                getSharedPreferences().edit().putBoolean("switch_app", true).apply();
            } else {
                jumpAccessibilitySetting();
            }
        }
        updateSwitchUIState();
    }

    /**
     * 跳转 辅助服务 设置页面
     */
    private void jumpAccessibilitySetting() {
        Intent intent = new Intent(
                android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "找到" + getBaseContext().getString(R.string.app_name) + ", 打开即可", Toast.LENGTH_LONG).show();
    }

    /**
     * 更新 switch UI 状态
     */
    private void updateSwitchUIState() {
        if (mIvSwitch != null && mTvSwitch != null) {
            if (isSwitchApp() && isServiceEnabled()) {
                mTvSwitch.setText(R.string.str_stop);
                mIvSwitch.setImageResource(R.mipmap.ic_stop);
            } else {
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
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.services.LuckyMoneyService")) {
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
    private boolean isSwitchApp() {
        return getSharedPreferences().getBoolean("switch_app", true);
    }


    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateSwitchUIState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
        return mSharedPreferences;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateSwitchUIState();
    }

    /**
     * 初始化权限
     */
    private void initPermission() {
        if (!XXPermissions.isHasPermission(this, Permission.SYSTEM_ALERT_WINDOW)) {
            showDeniedDialog();
        } else {
            firstHint();
        }
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        XXPermissions.with(this).permission(Permission.SYSTEM_ALERT_WINDOW).request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean isAll) {
                firstHint();
            }

            @Override
            public void noPermission(List<String> denied, boolean quick) {
                if (quick) {
                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                    showNeverAskDialog();
                } else {
                    //提示获取权限
                    showDeniedDialog();
                }
            }
        });
    }

    /**
     * 第一次提示
     */
    private void firstHint() {
        boolean isFirst = getSharedPreferences().getBoolean("is_first", true);
        if (isFirst) {
            Toast.makeText(MainActivity.this, "点击开启按钮,立即体验使用", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 权限拒绝后
     */
    protected void showDeniedDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.permission_btn_allow, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    checkPermission();
                })
                .setNegativeButton(R.string.permission_btn_exit, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .setCancelable(false)
                .setMessage(R.string.permission_denied)
                .show();
    }

    /**
     * 权限勾选 不再提示后
     */
    protected void showNeverAskDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.permission_btn_setting, (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                    //引导用户至设置页手动授权
                    XXPermissions.gotoPermissionSettings(MainActivity.this);
                })
                .setNegativeButton(R.string.permission_btn_exit, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .setCancelable(false)
                .setMessage(R.string.permission_never_ask_again)
                .show();
    }
}
