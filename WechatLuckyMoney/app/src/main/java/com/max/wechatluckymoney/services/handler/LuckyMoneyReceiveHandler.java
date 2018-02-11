package com.max.wechatluckymoney.services.handler;

import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.activitys.LoadingActivity;
import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;
import com.max.wechatluckymoney.support.enums.WidgetType;
import com.max.wechatluckymoney.utils.AccessibilityUtil;

/**
 * Created by max on 2018/2/9.
 * 红包详情 处理类
 */
public class LuckyMoneyReceiveHandler extends BaseAccessibilityHandler
{


    //红包领取
    private static final String WECHAT_ACTIVITY_LMR = "LuckyMoneyReceiveUI";

    private static final String WECHAT_TEXT_SLOW = "手慢了";
    private static final String WECHAT_TEXT_OUT_OF_DATE = "已超过24小时";

    public LuckyMoneyReceiveHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }

    @Override
    public boolean onHandler()
    {

        if (getClassName().contains(WECHAT_ACTIVITY_LMR))
        {

//            //红包弹窗页面
            if (hasOneNodeByTexts(getRootNode(), WECHAT_TEXT_SLOW, WECHAT_TEXT_OUT_OF_DATE))
            {
                //已经领完了 或者过期
                log("-> 已经领完了 或者过期");
                postDelayedBack();
                return true;
            } else
            {
                AccessibilityNodeInfo node = AccessibilityUtil.getOneNodeByViewTag(getRootNode(), WidgetType.Button.getContent());
                if (node != null)
                {
                    //还没有领
                    log("-> 还没有领");

                    if (! mListener.getSharedPreferences().getBoolean("pref_auto_open", true))
                    {
                        return true;
                    }

                    toast("领红包啦");
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }

            //红包页面 生命周期的改变 才能正常执行本程序
            //暂时这样解决 微信对 红包页面做的特殊处理
            getService().startActivity(LoadingActivity.getInstance(getService()));
        }
        return false;
    }
}
