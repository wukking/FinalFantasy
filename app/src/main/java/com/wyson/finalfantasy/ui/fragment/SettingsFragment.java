package com.wyson.finalfantasy.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.wyson.common.base.BaseFragment;
import com.wyson.finalfantasy.R;
import com.wyson.finalfantasy.ui.activity.BrowserActivity;
import com.wyson.finalfantasy.ui.activity.X5Activity;

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
    Unbinder unbinder;

    private static final String DEMO_URL = "http://onem.qiulinb.cn/ajax/regist?s=yimi079";
    private boolean mPreStatus = false;

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

    @OnClick({R.id.tv_x5, R.id.tv_vas,R.id.tv_vas_pre,R.id.tv_vas_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            default:
                break;
        }
    }
}
