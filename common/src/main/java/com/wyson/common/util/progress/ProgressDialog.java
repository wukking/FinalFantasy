package com.wyson.common.util.progress;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.wyson.common.R;

/**
 * @author: Wuyson
 * @date: 2017/12/15 - 14:28
 * @description: 进度加载
 */

public class ProgressDialog {
    public static Dialog loadingDialog;

    /**
     * 普通Dialog
     *
     * @param context   Activity
     * @param tip       提示语
     * @param indicator 加载样式：Indicator子类
     * @return 加载Dialog
     */
    public static Dialog showDialogForLoading(Context context, String tip, Indicator indicator) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_loading_av, null);
        AVLoadingIndicatorView cLoading = view.findViewById(R.id.c_loading);
        cLoading.setIndicator(indicator);
        TextView tvLoadingTip = view.findViewById(R.id.tv_loading_tip);
        tvLoadingTip.setText(tip);
        loadingDialog = new Dialog(context, R.style.CustomProgressDialog);

        loadingDialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 默认加载Dialog
     *
     * @param context activity
     * @return Dialog
     */
    public static Dialog showDefaultLoading(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_loading_av, null);
        AVLoadingIndicatorView cLoading = view.findViewById(R.id.c_loading);
        TextView tvLoadingTip = view.findViewById(R.id.tv_loading_tip);

        cLoading.setIndicator(new BallSpinFadeLoaderIndicator());
        tvLoadingTip.setText("加载中...");
        loadingDialog = new Dialog(context, R.style.CustomProgressDialog);

        loadingDialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 关闭DIalog
     */
    public static void dismissProgressDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }
}
