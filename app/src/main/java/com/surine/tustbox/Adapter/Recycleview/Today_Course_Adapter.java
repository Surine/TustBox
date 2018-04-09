package com.surine.tustbox.Adapter.Recycleview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void convert(BaseViewHolder helper, Course_Info item) {

        if(!item.getCourse_number().equals("*")){
            if (item.getCourse_name() != null)
            helper.setText(R.id.today_course_name, item.getCourse_name());
           //  helper.setText(R.id.course_time, TimeUtil.getCourseTime(item.getClass_()));
            if (item.getColor() != 0){
                helper.setImageResource(R.id.today_course_background, item.getColor());
            }else{
                helper.setImageResource(R.id.today_course_background, mContext.getColor(R.color.colorPrimary2));
            }
            if (item.getBuilding() != null && item.getClassroom() !=null && item.getClass_() != null){
                String course_string = "<font color='#FF0000'>@"+item.getBuilding()+item.getClassroom()+"第"+item.getClass_()+"节</font>";
                helper.setText(R.id.course_string, Html.fromHtml(course_string));
            }
           helper.addOnClickListener(R.id.view_card);

           Log.d("TAG",item.toString());
       }
    }
}

