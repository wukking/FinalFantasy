package com.wyson.common.support;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Activity 管理
 *
 * @author : Wuyson
 * @date : 2018/8/27-15:27
 */
public class AppManager {
    private static Stack<Activity> sActivityStack;
    private volatile static AppManager instance;

    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                    sActivityStack = new Stack<>();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (sActivityStack == null) {
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurActivity() {
        try {
            return sActivityStack.lastElement();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    public Activity getPreActivity() {
        int index = sActivityStack.size() - 2;
        if (index < 0) {
            return null;
        }
        return sActivityStack.get(index);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurActivity() {
        Activity activity = sActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            sActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            sActivityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < sActivityStack.size(); i++) {
            if (null != sActivityStack.get(i)) {
                sActivityStack.get(i).finish();
            }
        }
        sActivityStack.clear();
    }

    /**
     * 返回到指定的activity
     */
    public void return2Activity(Class<?> cls) {
        while (sActivityStack.size() > 0) {
            if (sActivityStack.peek().getClass() == cls) {
                break;
            } else {
                finishActivity(sActivityStack.peek());
            }
        }
    }

    /**
     * 是否已经打开指定的activity
     */
    public boolean isOpenAcitivity(Class<?> cls) {
        if (sActivityStack != null) {
            for (int i = 0; i < sActivityStack.size(); i++) {
                if (cls == sActivityStack.get(i).getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public void exitApp(Context context, boolean isBackground) {
        try {
            finishAllActivity();
            ActivityManager activityMgr
                    = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //2.2就废弃了，也不知道有用没用，
            activityMgr.restartPackage(context.getPackageName());
        } catch (Exception e) {

        } finally {
            if (!isBackground) {
                System.exit(0);
            }
        }
    }
}
