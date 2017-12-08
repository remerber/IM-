package com.wzh.common.widget.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by HP on 2017/12/7.
 *
 * @author by wangWei
 */

public abstract class TextWatcherAdapter implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
