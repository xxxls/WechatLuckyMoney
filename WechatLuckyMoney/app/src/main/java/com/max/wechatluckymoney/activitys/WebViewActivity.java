package com.max.wechatluckymoney.activitys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.base.BaseActivity;

import butterknife.Bind;


/**
 * Created by Max on 2018/2/11.
 * webView
 */
public class WebViewActivity extends BaseActivity
{
    @Bind(R.id.webView)
    WebView mWebView;

    private String mUrl;

    public static Intent getInstance(Context context, String title, String url)
    {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        return intent;
    }

    @Override
    protected void onInitialize()
    {
        initWebView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && ! bundle.isEmpty())
        {
            String title = bundle.getString("title");
            mUrl = bundle.getString("url");

            setTitleBarText(title);
            mWebView.loadUrl(mUrl);
        }
    }

    /**
     * 初始webview
     */
    private void initWebView()
    {
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                CookieSyncManager.getInstance().sync();
            }
        });
    }

    /**
     * 设置titlebar
     *
     * @param title
     */
    private void setTitleBarText(String title)
    {
        TextView webViewBar = (TextView) findViewById(R.id.webview_bar);
        webViewBar.setText(title);
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_webview;
    }

    public void performBack(View view)
    {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack())
                    {
                        mWebView.goBack();
                    } else
                    {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 跳转浏览器
     *
     * @param view
     */
    public void openLink(View view)
    {
        if (mUrl != null)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(this.mUrl));
            startActivity(intent);
        }
    }
}
