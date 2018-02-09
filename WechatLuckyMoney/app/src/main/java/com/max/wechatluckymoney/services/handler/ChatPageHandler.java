package com.max.wechatluckymoney.services.handler;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;

/**
 * Created by max on 2018/2/9.
 * 聊天界面 处理类
 */
public class ChatPageHandler extends BaseAccessibilityHandler
{

    private static final String WECHAT_TEXT_SEE_LM = "查看红包";
    private static final String WECHAT_TEXT_GET_LM= "领取红包";

    public ChatPageHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }


    @Override
    public boolean onHandler(AccessibilityNodeInfo nodeInfo)
    {

        AccessibilityNodeInfo node = getTheLastNodeByTexts(nodeInfo,WECHAT_TEXT_SEE_LM,WECHAT_TEXT_GET_LM);

        if (node!=null)
        {
            node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return true;
        }

        return false;
    }
}
