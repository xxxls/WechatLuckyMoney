package com.max.wechatluckymoney.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.base.BaseActivity;

import java.lang.reflect.Field;

import butterknife.BindView;


/**
 * Created by Max on 2018/2/11.
 * webView
 */
public class WebViewActivity extends BaseActivity
{
    @BindView(R.id.progressBar_webview_top)
    ProgressBar mProgressBar;

    @BindView(R.id.webView)
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
        mWebView.setWebViewClient(new H5WebViewClient());
        mWebView.setWebChromeClient(new HMWebViewClient());
        mWebView.getSettings().setBuiltInZoomControls(false);
        setZoomControlGone(mWebView);
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
     * 3.0前 隐藏 缩放按钮
     *
     * @param view
     */
    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
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


    private class HMWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mProgressBar != null) {
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    public class H5WebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
    }

}
