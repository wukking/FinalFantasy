package com.wyson.common.util;

public class ObjectUtills {
    /**
     * Object安全转换int
     * @param value obj对象
     * @param defaultValue 默认值
     * @return int值
     */
    public static final int convert2Int(Object value,int defaultValue){
        if (value == null || "".equals(value.toString().trim())){
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString().trim());
        } catch (NumberFormatException e) {
            try {
                return Double.valueOf(value.toString().trim()).intValue();
            } catch (NumberFormatException e1) {
                return defaultValue;
            }
        }
    }
}
