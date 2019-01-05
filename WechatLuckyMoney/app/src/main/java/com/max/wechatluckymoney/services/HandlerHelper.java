package com.max.wechatluckymoney.services;
import com.max.wechatluckymoney.base.AccessibilityHandler;
import com.max.wechatluckymoney.base.OnAccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.six_six_three.ChatDetailsHandler;
import com.max.wechatluckymoney.services.handler.six_six_three.ChatListHandler;
import com.max.wechatluckymoney.services.handler.six_six_three.LuckyMoneyDetailsHandler;
import com.max.wechatluckymoney.services.handler.six_six_three.LuckyMoneyReceiveHandler;

import java.util.ArrayList;

/**
 * Created by Max on 2019/1/5.
 */
public class HandlerHelper {

    private OnAccessibilityHandlerListener mListener;

    public HandlerHelper(OnAccessibilityHandlerListener listener) {
        this.mListener = listener;
    }

    /**
     * @param version
     * @return
     */
    public ArrayList<AccessibilityHandler> getHandlers(String version) {
        switch (version) {
            case "6.6.3":
                return getHandlersBySixSixThree();
            case "7.7.0":
            default:
                return getHandlersBySevenZeroZero();
        }
    }


    /**
     * 6.6.3
     *
     * @return
     */
    private ArrayList<AccessibilityHandler> getHandlersBySixSixThree() {
        ArrayList<AccessibilityHandler> handlers = new ArrayList<>();
        handlers.add(new ChatListHandler(mListener));
        handlers.add(new ChatDetailsHandler(mListener));
        handlers.add(new LuckyMoneyReceiveHandler(mListener));
        handlers.add(new LuckyMoneyDetailsHandler(mListener));
        return handlers;
    }


    /**
     * 7.0.0
     *
     * @return
     */
    private ArrayList<AccessibilityHandler> getHandlersBySevenZeroZero() {
        ArrayList<AccessibilityHandler> handlers = new ArrayList<>();
        handlers.add(new ChatListHandler(mListener));
        handlers.add(new ChatDetailsHandler(mListener));
        handlers.add(new LuckyMoneyReceiveHandler(mListener));
        handlers.add(new LuckyMoneyDetailsHandler(mListener));
        return handlers;
    }

}
