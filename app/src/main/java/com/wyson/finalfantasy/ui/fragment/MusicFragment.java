package com.wyson.finalfantasy.ui.fragment;

import android.os.Bundle;

import com.wyson.common.base.BaseFragment;
import com.wyson.finalfantasy.R;

public class MusicFragment extends BaseFragment {
    public static MusicFragment newInstance() {

        Bundle args = new Bundle();

        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int setupContentViewId() {
        return R.layout.fragment_music;
    }

    @Override
    protected void initView() {

    }
}
