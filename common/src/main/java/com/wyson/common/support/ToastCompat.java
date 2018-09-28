package com.wyson.common.support;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyson.common.R;
import com.wyson.common.base.BaseApplication;


/**
 * Toast统一管理类
 */
public class ToastCompat {

    private static Toast toast;
    private static Toast toast2;
    private static Toast toast3;

    private static Toast initToast(CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getAppContext(), message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        initToast(message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(@StringRes int strResId) {
        initToast(BaseApplication.getAppContext().getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        initToast(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    public static void showLong(@StringRes int strResId) {
        initToast(BaseApplication.getAppContext().getResources().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context  上下文
     * @param strResId 文本资源Id
     * @param duration 显示时间
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context.getResources().getText(strResId), duration).show();
    }

    /**
     * 显示有image的toast
     *
     * @param message  文本信息
     * @param imgResId 图片ResId
     * @return  A Toast
     */
    public static Toast showToastWithImg(String message, int imgResId) {
        if (toast2 == null) {
            toast2 = new Toast(BaseApplication.getAppContext());
        }
        View view = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.toast_custom, null);
        TextView tv = view.findViewById(R.id.toast_custom_tv);
        tv.setText(TextUtils.isEmpty(message) ? "" : message);
        ImageView iv = view.findViewById(R.id.toast_custom_iv);
        if (imgResId > 0) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(imgResId);
        } else {
            iv.setVisibility(View.GONE);
        }
        toast2.setView(view);
        toast2.setGravity(Gravity.CENTER, 0, 0);
        toast2.show();
        return toast2;
    }

    public static Toast showColorToast(String message) {
        if (toast3 == null) {
            toast3 = new Toast(BaseApplication.getAppContext());
        }
        View view = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.toast_custom_bg, null);
        TextView tv = view.findViewById(R.id.toast_custom_tv);
        tv.setText(TextUtils.isEmpty(message) ? "" : message);
        toast3.setView(view);
        toast3.setGravity(Gravity.BOTTOM, 0, 180);
        toast3.show();
        return toast3;
    }
}
