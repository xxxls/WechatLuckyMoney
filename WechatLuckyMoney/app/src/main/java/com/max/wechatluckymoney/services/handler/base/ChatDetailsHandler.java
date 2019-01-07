package com.max.wechatluckymoney.services.handler.base;

import android.support.annotation.NonNull;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;

/**
 * 聊天详情
 * Created by Max on 2019/1/7.
 */
public abstract class ChatDetailsHandler extends AccessibilityHandler {

    public ChatDetailsHandler(@NonNull AccessibilityHandlerListener listener) {
        super(listener);
    }

}
