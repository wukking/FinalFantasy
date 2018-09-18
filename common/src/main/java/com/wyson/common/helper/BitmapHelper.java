package com.wyson.common.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

/**
 * ImageView资源主动回收
 *
 * @author : Wuyson
 * @date : 2018/5/22-15:14
 */
public class BitmapHelper {
    /**
     * 对ImageView中引用的图片资源回收
     *
     * @param imageView ImageView
     */
    private static void recycleImageViewBitMap(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
            recycleBitmapDrawable(bd);
        }
    }

    private static void recycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            recycleBitmap(bitmap);
        }
        bitmapDrawable = null;
    }

    private static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 对ImageView背景资源回收
     *
     * @param view ImageView
     */
    public static void recycleBackgroundBitmap(ImageView view) {
        if (view != null) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            recycleBitmapDrawable(bd);
        }
    }
}
