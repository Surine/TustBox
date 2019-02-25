package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.Subject;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by Surine on 2018/8/24.
 */

public class SubjectHeaderAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> {

    public SubjectHeaderAdapter(int layoutResId, @Nullable List<Subject> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Subject item) {
        ImageView subjectImage = helper.getView(R.id.subject_iamge);
        Glide.with(mContext).load(item.getSubject_image()).into(subjectImage);
        helper.setText(R.id.subjec_text,item.getSubject_name());
    }
}

