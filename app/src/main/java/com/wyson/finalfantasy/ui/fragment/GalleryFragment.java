package com.wyson.finalfantasy.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wyson.common.base.BaseFragment;
import com.wyson.finalfantasy.R;
import com.wyson.finalfantasy.ui.activity.X5Activity;

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
    protected void initView() {
        TextView tvText = (TextView) mRootView.findViewById(R.id.tv_text);
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                X5Activity.startActivity(mContext,"http://www.baidu.com");
            }
        });
    }
}
