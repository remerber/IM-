package com.wzh.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.wzh.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by HP on 2017/10/26.
 *
 * @author by wangWei
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected PlaceHolderView mPlaceHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用的初始化窗口
        initWidows();
        if (initArgs(getIntent().getExtras())) {
            int layId = getContentLayoutId();
            setContentView(layId);
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }

    }

    /**
     * 初始化控件之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化控件
     */
    protected void initData() {

    }

    /**
     * 初始化相关参数
     *
     * @param bundle
     * @return 如果参数正确返回true，错误返回false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 初始化数据
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化窗口
     */
    protected void initWidows() {
    }

    /**
     * 得到当前的资源文件id
     *
     * @return 资源文件id
     */

    protected abstract int getContentLayoutId();

    @Override
    public void onBackPressed() {

        //得到当前Activity下的所有Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                //判断是否为我们能够处理的fragment类型
                if (fragment instanceof BaseFragment) {
                    //判断是否拦截的返回按钮
                    if (((BaseFragment) fragment).onBackPressed()) {
                        //如果有直接return
                        return;
                    }
                }
            }
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }


    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }
}
