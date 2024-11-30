package com.idreameducation.ipreppal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.idreameducation.ipreppal.util.Util;

import java.net.URISyntaxException;

public class Webview extends AppCompatActivity {

    private WebView webView;
    public static String URL;
    private ProgressBar progressBarWeb;
    private Webview mainActivity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setWindowSettings(this);
        setContentView(R.layout.activity_webview);

        mainActivity=this;
        webView = findViewById(R.id.webviewe);
        progressBarWeb = findViewById(R.id.progressbar);

        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.contentStatusBar));
            //Light mode
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (URL != null) webView.loadUrl(URL);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https")) return false;
                if (url.startsWith("intent")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        if (fallbackUrl != null) {
                            webView.loadUrl(fallbackUrl);
                            webView.setWebChromeClient(new WebChromeClient() {
                                @Override
                                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                                    callback.invoke(origin, true, false);
                                }
                            });
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            return true;
                        }
                    } catch (URISyntaxException e) {
                        //not an intent uri
                    }
                    return true;//do nothing in other cases
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            private View mCustomView;
            private CustomViewCallback mCustomViewCallback;
            protected FrameLayout mFullscreenContainer;
            private int mOriginalOrientation;
            private int mOriginalSystemUiVisibility;

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarWeb.setVisibility(View.VISIBLE);
                progressBarWeb.setProgress(newProgress);
                if (newProgress == 100) progressBarWeb.setVisibility(View.GONE);
                super.onProgressChanged(view, newProgress);
            }

            public Bitmap getDefaultVideoPoster()
            {
                if (mainActivity == null) return null;

                return BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), 2130837573);
            }

            public void onHideCustomView()
            {
                ((FrameLayout)mainActivity.getWindow().getDecorView()).removeView(this.mCustomView);
                this.mCustomView = null;
                mainActivity.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                mainActivity.setRequestedOrientation(this.mOriginalOrientation);
                this.mCustomViewCallback.onCustomViewHidden();
                this.mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback)
            {
                if (this.mCustomView != null)
                {
                    onHideCustomView();
                    return;
                }
                this.mCustomView = paramView;
                this.mOriginalSystemUiVisibility = mainActivity.getWindow().getDecorView().getSystemUiVisibility();
                this.mOriginalOrientation = mainActivity.getRequestedOrientation();
                this.mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout)mainActivity.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                mainActivity.getWindow().getDecorView().setSystemUiVisibility(3846);
            }




        });

    }
}