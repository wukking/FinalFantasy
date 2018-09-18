package com.wyson.common.ing;

import android.os.Build;
import android.os.Environment;

import com.wyson.common.helper.LangHelper;
import com.wyson.common.helper.LogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;


/**
 * TODO: 2018/8/28 qmui搬运
 *
 * @author : Wuyson
 * @date : 2018/8/28-13:51
 */
public class DeviceHelper {
    private static final String TAG = "DeviceHelper";
    private final static String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_FLYME_VERSION_NAME = "ro.build.display.id";
    private static String sMiuiVersionName;
    private static String sFlymeVersionName;

    static {
        Properties properties = new Properties();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                properties.load(fileInputStream);
            } catch (Exception e) {
                LogHelper.printErrStackTrace(TAG, e, "read file error");
            } finally {
                LangHelper.close(fileInputStream);
            }
        }

        Class<?> clzSystemProperties = null;
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties");
            Method getMethod = clzSystemProperties.getDeclaredMethod("get", String.class);
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME);
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getLowerCaseName(Properties p, Method get, String key) {
        String name = p.getProperty(key);
        if (name == null) {
            try {
                name = (String) get.invoke(null, key);
            } catch (Exception ignored) {
            }
        }
        if (name != null) {
            name = name.toLowerCase();
        }
        return name;
    }
}

