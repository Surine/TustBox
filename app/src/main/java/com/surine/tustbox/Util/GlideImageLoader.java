package com.surine.tustbox.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;
import com.surine.tustbox.R;

/**
 * Created by surine on 2017/10/30.
 */

public class GlideImageLoader implements NineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        if (url != null) {
            try {
                Glide.with(context).load(url)//
                        .placeholder(R.drawable.head)//
                        .error(R.drawable.head)//
                        .into(imageView);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
