package com.wyson.common.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author: Wuyson
 * @date: 2017/12/4 - 10:45
 * @description: 输入法工具类
 *                  1. android:focusable="true" 和 android:focusableInTouchMode="true"
 *                  2. 获取焦点可以使用 requestFocus()
 *                  3. 布局必须加载完成（1. Activity 上，设置 android:windowSoftInputMode 属性来完成
 *                                            eg : android:windowSoftInputMode="stateHidden|adjustPan"
 *                                       2. View.postDelayed()）
 *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
 * 1. 显示软键盘 showSoftInput()
 * 2. 隐藏软键盘 hideSoftInputFromWindow(View.getWindowToken()，0)
 *          ：只需要传递一个存在于当前布局 ViewTree 中，随意一个 View 的 windowToken 就可以了
 *
 * ViewTreeObserver监听软键盘变化： https://www.cnblogs.com/shelly-li/p/5639833.html
 */


public class KeyboardUtils {
    /**
     * 显示软键盘
     * @param view
     */
    public static void showSoftKeyboard(View view){
        InputMethodManager imm
                = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            view.requestFocus();
            imm.showSoftInput(view,0);
        }
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    public static void hideSoftKeyboard(View view){
        InputMethodManager imm
                = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    /**
     * 自动判断关闭还是打开（基本不用，无法根据imm判断当前的状态）
     * @param view
     */
    public static void toggleKeyboard(View view){
        InputMethodManager imm
                = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.toggleSoftInput(0,0);
        }
    }
}
