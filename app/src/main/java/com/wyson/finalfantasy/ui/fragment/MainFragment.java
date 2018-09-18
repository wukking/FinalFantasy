package com.wyson.finalfantasy.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.wyson.common.base.BaseFragment;
import com.wyson.finalfantasy.R;
import com.wyson.finalfantasy.ui.adapter.AppFragmentMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * @author : Wuyson
 * @date : 2018/9/17-17:36
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.rv_data)
    RecyclerView rvData;
    private List<String> mList = new ArrayList<>();
    private AppFragmentMainAdapter mAdapter;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setupContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        String str = "";
        for (int i = 0; i <100; i++) {
            if (i%3==0) {
                str = "大家啊对了发饭店客房杰拉德减肥啦的风景啊了";
            }else {
                str = i+"";
            }
            mList.add("name"+str);
        }
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        StaggeredGridLayoutManager staggeredGridLm = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        mAdapter = new AppFragmentMainAdapter(mContext, mList);
        rvData.setLayoutManager(staggeredGridLm);
        rvData.setAdapter(mAdapter);
    }
}
