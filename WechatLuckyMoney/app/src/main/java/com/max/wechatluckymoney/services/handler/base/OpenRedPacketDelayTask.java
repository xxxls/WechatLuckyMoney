package com.max.wechatluckymoney.services.handler.base;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import com.max.wechatluckymoney.utils.L;

import java.util.LinkedList;

/**
 * 打开红包延时任务
 * Created by Max on 2019/1/9.
 */
public class OpenRedPacketDelayTask {

    private Handler mHandler;

    /**
     * 打开红包 延迟时间
     */
    private int mOpenRedPacketDelayTime;

    /**
     * 当前延时打开的红包
     */
    private AccessibilityNodeInfo mCurDelayedNode;

    /**
     * 等待延时打开的红包列表
     */
    private LinkedList<AccessibilityNodeInfo> mRedPacketQueue;


    private View mView;

    /**
     * 打开红包延时任务
     */
    private Runnable mOpenRedPacketDelayedRunnable = () -> {
        L.e("延时任务,打开红包 ");
        if (mCurDelayedNode != null) {
            openRedPacket(mCurDelayedNode);
            mCurDelayedNode = null;
        }
    };

    /**
     * @param handler
     * @param delayTime 延时打开红包的时间
     */
    public OpenRedPacketDelayTask(Handler handler, int delayTime,View view) {
        this.mHandler = handler;
        this.mOpenRedPacketDelayTime = delayTime;
        this.mRedPacketQueue = new LinkedList<>();
        this.mView = view;
    }

    /**
     * 添加任务
     *
     * @param nodeInfo
     */
    public void addTask(AccessibilityNodeInfo nodeInfo) {

        L.e("mRedPacketQueue size = " + mRedPacketQueue.size() + "mCurDelayedNode = " + (mCurDelayedNode == null ? "null" : "Not null"));
        if (mCurDelayedNode == null) {
            //当前没有延时任务在执行

            if (mRedPacketQueue.isEmpty()) {
                //当前没有等待任务 可添加此任务
                startDelayedTask(nodeInfo);
            } else if (mRedPacketQueue.contains(nodeInfo)) {
                //已经在队列中 这次可直接打开
                mCurDelayedNode = nodeInfo;
                mRedPacketQueue.remove(nodeInfo);
                openRedPacket(nodeInfo);
            } else {
                //没有在队列中 这次添加到队列中 等待执行
                startDelayedTask(mRedPacketQueue.removeFirst());
                mRedPacketQueue.add(nodeInfo);
            }

        } else {
            //当前已有延时任务在执行
            if (isSameNode(mCurDelayedNode, nodeInfo)) return;
            if (!mRedPacketQueue.contains(nodeInfo)) {
                //之前没有添加过
                mRedPacketQueue.add(nodeInfo);
            }
        }
    }

    /**
     * 是否同一个Node
     *
     * @param nodeInfoA
     * @param nodeInfoB
     * @return
     */
    private boolean isSameNode(AccessibilityNodeInfo nodeInfoA, AccessibilityNodeInfo nodeInfoB) {
        if (nodeInfoA == nodeInfoB) return true;
        return nodeInfoA.equals(nodeInfoB);
    }

    /**
     * 更新  延时打开红包 的时间
     *
     * @param delayTime
     */
    public void updateDelayTime(int delayTime) {
        this.mOpenRedPacketDelayTime = delayTime;
    }

    /**
     * 延时任务
     */
    private void startDelayedTask(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            mCurDelayedNode = nodeInfo;
            mHandler.postDelayed(mOpenRedPacketDelayedRunnable, mOpenRedPacketDelayTime);
        }
    }

    /**
     * 打开红包
     *
     * @param nodeInfo
     */
    private void openRedPacket(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }
}
