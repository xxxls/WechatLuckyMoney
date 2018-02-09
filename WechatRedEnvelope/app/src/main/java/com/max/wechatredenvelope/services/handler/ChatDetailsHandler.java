package com.max.wechatredenvelope.services.handler;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatredenvelope.utils.L;

/**
 * Created by max on 2018/2/9.
 * 聊天详情 处理类
 */
public class ChatDetailsHandler extends BaseAccessibilityHandler
{

    private static final String WECHAT_TEXT_SEE_RE = "查看红包";
    private static final String WECHAT_TEXT_GET_RE= "领取红包";


    @Override
    public boolean onHandler(AccessibilityNodeInfo nodeInfo)
    {

        L.e("getClassName = "+nodeInfo.getClassName().toString());
        L.e("getPackageName = "+nodeInfo.getPackageName().toString());
        L.e("getViewIdResourceName = "+nodeInfo.getViewIdResourceName());


//        AccessibilityNodeInfo nodes = getNodeListByText(nodeInfo,WECHAT_TEXT_GET_RE);


        return false;
    }
}
