package com.max.wechatluckymoney.services.handler.seven_zero_zero;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.activitys.LoadingActivity;
import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.base.RedPacketGetHandler;
import com.max.wechatluckymoney.utils.AccessibilityNodeUtils;

/**
 * Created by max on 2019/1/7.
 * 领取红包详情 处理类
 */
public class SzzRedPacketGetHandler extends RedPacketGetHandler {

    /**
     * 打开红包按钮
     */
    private static final String WX_ID_OPEN = "com.tencent.mm:id/cv0";

    /**
     * 红包 名称
     */
    private static final String WX_ID_NAME = "com.tencent.mm:id/cuz";


    public SzzRedPacketGetHandler(AccessibilityHandlerListener listener) {
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

            AccessibilityNodeInfo openNode = AccessibilityNodeUtils.getFirstNodeById(getRootNode(), WX_ID_OPEN);

            if (openNode != null) {
                //还没有领
                log("-> 还没有领");
                if (isAutoOpen()) {

                    AccessibilityNodeInfo nameNode = AccessibilityNodeUtils.getFirstNodeById(getRootNode(), WX_ID_NAME);
                    if (nameNode != null && nameNode.getText() != null) {
                        if (isExcludeByRedPacketText(nameNode.getText().toString())) {
                            return true;
                        }
                    }

                    toast("领红包啦");
                    performClick(openNode);
                }
                return true;
            }
        }

        delayedChangeLifecycle();
        return true;
    }

    @Override
    protected String getInterceptActivityName() {
        return "LuckyMoneyNotHookReceiveUI";
    }

}
