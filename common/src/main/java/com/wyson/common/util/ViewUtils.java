package com.wyson.common.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2017/12/13.
 */

public class ViewUtils {

    /**
     * 当小于这个值，可以认为没有滑动
     */
    public static int getTouchSlop(Context context){
        int scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        return scaledTouchSlop;
    }

    /**
     * 计算 该段时间的速度
     * @param millisecond  毫秒
     * @return 手机划过的像素
     */
    public static int[] getVelocity(int  millisecond){
        int[] velocity = new int[2];
        VelocityTracker vt = VelocityTracker.obtain();
        // 必须先执行compute
        vt.computeCurrentVelocity(millisecond);
        int xVelocity = (int) vt.getXVelocity();
        int yVelocity = (int) vt.getYVelocity();
        velocity[0] = xVelocity;
        velocity[1] = yVelocity;

        //使用完要清理回收
        vt.clear();
        vt.recycle();
        return velocity;
    }

    /**
     * GestureDetector  对View长按，双击等接管
     *
     */
    public static void gestureDetector(Context context, GestureDetector.OnGestureListener listener){
        GestureDetector gd = new GestureDetector(context,listener);
    }

    /**
     * Scroller  弹性滑动
     */
}
