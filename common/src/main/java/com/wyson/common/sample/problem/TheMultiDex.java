package com.wyson.common.sample.problem;

/**
 * 方法数超过65536
 @date : 2018/8/27-14:25
 @author : Wuyson
 */
public class TheMultiDex {
    //1.首先我们要在module的build.gradle的dependencies 中添加：
    // compile 'com.android.support:multidex:1.0.1'

    //2. defaultConfig 中添加：multiDexEnabled true

    //3. BaseApplication中重写
    //@Override
    //protected void attachBaseContext(Context base) {
    //  super.attachBaseContext(base);
    //  MultiDex.install(this);
    //}
}
