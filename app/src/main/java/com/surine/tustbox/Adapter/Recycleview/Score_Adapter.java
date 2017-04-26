package com.surine.tustbox.Adapter.Recycleview;

/**
 * Created by surine on 2017/4/8.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surine.tustbox.Bean.Score_Info;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

public class Score_Adapter extends RecyclerView.Adapter<Score_Adapter.ViewHolder> {
    private List<Score_Info> mScoreInfoList = new ArrayList<>();
    private Context mContext;

    public Score_Adapter(List<Score_Info> courseList, Context context) {
        mScoreInfoList = courseList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score,parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            Score_Info score_info = mScoreInfoList.get(position);
            holder.name_view.setText(score_info.getName());
            holder.eng_view.setText(score_info.getEnglish_name());
            holder.score_view.setText(score_info.getScore());
            holder.mTextView.setText(score_info.getScore());
            holder.credit.setText(score_info.getCredit());
            holder.ranking_view.setText(score_info.getRanking());
            holder.ave_view.setText(score_info.getAve());

    }

    @Override
    public int getItemCount() {
        return mScoreInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_view;
        TextView eng_view;
        TextView score_view;
        TextView credit;
        TextView ranking_view;
        TextView ave_view;
        TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            name_view = (TextView) itemView.findViewById(R.id.book_name);
            mTextView = (TextView) itemView.findViewById(R.id.textView17);
            eng_view = (TextView) itemView.findViewById(R.id.english_view);
            score_view = (TextView) itemView.findViewById(R.id.socre_view);
            credit = (TextView) itemView.findViewById(R.id.credit);
            ranking_view = (TextView) itemView.findViewById(R.id.ranking_view);
            ave_view = (TextView) itemView.findViewById(R.id.ave_view);
        }
    }
}
