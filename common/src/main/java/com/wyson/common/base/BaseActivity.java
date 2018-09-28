package com.wyson.common.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.wyson.common.R;
import com.wyson.common.support.AppManager;
import com.wyson.common.util.progress.LoadingDialog;
import com.wyson.common.support.StatusBarCompat;
import com.wyson.common.support.ToastCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : Wuyson
 * @date : 2018/8/27-14:04
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = this.getClass().getSimpleName();
    private Unbinder mButterKnife;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBeforeContentView();
        setContentView(setupContentViewId());
        mButterKnife = ButterKnife.bind(this);
        mContext = this;
        initView(savedInstanceState);
    }

    protected void setupBeforeContentView() {
        AppManager.getInstance().addActivity(this);
        //AppCompatActivity无效，Activity中才有效,
        // 设置Application主题去掉Actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStatusBarColor();
    }


    /**
     * 设置布局文件
     */
    public abstract @LayoutRes int setupContentViewId();

    protected abstract void initView(Bundle savedInstanceState);

    //--------------  状态栏  ------------------//

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void setStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
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

    //----------------   Progress  -----------------//
    /**
     * 开启浮动加载进度条
     */
    protected void showProgressDialog(String message) {
        if (LoadingDialog.mLoadingDialog == null){
            LoadingDialog.showDialogForLoading(this, message, true);
        }
    }

    /**
     * 停止浮动加载进度条
     */
    protected void closeProgressDialog() {
        if (LoadingDialog.mLoadingDialog != null){
            LoadingDialog.cancelDialogForLoading();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mButterKnife.unbind();
        AppManager.getInstance().finishActivity(this);
    }
}
