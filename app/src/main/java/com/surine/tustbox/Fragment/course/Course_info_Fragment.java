package com.surine.tustbox.Fragment.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.R;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/4/5.
 */

public class Course_info_Fragment extends Fragment{
    private static final String ARG_ = "Course_info_Fragment";
    private TextView class_name_text_view;
    private TextView class_teacher_text_view;
    private TextView class_text_view;
    private TextView class_room_text_view;
    private TextView class_style_text_view;
    private TextView class_score_text_view;
    private EditText note;
    private EditText homework;
    int id;
    public static Course_info_Fragment getInstance(String title) {
        Course_info_Fragment fra = new Course_info_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_info, container, false);
        initView(v);   //初始化控件
        initData();
        return v;
    }

    private void initView(View v) {
        class_name_text_view = (TextView) v.findViewById(R.id.class_name_text_view);
        class_teacher_text_view = (TextView) v.findViewById(R.id.teacher_text_view);
        class_text_view = (TextView) v.findViewById(R.id.class_text_view);
        class_room_text_view = (TextView) v.findViewById(R.id.classroom_text_view);
        class_style_text_view = (TextView) v.findViewById(R.id.class_style_text_view);
        class_score_text_view = (TextView) v.findViewById(R.id.class_score_text_view);
        note = (EditText) v.findViewById(R.id.note);
        homework = (EditText) v.findViewById(R.id.homework);
    }

    private void initData() {
       id = getActivity().getIntent().getIntExtra("course_id",-1);
        if(id == -1){
            Toast.makeText(getActivity(),"未知错误！", Toast.LENGTH_SHORT).show();
        }else{
            Course_Info course_info = DataSupport.find(Course_Info.class,id);
            class_name_text_view.setText(course_info.getCourse_name());
            class_teacher_text_view.setText(course_info.getTeacher());
            class_text_view.setText("星期"+course_info.getWeek_number()+"  "+"第"+course_info.getClass_()+"节");
            class_room_text_view.setText(course_info.getSchool()+course_info.getBuilding()+course_info.getClassroom());
            class_style_text_view.setText(course_info.getExm());
            class_score_text_view.setText(course_info.getScore());

            if(course_info.getNote()!=null&&course_info.getHomework()!=null){
                note.setText(course_info.getNote());
                homework.setText(course_info.getHomework());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Course_Info course_info  = DataSupport.find(Course_Info.class,id);
        course_info.setNote(note.getText().toString());
        course_info.setHomework(homework.getText().toString());
        course_info.save();
    }
}
