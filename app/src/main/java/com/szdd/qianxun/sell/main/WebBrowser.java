package com.szdd.qianxun.sell.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.top.TActivity;

public class WebBrowser extends TActivity {
    private String title = "";
    private String url = "";

    @Override
    public void onCreate() {
        setContentView(R.layout.sell_main_web_browser);
        getMessage();
        if (title == null || title.trim().equals(""))
            title = getResources().getString(R.string.app_name);
        setTitle(title);
        showBackButton();
        initView();
        initActionBar(getResources().getColor(R.color.topbar_bg));
    }

    private void getMessage() {
        title = getIntent().getStringExtra("title");
        if (title == null) title = "";
        url = getIntent().getStringExtra("url");
        if (url == null) url = "";
    }

    private void initView() {
        WebView webView = (WebView) findViewById(R.id.sell_main_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setBlockNetworkLoads(false);
        //file:///android_asset表示到asset目录寻找文件
        if (url.equals("")) return;
        webView.loadUrl(url);
        webView.setWebViewClient(new ExampleWebViewClient());
    }


    private class ExampleWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 拦截命令，格式 qianxun:命令/参数
            // 示例 qianxun:dialog
            if (url.startsWith("qianxun:")) {
                view.stopLoading();
                String order = url.substring(8);
                dealOrder(order);
                return true;
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }

    private void dealOrder(String order) {

    }

    @Override
    public void showContextMenu() {
    }

    public static void browse(Activity context, String title, String url) {
        Intent intent = new Intent(context, WebBrowser.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
