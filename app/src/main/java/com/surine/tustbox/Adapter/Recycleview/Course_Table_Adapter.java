package com.surine.tustbox.Adapter.Recycleview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/3/29.
 */

public class Course_Table_Adapter extends RecyclerView.Adapter<Course_Table_Adapter.ViewHolder> {
    private List<Course_Info> mCourseList = new ArrayList<>();
    private Context mContext;

    public Course_Table_Adapter(List<Course_Info> courseList, Context context) {
        mCourseList = courseList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_card,parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (mCourseList.get(position) != null) {
                    mContext.startActivity(new Intent(mContext, ToolbarActivity.class).putExtra("course_id",mCourseList.get(position).getId()));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course_Info course_info = mCourseList.get(position);
        if (course_info != null) {
            holder.course_text.setText(course_info.getCourse_name());
            try {
                holder.back.setBackgroundColor(mContext.getResources().getColor(course_info.getColor()));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                Toast.makeText(mContext,
                        "颜色加载错误，请重新获取或者联系开发者",Toast.LENGTH_LONG).show();
            }
            holder.loca_text.setText("@"+course_info.getBuilding()+course_info.getClassroom());
        }
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView course_text;
        TextView loca_text;
        RelativeLayout back;
        public ViewHolder(View itemView) {
            super(itemView);
            course_text = (TextView) itemView.findViewById(R.id.course_text);
            loca_text = (TextView) itemView.findViewById(R.id.loca_text);
            back = (RelativeLayout) itemView.findViewById(R.id.back_color);
        }
    }
}
