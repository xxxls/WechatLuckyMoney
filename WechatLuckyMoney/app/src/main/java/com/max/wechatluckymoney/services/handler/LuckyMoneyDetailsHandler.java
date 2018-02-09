package com.max.wechatluckymoney.services.handler;


import com.max.wechatluckymoney.base.BaseAccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;

/**
 * Created by max on 2018/2/9.
 * 红包详情 处理类
 */
public class LuckyMoneyDetailsHandler extends BaseAccessibilityHandler
{
    //红包详情
    public static final String WECHAT_ACTIVITY_LMD = "LuckyMoneyDetailUI";

    public LuckyMoneyDetailsHandler(OnAccessibilityHandlerListener listener)
    {
        super(listener);
    }

    @Override
    public boolean onHandler()
    {
        String name = getPageName();

        log("name : "+ name);

        if (name.contains(WECHAT_ACTIVITY_LMD))
        {
            postDelayedBack();
            return true;
        }
        return false;
    }


}
