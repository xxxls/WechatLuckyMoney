package com.max.wechatluckymoney.utils;

import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Max on 2018/2/12.
 */

public class Utils {

    //震动milliseconds毫秒
    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }


    /**
     * 判断 list  是否为空
     *
     * @param list
     * @return
     */
    public static boolean listIsEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 获取Map 第一条数据
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map.Entry<K, V> getMapFirstEntry(LinkedHashMap<K, V> map) {
        return map.entrySet().iterator().next();
    }

    /**
     * 获取Map最后一条数据
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map.Entry<K, V> getMapLastEntry(LinkedHashMap<K, V> map) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> tail = null;
        while (iterator.hasNext()) {
            tail = iterator.next();
        }
        return tail;
    }

    /**
     * 获取微信版本号
     *
     * @return
     */
    public static String getWeChatVersion(Context context) {
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            return context.getPackageManager().
                    getPackageInfo("com.tencent.mm", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 是否包含 某文本
     *
     * @param arr 数组
     * @param str 文本
     * @return
     */
    public static boolean isArrContains(String[] arr, String str) {
        if (str == null || arr == null)
            return false;

        for (String s : arr) {
            if (!TextUtils.isEmpty(s) && str.contains(s)) {
                return true;
            }
        }

        return false;
    }
}
