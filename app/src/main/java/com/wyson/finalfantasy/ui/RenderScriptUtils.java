package com.wyson.finalfantasy.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.FloatRange;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.Type;

/**
 * 如何使用？
 * 注意：
 * 使用之前最好降低图片质量，因为这是一个像素一个像素的处理
 * <p>
 * 1.
 * defaultConfig{}下
 * renderscriptTargetApi 19
 * renderscriptSupportModeEnabled true
 * <p>
 * <p>
 * 2.
 * 为了兼容国内定制系统，需要 copy android SDK/build-tools\27.0.3\renderscript\lib\packaged 到 jniLibs
 * gradle 中添加 jniLibs.srcDirs
 * sourceSets{
 * main{
 * jniLibs.srcDirs = ['libs']
 * }
 * }
 * 3. proguard-rules
 */
public class RenderScriptUtils {

    private RenderScript mRenderScript;

    public RenderScriptUtils(Context context) {
        this.mRenderScript = RenderScript.create(context);
    }

    /**
     * 模糊
     */
    public Bitmap blur(Bitmap original, @FloatRange(from = 0, to = 25f, fromInclusive = false, toInclusive = true) float radius) {

        Bitmap bitmap = Bitmap.createBitmap(original);
        //Rendscript没有使用VM来分配内存，需要使用Allocation类来创建和分配内存
        Allocation input = Allocation.createFromBitmap(mRenderScript, bitmap);
        //Type: 一个Type描述一个Allocation或者并行操作的Element和Dimensions
        Type type = input.getType();
        Allocation output = Allocation.createTyped(mRenderScript, type);

        // 创建一个模糊效果的RenderScript
        // 第二个参数Element相当于一种像素的处理方法，高斯模糊用这个就可以
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
        //设置模糊程度，25f是最大的
        blur.setRadius(radius);
        blur.setInput(input);
        //输出保存到刚才创建的输出内存中
        blur.forEach(output);
        //将数据填充到Bitmap中
        output.copyTo(bitmap);

        //销毁内存
        input.destroy();
        output.destroy();
        blur.destroy();
        type.destroy();
        return bitmap;
    }

    public void destory() {
        mRenderScript.destroy();
    }
}
