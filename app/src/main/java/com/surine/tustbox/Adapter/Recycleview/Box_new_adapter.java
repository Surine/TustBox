package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Bean.Box;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by surine on 2017/10/16.
 */

public class Box_new_adapter extends BaseQuickAdapter<Box, BaseViewHolder> {
    public Box_new_adapter(@LayoutRes int layoutResId, @Nullable List<Box> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Box item) {
        holder.setText(R.id.box_text,item.getBox_name());
        holder.setImageResource(R.id.box_image,item.getImage_id());
        holder.addOnClickListener(R.id.box_background);
    }
}