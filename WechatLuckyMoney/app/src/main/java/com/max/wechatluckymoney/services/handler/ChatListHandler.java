package com.max.wechatluckymoney.services.handler;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;

/**
 * Created by max on 2018/2/10.
 * 聊天列表 处理类
 */
public class ChatListHandler extends BaseAccessibilityHandler
{
    private static final String WECHAT_ACTIVITY_CHAT_LIST = "LauncherUI";

    private static final String WECHAT_TEXT_LUCKY_MONEY = "[微信红包]";

    public ChatListHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }

    @Override
    public boolean onHandler()
    {
        if (getClassName().contains(WECHAT_ACTIVITY_CHAT_LIST))
        {
            AccessibilityNodeInfo node = getTheLastNodeByTexts(getRootNode(), WECHAT_TEXT_LUCKY_MONEY);

            if (node != null)
            {
                node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
        }

        return false;
    }
}
