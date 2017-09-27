package com.surine.tustbox.Adapter.Recycleview;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.TimeUtil;

import java.util.List;

/**
 * Created by surine on 2017/9/17.
 */

public class Today_Course_Adapter extends BaseQuickAdapter<Course_Info, BaseViewHolder> {
    public Today_Course_Adapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Course_Info item) {
        helper.setText(R.id.today_course_name, item.getCourse_name());
        helper.setText(R.id.course_time, TimeUtil.getCourseTime(item.getClass_()));
        helper.setImageResource(R.id.today_course_background, item.getColor());
        String course_string = "<font color='#FF0000'>@"+item.getBuilding()+item.getClassroom()+"</font>";
        helper.setText(R.id.course_string, Html.fromHtml(course_string));
        helper.addOnClickListener(R.id.view_card);
    }
}

