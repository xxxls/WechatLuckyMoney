package com.max.wechatluckymoney.services.handler.base;

import android.support.annotation.NonNull;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;

/**
 * 红包领取记录
 * Created by Max on 2019/1/7.
 */
public abstract class RedPacketGetRecordHandler extends AccessibilityHandler {
    public RedPacketGetRecordHandler(@NonNull AccessibilityHandlerListener listener) {
        super(listener);
    }
}
