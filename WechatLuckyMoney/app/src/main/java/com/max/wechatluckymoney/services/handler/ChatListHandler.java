package com.max.wechatluckymoney.services.handler;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;

import java.util.List;

/**
 * Created by max on 2018/2/10.
 * 聊天列表 处理类
 */
public class ChatListHandler extends BaseAccessibilityHandler
{
    //聊天列表 className
    private static final String WECHAT_ACTIVITY_CHAT_LIST = "LauncherUI";

    //listview
    private static final String WECHAT_VIEW_LISTVIEW = "ListView";

    //聊天列表 的 消息内容ID
    private static final String WECHAT_ID_LUCKY_MONEY = "com.tencent.mm:id/apv";

    private static final String WECHAT_TEXT_LUCKY_MONEY = "[微信红包]";

    public ChatListHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }

    @Override
    public boolean onHandler()
    {
        String name = getClassName();

        log("name: " + name);

        if (name.contains(WECHAT_ACTIVITY_CHAT_LIST) || name.contains(WECHAT_VIEW_LISTVIEW))
        {

            //开关
            boolean switchList = mListener.getSharedPreferences().getBoolean("pref_watch_list", true);

            if (! switchList)
            {
                return false;
            }

            List<AccessibilityNodeInfo> nodes = findNodeListById(getRootNode(), WECHAT_ID_LUCKY_MONEY);

            if (nodes != null)
            {

                for (AccessibilityNodeInfo nodeInfo : nodes)
                {
                    if (nodeInfo != null && nodeInfo.getText() != null)
                    {
                        if (nodeInfo.getText().toString().contains(WECHAT_TEXT_LUCKY_MONEY))
                        {
                            log("TEXT：" + nodeInfo.getText().toString());
                            toast("发现红包了");
                            nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
