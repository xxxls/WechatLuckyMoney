package com.max.wechatluckymoney.services.handler.base;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.support.OpenRedPacketDelayTask;
import com.max.wechatluckymoney.utils.ScreenUtils;
import com.max.wechatluckymoney.utils.Utils;

import java.util.regex.Pattern;

/**
 * 聊天详情
 * Created by Max on 2019/1/7.
 */
public abstract class ChatDetailsHandler extends AccessibilityHandler {

    /**
     * 屏幕宽度
     */
    private long mScreenWidth;

    /**
     * 是否群裏 正则
     */
    private Pattern mGroupNamePattern = Pattern.compile("^\\w*\\(\\d+\\)$");

    private OpenRedPacketDelayTask mDelayTask;


    public ChatDetailsHandler(@NonNull AccessibilityHandlerListener listener) {
        super(listener);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    protected long getScreenWidth() {
        if (mScreenWidth == 0) {
            mScreenWidth = ScreenUtils.getScreenWidth(getService());
        }
        return mScreenWidth;
    }

    /**
     * 获取 打开红包 延迟时间
     *
     * @return
     */
    protected int getDelayTime() {
        return getSharedPreferences().getInt("pref_open_delay", 0);
    }

    /**
     * 是否拆自己发的红包
     *
     * @return
     */
    protected boolean isOpenMyRedPaclet() {
        return getSharedPreferences().getBoolean("pref_watch_self", false);
    }

    /**
     * 是否返回首页列表
     *
     * @return
     */
    protected boolean isBackHomeList() {
        return getSharedPreferences().getBoolean("pref_back_list", true);
    }


    /**
     * 是否我发的红包
     *
     * @param rect
     */
    protected boolean isMyRedPacket(Rect rect) {
        if (rect != null) {
            return rect.left > getScreenWidth() - rect.right;
        }
        return false;
    }

    /**
     * 是否群聊
     *
     * @param chatName 聊天对象名称
     * @return
     */
    protected boolean isGroupChat(String chatName) {
        return mGroupNamePattern.matcher(chatName).find();
    }

    /**
     * 获取排除词  包含这些文本的 红包不领
     *
     * @return
     */
    private String[] getRedPacketExcludeWords() {
        return getSharedPreferences().getString("pref_watch_exclude_words", "").split(" ");
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

    /**
     * 是否排除这个红包 根据红包名称
     *
     * @param redPacketText 红包文本
     * @return
     */
    protected boolean isExcludeByRedPacketText(String redPacketText) {
        return Utils.isArrContains(getRedPacketExcludeWords(), redPacketText);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        super.onSharedPreferenceChanged(sharedPreferences, s);

        if (mDelayTask != null) {
            mDelayTask.updateDelayTime(getDelayTime());
        }
    }

    /**
     * 添加 延时打开红包 延时任务
     *
     * @param nodeInfo
     */
    protected void addDelayTask(AccessibilityNodeInfo nodeInfo) {
        if (mDelayTask == null) {
            mDelayTask = new OpenRedPacketDelayTask(mHandler, getDelayTime());
        }

        mDelayTask.addTask(nodeInfo);
    }

}
