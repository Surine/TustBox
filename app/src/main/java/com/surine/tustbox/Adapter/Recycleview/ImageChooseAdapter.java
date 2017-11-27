package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.R;

import java.io.File;
import java.util.List;

/**
 * Created by surine on 2017/11/1.
 */

public class ImageChooseAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ImageChooseAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if(item != null){
            try {
                Glide.with(mContext).load(new File(item)).crossFade().into((ImageView) helper.getView(R.id.images_choose));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
