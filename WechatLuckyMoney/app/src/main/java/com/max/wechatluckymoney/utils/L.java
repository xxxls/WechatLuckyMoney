package com.max.wechatluckymoney.utils;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by max on 2017/4/10.
 * LOG 日志
 */
public final class L
{
    static
    {

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("MAX")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .build();

        AndroidLogAdapter androidLogAdapter = new AndroidLogAdapter(formatStrategy)
        {
            @Override
            public boolean isLoggable(int priority, String tag)
            {
                return DebugUtils.isDebug();
            }
        };

        Logger.addLogAdapter(androidLogAdapter);
    }

    private L()
    {
    }

    /**
     * 设置 TAG
     *
     * @param tag
     */
    public static void setTag(String tag)
    {
        Logger.t(tag);
    }

    public static void v(String msg)
    {
        Logger.v(msg);
    }

    public static void i(String msg)
    {
        Logger.i(msg);
    }

    public static void e(String msg)
    {
        Logger.e(msg);
    }

    public static void d(String msg)
    {
        Logger.d(msg);
    }


    public static void v(String tag, String msg)
    {
        setTag(tag);
        Logger.v(msg);
    }

    public static void d(String tag, String msg)
    {
        setTag(tag);
        Logger.d(msg);
    }

    public static void i(String tag, String msg)
    {
        setTag(tag);
        Logger.i(msg);
    }

    public static void e(String tag, String msg)
    {
        setTag(tag);
        Logger.e(msg);
    }

}

