package com.max.wechatluckymoney.services.handler.base;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.services.handler.AccessibilityHandler;
import com.max.wechatluckymoney.services.handler.AccessibilityHandlerListener;
import com.max.wechatluckymoney.utils.AccessibilityNodeUtils;
import com.max.wechatluckymoney.utils.ScreenUtils;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 聊天详情
 * Created by Max on 2019/1/7.
 */
public abstract class ChatDetailsHandler extends AccessibilityHandler
{

    /**
     * 屏幕宽度
     */
    private long mScreenWidth;

    /**
     * 是否拆自己发的红包
     */
    private Boolean mIsOpenMyRedPaclet;

    /**
     * 是否群裏 正則
     */
    private Pattern mGroupNamePattern = Pattern.compile("^\\w*\\(\\d+\\)$");

    /**
     * 打开红包 延迟时间
     */
    private Integer mOpenRedPacketDelayTime;

    /**
     * 要延时打开的红包列表
     */
    private LinkedList<AccessibilityNodeInfo> mLinkedList;

    /**
     * 打开红包延时任务
     */
    private Runnable mOpenRedPacketDelayedRunnable = () -> {
        if (mLinkedList != null)
        {
            if (! mLinkedList.isEmpty())
            {
                log("延时任务,打开红包 ");
                performClick(mLinkedList.removeFirst());
            }
        }
    };

    public ChatDetailsHandler(@NonNull AccessibilityHandlerListener listener)
    {
        super(listener);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    protected long getScreenWidth()
    {
        if (mScreenWidth == 0)
        {
            mScreenWidth = ScreenUtils.getScreenWidth(getService());
        }
        return mScreenWidth;
    }

    /**
     * 获取 打开红包 延迟时间
     *
     * @return
     */
    protected int getDelayTime()
    {
        if (mOpenRedPacketDelayTime == null)
        {
            mOpenRedPacketDelayTime = getSharedPreferences().getInt("pref_open_delay", 0);
        }
        return mOpenRedPacketDelayTime;
    }

    /**
     * 是否拆自己发的红包
     *
     * @return
     */
    protected boolean isOpenMyRedPaclet()
    {
        if (mIsOpenMyRedPaclet == null)
        {
            mIsOpenMyRedPaclet = getSharedPreferences().getBoolean("pref_watch_self", false);
        }
        return mIsOpenMyRedPaclet;
    }

    /**
     * 是否我发的红包
     *
     * @param rect
     */
    protected boolean isMyRedPacket(Rect rect)
    {
        if (rect != null)
        {
            return rect.left > getScreenWidth() - rect.right;
        }
        return false;
    }

    /**
     * 是否群聊
     *
     * @param chatName 聊天对象名称
     * @return
     */
    protected boolean isGroupChat(String chatName)
    {
        return mGroupNamePattern.matcher(chatName).find();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s)
    {
        super.onSharedPreferenceChanged(sharedPreferences, s);
        mIsOpenMyRedPaclet = getSharedPreferences().getBoolean("pref_watch_self", false);
        mOpenRedPacketDelayTime = getSharedPreferences().getInt("pref_open_delay", 0);
    }

    /**
     * 添加 延时打开红包 延时任务
     *
     * @param nodeInfo
     */
    protected void addDelayTask(AccessibilityNodeInfo nodeInfo)
    {
        if (mLinkedList == null)
        {
            mLinkedList = new LinkedList<>();
        }

        if (! mLinkedList.contains(nodeInfo))
        {
            mLinkedList.add(nodeInfo);
            startDelayedTask(mOpenRedPacketDelayedRunnable, getDelayTime());
        }
    }

}
