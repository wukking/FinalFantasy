package com.wyson.common.demo;

import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.widget.TextView;

public class OreoDemo {
    public static void autoSize(TextView textView){
        //TextView 8.0自适应大小
        int[] ints = {6,16,18};
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textView,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView,6,16,2, TypedValue.COMPLEX_UNIT_SP);
        TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes(textView,ints,TypedValue.COMPLEX_UNIT_SP);
    }
}
