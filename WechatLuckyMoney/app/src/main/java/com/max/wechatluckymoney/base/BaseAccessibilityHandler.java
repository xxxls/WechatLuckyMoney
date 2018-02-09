package com.max.wechatluckymoney.base;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.max.wechatluckymoney.utils.L;

import java.util.List;

/**
 * Created by max on 2018/2/9.
 * 辅助服务
 */
public abstract class BaseAccessibilityHandler
{

    protected OnAccessibilityHandlerListener mListener;

    protected AccessibilityNodeInfo mRootNode;

    public BaseAccessibilityHandler(@NonNull OnAccessibilityHandlerListener listener)
    {
        this.mListener = listener;
    }

    /**
     * 处理类
     *
     * @return
     */
    public abstract boolean onHandler(AccessibilityNodeInfo nodeInfo);

    public boolean onExecute(AccessibilityNodeInfo nodeInfo)
    {
        mRootNode = nodeInfo;
        return onHandler(nodeInfo);
    }


    protected List<AccessibilityNodeInfo> getNodeListById(AccessibilityNodeInfo nodeInfo, String viewId)
    {
        return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
    }


    protected List<AccessibilityNodeInfo> getNodeListByText(AccessibilityNodeInfo nodeInfo, String text)
    {
        return nodeInfo.findAccessibilityNodeInfosByText(text);
    }

    public OnAccessibilityHandlerListener getListener()
    {
        return mListener;
    }

    public AccessibilityService getService()
    {
        return getListener().getAccessibilityService();
    }

    public AccessibilityEvent getEvent(){
        return getListener().getAccessibilityEvent();
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
    protected boolean hasOneOfThoseNodesByTexts(AccessibilityNodeInfo nodeInfo, String... texts)
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
     * 查找 指定控件的 node
     *
     * @param node
     * @param viewTag  view 包名
     * @return
     */
    protected AccessibilityNodeInfo findOneNodeByViewTag(AccessibilityNodeInfo node, String viewTag)
    {
        if (node == null || viewTag == null)
        {
            return null;
        }

        L.e("viewNode : "+node.getClassName());

        //非layout元素
        if (node.getChildCount() == 0)
        {
            if (viewTag.equals(node.getClassName()))
            {
                return node;
            } else
            {
                return null;
            }
        }

        //layout元素，遍历找button
        AccessibilityNodeInfo viewNode;
        for (int i = 0; i < node.getChildCount(); i++)
        {
            viewNode = findOneNodeByViewTag(node.getChild(i), viewTag);
            if (viewNode != null)
            {
                return viewNode;
            }
        }
        return null;
    }


}
