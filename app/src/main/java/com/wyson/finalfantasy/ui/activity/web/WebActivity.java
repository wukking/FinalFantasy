package com.wyson.finalfantasy.ui.activity.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wyson.finalfantasy.R;
import com.wyson.finalfantasy.ui.activity.web.support.WebAppInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
import static com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND;

/**
 * 原生Webview
 */
public class WebActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private WebView webView;
    private boolean mSafeBrowsingIsInitialized;
    private String mUrl;

    private static final String PARAMS_URL = "params_url";

    public static void startAction(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(PARAMS_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mUrl = getIntent().getStringExtra(PARAMS_URL);

        webView = findViewById(R.id.wv_show);

        setupWebSettings();
        setupJs();
        setupWebViewClient();
    }

    private void setupJs() {
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        //webView 加载本地HTML文件
        //如果使用 loadData() or loadDataWithBaseURL() 参数无效或者为空，不会回调shouldOverrideUrlLoading()
        webView.loadUrl(mUrl);
//        webView.loadUrl("https://www.baidu.com");
//        webView.loadData("<a href=\"example-app:showProfile\">Show Profile</a>","","");
//        webView.loadDataWithBaseURL("example-app://example.co.uk/", "HTML_DATA",
//                null, "UTF-8", null);
    }

    //Andromeda
    private void setupWebSettings() {
        WebSettings webSetting = webView.getSettings();
        //设置缓存
        webSetting.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);

        //Javascript
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);

        // Need
        webSetting.setUseWideViewPort(true);
        webSetting.setAllowFileAccess(true);
        // 图片过大时自动适应屏幕用Normal
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setGeolocationEnabled(true);
        webSetting.setDomStorageEnabled(true);

        webSetting.setSupportZoom(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setBuiltInZoomControls(true);
        //webSetting.setDatabaseEnabled(true);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);

        //other
        webSetting.setDefaultTextEncodingName("UTF-8");

        //If your app targets Android 7.1 (API level 25) or lower
        //  <application>
        //        <meta-data android:name="android.webkit.WebView.EnableSafeBrowsing"
        //                   android:value="false" />
        //        ...
        //  </application>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSetting.setSafeBrowsingEnabled(false);
            //如果您希望在很长一段时间内不显示 web 视图对象,
            // 以便系统可以回收Renderer使用的内存, 则可以这样做。
            //本句中，第一个参数Renderer的优先级与应用程序的默认优先级相同
            // 第二个参数：true,当关联的 web 视图对象不再可见时, 降低Renderer的优先级 RENDERER_PRIORITY_WAIVED
            // 换言之, 参数true表示您的应用程序不关心系统是否保持渲染程序的生存状态。
            // 事实上,  这种较低的优先级使得渲染器进程可能在内存不足的情况下被杀死
            webView.setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_BOUND, true);
        }

    }

    private static final String APP_SCHEME = "example-app:";
    private String urlData;

    private void setupWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //false是在本APP中打开
/*                if (Uri.parse(url).getHost().equals("www.baidu.com")) {
                    //是我的Url，再APP内部处理
                    return false;
                }
                // 不是我的Url 跳转到外部应用去
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;*/
                if (url.startsWith(APP_SCHEME)) {
                    try {
                        urlData = URLDecoder.decode(url.substring(APP_SCHEME.length()), "UTF-8");
//                        respondToData(urlData);
                        Toast.makeText(WebActivity.this, urlData, Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });

        mSafeBrowsingIsInitialized = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            WebView.startSafeBrowsing(this, new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean success) {
                    mSafeBrowsingIsInitialized = true;
                    if (!success) {
                        Log.e("MY_APP_TAG", "Unable to initialize Safe Browsing!");
                    }
                }
            });
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Code for WebView goes here
            }
        });
    }

    private void other() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            PackageInfo packageInfo = WebView.getCurrentWebViewPackage();
            Log.d(TAG, "WebView version: " + packageInfo.versionCode);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void viewPort() {
        WebSettings settings = webView.getSettings();
        //如果无法在 HTML 中设置视区的宽度, 则应调用 setUseWideViewPort () 以确保该页被赋予较大的视区
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }
}
