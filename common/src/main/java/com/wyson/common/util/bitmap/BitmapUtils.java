package com.wyson.common.util.bitmap;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;

import com.wyson.common.util.DisplayUtils;

import java.io.FileDescriptor;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    public static Bitmap createPureColorBitmap(String colorString,int width,int height){
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //"#F8F8F8"
        bitmap.eraseColor(Color.parseColor(colorString));
        return bitmap;
    }

    /**
     * 首选这个
     */
    public static Bitmap decodeBitmapFromResource(Context context, @IdRes int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return bitmap;
    }

    /**
     * 直接传入inSampleASize
     */
    public static Bitmap decodeBitmapBySample(Context context, int resId, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //原始图片信息
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        int width = options.outWidth;
        int height = options.outHeight;
        String mimeType = options.outMimeType;
        Log.d(TAG, "BitmapInfo: width = " + width + "  height = " + height + "  mimeType = " + mimeType);

        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return bitmap;
    }

    /**
     * 输入长宽自动计算inSample值来压缩图片
     */
    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        // 如果在 Honeycomb 或更新版本系统中运行，尝试使用 inBitmap
        //使用 inBitmap 重复利用内存空间，避免重复开辟新内存
//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB) {
//            addInBitmapOptions(options, cache);
//        }
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampleBitmapFromResource(FileDescriptor fd, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

//    private static void addInBitmapOptions(BitmapFactory.Options options,
//                                           ImageCache cache) {
//        options.inMutable = true;
//        if (cache != null) {
//            Bitmap inBitmap = cache.getBitmapFromReusableSet(options);
//            if (inBitmap != null) {
//                options.inBitmap = inBitmap;
//            }
//        }
//    }

    /**
     * 图片转出 RGB_565
     * @param context 上下文
     * @param id    图片Id
     * @param reqWidth  宽度
     * @param reqHeight 高度
     * @return Bitmap
     */
    public static Bitmap bitmap2RGB_565(Context context, @DrawableRes int id, int reqWidth, int reqHeight) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        if (drawable != null) {
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 :
                    Bitmap.Config.RGB_565;
            return Bitmap.createBitmap(reqWidth, reqHeight, config);
        }
        return null;
    }

    /**
     * Bitmap 转成 Drawable
     */
    public static BitmapDrawable bitmap2Drawable(Resources resources, Bitmap bitmap){
        return new BitmapDrawable(resources,bitmap);
    }

    /**
     * 修改一些特别（带有圆角）Drawable里的颜色
     *  tintDrawable(holder.tvStatus.getBackground(), ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.status_green)));
     */
    private static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 获取当前View的Bitmap
     */
    public static Bitmap getBitmap(View view) {
        Bitmap bmp = view.getDrawingCache();
        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 获取当前View截图
     */
    public static Bitmap snapView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Log.e(TAG, "snapView: " + bmp.getWidth());
        int width = DisplayUtils.getViewWidth(view);
        int height = DisplayUtils.getViewHeight(view);
        Log.e(TAG, "snapView: width = " + width + "height=" + height);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width,
                height);
        view.destroyDrawingCache();
        return bp;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
