package com.wyson.common.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/1/2.
 */

public class FileUtils {
    /**
     * 创建文件
     * @param filePath 路径
     */
    public static void mkDirs(String filePath){
        File file = new File(filePath);
        try{
            if (!file.exists()){
                file.mkdirs();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     * @param filePath 路径
     * @param fileName 名称
     */
    public static void mkFile(String filePath ,String fileName){
        File file = new File(filePath,fileName);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
