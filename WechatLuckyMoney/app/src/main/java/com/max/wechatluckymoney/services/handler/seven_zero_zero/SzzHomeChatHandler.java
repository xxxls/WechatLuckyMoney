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
     * 聊天列表 Item
     */
    private static final String WX_ID_CHAT_ITEM = "com.tencent.mm:id/b4m";

    /**
     * 聊天列表 的 聊天对象
     */
    private static final String WX_ID_CHAT_ITEM_NAME = "com.tencent.mm:id/b4o";

    /**
     * 聊天列表 的 消息内容ID
     */
    private static final String WX_ID_CHAT_ITEM_CONTENT = "com.tencent.mm:id/b4q";

    public SzzHomeChatHandler(AccessibilityHandlerListener listener) {
        super(listener);
    }

    @Override
    public boolean onHandler() {

        if (!isChatListSwitch()) {
            return false;
        }

        List<AccessibilityNodeInfo> itemNodes = AccessibilityNodeUtils.findNodeListById(getRootNode(), WX_ID_CHAT_ITEM);

        if (itemNodes != null) {
            for (AccessibilityNodeInfo nodeInfo : itemNodes) {

                AccessibilityNodeInfo chatContentNode = AccessibilityNodeUtils.getFirstNodeById(nodeInfo, WX_ID_CHAT_ITEM_CONTENT);
                if (chatContentNode != null && chatContentNode.getText() != null) {
                    if (chatContentNode.getText().toString().contains(WX_TEXT_LUCKY_MONEY)) {

                        AccessibilityNodeInfo chatNameNode = AccessibilityNodeUtils.getFirstNodeById(nodeInfo, WX_ID_CHAT_ITEM_NAME);
                        if (chatNameNode != null && chatNameNode.getText() != null) {
                            if (isExcludeByChatName(chatNameNode.getText().toString())) {
                                continue;
                            }
                        }

                        toast("发现红包");
                        performClick(nodeInfo);
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
