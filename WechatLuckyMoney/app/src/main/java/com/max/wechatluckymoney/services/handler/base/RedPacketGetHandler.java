package com.max.wechatluckymoney.services.handler.base;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;

/**
 * 红包领取 （红包弹窗）
 * Created by Max on 2019/1/7.
 */
public abstract class RedPacketGetHandler extends AccessibilityHandler {

    protected static final String WX_TEXT_SLOW = "手慢了";
    protected static final String WX_TEXT_OUT_OF_DATE = "已超过24小时";

    /**
     * 自动打开红包
     */
    private Boolean mIsAutoOpenRedPacket;

    /**
     * 是否 自动拆红包
     * @return
     */
    public Boolean isAutoOpen() {
        if (mIsAutoOpenRedPacket == null) {
            mIsAutoOpenRedPacket = getSharedPreferences().getBoolean("pref_auto_open", true);
        }
        return mIsAutoOpenRedPacket;
    }

    public RedPacketGetHandler(@NonNull AccessibilityHandlerListener listener) {
        super(listener);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        super.onSharedPreferenceChanged(sharedPreferences, s);
        mIsAutoOpenRedPacket = getSharedPreferences().getBoolean("pref_auto_open", true);
    }
}
