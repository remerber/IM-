package com.wzh.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.wzh.common.R;

/**
 * Created by HP on 2017/11/24.
 *
 * @author by wangWei
 */

public abstract class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar(Toolbar toolbar) {

        mToolbar = toolbar;
        if (mToolbar != null) {
            setSupportActionBar(toolbar);
        }

        initTitleNeedBack();
    }

    protected void initTitleNeedBack() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }


}
