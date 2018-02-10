package com.max.wechatluckymoney.activitys;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.fragments.GeneralSettingsFragment;

/**
 * Created by max on 2018/2/9.
 * 设置
 */
public class SettingActivity extends AppCompatActivity
{

    public static Intent getInstance(Context context)
    {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        init();
        loadUI();
    }

    private void init()
    {

        TextView textView = (TextView) findViewById(R.id.settings_bar);
        textView.setText(R.string.preference);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.preferences_fragment, GeneralSettingsFragment.getInstance());
        fragmentTransaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void loadUI()
    {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(getResources().getColor(R.color.theme));
    }

    public void jumpAccessibilityPage(View view)
    {
        Intent mAccessibleIntent =
                new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(mAccessibleIntent);
    }

}
