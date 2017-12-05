package com.wzh.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.wzh.common.R;
import com.wzh.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP on 2017/10/31.
 *
 * @author by wangWei
 */

public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null) {
            url = "";
        }
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()
                .into(this);

    }

    public void setup(RequestManager manager, String url) {
        setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, Author author) {
        setup(manager, author.getPortrait());
    }
}
