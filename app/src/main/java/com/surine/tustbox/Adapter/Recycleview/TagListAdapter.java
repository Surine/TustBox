package com.surine.tustbox.Adapter.Recycleview;

import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.TagInfo;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2018/2/20.
 */

public class TagListAdapter extends BaseQuickAdapter<TagInfo, BaseViewHolder> {
    public TagListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagInfo item) {
        helper.setText(R.id.tag_title, item.getTagTitle());
        Glide.with(mContext).load(item.getImageUrl()).crossFade().into((ImageView) helper.getView(R.id.tag_image));
    }
}

