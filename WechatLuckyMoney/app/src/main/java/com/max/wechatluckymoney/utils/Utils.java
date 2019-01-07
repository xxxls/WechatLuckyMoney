package com.max.wechatluckymoney.utils;

import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;

import java.util.List;

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
}
