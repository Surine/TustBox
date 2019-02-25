package com.surine.tustbox.Adapter.Recycleview;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.Pojo.CourseInfoHelper;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.TimeUtil;

import java.util.List;

/**
 * Created by Surine on 2019/1/29.
 */

public class CourseTodayAdapter_v5 extends BaseQuickAdapter<CourseInfoHelper,BaseViewHolder>{
    public CourseTodayAdapter_v5(int layoutResId, @Nullable List<CourseInfoHelper> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfoHelper item) {
        helper.setText(R.id.classInfo,TimeUtil.getCourseSessionShow(item.getClassSessions()));
        helper.setText(R.id.className,item.getCourseName());
        helper.setText(R.id.classRoom,item.getCampusName()+item.getTeachingBuildingName()+item.getClassroomName()
        + " "+ TimeUtil.getCourseTime(item.getClassSessions())
        );
        TextView tv = helper.getView(R.id.classInfo);
        //tv.setTextColor(mContext.getResources().getColor(item.getJwColor()));
        helper.addOnClickListener(R.id.classCard);
    }
}
