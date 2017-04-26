package com.surine.tustbox.Adapter.Recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surine.tustbox.Bean.Student_info;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/3/28.
 */

public class StuInfoAdapter extends RecyclerView.Adapter<StuInfoAdapter.ViewHolder>{
    private List<Student_info> mStudent_infos = new ArrayList<>();

    public StuInfoAdapter(List<Student_info> student_infos) {
        mStudent_infos = student_infos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stu_info,parent
        ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       Student_info student_info = mStudent_infos.get(position);
        holder.tag.setText(student_info.getTag());
        holder.info.setText(student_info.getValue());
    }

    @Override
    public int getItemCount() {
        return mStudent_infos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView tag;
        TextView info;
        public ViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tag);
            info = (TextView) itemView.findViewById(R.id.info);
        }
    }
}
