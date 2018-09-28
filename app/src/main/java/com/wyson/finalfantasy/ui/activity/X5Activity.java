package com.wyson.finalfantasy.ui.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;

import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wyson.common.base.BaseWebActivity;
import com.wyson.finalfantasy.R;
import com.wyson.finalfantasy.util.X5DataHelper;


import java.io.File;

import butterknife.BindView;

import static com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS;
import static com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND;

/**
 * * 注意：
 * 1）X5禁止调用 ：webview.setLayerType()、webview.setDrawingCacheEnabled(true);
 * <p>
 * 其他使用点：
 * 1）自定义UA webSetting.setUserAgentString(webSetting.getUserAgentString() + APP_NAME_UA);
 *
 * @author : Wuyson
 * @date : 2018/9/21-15:44
 */
public class X5Activity extends BaseWebActivity {

    @BindView(R.id.x5_webview)
    WebView x5Webview;
    private String mUrl;
    private static final String INTENT_EXTRA_KEY_URL = "intent_extra_key_url";
    private X5DataHelper mDataHelper = new X5DataHelper();
    private ValueCallback<Uri> uploadFile;
    private android.webkit.ValueCallback<Uri[]> uploadFiles;
    private Uri imageUri;

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, X5Activity.class);
        intent.putExtra(INTENT_EXTRA_KEY_URL, url);
        context.startActivity(intent);
    }

    @Override
    public int setupContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //避免输入法界面弹出后遮挡输入光标的问题 ,可以在清单中 android:windowSoftInputMode="stateHidden|adjustResize"
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mUrl = getIntent().getStringExtra(INTENT_EXTRA_KEY_URL);

        setupX5WebViewClient();
        setupX5WebChromeClient();
        setupX5Settings();
    }

    private void setupX5Settings() {
        WebSettings webSetting = x5Webview.getSettings();
//        String ua = webSetting.getUserAgentString();
//        webSetting.setUserAgentString(ua+"/qwd");
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        //webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);

        x5Webview.setDownloadListener(new MyWebViewDownLoadListener());

        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
        x5Webview.addJavascriptInterface(new Js2App(), "JS2APP");

        x5Webview.loadUrl(mUrl);

        // 注册ContextMenu
        registerForContextMenu(x5Webview);
    }

    private void setupX5WebChromeClient() {
        x5Webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadFile = valueCallback;
                openFileChooseProcess(acceptType);
            }

            //Lollipop
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                uploadFiles = valueCallback;
                try {
                    openFileChooseProcess(fileChooserParams.getAcceptTypes()[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    openFileChooseProcess("*/*");
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView webView, int progress) {
                if (progress == 100) {
                    //隐藏进度条
                } else {
                    //显示进度条，setMax,setProgress
                }
            }

            @Override
            public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsConfirm(webView, s, s1, jsResult);
            }

            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
            }
        });
    }

    //onPageStarted()会调用两次，用dialog要先判空，为空时才show；用GONE和VISIBLE随意
    private void setupX5WebViewClient() {
        x5Webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                if (!s.startsWith("http")) {
                    //不是Http的链接，其他应用打开
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        e.printStackTrace();
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, s);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
                if (mDataHelper.hasLocalResource(s)) {
                    WebResourceResponse response = mDataHelper.getReplaceWebResourceResponse(mContext, s);
                    if (response != null) {
                        return response;
                    }
                }
                return super.shouldInterceptRequest(webView, s);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                String url = webResourceRequest.getUrl().toString();
                if (mDataHelper.hasLocalResource(url)) {
                    WebResourceResponse response = mDataHelper.getReplaceWebResourceResponse(mContext, url);
                    if (response != null) {
                        return response;
                    }
                }
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                Logger.i("显示进度条");
                showProgressDialog("加载中...");
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                Logger.i("关闭进度条");
                closeProgressDialog();
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                Logger.i("关闭进度条，网页加载失败");
                closeProgressDialog();
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();//接受所有证书
            }
        });
    }


    private static final String ACCEPT_TYPE_VIDEO = "video";
    private static final String ACCEPT_TYPE_IMAGE = "image";
    private static final int INTENT_REQUEST_CODE_IMAGE = 1;
    private static final int INTENT_REQUEST_CODE_VIDEO = 2;
    private static final int INTENT_REQUEST_CODE_OTHER = 3;

    private void openFileChooseProcess(String acceptType) {
        if (TextUtils.isEmpty(acceptType)) {
            acceptType = "*/*";
        }

        if (acceptType.contains(ACCEPT_TYPE_VIDEO)) {
            recordVideo();
        } else if (acceptType.contains(ACCEPT_TYPE_IMAGE)) {
            takePhoto();
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(acceptType);
            startActivityForResult(Intent.createChooser(intent, "test"), INTENT_REQUEST_CODE_OTHER);
        }
    }

    private void takePhoto() {
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SystemClock.currentThreadTimeMillis() + ".jpg");
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(mContext, getPackageName() + ".fileprovider", fileUri);
            Log.d("mytest", "imageUri:" + imageUri);
        }
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCamera, INTENT_REQUEST_CODE_IMAGE);
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        //限制时长
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        //开启摄像机
        startActivityForResult(intent, INTENT_REQUEST_CODE_VIDEO);
    }


    static class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(final String url, String userAgent, final String contentDisposition, String mimetype, long contentLength) {
            try {
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                // 不过如果是要访问“www.baidu.com”这样的网址或者纯文字时，会报ActivityNotFoundException错误。
//                // 此时需要设置如下属性，就可以正常跳转了：
//                // 此处指定系统自带浏览器包名和Activity名称.
//                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//                startActivity(intent);

                Log.d("mytest", "userAgent:" + userAgent + " contentDisposition:" + contentDisposition + " mimetype:" + mimetype + " contentLength" + contentLength);
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);

                if (fileName != null && fileName.contains(".apk") && !mimetype.equals("application/vnd.android.package-archive")) {
                    mimetype = "application/vnd.android.package-archive";
                }

                final String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qwd/download/" + fileName;

                final String finalType = mimetype;
                // TODO: 2018/9/25  请求读取权限下载APK
//                PermissionManager.performCodeWithPermission(mContext, "为了不影响您的正常使用，请您去设置中开启 允许程序读写存储 权限",
//                        new RequestPermissionCallback() {
//                            @Override
//                            public void hasPermission() {
//                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/qwd/download/");
//                                if (!file.exists()) {
//                                    file.mkdirs();
//                                }
//                                startDown(url, destPath, finalType);
//                            }
//                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Js2App {
        @JavascriptInterface
        public void getAppMethod(String flg, String val) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTENT_REQUEST_CODE_IMAGE:
                    if (null != uploadFile) {
                        Uri uri = data == null ? null : data.getData();
                        uploadFile.onReceiveValue(uri);
                        uploadFile = null;
                    }
                    if (null != uploadFiles) {
                        onActivityResultAboveL(requestCode, resultCode, data);
                    }
                    break;
                case INTENT_REQUEST_CODE_VIDEO:
                    try {
                        if (null != uploadFile) {
                            Uri result = data == null ? null : data.getData();
                            uploadFile.onReceiveValue(result);
                            uploadFile = null;
                        }
                        if (null != uploadFiles) {
                            Uri result = data == null ? null : data.getData();
                            uploadFiles.onReceiveValue(new Uri[]{result});
                            uploadFiles = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case INTENT_REQUEST_CODE_OTHER:
                    break;
                default:
                    break;
            }
        } else {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(Uri.EMPTY);
                uploadFile = null;
            }
            if (null != uploadFiles) {
                uploadFiles.onReceiveValue(new Uri[]{});
                uploadFiles = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != INTENT_REQUEST_CODE_IMAGE || uploadFiles == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        uploadFiles.onReceiveValue(results);
        uploadFiles = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (x5Webview != null && x5Webview.canGoBack()){
                    x5Webview.goBack();
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (x5Webview != null) {
            x5Webview.destroy();
        }
    }
}
