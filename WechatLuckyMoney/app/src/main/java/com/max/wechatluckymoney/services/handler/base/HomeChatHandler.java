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
        return getSharedPreferences().getBoolean("pref_watch_list", true);
    }

    /**
     * 获取排除词  包含这些文本的 红包不领
     *
     * @return
     */
    private String[] getChatExcludeWords() {
        return getSharedPreferences().getString("pref_watch_exclude_words_chat", "").split(" ");
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
}
