package com.wyson.finalfantasy.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.orhanobut.logger.Logger;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.wyson.common.base.BaseActivity;
import com.wyson.common.support.AppManager;
import com.wyson.common.util.DisplayUtils;
import com.wyson.common.util.bitmap.BitmapUtils;
import com.wyson.finalfantasy.R;
import com.wyson.finalfantasy.app.AppConstant;
import com.wyson.finalfantasy.impl.TabEntity;
import com.wyson.finalfantasy.ui.activity.vas.SonicRuntimeImpl;
import com.wyson.finalfantasy.ui.fragment.GalleryFragment;
import com.wyson.finalfantasy.ui.fragment.MainFragment;
import com.wyson.finalfantasy.ui.fragment.MusicFragment;
import com.wyson.finalfantasy.ui.fragment.SettingsFragment;
import com.wyson.finalfantasy.util.RenderScriptUtils;

import java.util.ArrayList;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author : Wuyson
 * @date : 2018/9/17-16:08
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.bottom_tab)
    CommonTabLayout bottomTab;

    private String[] mTitleArray = {"首页", "相册", "音乐", "设置"};
    private ArrayList<CustomTabEntity> mTabEntityList = new ArrayList<>();
    private int[] mIconSelArray = {R.drawable.ic_main,
            R.drawable.ic_gallery, R.drawable.ic_music, R.drawable.ic_settings};
    private int[] mIconUnselArray = {R.drawable.ic_main_default, R.drawable.ic_gallery_default,
            R.drawable.ic_music_default, R.drawable.ic_settings_default};
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private Fragment mMainFragment;
    private Fragment mGalleryFragment;
    private Fragment mMusicFragment;
    private Fragment mSettingsFragment;

    private static final String TAG_MAIN = "mMainFragment";
    private static final String TAG_GALLERY = "mGalleryFragment";
    private static final String TAG_MUSIC = "mMusicFragment";
    private static final String TAG_SETTINGS = "mSettingsFragment";
    private FragmentManager mFm;
    private RenderScriptUtils mRenderScriptUtils;

    @Override
    public int setupContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bitmap whiteBg = Bitmap.createBitmap(DisplayUtils.getRealScreenWidth(this),
                DisplayUtils.getActionBarSize(this), Bitmap.Config.ARGB_8888);
        whiteBg.eraseColor(Color.parseColor("#F8F8F8"));
        mRenderScriptUtils = new RenderScriptUtils(this);
        Bitmap blurBg = mRenderScriptUtils.blur(whiteBg, 25);
        bottomTab.setBackground(BitmapUtils.bitmap2Drawable(getResources(), blurBg));
        for (int i = 0; i < mTitleArray.length; i++) {
            mTabEntityList.add(new TabEntity(mTitleArray[i],
                    mIconSelArray[i], mIconUnselArray[i]));
        }

        // 第一种，监听tab
