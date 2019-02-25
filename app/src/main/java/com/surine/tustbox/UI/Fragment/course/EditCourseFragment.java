package com.surine.tustbox.UI.Fragment.course;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Pojo.CourseInfoHelper;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.Pojo.EventBusBean.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Surine on 2018/3/8.
 */

@SuppressLint("ValidFragment")
public class EditCourseFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private int id;
    private EditTextPreference courseName;
    private EditTextPreference courseBuilding;
    private ListPreference courseClass_;
    private EditTextPreference courseClassRoom;
    private ListPreference courseClassSchool;
    private ListPreference courseCourseNumber;
    private EditTextPreference courseScore;
    private EditTextPreference courseTeacher;
    private EditTextPreference courseWeek;
    private ListPreference courseWeekNumber;

    private Preference courseuser;
    private Preference course_number;
    private Preference course_color;
    private Preference tust_number;
    private String tustNumber;
    private SharedPreferences prefs;
    private CourseInfoHelper course_info = new CourseInfoHelper();
    private String courseNameString;
    private String courseBuildingString;
    private String courseClassString;
    private String courseClassRoomString;
    private String courseScoreString;
    private String courseTeacherString;
    private String courseWeekString;
    private String courseWeekNumberString;
    private CharSequence courseClassSchoolString;
    private CharSequence courseCourseNumberString;
    final int[] colors = new int[]{
            R.color.Tust_1,
            R.color.Tust_2,
            R.color.Tust_3,
            R.color.Tust_4,
            R.color.Tust_5,
            R.color.Tust_6,
            R.color.Tust_7,
            R.color.Tust_8,
            R.color.Tust_9,
            R.color.Tust_10,
            R.color.Tust_11,
            R.color.Tust_12,
            R.color.Tust_13,
            R.color.Tust_14,
            R.color.Tust_15,
            R.color.Tust_16,
    };
    public EditCourseFragment(int id) {
        this.id = id;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.course);
        EventBus.getDefault().register(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        findview();   //初始化view
        courseuser.setSummary("0");
        tustNumber = SharedPreferencesUtil.Read(getActivity(), FormData.tust_number_server, "");
        tust_number.setSummary(tustNumber);
        if(id != -1){
           init(id);
        }

        setLinsener();   //setlinstener
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init(int id) {
        course_info = DataSupport.find(CourseInfoHelper.class, id);
        if(course_info == null){
            return;
        }
        //课程名
        courseName.setSummary(course_info.getCourseName()+"\n"+courseName.getSummary());
        courseName.setText(course_info.getCourseName());
        courseNameString = course_info.getCourseName();

        courseBuilding.setSummary(course_info.getTeachingBuildingName()+"\n"+courseBuilding.getSummary());
        courseBuilding.setText(course_info.getTeachingBuildingName());
        courseBuildingString = course_info.getTeachingBuildingName();

        courseClass_.setSummary(course_info.getClassSessions()+"\n"+courseClass_.getSummary());
        courseClassString = course_info.getClassSessions();

        courseClassRoom.setSummary(course_info.getClassroomName()+"\n"+courseClassRoom.getSummary());
        courseClassRoom.setText(course_info.getClassroomName());
        courseClassRoomString = course_info.getClassroomName();

        courseScore.setSummary(course_info.getUnit()+"\n"+courseScore.getSummary());
        courseScore.setText(course_info.getUnit());
        courseScoreString = course_info.getUnit();

        courseTeacher.setSummary(course_info.getAttendClassTeacher()+"\n"+courseTeacher.getSummary());
        courseTeacher.setText(course_info.getAttendClassTeacher());
        courseTeacherString = course_info.getAttendClassTeacher();

        courseClassSchool.setSummary(course_info.getCampusName()+"\n"+courseClassSchool.getSummary());
        courseClassSchoolString = course_info.getCampusName();

        courseWeek.setSummary(course_info.getWeekDescription()+"\n"+courseWeek.getSummary());
        courseWeek.setText(course_info.getWeekDescription());
        courseWeekString = course_info.getWeekDescription();

        courseWeekNumber.setSummary(course_info.getClassDay()+"\n"+courseWeekNumber.getSummary());
        courseWeekNumberString = course_info.getClassDay();

        courseCourseNumber.setSummary(course_info.getContinuingSession()+"\n"+courseCourseNumber.getSummary());
        courseCourseNumberString = course_info.getContinuingSession();


        course_number.setSummary(course_info.getCoureNumber());
        course_color.setSummary(course_info.getJwColor());

    }

    private void setLinsener() {
       courseName.setOnPreferenceClickListener(this);
    //   courseName.setOnPreferenceChangeListener(this);

       courseBuilding.setOnPreferenceClickListener(this);
     //   courseBuilding.setOnPreferenceChangeListener(this);

        courseClass_.setOnPreferenceClickListener(this);
      //  courseClass_.setOnPreferenceChangeListener(this);

        courseClassRoom.setOnPreferenceClickListener(this);
      //  courseClassRoom.setOnPreferenceChangeListener(this);

        courseScore.setOnPreferenceClickListener(this);
      //  courseScore.setOnPreferenceChangeListener(this);

        courseTeacher.setOnPreferenceClickListener(this);
       // courseTeacher.setOnPreferenceChangeListener(this);

        courseClassSchool.setOnPreferenceClickListener(this);
     //   courseClassSchool.setOnPreferenceChangeListener(this);

        courseWeek.setOnPreferenceClickListener(this);
      //  courseWeek.setOnPreferenceChangeListener(this);

        courseCourseNumber.setOnPreferenceClickListener(this);

        courseWeekNumber.setOnPreferenceClickListener(this);
     //   courseWeekNumber.setOnPreferenceChangeListener(this);

        courseuser.setOnPreferenceClickListener(this);
       // courseuser.setOnPreferenceChangeListener(this);

        course_number.setOnPreferenceClickListener(this);
     //   course_number.setOnPreferenceChangeListener(this);

        course_color.setOnPreferenceClickListener(this);
      //  course_color.setOnPreferenceChangeListener(this);

        tust_number.setOnPreferenceClickListener(this);
      //  tust_number.setOnPreferenceChangeListener(this);
    }

    private void findview() {
        courseName = (EditTextPreference) findPreference("courseName");
        courseBuilding = (EditTextPreference) findPreference("courseBuilding");
        courseClass_ = (ListPreference) findPreference("courseClass_");
        courseClassRoom = (EditTextPreference) findPreference("courseClassRoom");
        courseScore = (EditTextPreference) findPreference("courseScore");
        courseTeacher = (EditTextPreference) findPreference("courseTeacher");
        courseClassSchool = (ListPreference) findPreference("courseClassSchool");
        courseWeek = (EditTextPreference) findPreference("courseWeek");
        courseWeekNumber = (ListPreference) findPreference("courseWeekNumber");
        courseCourseNumber = (ListPreference) findPreference("courseCourseNumber");

        courseuser =  findPreference("courseuser");
        course_number =  findPreference("course_number");
        course_color =  findPreference("course_color");
        tust_number =  findPreference("tust_number");
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        // Set up a listener whenever a key changes  
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes  
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key == courseName.getKey()){
            //课程名处理
            courseNameString = prefs.getString("courseName",course_info.getCourseName());
            courseName.setSummary(courseNameString);

        }else if(key == courseBuilding.getKey()){
            //教学楼处理
            courseBuildingString = prefs.getString("courseBuilding",course_info.getTeachingBuildingName());
            courseBuilding.setSummary(courseBuildingString);
        }else if(key == courseClass_.getKey()){
            //节次
            int value = Integer.parseInt(prefs.getString("courseClass_","1"));
            courseClass_.setSummary(getResources().getStringArray(R.array.list_bj_class_)[value-1]);
            courseClassString = String.valueOf(courseClass_.getSummary());

        }else if(key == courseClassRoom.getKey()){
            //教室
            courseClassRoomString = prefs.getString("courseClassRoom",course_info.getClassroomName());
            courseClassRoom.setSummary(courseClassRoomString);
        }else if(key == courseScore.getKey()){

            //学分
            courseScoreString = prefs.getString("courseScore",course_info.getUnit());
            courseScore.setSummary(courseScoreString);
        }else if(key == courseTeacher.getKey()){
            //教师
            courseTeacherString = prefs.getString("courseTeacher",course_info.getTeachingBuildingName());
            courseTeacher.setSummary(courseTeacherString);
        }else if(key == courseWeek.getKey()){
            //上课周
            courseWeekString = prefs.getString("courseWeek",course_info.getWeekDescription());
            courseWeek.setSummary(courseWeekString);

        }else if(key == courseWeekNumber.getKey()){
            //周几
            courseWeekNumberString = prefs.getString("courseWeekNumber",course_info.getClassDay());
            courseWeekNumber.setSummary(courseWeekNumberString);
        }else if(key == courseClassSchool.getKey()){
            //校区
            int value = Integer.parseInt(prefs.getString("courseClassSchool","1"));
            courseClassSchool.setSummary(getResources().getStringArray(R.array.list_bj_school)[value-1]);
            courseClassSchoolString = courseClassSchool.getSummary();
        }else if(key == courseCourseNumber.getKey()){
            //节数
            int value = Integer.parseInt(prefs.getString("courseCourseNumber","1"));
            courseCourseNumber.setSummary(getResources().getStringArray(R.array.list_bj_course)[value-1]);
            courseCourseNumberString = courseCourseNumber.getSummary();
        }else if(key == courseWeekNumber.getKey()){
            //周几
            int value = Integer.parseInt(prefs.getString("courseWeekNumber","1"));
            courseWeekNumber.setSummary(getResources().getStringArray(R.array.list_bj_week_number)[value-1]);
            courseWeekNumberString = String.valueOf(courseWeekNumber.getSummary());
        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(course_info != null){
            if(preference == courseName){
                courseNameString = prefs.getString("courseName",course_info.getCourseName());
                courseName.setSummary(courseNameString);
            }else if(preference == courseBuilding){
                //教学楼处理
                courseBuildingString = prefs.getString("courseBuilding",course_info.getTeachingBuildingName());
                courseBuilding.setSummary(courseBuildingString);
            }else if(preference == courseClass_){
                //节次
                int value = Integer.parseInt(prefs.getString("courseClass_","1"));
                courseClass_.setSummary(getResources().getStringArray(R.array.list_bj_class_)[value-1]);
                courseClassString = String.valueOf(courseClass_.getSummary());

            }else if(preference == courseClassRoom){
                //教室
                courseClassRoomString = prefs.getString("courseClassRoom",course_info.getClassroomName());
                courseClassRoom.setSummary(courseClassRoomString);
            }else if(preference == courseScore){

                //学分
                courseScoreString = prefs.getString("courseScore",course_info.getUnit());
                courseScore.setSummary(courseScoreString);
            }else if(preference == courseTeacher){
                //教师
                courseTeacherString = prefs.getString("courseTeacher",course_info.getAttendClassTeacher());
                courseTeacher.setSummary(courseTeacherString);
            }else if(preference == courseWeek){
                //上课周
                courseWeekString = prefs.getString("courseWeek",course_info.getWeekDescription());
                courseWeek.setSummary(courseWeekString);

            }else if(preference == courseWeekNumber){
                //周几
                courseWeekNumberString = prefs.getString("courseWeekNumber",course_info.getClassDay());
                courseWeekNumber.setSummary(courseWeekNumberString);
            }else if(preference == courseClassSchool){
                //校区
                int value = Integer.parseInt(prefs.getString("courseClassSchool","1"));
                courseClassSchool.setSummary(getResources().getStringArray(R.array.list_bj_school)[value-1]);
                courseClassSchoolString = courseClassSchool.getSummary();
            }else if(preference == courseCourseNumber){
                //节数
                int value = Integer.parseInt(prefs.getString("courseCourseNumber","1"));
                courseCourseNumber.setSummary(getResources().getStringArray(R.array.list_bj_course)[value-1]);
                courseCourseNumberString = courseCourseNumber.getSummary();
            }else if(preference == courseWeekNumber){
                //周几
                int value = Integer.parseInt(prefs.getString("courseWeekNumber","1"));
                courseWeekNumber.setSummary(getResources().getStringArray(R.array.list_bj_week_number)[value-1]);
                courseWeekNumberString = String.valueOf(courseWeekNumber.getSummary());
            }
        }
        return false;
    }


    //TODO:add method 正常


    @Subscribe
    public void GetMessage(SimpleEvent event) {
        if (event.getId() == 9) {
          //储存 （i = 1 标识更新）
            Toast.makeText(getActivity(), save(1), Toast.LENGTH_SHORT).show();

        }else if(event.getId() == 10){
            //删除
          showDeleteDialog();
        } else if(event.getId() == 11){
            //新建 （i = 2标识新建）
            Toast.makeText(getActivity(), save(2), Toast.LENGTH_SHORT).show();
        }
    }

    private void delete() {
        if(DataSupport.delete(CourseInfoHelper.class,id)>=1){
            Toast.makeText(getActivity(), "删除成功！重启后生效", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_a_text_notice,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        TextView label = (TextView) view.findViewById(R.id.label);
        TextView message = (TextView) view.findViewById(R.id.message);

        label.setText("是否删除？");
        message.setText("请仔细检查是否需要删掉此课程，一旦删除只能通过重新登录来获取，同时自定义的课程将永久删除，请谨慎操作!");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            delete();
            dialog.dismiss();
            }
        });

    }

    private String save(final int i) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_a_text_notice,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        TextView label = (TextView) view.findViewById(R.id.label);
        TextView message = (TextView) view.findViewById(R.id.message);

        label.setText("请核对");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        StringBuilder sbuilder = new StringBuilder();

        //课程
        if(courseNameString == null||courseNameString.equals("")){
             return "课程名为空";
        }


        //教学楼
        if(courseBuildingString == null||courseBuildingString.equals("")||!courseBuildingString.contains("-")){
            return "教学楼信息为空或者不存在-字符";
        }

        //节次
        if(courseClassString == null||courseClassString.equals("")){
            return "节次不正确";
        }

        //教室
        if(courseClassRoomString == null||courseClassRoomString.equals("")){
            return "教室信息为空";
        }

        //校区
        if(courseClassSchoolString == null||courseClassSchoolString.toString().equals("")){
            return "校区内容错误";
        }

        //学分
        if(courseScoreString == null||courseScoreString.equals("")){
           courseScoreString = "0";
        }

        //教师
        if(courseTeacherString == null||courseTeacherString.toString().equals("")){
            return "教师信息填写错误";
        }


        //上课周
        if(courseWeekString == null||
                courseWeekString.toString().equals("")||
                courseWeekString.contains("-")){
                return "上课周请填0或1序列";
        }

        //判断是否有中文
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(courseWeekString);
        if (m.find()) {
            return "上课周请填0或1序列";
        }

        //是否有非01数
        p = Pattern.compile("[2-9]");
        m = p.matcher(courseWeekString);
        if (m.find()) {
            return "上课周请填0或1序列";
        }

        //上课周
        if(courseWeekNumberString == null||courseWeekNumberString.toString().equals("")){
            return "周几上课填写为空";
        }

        //节数
        if(courseCourseNumberString == null||courseCourseNumberString.equals("")){
            return "节数信息错误";
        }


        sbuilder.append("课程名："+courseNameString+"\n");
        sbuilder.append("上课地点:"+courseClassSchoolString+courseBuildingString+courseClassRoomString+"\n");
        sbuilder.append("上课时间:"+courseWeekString+"周"+courseWeekNumberString+"第"+courseClassString+"节"+"\n");
        sbuilder.append("教师:"+courseTeacherString+"\n");
        sbuilder.append("学分:"+courseScoreString+"\n");
        sbuilder.append("节数:"+courseCourseNumberString+"\n");

        message.setText(sbuilder+"请检查信息是否正确，并且不能跟其他课程冲突，如果冲突可能导致课表错乱。");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseInfoHelper course_info = new CourseInfoHelper();
                course_info.setCourseName(courseNameString);
                course_info.setTeachingBuildingName(courseBuildingString);
                course_info.setClassSessions(String.valueOf(Integer.parseInt(courseClassString) * 2 -1));
                course_info.setContinuingSession(String.valueOf(courseCourseNumberString));
                course_info.setClassroomName(courseClassRoomString);
                course_info.setCampusName(String.valueOf(courseClassSchoolString));
                course_info.setAttendClassTeacher(courseTeacherString);
                //处理以下courseWeekString补全24位
                if(courseWeekString.length() > 24){
                    courseWeekString = courseWeekString.substring(0,23);
                }else if(courseWeekString.length() < 24){
                    StringBuilder builderWeek = new StringBuilder(courseWeekString);
                    for (int j = 0; j < 24 - courseWeekString.length(); j++) {
                        builderWeek.append("0");
                    }
                    courseWeekString = builderWeek.toString();
                }
                course_info.setWeekDescription(courseWeekString);
                course_info.setUnit(courseScoreString);
                course_info.setClassDay(courseWeekNumberString);
                course_info.setStudyModeName("正常");
                if(id == -1){
                    course_info.setJwColor(colors[new Random().nextInt(15)]);
                }
                if(id == -1){
                    course_info.setCoureNumber("AD"+tustNumber+System.currentTimeMillis());
                }

                if(i == 1){
                    if(course_info.update(id)>= 0){
                        Toast.makeText(getActivity(), "修改成功！重启后生效", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                }else if(i == 2){
                    if(course_info.save()){
                        Toast.makeText(getActivity(), "添加成功！重启后生效", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "添加失败！", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        dialog.show();

        return "课程合法检查已通过";
    }
}
