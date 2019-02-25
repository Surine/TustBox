package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.RootDir;
import com.surine.tustbox.R;

import java.util.List;


/**
 * Created by Surine on 2018/12/31.
 */

public class RootDirAdapter extends BaseQuickAdapter<RootDir,BaseViewHolder> {
    public RootDirAdapter(int layoutResId, @Nullable List<RootDir> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RootDir item) {
        if(item.getIntro() == null || item.getIntro().isEmpty()){
            item.setIntro("暂无简介");
        }
        helper.setText(R.id.name,item.getName());
        helper.setText(R.id.des,item.getIntro());
    }
}
