package com.max.wechatluckymoney.activitys;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.max.wechatluckymoney.R;

/**
 * Created by Max on 2018/2/10.
 * 加载进度
 */

public class LoadingActivity extends AppCompatActivity
{

    public static Intent getInstance(Context context)
    {
        return new Intent(context, LoadingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(() -> finish(), 100);
    }
}
