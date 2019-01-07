package com.max.wechatluckymoney.services.handler.six_six_three;


import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.base.RedPacketGetRecordHandler;

/**
 * Created by max on 2018/2/9.
 * 红包详情 处理类
 */
public class SstRedPacketGetRecordHandler extends RedPacketGetRecordHandler
{

    private boolean mIsComplete = true;

    private Runnable mBackrunnable = () -> {
        getService().performGlobalAction(getService().GLOBAL_ACTION_BACK);
        mIsComplete = true;
    };

    public SstRedPacketGetRecordHandler(AccessibilityHandlerListener listener)
    {
        super(listener);
    }

    @Override
    public boolean onHandler()
    {
        if (mIsComplete)
        {
            mIsComplete = false;
            startDelayedTask(mBackrunnable, 300);
        }
        return true;
    }

    @Override
    protected String getInterceptActivityName()
    {
        return "LuckyMoneyDetailUI";
    }


}
