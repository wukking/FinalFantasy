package com.wyson.finalfantasy.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.wyson.common.base.BaseActivity;
import com.wyson.finalfantasy.R;

public class SpalshActivity extends BaseActivity {

    @Override
    public int setupContentViewId() {
        return R.layout.activity_spalsh;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            moveTaskToBack(true);
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
