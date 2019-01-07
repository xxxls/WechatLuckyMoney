package com.max.wechatluckymoney.services.handler.seven_zero_zero;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.base.HomeChatHandler;
import com.max.wechatluckymoney.utils.AccessibilityNodeUtils;

import java.util.List;

/**
 * Created by max on 2019/1/7.
 * 聊天列表 处理类
 */
public class SzzHomeChatHandler extends HomeChatHandler {

    /**
     * 聊天列表 的 消息内容ID
     */
    private static final String WX_ID_LUCKY_MONEY = "com.tencent.mm:id/b4q";

    public SzzHomeChatHandler(AccessibilityHandlerListener listener) {
        super(listener);
    }

    @Override
    public boolean onHandler() {

        if (!isChatListSwitch()) {
            return false;
        }

        List<AccessibilityNodeInfo> nodes = AccessibilityNodeUtils.findNodeListById(getRootNode(), WX_ID_LUCKY_MONEY);
        if (nodes == null) {
            nodes = AccessibilityNodeUtils.findNodeListById(getRootNode(), WX_ID_LUCKY_MONEY);
        }

        if (nodes != null) {
            for (AccessibilityNodeInfo nodeInfo : nodes) {
                if (nodeInfo != null && nodeInfo.getText() != null) {
                    if (nodeInfo.getText().toString().contains(WX_TEXT_LUCKY_MONEY)) {
                        toast("发现红包");
                        performClick(nodeInfo.getParent());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected String getInterceptActivityName() {
        return "LauncherUI";
    }

}
