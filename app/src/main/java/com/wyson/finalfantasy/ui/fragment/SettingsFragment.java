package com.wyson.finalfantasy.ui.fragment;

import android.os.Bundle;

import com.wyson.common.base.BaseFragment;
import com.wyson.finalfantasy.R;

public class SettingsFragment extends BaseFragment {
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
}
