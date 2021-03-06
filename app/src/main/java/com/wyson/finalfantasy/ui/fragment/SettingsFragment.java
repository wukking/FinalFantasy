package com.wyson.finalfantasy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.soter.wrapper.SoterWrapperApi;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessAuthenticationResult;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessKeyPreparationResult;
import com.tencent.soter.wrapper.wrap_fingerprint.SoterFingerprintCanceller;
import com.tencent.soter.wrapper.wrap_fingerprint.SoterFingerprintStateCallback;
import com.tencent.soter.wrapper.wrap_task.AuthenticationParam;
import com.wyson.common.base.BaseFragment;
import com.wyson.finalfantasy.R;
import com.wyson.finalfantasy.ui.activity.web.BrowserActivity;
import com.wyson.finalfantasy.ui.activity.web.WebActivity;
import com.wyson.finalfantasy.ui.activity.web.X5Activity;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.tv_x5)
    TextView tvX5;
    @BindView(R.id.tv_vas)
    TextView tvVas;
    @BindView(R.id.tv_vas_pre)
    TextView tvVasPre;
    @BindView(R.id.tv_vas_reset)
    TextView tvVasReset;
    @BindView(R.id.tv_rich)
    TextView tvRich;
    Unbinder unbinder;

    private static final String DEMO_URL = "https://www.bilibili.com/video/av9467312?from=search&seid=2633469376916998331";
    private boolean mPreStatus = false;
    private AlertDialog mAlertDialog;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setupContentViewId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.tv_original,R.id.tv_x5, R.id.tv_vas,R.id.tv_vas_pre,R.id.tv_vas_reset,R.id.tv_vas_soter,R.id.tv_rich})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_original:
                WebActivity.startAction(mContext,DEMO_URL);
                break;
            case R.id.tv_x5:
                X5Activity.startActivity(mContext, DEMO_URL);
                break;
            case R.id.tv_vas:
                BrowserActivity.startAction(mContext, DEMO_URL, BrowserActivity.MODE_SONIC);
                break;
            case R.id.tv_vas_pre:
                if (!mPreStatus){
                    SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
                    sessionConfigBuilder.setSupportLocalServer(true);
                    mPreStatus = SonicEngine.getInstance().preCreateSession(DEMO_URL, sessionConfigBuilder.build());
                    Logger.e(mPreStatus+"");
                }
                showColorToast(mPreStatus ?"预加载打开成功":"已经成功预加载了");
                break;
            case R.id.tv_vas_reset:
                SonicEngine.getInstance().cleanCache();
                showColorToast("清除成功");
                break;
            case R.id.tv_vas_soter:
                soter();
                break;
            case R.id.tv_rich:
                richText();
                break;
            default:
                break;
        }
    }

    private void richText() {
        RichText.initCacheDir(mContext);
        RichText.from("file:///android_asset/testJs").into(tvRich);
    }

    private void soter(){
        SoterWrapperApi.prepareAuthKey(new SoterProcessCallback<SoterProcessKeyPreparationResult>() {
            @Override
            public void onResult(@NonNull SoterProcessKeyPreparationResult result) {
                if (result.isSuccess()) {
                    startSoter();
                }
            }
        },false, true, 0, null, null);
    }

    private void startSoter(){
        AuthenticationParam param = new AuthenticationParam.AuthenticationParamBuilder()
                .setScene(0)
                .setContext(mContext)
                .setFingerprintCanceller(new SoterFingerprintCanceller())
                .setPrefilledChallenge("test challenge")
                .setSoterFingerprintStateCallback(new SoterFingerprintStateCallback() {
                    @Override
                    public void onStartAuthentication() {
                        mAlertDialog = new AlertDialog.Builder(mContext).setMessage("请放置指纹")
                                .setCancelable(false).create();
                        mAlertDialog.show();
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        showColorToast("onAuthenticationHelp");
                    }

                    @Override
                    public void onAuthenticationSucceed() {
                        mAlertDialog.dismiss();
                        showColorToast("指纹验证成功");
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        showColorToast("指纹验证失败");
                    }

                    @Override
                    public void onAuthenticationCancelled() {
                        showColorToast("指纹验证取消");
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errorString) {
                        showColorToast("指纹验证错误");
                    }
                }).build();

        SoterWrapperApi.requestAuthorizeAndSign(new SoterProcessCallback<SoterProcessAuthenticationResult>() {
            @Override
            public void onResult(@NonNull SoterProcessAuthenticationResult result) {

            }
        }, param);
    }
}
