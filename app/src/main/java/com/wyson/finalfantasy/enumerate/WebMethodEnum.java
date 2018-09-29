package com.wyson.finalfantasy.enumerate;

public enum WebMethodEnum {

    //无效识别码
    None(-1, "无效识别码"),
    TOAST(1, "Toast");

    private int code;
    private String desc;

    WebMethodEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean isValid(Integer code) {
        for (WebMethodEnum e : values()) {
            if (e.code == code) {
                return true;
            }
        }
        return false;
    }

    public static WebMethodEnum valueOf(Integer code) {
        if (code == null) {
            return None;
        }
        for (WebMethodEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        //自定义一个
        return None;
    }

    /**
     * ----------GET  **  SET---------
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
