package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Bean.ScoreInfo;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.PatternUtil;

import java.util.List;

/**
 * 新的分数适配器
 * Created by Surine on 2018/3/16.
 */

public class ScoreBaseAdapter extends BaseQuickAdapter<ScoreInfo, BaseViewHolder> {

    public ScoreBaseAdapter(int layoutResId, @Nullable List<ScoreInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreInfo item) {
        TextView textView = (TextView) helper.getView(R.id.score_view);
        helper.setText(R.id.book_name, item.getName());
        helper.setText(R.id.english_view, item.getEnglish_name());
        helper.setText(R.id.score_view, item.getScore());
        helper.setText(R.id.credit, item.getCredit());
        helper.setText(R.id.ranking_view, item.getRanking());
        helper.setText(R.id.ave_view, item.getAve());

        try {
            int score = 100;
            List<String> strings = PatternUtil.getNumber(item.getScore());
            if(strings.size() > 0){
                score = Integer.parseInt(strings.get(0));
            }else{
                return;
            }
            if(score > 90){
                textView.setTextSize(18);
                textView.setTextColor(mContext.getResources().getColor(R.color.Tust_more_color_1));
                helper.setText(R.id.score_view, item.getScore()+"   棒棒哒！");
            }else if(score < 70){
                helper.setText(R.id.score_view, item.getScore()+"  快去学习！");
                textView.setTextSize(18);
                textView.setTextColor(mContext.getResources().getColor(R.color.Tust_1));
            }else{
                textView.setTextSize(18);
                textView.setTextColor(mContext.getResources().getColor(R.color.Tust_Black));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

}
