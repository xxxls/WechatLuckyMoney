package com.max.wechatluckymoney.utils;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;


/**
 * Created by Max on 2018/2/10.
 * 辅助服务 工具类
 */

public class AccessibilityNodeUtils {


    /**
     * 查找 符合ID 的AccessibilityNodeInfo 列表
     *
     * @param nodeInfo
     * @param viewId
     * @return
     */
    public static List<AccessibilityNodeInfo> findNodeListById(AccessibilityNodeInfo nodeInfo, String viewId) {
        return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
    }

    /**
     * 查找 符合Text 的AccessibilityNodeInfo 列表
     *
     * @param nodeInfo
     * @param text
     * @return
     */
    public static List<AccessibilityNodeInfo> findNodeListByText(AccessibilityNodeInfo nodeInfo, String text) {
        return nodeInfo.findAccessibilityNodeInfosByText(text);
    }

    /**
     * 查找 符合ID 的第一个AccessibilityNodeInfo
     *
     * @param nodeInfo
     * @param viewId
     * @return
     */
    public static AccessibilityNodeInfo getFirstNodeById(AccessibilityNodeInfo nodeInfo, String viewId) {
        List<AccessibilityNodeInfo> list = findNodeListById(nodeInfo, viewId);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查找 符合ID 的最后一个AccessibilityNodeInfo
     *
     * @param nodeInfo
     * @param viewId
     * @return
     */
    public static AccessibilityNodeInfo getTheLastNodeById(AccessibilityNodeInfo nodeInfo, String viewId) {
        List<AccessibilityNodeInfo> list = findNodeListById(nodeInfo, viewId);
        if (list != null && !list.isEmpty()) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    /**
     * 查找 符合 Text数组  的最后一个AccessibilityNodeInfo
     *
     * @param nodeInfo
     * @param texts    文本数组
     * @return
     */
    public static AccessibilityNodeInfo getTheLastNodeByTexts(AccessibilityNodeInfo nodeInfo, String... texts) {
        int bottom = 0;
        AccessibilityNodeInfo lastNode = null, tempNode;
        List<AccessibilityNodeInfo> nodes;

        for (String text : texts) {
            if (text == null) {
                continue;
            }

            nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) {
                tempNode = nodes.get(nodes.size() - 1);
                if (tempNode == null) {
                    return null;
                }
                Rect bounds = new Rect();
                tempNode.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom) {
                    bottom = bounds.bottom;
                    lastNode = tempNode;
                }
            }
        }
        return lastNode;
    }

    /**
     * 判断是否有一个  符合 Text数组的 Node存在
     *
     * @param nodeInfo
     * @param texts    文本数组
     * @return
     */
    public static boolean hasOneNodeByTexts(AccessibilityNodeInfo nodeInfo, String... texts) {
        List<AccessibilityNodeInfo> nodes;
        for (String text : texts) {
            if (text == null) {
                continue;
            }

            nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取一个 node 根据 文本
     *
     * @param info  节点
     * @param texts 需要匹配的文字
     */
    public static AccessibilityNodeInfo getOneNodeByText(AccessibilityNodeInfo info, String... texts) {
        if (info != null) {
            if (info.getChildCount() == 0) {
                if (info.getText() != null) {
                    String text = info.getText().toString();

                    for (String str : texts) {
                        if (text.contains(str)) {
                            return info;
                        }
                    }
                }
            } else {
                int size = info.getChildCount();
                for (int i = 0; i < size; i++) {
                    AccessibilityNodeInfo childInfo = info.getChild(i);
                    if (childInfo != null) {
                        AccessibilityNodeInfo nodeInfo = getOneNodeByText(childInfo, texts);
                        if (nodeInfo != null) {
                            return nodeInfo;
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * 获取一个 node  根据 控件
     *
     * @param node
     * @param viewTag view 包名
     * @return
     */
    public static AccessibilityNodeInfo getOneNodeByViewTag(AccessibilityNodeInfo node, String viewTag) {
        if (node == null || viewTag == null) {
            return null;
        }
        //非layout元素
        if (node.getChildCount() == 0) {
            if (viewTag.equals(node.getClassName())) {
                return node;
            } else {
                return null;
            }
        }

        //layout元素，遍历找button
        AccessibilityNodeInfo viewNode;
        for (int i = 0; i < node.getChildCount(); i++) {
            viewNode = getOneNodeByViewTag(node.getChild(i), viewTag);
            if (viewNode != null) {
                return viewNode;
            }
        }
        return null;
    }


    /**
     * 打印 所有子node
     */
    public static void printAllNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }

        if (nodeInfo.getChildCount() == 0) {
            printNodeInfo(nodeInfo);
        } else {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
                printAllNodeInfo(childNode);
            }
        }
    }

    /**
     * 打印 AccessibilityNodeInfo
     *
     * @param nodeInfo
     */
    public static void printNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }

        L.e("printNodeInfo | ClassName > " + nodeInfo.getClassName().toString());
        L.e("printNodeInfo | ChildCount > " + nodeInfo.getChildCount());
        if (nodeInfo.getContentDescription() != null) {
            L.e("printNodeInfo | Description > " + nodeInfo.getContentDescription().toString());
        }
        if (nodeInfo.getText() != null) {
            L.e("printNodeInfo | Text > " + nodeInfo.getText().toString());
        }
    }
}
