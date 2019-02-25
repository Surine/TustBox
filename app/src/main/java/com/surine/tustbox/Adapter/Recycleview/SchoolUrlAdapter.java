package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.SchoolUrl;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2018/8/20.
 */

public class SchoolUrlAdapter extends BaseQuickAdapter<SchoolUrl, BaseViewHolder> {
    public SchoolUrlAdapter(int layoutResId, @Nullable List<SchoolUrl> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SchoolUrl item) {
        helper.addOnClickListener(R.id.go);
       helper.setText(R.id.url_name,item.getUrlName());
       helper.setText(R.id.url_intro,item.getUrlIntro());
    }
}
