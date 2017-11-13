package com.wzh.italker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wzh.italker.activities.MainActivity;
import com.wzh.italker.frags.assist.PermissionsFragment;

/** 欢迎界面
 * @author  wang
 */
public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionsFragment.haveAll(this,getSupportFragmentManager())){
            MainActivity.show(this);
            finish();
        }
    }
}
