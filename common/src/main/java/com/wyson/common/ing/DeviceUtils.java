package com.wyson.common.ing;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

/**
 * @author: Wuyson
 * @date: 2018/1/2 - 11:16
 * @description: 设备信息
 */

public class DeviceUtils {

    private static int versionCode = 0;

    /**
     * 是否是android 2.3 以上
     * @return
     */
    private static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * 严格模式
     *
     * 1.正式版本不要开启
     * 2.调用JNI实现的磁盘读写操作和网络操作不会激活StrictMode
     *
     */
    public static void strictMode(){
        if (hasGingerbread()){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskReads()
                    .detectNetwork()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    /**
     * 是否有 SDCard
     * @return true：有 ，false: 没有
     */
    public static boolean hasSDCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // 获取系统版本信息还可以通过android.os.Build来获取

    /**
     * 获取 android 版本
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        if (versionCode > 0){
            return versionCode;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageInfo.INSTALL_LOCATION_AUTO);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0.0";
        }
    }

    /**
     * 获取系统名
     * @return
     */
    public static String getSysName() {
        String romType = Build.PRODUCT;
        return romType;
    }

}
