package com.max.wechatluckymoney.support;

import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.utils.L;
import com.max.wechatluckymoney.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

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
    private LinkedHashMap<AccessibilityNodeInfo, Long> mRedPacketQueue;

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
    public OpenRedPacketDelayTask(Handler handler, int delayTime) {
        this.mHandler = handler;
        this.mOpenRedPacketDelayTime = delayTime;
        this.mRedPacketQueue = new LinkedHashMap<>();
    }

    /**
     * 添加任务
     *
     * @param nodeInfo
     */
    public void addTask(AccessibilityNodeInfo nodeInfo) {

        if (mCurDelayedNode == null) {
            //当前没有延时任务在执行

            if (mRedPacketQueue.isEmpty()) {
                //当前没有等待任务 可添加此任务
                startDelayedTask(nodeInfo, System.currentTimeMillis());

            } else if (mRedPacketQueue.containsKey(nodeInfo)) {
                //已经在队列中  这次直接执行该红包任务
                startDelayedTask(nodeInfo, mRedPacketQueue.remove(nodeInfo));

            } else {
                //没有在队列中 这次添加到队列中等待执行

                //拿取第一个Node 执行定时任务
                Map.Entry<AccessibilityNodeInfo, Long> entry = Utils.getMapFirstEntry(mRedPacketQueue);
                mRedPacketQueue.remove(entry.getKey());
                startDelayedTask(entry.getKey(), entry.getValue());

                mRedPacketQueue.put(nodeInfo, System.currentTimeMillis());
            }

        } else {

            //当前已有延时任务在执行
            if (isSameNode(mCurDelayedNode, nodeInfo)) return;
            if (!mRedPacketQueue.containsKey(nodeInfo)) {
                //之前没有添加过
                mRedPacketQueue.put(nodeInfo, System.currentTimeMillis());
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
    private void startDelayedTask(AccessibilityNodeInfo nodeInfo, long nodeCreateTime) {
        if (nodeInfo != null) {
            mCurDelayedNode = nodeInfo;
            long time = mOpenRedPacketDelayTime * 1000 - (System.currentTimeMillis() - nodeCreateTime);
            time = time > 0 ? time : 0;
            mHandler.postDelayed(mOpenRedPacketDelayedRunnable, time);
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
