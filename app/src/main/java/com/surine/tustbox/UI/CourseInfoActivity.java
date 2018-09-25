package com.surine.tustbox.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by surine on 2017/4/5.
 * modify by surine on 2018年8月7日 ：更新为courseinfoactivity
 */

public class CourseInfoActivity extends TustBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.courseInfoImage)
    ImageView courseInfoImage;
    @BindView(R.id.course_info_name)
    TextView courseInfoName;
    @BindView(R.id.info1)
    TextView info1;
    @BindView(R.id.info2)
    TextView info2;
    @BindView(R.id.info3)
    TextView info3;
    private Context context;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ButterKnife.bind(this);
        context = this;
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.course_info));
        toolbar.setTitleTextAppearance(context, R.style.ToolbarTitleWhite);
        toolbar.setBackgroundColor(getResources().getColor(R.color.trans_white));

        //加载课程信息
        try {
            initCourseData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCourseData() throws Exception {
        id = getIntent().getIntExtra(Constants.COURSE_ID, -1);
        if (id == -1) {
            Toast.makeText(context, getResources().getString(R.string.CourseInfoFragment_load_error), Toast.LENGTH_SHORT).show();
        } else {
            CourseInfoHelper course_info = DataSupport.find(CourseInfoHelper.class, id);

            if(course_info.getCourseName() != null)
                courseInfoName.setText(course_info.getCourseName());

            if(course_info.getAttendClassTeacher() != null && course_info.getUnit() != null)
                info3.setText(course_info.getAttendClassTeacher()+"老师"+ course_info.getUnit()+"学分");

            if(course_info.getClassDay() != null && course_info.getClassSessions() != null)
                info2.setText("星期" + course_info.getClassDay() + "第" + course_info.getClassSessions() + "节");

            if(course_info.getCampusName() != null && course_info.getTeachingBuildingName() != null && course_info.getClassroomName() != null)
                info1.setText(course_info.getCampusName() + course_info.getTeachingBuildingName() + course_info.getClassroomName());

            if(course_info.getJwColor() != 0){
                courseInfoImage.setBackgroundColor(getResources().getColor(course_info.getJwColor()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            finish();
        }
        if(item.getItemId() == R.id.edit){
            startActivity(new Intent(context, EditCourseActivity.class).putExtra(FormData.id,id));
        }
        return super.onOptionsItemSelected(item);
    }
}
