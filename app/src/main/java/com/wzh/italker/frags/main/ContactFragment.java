package com.wzh.italker.frags.main;


import com.wzh.common.app.BaseFragment;
import com.wzh.common.widget.GalleryView;
import com.wzh.italker.R;

import butterknife.BindView;


public class ContactFragment extends BaseFragment {

    @BindView(R.id.galleryView)
    GalleryView galleryView;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initData() {
        super.initData();
        galleryView.setUp(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
