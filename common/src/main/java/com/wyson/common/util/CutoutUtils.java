package com.wyson.common.util;//package com.wuyson.common.utils;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.graphics.Rect;
//import android.util.Log;
//import android.view.DisplayCutout;
//import android.view.View;
//import android.view.WindowManager;
//
//import java.util.List;
//
///**
// * 页面有状态栏时，一般使用默认Default模式就可以，避免刘海部分不要有操作
// *
// * @author : Administrator
// * @date : 2018/6/11-10:45
// *
// * Tips: 如果刘海屏左右有颜色，设置状态栏颜色为透明
// */
//public class CutoutUtils {
//    private static final String TAG = "DisplayCutout";
//
//    @TargetApi(28)
//    public static void getDisplayCutoutArray(Activity activity) {
//        View decorView = activity.getWindow().getDecorView();
//        DisplayCutout displayCutout = decorView.getRootWindowInsets().getDisplayCutout();
//
//        Log.e(TAG, "getDisplayCutout: 安全区域距离屏幕左边的距离px " + displayCutout.getSafeInsetLeft());
//        Log.e(TAG, "getDisplayCutout: 安全区域距离屏幕上边的距离px" + displayCutout.getSafeInsetTop());
//        Log.e(TAG, "getDisplayCutout: 安全区域距离屏幕右边的距离px" + displayCutout.getSafeInsetRight());
//        Log.e(TAG, "getDisplayCutout: 安全区域距离屏幕下边的距离px" + displayCutout.getSafeInsetBottom());
//
//        List<Rect> rects = displayCutout.getBoundingRects();
//        if (rects == null || rects.size() == 0) {
//            Log.e(TAG, "getDisplayCutout: " + "不是刘海屏");
//        } else {
//            Log.e(TAG, "getDisplayCutout: " + "是刘海屏");
//            for (Rect rect : rects) {
//                Log.e(TAG, "getDisplayCutout: 刘海的大小 " + rect);
//            }
//        }
//    }
//
//    @TargetApi(28)
//    public static List<Rect> getDisplayCutoutRect(Activity activity) {
//        View decorView = activity.getWindow().getDecorView();
//        DisplayCutout displayCutout = decorView.getRootWindowInsets().getDisplayCutout();
//        return displayCutout.getBoundingRects();
//    }
//
//    /**
//     * 刘海屏适配的区域显示颜色为 : colorPrimaryDark -> colorPrimary
//     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT : 自适应刘海（默认）
//     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER : 不适配刘海，页面在刘海下显示，不延伸上去
//     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 页面延伸到刘海上
//     */
//    public static void suitDisplayCutout(Activity activity) {
//        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//        activity.getWindow().setAttributes(lp);
//    }
//}
