package com.surine.tustbox.UI.View.Header;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.CourseTodayAdapter_v5;
import com.surine.tustbox.Helper.Dao.CourseInfoDao;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.Pojo.CourseInfoHelper;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.CourseInfoActivity;

import java.util.List;

/**
 * Created by Surine on 2019/1/28.
 * 今日课表部分
 */

public class HeaderCourse {
    public static View getInstance(final Context context, RecyclerView r){
        View view = LayoutInflater.from(context).inflate(R.layout.header_course,(ViewGroup) r.getParent(),false);
        CourseInfoDao courseInfoDao = new CourseInfoDao();



        RecyclerView recyclerView = view.findViewById(R.id.myTodayCourse);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final List<CourseInfoHelper> courseInfoHelperList = courseInfoDao.getToDayCourse(context);
        CourseTodayAdapter_v5 adapter = new CourseTodayAdapter_v5(R.layout.item_course_today_v5, courseInfoHelperList);
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(R.layout.view_empty_card, recyclerView);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
            if (courseInfoHelperList.get(pos) == null) {
                return;
            } else {
                LinearLayout layout = view.findViewById(R.id.classCard);
                Intent intent = new Intent(context, CourseInfoActivity.class).putExtra(Constants.COURSE_ID, courseInfoHelperList.get(pos).getId());
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, layout, "backRelative").toBundle());
            }
            }
        });


        return view;
    }
}
