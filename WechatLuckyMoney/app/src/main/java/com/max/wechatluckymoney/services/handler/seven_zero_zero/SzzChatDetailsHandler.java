package com.max.wechatluckymoney.services.handler.seven_zero_zero;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.base.ChatDetailsHandler;
import com.max.wechatluckymoney.utils.AccessibilityNodeUtils;
import com.max.wechatluckymoney.utils.Utils;

import java.util.List;

/**
 * Created by max on 2019/1/7.
 * 聊天界面 处理类
 */
public class SzzChatDetailsHandler extends ChatDetailsHandler {


    /**
     * 聊天对象 名称
     */
    private static final String WX_ID_NAME = "com.tencent.mm:id/jw";

    /**
     * 微信红包 Item
     */
    private static final String WX_ID_LUCKYMONEY_ITEM = "com.tencent.mm:id/ao4";

    /**
     * 微信红包 无法领取的文本 (已过期,被领取 等提示文本)
     */
    private static final String WX_ID_LUCKYMONEY_BAN_TEXT = "com.tencent.mm:id/ape";

    /**
     * 微信红包文本 （用户发红包设置的文本）
     */
    private static final String WX_ID_LUCKYMONEY_TEXT = "com.tencent.mm:id/apd";


    public SzzChatDetailsHandler(AccessibilityHandlerListener listener) {
        super(listener);
    }

    @Override
    public boolean onHandler() {

        List<AccessibilityNodeInfo> nodes = AccessibilityNodeUtils.findNodeListById(getRootNode(), WX_ID_LUCKYMONEY_ITEM);

        if (!Utils.listIsEmpty(nodes)) {
            for (int i = nodes.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo itemNode = nodes.get(i);

                AccessibilityNodeInfo canGetNode = AccessibilityNodeUtils.getFirstNodeById(itemNode, WX_ID_LUCKYMONEY_TEXT);
                if (canGetNode != null) {
                    AccessibilityNodeInfo banNode = AccessibilityNodeUtils.getFirstNodeById(itemNode, WX_ID_LUCKYMONEY_BAN_TEXT);
                    if (banNode == null) {

                        Rect rectScreen = new Rect();
                        itemNode.getBoundsInScreen(rectScreen);
                        //是我的红包？
                        boolean isMyRedPacket = isMyRedPacket(rectScreen);

                        if (isMyRedPacket && !isOpenMyRedPaclet()) {
                            log("是我的红包  但设置说 不能领");
                            return false;
                        }
                        performClick(itemNode);
                    }
                }
            }
            return true;
        }

        return false;
    }

    @Override
    protected String getInterceptActivityName() {
        return "LauncherUI";
    }
}
