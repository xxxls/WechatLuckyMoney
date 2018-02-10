package com.max.wechatluckymoney.services.handler;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;

/**
 * Created by max on 2018/2/9.
 * 聊天界面 处理类
 */
public class ChatDetailsHandler extends BaseAccessibilityHandler
{
    //聊天界面className
    private static final String WECHAT_ACTIVITY_CHAT_DEATILS = "LauncherUI";

    //listview
    private static final String WECHAT_VIEW_LISTVIEW = "ListView";

    private static final String WECHAT_TEXT_SEE_LM = "查看红包";
    private static final String WECHAT_TEXT_GET_LM = "领取红包";

    public ChatDetailsHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }


    @Override
    public boolean onHandler()
    {
        String name = getClassName();

        log("name: " + name);

        if (name.contains(WECHAT_ACTIVITY_CHAT_DEATILS) || name.contains(WECHAT_VIEW_LISTVIEW))
        {
            AccessibilityNodeInfo node = getTheLastNodeByTexts(getRootNode(), WECHAT_TEXT_GET_LM);

            if (node != null)
            {
                toast("开始领红包");
                if (node.getParent() != null)
                {
                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                return true;
            }
        }
        return false;
    }
}
