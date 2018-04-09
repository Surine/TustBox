package com.surine.tustbox.Fragment.course;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.Random;

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
    private Course_Info course_info = new Course_Info("","","","","","","","","","","","","","","","","",0,"","",0,0);
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
        course_info = DataSupport.find(Course_Info.class, id);
        if(course_info == null){
            return;
        }
        //课程名
        courseName.setSummary(course_info.getCourse_name()+"\n"+courseName.getSummary());
        courseName.setText(course_info.getCourse_name());
        courseNameString = course_info.getCourse_name();

        courseBuilding.setSummary(course_info.getBuilding()+"\n"+courseBuilding.getSummary());
        courseBuilding.setText(course_info.getBuilding());
        courseBuildingString = course_info.getBuilding();

        courseClass_.setSummary(course_info.getClass_()+"\n"+courseClass_.getSummary());
        courseClassString = course_info.getClass_();

        courseClassRoom.setSummary(course_info.getClassroom()+"\n"+courseClassRoom.getSummary());
        courseClassRoom.setText(course_info.getClassroom());
        courseClassRoomString = course_info.getClassroom();

        courseScore.setSummary(course_info.getScore()+"\n"+courseScore.getSummary());
        courseScore.setText(course_info.getScore());
        courseScoreString = course_info.getScore();

        courseTeacher.setSummary(course_info.getTeacher()+"\n"+courseTeacher.getSummary());
        courseTeacher.setText(course_info.getTeacher());
        courseTeacherString = course_info.getTeacher();

        courseClassSchool.setSummary(course_info.getSchool()+"\n"+courseClassSchool.getSummary());
        courseClassSchoolString = course_info.getSchool();

        courseWeek.setSummary(course_info.getWeek()+"\n"+courseWeek.getSummary());
        courseWeek.setText(course_info.getWeek());
        courseWeekString = course_info.getWeek();

        courseWeekNumber.setSummary(course_info.getWeek_number()+"\n"+courseWeekNumber.getSummary());
        courseWeekNumberString = course_info.getWeek_number();

        courseCourseNumber.setSummary(course_info.getClass_count()+"\n"+courseCourseNumber.getSummary());
        courseCourseNumberString = course_info.getClass_count();


        course_number.setSummary(course_info.getCourse_number());
        course_color.setSummary(course_info.getColor());

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
            courseNameString = prefs.getString("courseName",course_info.getCourse_name());
            courseName.setSummary(courseNameString);

        }else if(key == courseBuilding.getKey()){
            //教学楼处理
            courseBuildingString = prefs.getString("courseBuilding",course_info.getBuilding());
            courseBuilding.setSummary(courseBuildingString);
        }else if(key == courseClass_.getKey()){
            //节次
            int value = Integer.parseInt(prefs.getString("courseClass_","1"));
            courseClass_.setSummary(getResources().getStringArray(R.array.list_bj_class_)[value-1]);
            courseClassString = String.valueOf(courseClass_.getSummary());

        }else if(key == courseClassRoom.getKey()){
            //教室
            courseClassRoomString = prefs.getString("courseClassRoom",course_info.getClassroom());
            courseClassRoom.setSummary(courseClassRoomString);
        }else if(key == courseScore.getKey()){

            //学分
            courseScoreString = prefs.getString("courseScore",course_info.getScore());
            courseScore.setSummary(courseScoreString);
        }else if(key == courseTeacher.getKey()){
            //教师
            courseTeacherString = prefs.getString("courseTeacher",course_info.getTeacher());
            courseTeacher.setSummary(courseTeacherString);
        }else if(key == courseWeek.getKey()){
            //上课周
            courseWeekString = prefs.getString("courseWeek",course_info.getWeek());
            courseWeek.setSummary(courseWeekString);

        }else if(key == courseWeekNumber.getKey()){
            //周几
            courseWeekNumberString = prefs.getString("courseWeekNumber",course_info.getWeek_number());
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
                courseNameString = prefs.getString("courseName",course_info.getCourse_name());
                courseName.setSummary(courseNameString);
            }else if(preference == courseBuilding){
                //教学楼处理
                courseBuildingString = prefs.getString("courseBuilding",course_info.getBuilding());
                courseBuilding.setSummary(courseBuildingString);
            }else if(preference == courseClass_){
                //节次
                int value = Integer.parseInt(prefs.getString("courseClass_","1"));
                courseClass_.setSummary(getResources().getStringArray(R.array.list_bj_class_)[value-1]);
                courseClassString = String.valueOf(courseClass_.getSummary());

            }else if(preference == courseClassRoom){
                //教室
                courseClassRoomString = prefs.getString("courseClassRoom",course_info.getClassroom());
                courseClassRoom.setSummary(courseClassRoomString);
            }else if(preference == courseScore){

                //学分
                courseScoreString = prefs.getString("courseScore",course_info.getScore());
                courseScore.setSummary(courseScoreString);
            }else if(preference == courseTeacher){
                //教师
                courseTeacherString = prefs.getString("courseTeacher",course_info.getTeacher());
                courseTeacher.setSummary(courseTeacherString);
            }else if(preference == courseWeek){
                //上课周
                courseWeekString = prefs.getString("courseWeek",course_info.getWeek());
                courseWeek.setSummary(courseWeekString);

            }else if(preference == courseWeekNumber){
                //周几
                courseWeekNumberString = prefs.getString("courseWeekNumber",course_info.getWeek_number());
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
          delete();
        } else if(event.getId() == 11){
            //新建 （i = 2标识新建）
            Toast.makeText(getActivity(), save(2), Toast.LENGTH_SHORT).show();
        }
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("危险操作");
        builder.setMessage("请仔细检查是否需要删掉此课程，一旦删除只能通过重新登录来获取，同时自定义的课程将永久删除，请谨慎操作!");
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(DataSupport.delete(Course_Info.class,id)>=1){
                    Toast.makeText(getActivity(), "删除成功！重启后生效", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setPositiveButton("取消",null);
        builder.show();
    }

    private String save(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请仔细核对信息");
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
        if(courseWeekString == null||courseWeekString.toString().equals("")){
            return "上课周信息填写错误";
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

        sbuilder.append("请检查信息是否正确，并且不能跟其他课程冲突，如果冲突可能导致课表错乱。");
        builder.setMessage(sbuilder.toString());
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Course_Info course_info = new Course_Info();
               course_info.setCourse_name(courseNameString);
               course_info.setBuilding(courseBuildingString);
               course_info.setClass_(courseClassString);
               course_info.setClass_count(String.valueOf(courseCourseNumberString));
               course_info.setClassroom(courseClassRoomString);
               course_info.setSchool(String.valueOf(courseClassSchoolString));
               course_info.setTeacher(courseTeacherString);
               course_info.setWeek(courseWeekString);
               course_info.setScore(courseScoreString);
               course_info.setWeek_number(" "+courseWeekNumberString);
               course_info.setUser(0);
               course_info.setMethod("正常");
               if(id == -1){
                   course_info.setColor(colors[new Random().nextInt(15)]);
               }
               if(id == -1){
                   course_info.setCourse_number("AD"+tustNumber+System.currentTimeMillis());
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

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();

        return "课程合法检查已通过";
    }
}
