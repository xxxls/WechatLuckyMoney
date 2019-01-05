package com.max.wechatluckymoney.app;

import android.app.Application;

import com.max.wechatluckymoney.utils.DebugUtils;

/**
 * Created by Max on 2019/1/5.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //是否debug 初始化
        DebugUtils.syncIsDebug(this);
    }
}
