package com.max.wechatluckymoney.services;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.services.handler.seven_zero_zero.SzzChatDetailsHandler;
import com.max.wechatluckymoney.services.handler.seven_zero_zero.SzzHomeChatHandler;
import com.max.wechatluckymoney.services.handler.seven_zero_zero.SzzRedPacketGetRecordHandler;
import com.max.wechatluckymoney.services.handler.seven_zero_zero.SzzRedPacketGetHandler;
import com.max.wechatluckymoney.services.handler.six_six_three.SstChatDetailsHandler;
import com.max.wechatluckymoney.services.handler.six_six_three.SstHomeChatHandler;
import com.max.wechatluckymoney.services.handler.six_six_three.SstRedPacketGetRecordHandler;
import com.max.wechatluckymoney.services.handler.six_six_three.SstRedPacketGetHandler;

import java.util.ArrayList;

/**
 * 处理帮助类
 * Created by Max on 2019/1/5.
 */
public class HandlerHelper {

    private AccessibilityHandlerListener mListener;

    public HandlerHelper(AccessibilityHandlerListener listener) {
        this.mListener = listener;
    }


    /**
     * 获取已经适配版本
     *
     * @return
     */
    public static String[] getAdapterVersion() {
        return new String[]{"6.6.3", "7.0.0"};
    }

    /**
     * @param version
     * @return
     */
    public ArrayList<AccessibilityHandler> getHandlers(String version) {
        switch (version) {
            case "6.6.3":
                return getHandlersBySixSixThree();
            case "7.0.0":
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
        handlers.add(new SstHomeChatHandler(mListener));
        handlers.add(new SstChatDetailsHandler(mListener));
        handlers.add(new SstRedPacketGetHandler(mListener));
        handlers.add(new SstRedPacketGetRecordHandler(mListener));
        return handlers;
    }


    /**
     * 7.0.0
     *
     * @return
     */
    private ArrayList<AccessibilityHandler> getHandlersBySevenZeroZero() {
        ArrayList<AccessibilityHandler> handlers = new ArrayList<>();
        handlers.add(new SzzHomeChatHandler(mListener));
        handlers.add(new SzzChatDetailsHandler(mListener));
        handlers.add(new SzzRedPacketGetHandler(mListener));
        handlers.add(new SzzRedPacketGetRecordHandler(mListener));
        return handlers;
    }

}
