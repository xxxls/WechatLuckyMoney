package com.max.wechatluckymoney.base;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.max.wechatluckymoney.utils.L;

import java.util.List;

/**
 * Created by max on 2018/2/9.
 * 辅助服务
 */
public abstract class BaseAccessibilityHandler
{
    protected final String TAG = this.getClass().getSimpleName();

    protected OnAccessibilityHandlerListener mListener;

    public BaseAccessibilityHandler(@NonNull OnAccessibilityHandlerListener listener)
    {
        this.mListener = listener;
    }

    /**
     * 处理类
     *
     * @return
     */
    protected abstract boolean onHandler();

    public boolean onExecute()
    {
        return onHandler();
    }


    private OnAccessibilityHandlerListener getListener()
    {
        return mListener;
    }

    protected AccessibilityService getService()
    {
        return getListener().getAccessibilityService();
    }

    protected AccessibilityEvent getEvent()
    {
        return getListener().getAccessibilityEvent();
    }

    protected AccessibilityNodeInfo getRootNode()
    {
        return getEvent().getSource();
    }

    /**
     * 设置当前 事件 类名称
     */
    protected String getClassName()
    {
        if (getEvent().getClassName()==null){
            return "";
        }
        return getEvent().getClassName().toString();
    }



    protected List<AccessibilityNodeInfo> findNodeListById(AccessibilityNodeInfo nodeInfo, String viewId)
    {
        return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
    }

    protected List<AccessibilityNodeInfo> findNodeListByText(AccessibilityNodeInfo nodeInfo, String text)
    {
        return nodeInfo.findAccessibilityNodeInfosByText(text);
    }

    /**
     * 最后一个 node
     *
     * @param nodeInfo
     * @param texts    文本
     * @return
     */
    protected AccessibilityNodeInfo getTheLastNodeByTexts(AccessibilityNodeInfo nodeInfo, String... texts)
    {
        int bottom = 0;
        AccessibilityNodeInfo lastNode = null, tempNode;
        List<AccessibilityNodeInfo> nodes;

        for (String text : texts)
        {
            if (text == null)
            {
                continue;
            }

            nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && ! nodes.isEmpty())
            {
                tempNode = nodes.get(nodes.size() - 1);
                if (tempNode == null)
                {
                    return null;
                }
                Rect bounds = new Rect();
                tempNode.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom)
                {
                    bottom = bounds.bottom;
                    lastNode = tempNode;
                }
            }
        }
        return lastNode;
    }

    /**
     * 有一个 存在
     *
     * @param nodeInfo
     * @param texts    文本
     * @return
     */
    protected boolean hasOneNodeByTexts(AccessibilityNodeInfo nodeInfo, String... texts)
    {
        List<AccessibilityNodeInfo> nodes;
        for (String text : texts)
        {
            if (text == null)
            {
                continue;
            }

            nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && ! nodes.isEmpty())
            {
                return true;
            }
        }
        return false;
    }




    /**
     * 开始一个延时任务
     *
     * @param runnable
     * @param time
     */
    protected void startDelayedTask(Runnable runnable, long time)
    {
        new Handler().postDelayed(runnable, time);
    }

    /**
     * 延时返回
     */
    protected void postDelayedBack()
    {
        startDelayedTask(
                new Runnable()
                {
                    public void run()
                    {
                        getService().performGlobalAction(getService().GLOBAL_ACTION_BACK);
                    }
                },
                300);
    }

    protected void toast(CharSequence sequence)
    {
        Toast.makeText(getService(), sequence, Toast.LENGTH_SHORT).show();
    }

    protected void log(String message)
    {
        L.e(TAG, message);
    }
}
