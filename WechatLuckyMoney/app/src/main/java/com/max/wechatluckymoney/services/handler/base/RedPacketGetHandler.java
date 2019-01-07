package com.max.wechatluckymoney.services.handler.base;

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


    public RedPacketGetHandler(@NonNull AccessibilityHandlerListener listener) {
        super(listener);
    }
}
