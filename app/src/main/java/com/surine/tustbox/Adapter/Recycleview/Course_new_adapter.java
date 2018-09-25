package com.surine.tustbox.Adapter.Recycleview;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by surine on 2017/9/26.
 */

public class Course_new_adapter extends BaseQuickAdapter<CourseInfoHelper, BaseViewHolder> {

    public Course_new_adapter(@LayoutRes int layoutResId, @Nullable List<CourseInfoHelper> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, CourseInfoHelper course_info) {

        if (course_info != null) {

                holder.setText(R.id.course_text, course_info.getCourseName());
                try {
                    holder.setBackgroundColor(R.id.item_course_relativelayout, mContext.getResources().getColor(course_info.getJwColor()));
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.setBackgroundColor(R.id.item_course_relativelayout, mContext.getColor(R.color.colorPrimary2));
                    }else{
                        holder.setBackgroundColor(R.id.item_course_relativelayout, mContext.getResources().getColor(R.color.colorPrimary2));
                    }
                    Toast.makeText(mContext,
                            "颜色加载错误，请重新登录", Toast.LENGTH_LONG).show();
                }
                holder.setText(R.id.loca_text, "@" + course_info.getTeachingBuildingName() + course_info.getClassroomName());
                holder.addOnClickListener(R.id.item_course_relativelayout);


        }
    }
}
