package com.wyson.common.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Application 基类
 *
 * @author : Wuyson
 * @date : 2018/8/27-14:30
 */
public class BaseApplication extends Application {
    private static BaseApplication sBaseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseApplication = this;
    }

    public static Context getAppContext() {
        return sBaseApplication;
    }

    public static Resources getAppResource() {
        return sBaseApplication.getResources();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
