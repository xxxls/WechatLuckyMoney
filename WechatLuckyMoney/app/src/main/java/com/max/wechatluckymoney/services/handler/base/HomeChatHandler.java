package com.max.wechatluckymoney.services.handler.base;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.utils.Utils;

/**
 * 首页聊天界面
 * Created by Max on 2019/1/7.
 */
public abstract class HomeChatHandler extends AccessibilityHandler {

    /**
     * 是否监视聊天列表
     */
    private Boolean mSwitchChatList;

    /**
     * 排除词  不打开这些人 或群发的红包
     */
    private String[] mChatExcludeWords;

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
            mSwitchChatList = getSharedPreferences().getBoolean("pref_watch_list", true);
        }
        return mSwitchChatList;
    }

    /**
     * 获取排除词  包含这些文本的 红包不领
     *
     * @return
     */
    private String[] getChatExcludeWords() {
        if (mChatExcludeWords == null) {
            mChatExcludeWords = getSharedPreferences().getString("pref_watch_exclude_words_chat", "").split(" ");
        }
        return mChatExcludeWords;
    }

    /**
     * 是否排除这个红包 根据聊天对象名
     *
     * @param chatName 聊天对象
     * @return
     */
    protected boolean isExcludeByChatName(String chatName) {
        return Utils.isArrContains(getChatExcludeWords(), chatName);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        super.onSharedPreferenceChanged(sharedPreferences, s);
        mSwitchChatList = getSharedPreferences().getBoolean("pref_watch_list", true);
        mChatExcludeWords = getSharedPreferences().getString("pref_watch_exclude_words_chat", "").split(" ");
    }
}
