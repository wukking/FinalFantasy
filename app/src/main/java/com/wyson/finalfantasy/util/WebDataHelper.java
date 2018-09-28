package com.wyson.finalfantasy.util;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WebDataHelper {
    private Map<String,String> mMap;

    public WebDataHelper() {
        mMap = new HashMap<>();
        initData();
    }

    private void initData() {
        String imageDir = "images/";
        String pngSuffix = ".png";
    }

    public boolean hasLocalResource(String url){
        return mMap.containsKey(url);
    }

    public WebResourceResponse getReplaceWebResourceResponse(Context context,String url){
        String localResourcePath = mMap.get(url);
        if (TextUtils.isEmpty(localResourcePath)){
            return null;
        }
        InputStream is = null;
        try {
            is = context.getApplicationContext().getAssets().open(localResourcePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String mimeType;
        if (url.contains("css")){
            mimeType = "text/css";
        }else if (url.contains("jpg")){
            mimeType = "image/jpeg";
        }else {
            mimeType = "image.png";
        }
        WebResourceResponse response = new WebResourceResponse(mimeType, "UTF-8", is);
        return response;
    }
}
