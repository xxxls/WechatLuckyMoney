package com.max.wechatluckymoney.activitys;


import android.content.Context;
import android.content.Intent;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.base.BaseActivity;

/**
 * Created by Max on 2018/2/10.
 * 微信对 红包弹窗做了处理
 * 暂时想到这个方法破解
 */

public class LoadingActivity extends BaseActivity
{

    public static Intent getInstance(Context context)
    {
        return new Intent(context, LoadingActivity.class);
    }

    @Override
    protected void onInitialize()
    {
        finish();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_loading;
    }
}
