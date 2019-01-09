package com.max.wechatluckymoney.services.handler.base;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.utils.Utils;

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
     * 排除词  不打开这些文字的红包
     */
    private String[] mRedPacketExcludeWords;

    /**
     * 是否 自动拆红包
     * @return
     */
    protected Boolean isAutoOpen() {
        if (mIsAutoOpenRedPacket == null) {
            mIsAutoOpenRedPacket = getSharedPreferences().getBoolean("pref_auto_open", true);
        }
        return mIsAutoOpenRedPacket;
    }

    public RedPacketGetHandler(@NonNull AccessibilityHandlerListener listener) {
        super(listener);
    }

    /**
     * 获取排除词  包含这些文本的 红包不领
     *
     * @return
     */
    private String[] getRedPacketExcludeWords() {
        if (mRedPacketExcludeWords == null) {
            mRedPacketExcludeWords = getSharedPreferences().getString("pref_watch_exclude_words", "").split(" ");
        }
        return mRedPacketExcludeWords;
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
        mIsAutoOpenRedPacket = getSharedPreferences().getBoolean("pref_auto_open", true);
        mRedPacketExcludeWords = getSharedPreferences().getString("pref_watch_exclude_words", "").split(" ");
    }
}
