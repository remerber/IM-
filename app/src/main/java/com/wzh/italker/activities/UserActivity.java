package com.wzh.italker.activities;

import android.content.Context;
import android.content.Intent;

import com.wzh.common.app.BaseActivity;
import com.wzh.italker.R;
import com.wzh.italker.frags.user.UpdateInfoFragment;

/**
 * Created by HP on 2017/11/3.
 *
 * @author by wangWei
 */

public class UserActivity extends BaseActivity {

    private UpdateInfoFragment mFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mFragment=new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mFragment)
                .commit();

    }

    public static void show(Context context) {

        context.startActivity(new Intent(context, UserActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         mFragment.onActivityResult(requestCode,resultCode,data);
    }
}
