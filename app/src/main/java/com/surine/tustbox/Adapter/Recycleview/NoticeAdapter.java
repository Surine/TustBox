package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.Notice;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2018/4/12.
 */

public class NoticeAdapter extends BaseQuickAdapter<Notice, BaseViewHolder> {


    public NoticeAdapter(int layoutResId, @Nullable List<Notice> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Notice item) {
        helper.setText(R.id.notice_title,item.getTitle());
        helper.setText(R.id.notice_content,item.getContent());
        helper.setText(R.id.notice_time,item.getCtime());
    }
}