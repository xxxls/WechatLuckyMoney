package com.max.wechatluckymoney.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.base.BaseActivity;
import com.max.wechatluckymoney.fragments.GeneralSettingsFragment;

/**
 * Created by max on 2018/2/9.
 * 设置
 */
public class SettingActivity extends BaseActivity
{

    public static Intent getInstance(Context context)
    {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }


    @Override
    protected void onInitialize()
    {
        setStatusBarColor(R.color.theme);
        initView();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_preferences;
    }

    private void initView()
    {

        TextView textView = (TextView) findViewById(R.id.settings_bar);
        textView.setText(R.string.preference);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.preferences_fragment, GeneralSettingsFragment.getInstance());
        fragmentTransaction.commit();
    }


    public void jumpAccessibilityPage(View view)
    {
        Intent mAccessibleIntent =
                new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(mAccessibleIntent);
    }

}
