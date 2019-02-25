package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.ScoreInfoHelper;
import com.surine.tustbox.R;

import java.util.List;

/**
 * 新的分数适配器
 * Created by Surine on 2018/3/16.
 */

public class ScoreBaseAdapter extends BaseQuickAdapter<ScoreInfoHelper, BaseViewHolder> {

    public ScoreBaseAdapter(int layoutResId, @Nullable List<ScoreInfoHelper> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreInfoHelper item) {
        helper.setText(R.id.book_name, item.getCourseName());
        helper.setText(R.id.english_view, item.getEnglishCourseName());
        helper.setText(R.id.score_view, item.getCj());
        helper.setText(R.id.credit, item.getCredit());
        helper.setText(R.id.ranking_view,item.getGradeName());
        helper.setText(R.id.ave_view, item.getGradePointScore());
        helper.setText(R.id.textView17, item.getCj());
    }

}
