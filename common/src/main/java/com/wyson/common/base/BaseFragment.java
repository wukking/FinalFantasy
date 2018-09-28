package com.wyson.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.wyson.common.support.ToastCompat;
import com.wyson.common.util.progress.ProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : Wuyson
 * @date : 2018/8/27-14:07
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    private Unbinder mButterKnife;
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(setupContentViewId(), container, false);
        }
        mButterKnife = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @LayoutRes
    protected abstract int setupContentViewId();

    protected abstract void initView();


    //------------    Progress  ---------------//

    protected void showDefaultDialog(Activity activity) {
        ProgressDialog.showDefaultLoading(activity);
    }

    protected void showProgressDialog(Activity activity, String msg) {
        ProgressDialog.showDialogForLoading(activity, msg, new BallSpinFadeLoaderIndicator());
    }

    protected void closeProgressDialog() {
        ProgressDialog.dismissProgressDialog();
    }

    //--------------   Toast  ----------------------//

    /**
     * 短暂显示Toast提示(来自String)
     **/
    protected void showShortToast(String message) {
        ToastCompat.showShort(message);
    }

    /**
     * 短暂显示Toast提示(来自res)
     **/
    protected void showShortToast(int resId) {
        ToastCompat.showShort(resId);
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String message) {
        ToastCompat.showLong(message);
    }

    /**
     * 长时间显示Toast提示(来自res)
     **/
    public void showLongToast(int resId) {
        ToastCompat.showLong(resId);
    }

    /**
     * 带图片的toast
     */
    public void showToastWithImg(String message, int resId) {
        ToastCompat.showToastWithImg(message, resId);
    }
    /**
     * 带图片的toast
     */
    public void showColorToast(String message) {
        ToastCompat.showColorToast(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mButterKnife.unbind();
    }
}
