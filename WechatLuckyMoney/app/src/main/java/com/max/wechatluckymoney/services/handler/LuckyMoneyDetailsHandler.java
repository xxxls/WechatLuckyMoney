package com.max.wechatluckymoney.services.handler;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;
import com.max.wechatluckymoney.support.enums.WidgetType;

/**
 * Created by max on 2018/2/9.
 * 红包详情 处理类
 */
public class LuckyMoneyDetailsHandler extends BaseAccessibilityHandler
{
    /**
     * 红包详情页
     */
    public static final String WECHAT_ACTIVITY_LMD = "LuckyMoneyDetailUI";

    private static final String WECHAT_TEXT_SLOW = "手慢了，红包派完了";
    private static final String WECHAT_TEXT_OUT_OF_DATE = "已超过24小时";
    private static final String WECHAT_TEXT_DETAILS_OF_RE = "红包详情";

    public LuckyMoneyDetailsHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }

    @Override
    public boolean onHandler(AccessibilityNodeInfo nodeInfo)
    {

        if (hasOneOfThoseNodesByTexts(nodeInfo, WECHAT_TEXT_SLOW, WECHAT_TEXT_OUT_OF_DATE, WECHAT_TEXT_DETAILS_OF_RE))
        {
            //已经领完了 或者过期
            getService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            return true;
        } else
        {
            AccessibilityNodeInfo node = findOneNodeByViewTag(nodeInfo, WidgetType.Button.getContent());
            if (node != null)
            {
                //还没有领
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
        }
        return false;
    }
}
