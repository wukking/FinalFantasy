package com.wyson.common.ing;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * sharedPreferences工具类
 * Created by wuyson on 2017/7/11.
 */

/**
 * *********PlatformListActivity*********
 * platformList：列表数据
 */

public class SPUtils {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static final String FILE_NAME = "sp_data_";

    public SPUtils(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public SPUtils(Context context, int mode, String filename) {
        sp = context.getSharedPreferences(FILE_NAME + filename, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 向SP存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public <T> void putDataList(String tag, List<T> datalist) {
   /*     if (null == datalist || datalist.size() <= 0)
            return;*/

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        Log.e("写入LIst数据到SP", "putDataList: " + strJson);
        editor.commit();
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist = new ArrayList<T>();
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;
    }


    /**
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    /**
     * 清空SP里所以数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 判断SP是否包含特定key的数据
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

}
