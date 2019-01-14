package com.max.wechatluckymoney.services.handler.base;

import android.support.annotation.NonNull;

import com.max.wechatluckymoney.activitys.LoadingActivity;
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

    private Runnable mRunnableLifecycle = () -> {
        //红包页面 生命周期的改变 才能正常获取有用信息
        //暂时这样解决
        getService().startActivity(LoadingActivity.getInstance(getService()));
    };

    /**
     * 是否 自动拆红包
     *
     * @return
     */
    protected Boolean isAutoOpen() {
        return getSharedPreferences().getBoolean("pref_auto_open", true);
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
        return getSharedPreferences().getString("pref_watch_exclude_words", "").split(" ");
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

    /**
     * 延时 改变生命周期
     */
    protected void delayedChangeLifecycle() {
        startDelayedTask(mRunnableLifecycle, 200);
    }
}
