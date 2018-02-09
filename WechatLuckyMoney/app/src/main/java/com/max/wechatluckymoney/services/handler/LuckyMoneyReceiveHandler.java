package com.max.wechatluckymoney.services.handler;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;
import com.max.wechatluckymoney.support.enums.WidgetType;
import com.max.wechatluckymoney.utils.L;

/**
 * Created by max on 2018/2/9.
 * 红包详情 处理类
 */
public class LuckyMoneyReceiveHandler extends BaseAccessibilityHandler
{


    //红包领取
    public static final String WECHAT_ACTIVITY_LMR = "LuckyMoneyReceiveUI";

    private static final String WECHAT_TEXT_SLOW = "手慢了，红包派完了";
    private static final String WECHAT_TEXT_OUT_OF_DATE = "已超过24小时";

    public LuckyMoneyReceiveHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }

    @Override
    public boolean onHandler()
    {

//        //红包弹窗页面
//        if (hasOneOfThoseNodesByTexts(getRootNode(), WECHAT_TEXT_SLOW, WECHAT_TEXT_OUT_OF_DATE))
//        {
//            log("->  已经领完了 或者过期 ************ ");
//            postDelayedBack();
//            //已经领完了 或者过期
//            return true;
//        }


        String name = getPageName();

        log("name : " + name);

        if (name.contains(WECHAT_ACTIVITY_LMR))
        {
            //红包弹窗页面
            if (hasOneOfThoseNodesByTexts(getRootNode(), WECHAT_TEXT_SLOW, WECHAT_TEXT_OUT_OF_DATE))
            {

                log("-> 已经领完了 或者过期");
                //已经领完了 或者过期
                postDelayedBack();
                return true;
            } else
            {
                AccessibilityNodeInfo node = findOneNodeByViewTag(getRootNode(), WidgetType.Button.getContent());
                if (node != null)
                {
                    log("-> 还没有领");
                    //还没有领
                    toast("领红包了");
//                         node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
        }

        return false;
    }
}
