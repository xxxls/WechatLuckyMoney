package com.max.wechatluckymoney.base;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by max on 2018/2/9.
 */
public interface OnAccessibilityHandlerListener
{

    /**
     * 获取 service
     * @return
     */
    AccessibilityService getAccessibilityService();

    /**
     * 当前活动页面名称
     * @return
     */
    String getCurrentActivityName();

    /**
     * 获取 event
     * @return
     */
    AccessibilityEvent getAccessibilityEvent();
}
