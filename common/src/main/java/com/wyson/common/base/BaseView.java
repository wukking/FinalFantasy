package com.wyson.common.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class BaseView extends View {
    private Context mContext;
    private static final int DEFAULT_SIZE = 200;

    public BaseView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }


    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(reMeasure(widthMeasureSpec), reMeasure(heightMeasureSpec));
    }

    private int reMeasure(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            result = Math.min(DEFAULT_SIZE, size);
        } else {
            result = DEFAULT_SIZE;
        }
        return result;
    }
}
