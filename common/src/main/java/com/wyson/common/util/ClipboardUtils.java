package com.wyson.common.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;


public class ClipboardUtils {

    /**
     * 长按自由复制
     * @param context 上下文
     * @param textView TextView
     */
    public static void copy(final Context context, final TextView textView){

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cd = ClipData.newPlainText("text", textView.getText().toString());
                cm.setPrimaryClip(cd);
                return false;
            }
        });
    }

}
