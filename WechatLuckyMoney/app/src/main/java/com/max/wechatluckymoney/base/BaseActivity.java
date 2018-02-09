package com.max.wechatluckymoney.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by max on 2018/2/9.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(getLayoutResId());
            onInitialize();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy()
    {
        ButterKnife.unbind(this);
        super.onDestroy();
    }


    /**
     * 初始化
     */
    protected abstract void onInitialize();

    /**
     * 布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutResId();
}
