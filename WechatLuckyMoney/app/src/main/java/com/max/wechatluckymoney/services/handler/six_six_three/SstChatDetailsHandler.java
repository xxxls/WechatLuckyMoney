package com.max.wechatluckymoney.services.handler.six_six_three;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.base.ChatDetailsHandler;
import com.max.wechatluckymoney.utils.AccessibilityNodeUtils;

/**
 * Created by max on 2018/2/9.
 * 聊天界面 处理类
 */
public class SstChatDetailsHandler extends ChatDetailsHandler
{
    //聊天界面className
    private static final String WECHAT_ACTIVITY_CHAT_DEATILS = "LauncherUI";

    //listview
    private static final String WECHAT_VIEW_LISTVIEW = "ListView";

    private static final String WECHAT_TEXT_GET_LM = "领取红包";

    public SstChatDetailsHandler(AccessibilityHandlerListener listener)
    {
        super(listener);
    }


    @Override
    public boolean onHandler()
    {
        String name = getClassName();

        if (name.contains(WECHAT_ACTIVITY_CHAT_DEATILS) || name.contains(WECHAT_VIEW_LISTVIEW))
        {
            AccessibilityNodeInfo node = AccessibilityNodeUtils.getTheLastNodeByTexts(getRootNode(), WECHAT_TEXT_GET_LM);

            if (node != null)
            {
                if (node.getParent() != null)
                {

                    Rect rectScreen = new Rect();
                    node.getParent().getBoundsInScreen(rectScreen);
                    //是我的红包？
                    boolean isMyRedPacket = isMyRedPacket(rectScreen);

                    if (isMyRedPacket)
                    {
                        return false;
                    }
                    performClick(node.getParent());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getInterceptActivityName()
    {
        return "LauncherUI";
    }
}
