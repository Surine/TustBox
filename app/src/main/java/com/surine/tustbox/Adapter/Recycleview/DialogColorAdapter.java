package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2018/7/24.
 */

public class DialogColorAdapter extends BaseQuickAdapter<Integer,BaseViewHolder> {
    public DialogColorAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setImageResource(R.id.item_color,item);
    }
}
