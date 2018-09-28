package com.wyson.finalfantasy.ui.fragment;

import android.os.Bundle;

import com.wyson.common.base.BaseFragment;
import com.wyson.finalfantasy.R;

public class GalleryFragment extends BaseFragment {
    public static GalleryFragment newInstance() {

        Bundle args = new Bundle();

        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int setupContentViewId() {
        return R.layout.fragment_gallery;
    }

    @Override
    protected void initView() {}

}
