package com.max.wechatluckymoney.services.handler.base;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;

/**
 * 首页聊天界面
 * Created by Max on 2019/1/7.
 */
public abstract class HomeChatHandler extends AccessibilityHandler {

    /**
     * 是否监视聊天列表
     */
    private Boolean mSwitchChatList;


    protected static final String WX_TEXT_LUCKY_MONEY = "[微信红包]";

    public HomeChatHandler(@NonNull AccessibilityHandlerListener listener) {
        super(listener);
    }

    /**
     * 是否监视聊天列表
     *
     * @return
     */
    protected boolean isChatListSwitch() {
        if (mSwitchChatList == null) {
            mSwitchChatList = mListener.getSharedPreferences().getBoolean("pref_watch_list", true);
        }
        return mSwitchChatList;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        super.onSharedPreferenceChanged(sharedPreferences, s);
        mSwitchChatList = mListener.getSharedPreferences().getBoolean("pref_watch_list", true);
    }
}
