package com.wzh.italker.frags.search;


import android.support.v4.app.Fragment;

import com.wzh.common.app.BaseFragment;
import com.wzh.italker.R;
import com.wzh.italker.activities.SearchActivity;


/**
 * A simple {@link Fragment} subclass.
 *
 * @author wang
 */
public class SearchGroupFragment extends BaseFragment implements SearchActivity.SearchListener {


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
