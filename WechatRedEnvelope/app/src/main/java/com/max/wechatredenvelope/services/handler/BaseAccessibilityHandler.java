package com.max.wechatredenvelope.services.handler;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by max on 2018/2/9.
 * 辅助服务
 */
public abstract class BaseAccessibilityHandler
{


    /**
     * 处理类
     *
     * @return
     */
    public abstract boolean onHandler(AccessibilityNodeInfo nodeInfo);


    protected List<AccessibilityNodeInfo> getNodeListById(AccessibilityNodeInfo nodeInfo, String viewId)
    {
        return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
    }


    protected List<AccessibilityNodeInfo> getNodeListByText(AccessibilityNodeInfo nodeInfo, String text)
    {
        return nodeInfo.findAccessibilityNodeInfosByText(text);
    }
}
