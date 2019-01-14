package com.max.wechatluckymoney.services.handler.six_six_three;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.activitys.LoadingActivity;
import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.base.RedPacketGetHandler;
import com.max.wechatluckymoney.support.enums.WidgetType;
import com.max.wechatluckymoney.utils.AccessibilityNodeUtils;

/**
 * Created by max on 2018/2/9.
 * 领取红包详情 处理类
 */
public class SstRedPacketGetHandler extends RedPacketGetHandler {

    public SstRedPacketGetHandler(AccessibilityHandlerListener listener) {
        super(listener);
    }

    @Override
    public boolean onHandler() {

        //红包弹窗页面
        if (AccessibilityNodeUtils.hasOneNodeByTexts(getRootNode(), WX_TEXT_SLOW, WX_TEXT_OUT_OF_DATE)) {
            //已经领完了 或者过期
            log("-> 已经领完了 或者过期");
            postDelayedBack();
            return true;
        } else {
            AccessibilityNodeInfo node = AccessibilityNodeUtils.getOneNodeByViewTag(getRootNode(), WidgetType.Button.getContent());
            if (node != null) {
                //还没有领
                log("-> 还没有领");

                if (isAutoOpen()) {
                    toast("领红包啦");
                    performClick(node);
                }

                return true;
            }
        }

        delayedChangeLifecycle();
        return true;
    }

    @Override
    protected String getInterceptActivityName() {
        return "LuckyMoneyReceiveUI";
    }
}
