package com.surine.tustbox.Adapter.Recycleview;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by surine on 2017/9/26.
 */

public class Course_new_adapter extends BaseQuickAdapter<Course_Info, BaseViewHolder> {
    public Course_new_adapter(@LayoutRes int layoutResId, @Nullable List<Course_Info> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Course_Info course_info) {
        if (course_info != null) {
            holder.setText(R.id.course_text,course_info.getCourse_name());
            try {
                holder.setBackgroundColor(R.id.back_color,mContext.getResources().getColor(course_info.getColor()));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                Toast.makeText(mContext,
                        "颜色加载错误，请重新获取或者联系开发者",Toast.LENGTH_LONG).show();
            }
            holder.setText(R.id.loca_text,"@"+course_info.getBuilding()+course_info.getClassroom());
            holder.addOnClickListener(R.id.back_color);
        }
    }
}
