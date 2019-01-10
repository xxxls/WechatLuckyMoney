package com.max.wechatluckymoney.app;

import android.app.Application;

import com.max.wechatluckymoney.utils.DebugUtils;
import com.max.wechatluckymoney.utils.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Max on 2019/1/5.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //是否debug 初始化
        DebugUtils.syncIsDebug(this);

        Bugly.init(getApplicationContext(), "4e99088771", false);
        Bugly.setAppChannel(this, "wechat_version" + Utils.getWeChatVersion(this));
    }
}