//        bottomTab.setTabData(mTabEntityList);
//        bottomTab.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                switchTo(position);
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//                showShortToast(mTitleArray[position]);
//            }
//        });
        initFragment(savedInstanceState);
        switchTo(1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WES);
        } else {
            init();
        }
    }

    private static final int REQUEST_CODE_WES = 1;

    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WES:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    init();
                }else {
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initFragment(Bundle savedInstanceState) {
        mFm = getSupportFragmentManager();
        int currentPosition = 0;
        if (savedInstanceState != null) {
            mMainFragment = mFm.findFragmentByTag(TAG_MAIN);
            mGalleryFragment = mFm.findFragmentByTag(TAG_GALLERY);
            mMusicFragment = mFm.findFragmentByTag(TAG_MUSIC);
            mSettingsFragment = mFm.findFragmentByTag(TAG_SETTINGS);
            currentPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            mMainFragment = MainFragment.newInstance();
            mGalleryFragment = GalleryFragment.newInstance();
            mMusicFragment = MusicFragment.newInstance();
            mSettingsFragment = SettingsFragment.newInstance();
        }
        FragmentTransaction transaction = mFm.beginTransaction();
        transaction.add(R.id.fl_content, mMainFragment, TAG_MAIN);
        transaction.add(R.id.fl_content, mGalleryFragment, TAG_GALLERY);
        transaction.add(R.id.fl_content, mMusicFragment, TAG_MUSIC);
        transaction.add(R.id.fl_content, mSettingsFragment, TAG_SETTINGS);
        transaction.commit();

        mFragmentList.add(MainFragment.newInstance());
        mFragmentList.add(GalleryFragment.newInstance());
        mFragmentList.add(MusicFragment.newInstance());
        mFragmentList.add(SettingsFragment.newInstance());
        //第二种，一句话解决
        bottomTab.setTabData(mTabEntityList, this, R.id.fl_content, mFragmentList);
        switchTo(currentPosition);
    }

    private void switchTo(int position) {
        bottomTab.setCurrentTab(position);
        FragmentTransaction transaction = mFm.beginTransaction();
        switch (position) {
            case 0:
                transaction.show(mMainFragment);
                transaction.hide(mGalleryFragment);
                transaction.hide(mMusicFragment);
                transaction.hide(mSettingsFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 1:
                transaction.hide(mMainFragment);
                transaction.show(mGalleryFragment);
                transaction.hide(mMusicFragment);
                transaction.hide(mSettingsFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 2:
                transaction.hide(mMainFragment);
                transaction.hide(mGalleryFragment);
                transaction.show(mMusicFragment);
                transaction.hide(mSettingsFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 3:
                transaction.hide(mMainFragment);
                transaction.hide(mGalleryFragment);
                transaction.hide(mMusicFragment);
                transaction.show(mSettingsFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (bottomTab != null) {
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, bottomTab.getCurrentTab());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int backStackEntryCount = mFm.getBackStackEntryCount();
            // 判断当前回退栈中的fragment个数,
            if (backStackEntryCount > 1) {
                // 立即回退一步
                mFm.popBackStackImmediate();
                // 获取当前退到了哪一个Fragment上,重新获取当前的Fragment回退栈中的个数
                FragmentManager.BackStackEntry backStack = mFm
                        .getBackStackEntryAt(mFm.getBackStackEntryCount() - 1);
                // 获取当前栈顶的Fragment的标记值
                String tag = backStack.getName();
                // 判断当前是哪一个标记
                if (TAG_MAIN.equals(tag)) {
                    bottomTab.setCurrentTab(0);
                } else if (TAG_GALLERY.equals(tag)) {
                    bottomTab.setCurrentTab(1);
                } else if (TAG_MUSIC.equals(tag)) {
                    bottomTab.setCurrentTab(2);
                } else if (TAG_SETTINGS.equals(tag)) {
                    bottomTab.setCurrentTab(3);
                }
            } else {
                //移动到后台 false: 只对启动的Activity有效；true：任何Activity都有效
//                moveTaskToBack(false);
                exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long lastTimeStamp;

    private void exitApp() {
        long currentTimeStamp = System.currentTimeMillis();
        if (currentTimeStamp - lastTimeStamp > 1350L) {
            showShortToast("再按一次退出应用");
        } else {
            AppManager.getInstance().exitApp(this, false);
        }
        lastTimeStamp = currentTimeStamp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRenderScriptUtils.destory();
    }

    /**
     * Fragment返回栈 将switchTo 替换为该方法
     */
    private void switchToStack(int position) {
        bottomTab.setCurrentTab(position);
        FragmentTransaction transaction = mFm.beginTransaction();
        switch (position) {
            case 0:
                transaction.replace(R.id.fl_content, mMainFragment);
                transaction.addToBackStack(TAG_MAIN);
                transaction.commitAllowingStateLoss();
                break;
            case 1:
                transaction.replace(R.id.fl_content, mGalleryFragment);
                transaction.addToBackStack(TAG_GALLERY);
                transaction.commitAllowingStateLoss();
                break;
            case 2:
                transaction.replace(R.id.fl_content, mMusicFragment);
                transaction.addToBackStack(TAG_MUSIC);
                transaction.commitAllowingStateLoss();
                break;
            case 3:
                transaction.replace(R.id.fl_content, mSettingsFragment);
                transaction.addToBackStack(TAG_SETTINGS);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    private void init() {
        // init sonic engine
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
    }
}
