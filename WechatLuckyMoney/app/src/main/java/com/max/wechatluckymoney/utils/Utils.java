package com.max.wechatluckymoney.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Max on 2018/2/12.
 */

public class Utils
{

    //震动milliseconds毫秒
    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

}
