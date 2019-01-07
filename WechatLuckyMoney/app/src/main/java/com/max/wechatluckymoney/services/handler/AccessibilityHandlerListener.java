package com.max.wechatluckymoney.services.handler;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by max on 2018/2/9.
 */
public interface AccessibilityHandlerListener {

    /**
     * 获取 service
     *
     * @return
     */
    AccessibilityService getAccessibilityService();

    /**
     * 获取 event
     *
     * @return
     */
    AccessibilityEvent getAccessibilityEvent();

    /**
     * 获取SharedPreferences
     *
     * @return
     */
    SharedPreferences getSharedPreferences();


    /**
     * 当前页面 类名
     *
     * @return
     */
    String getCurActivityClassName();
}
