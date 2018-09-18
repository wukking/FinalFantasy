package com.wyson.common.util;

import android.graphics.Bitmap;
import android.util.LruCache;

public class CacheUtils {

    /**
     * 通过mMemoryCache.get(key)来获取缓存对象
     * 通过mMemoryCache.put(key,bitmap)来添加缓存对象
     */
    private static LruCache<String, Bitmap> mMemoryCache;

    public static void lru() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
//                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                return bitmap.getByteCount()/1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    public static void diskLru(){

    }
}
