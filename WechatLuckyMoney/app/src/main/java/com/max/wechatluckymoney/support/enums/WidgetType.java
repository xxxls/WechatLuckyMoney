package com.max.wechatluckymoney.support.enums;

/**
 * Created by max on 2018/2/9.
 * 控件
 */
public enum WidgetType
{
    Button("android.widget.Button"),

    TextView("android.widget.TextView"),

    LinearLayout("android.widget.RelativeLayout"),

    RelativeLayout("android.widget.RelativeLayout");


    WidgetType(String content)
    {
        this.content = content;
    }

    private String content;

    public String getContent()
    {
        return content;
    }
}
