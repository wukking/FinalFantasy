package com.wyson.common.support;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ResourceCompat {

    /**
     * 读取Assets下的文件
     */
    public String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
