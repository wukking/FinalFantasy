package com.wyson.finalfantasy.app;

import android.support.annotation.NonNull;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.soter.wrapper.SoterWrapperApi;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessNoExtResult;
import com.tencent.soter.wrapper.wrap_task.InitializeParam;
import com.wyson.common.base.BaseApplication;

/**
 * 应用Application
 *
 * @author : Wuyson
 * @date : 2018/8/27-14:33
 */
public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Logger.d("CoreInitFinished");
                //com.tencent.smtt.sdk.CookieManager和com.tencent.smtt.sdk.CookieSyncManager的相关接口要在内核加载完之后调用
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Logger.d(b+"");
            }
        });

        InitializeParam param = new InitializeParam.InitializeParamBuilder()
                // 场景值常量，后续使用该常量进行密钥生成或指纹认证
                .setScenes(0)
                .build();
        SoterWrapperApi.init(this,
                new SoterProcessCallback<SoterProcessNoExtResult>() {
                    @Override
                    public void onResult(@NonNull SoterProcessNoExtResult result) {

                    }
                },
                param);
    }
}
