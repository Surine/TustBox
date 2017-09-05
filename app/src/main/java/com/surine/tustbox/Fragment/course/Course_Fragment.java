package com.surine.tustbox.Fragment.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Adapter.Recycleview.Course_Table_Adapter;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.ToolbarActivity;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/3/29.
 */

public class Course_Fragment extends Fragment {
    private static final String ARG_ = "Course_Fragment";
    RecyclerView course_rec;
    int choose_week;
    Course_Info course_info;
    int position1;
    int position2;
    int number = 0;
    String help_string;
    String number_b = ",";
    TextView tex1;
    TextView tex2;
    TextView tex3;
    TextView tex4;
    TextView tex5;
    TextView tex6;
    TextView tex7;
    ImageView mImageView;
    ImageView backimageView;
    TextView today_text;
    ListView listview;
    int week_number_for_todays_course = 0;
    private List<Course_Info> mCourseList = new ArrayList<>();
    private List<Course_Info> mLastList = new ArrayList<>();
    int user;
    static String other_user_string = "-1";
    private static final String EXTRA = "other_user";
    public static Course_Fragment getInstance(String title) {
        Course_Fragment fra = new Course_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        other_user_string = title;
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
        View v = inflater.inflate(R.layout.fragment_course, container, false);
        initView(v);  //init the view
        initData();
        course_rec = (RecyclerView) v.findViewById(R.id.course_table);
        course_rec.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        course_rec.setAdapter(new Course_Table_Adapter(mLastList, getActivity()));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        boolean value = prefs.getBoolean("setting_dialog", false);
        if(!value) {
            initDialog();  //init the course dialog
        }
        return v;
    }


    //init the dialog
    private void initDialog() {
        SharedPreferences pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        //reset
        if (week_number_for_todays_course != pref.getInt("day_", 1)) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
            editor.putBoolean("today_course_is_show", false);
            editor.apply();
        }
        //if this is not the first login and  no show today ,
        if (!pref.getBoolean("today_course_is_show", false) && pref.getBoolean("is_login", false)) {
            showDialog();   //show the dialog
        }
    }

    //show the dialog
    private void showDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view_today_class, null);
        listview = (ListView) view.findViewById(R.id.listview);
        backimageView = (ImageView) view.findViewById(R.id.imageView3);
        today_text = (TextView) view.findViewById(R.id.today_text);

        //TODO:在这里获取出生日期，来判断展示
        if(false){
            backimageView.setImageResource(R.drawable.birthday);
            today_text.setText("生日快乐!");
        }else{
            today_text.setText("今天·" + TimeUtil.GetWeek());
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), initListViewData(), R.layout.item_today,
                new String[]{"background", "location", "name", "name_little", "time"},
                new int[]{R.id.today_course_background, R.id.today_courese_location, R.id.today_course_name, R.id.today_course_name_little, R.id.today_course_time});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!initListViewData().get(i).get("name").equals(getString(R.string.no_class))) {
                    int id = (int) initListViewData().get(i).get("id");
                    startActivity(new Intent(getActivity(), ToolbarActivity.class).putExtra("course_id", id));
                } else {
                    //start the qq
                    startQQ();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(R.string.i_know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //save "it was shown today"
                SharedPreferencesUtil.Save(getActivity(),"today_course_is_show", true);
                SharedPreferencesUtil.Save(getActivity(),"day_", week_number_for_todays_course);
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    private void startQQ() {
        PackageManager packageManager = null;
        try {
            packageManager = getActivity().getPackageManager();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        }
}

    //init today course's hashmap
    private ArrayList<HashMap<String, Object>> initListViewData() {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String,Object>>();
              //
            for(int i = week_number_for_todays_course-1;i<mLastList.size();i++) {
                Log.d("DEBUG", "initListViewData: "+week_number_for_todays_course);
                    if (mLastList.get(i)!= null) {
                    Course_Info course_info = mLastList.get(i);
                    HashMap<String, Object> today_hasmap = new HashMap<String, Object>();
                    today_hasmap.put("id",course_info.getId());
                    today_hasmap.put("background", course_info.getColor());
                    today_hasmap.put("location", course_info.getBuilding() + course_info.getClassroom());
                    today_hasmap.put("name", course_info.getCourse_name());
                    today_hasmap.put("name_little", course_info.getCourse_name().substring(1, 3));
                    if (course_info.getClass_().contains("一")) {
                        today_hasmap.put("time", "8:20-10:00");
                    } else if (course_info.getClass_().contains("二")) {
                        today_hasmap.put("time", "10:20-12:00");
                    } else if (course_info.getClass_().contains("三")) {
                        today_hasmap.put("time", "2:00-3:40");
                    } else if (course_info.getClass_().contains("四")) {
                        today_hasmap.put("time", "4:00-5:40");
                    }
                    arrayList.add(today_hasmap);
                }
                i = i+6;
            }
            if(arrayList.size()==0){
                HashMap<String, Object> today_hasmap = new HashMap<String, Object>();
                today_hasmap.put("name", getString(R.string.no_class));
                today_hasmap.put("location", getString(R.string.happy));
                arrayList.add(today_hasmap);
            }

        return arrayList;
    }

    //init the view
    private void initView(View v) {
        tex1 = (TextView) v.findViewById(R.id.mon);
        tex2 = (TextView) v.findViewById(R.id.tus);
        tex3 = (TextView) v.findViewById(R.id.wes);
        tex4 = (TextView) v.findViewById(R.id.thr);
        tex5 = (TextView) v.findViewById(R.id.fri);
        tex6 = (TextView) v.findViewById(R.id.sta);
        tex7 = (TextView) v.findViewById(R.id.sun);
        mImageView = (ImageView) v.findViewById(R.id.back_ground_picture);
    }

    //load the Curriculum schedule
    private void initData() {

        if(other_user_string.equals("1")){
            user = 0;
        }else{
            if(SharedPreferencesUtil.Read(getActivity(),"user_flag",0)==0){
                user = 0;
                SharedPreferencesUtil.Save(getActivity(),"user_flag",user);
            }else{
                user = 1;
                SharedPreferencesUtil.Save(getActivity(),"user_flag",user);
            }
        }
        load_image();
        initWeek();  //init the week
        mCourseList = DataSupport.findAll(Course_Info.class);
        choose_week =  SharedPreferencesUtil.Read(getActivity(),"choice_week",0);
        if(choose_week == 0){
            Toast.makeText(getActivity(),"请检查网络链接（可访问互联网而非校园网）",Toast.LENGTH_SHORT).show();
        }
        choose_week = SetWeek(choose_week);

        //表格为6*7
        for(int j = 0;j<42;j++){
            mLastList.add(null);
        }
        for(int i = 0;i<mCourseList.size();i++){
            try {
                course_info = mCourseList.get(i);
                if(course_info.getUser()==user) {
                    //out of some cases(they may cause App ANR,for example:network course,etc)
                    if (course_info.getMethod().contains("正常") && !(course_info.getCourse_number().contains("WL"))) {
                        position1 = Integer.parseInt(String.valueOf(course_info.getWeek_number().charAt(2))) - 1;
                        position2 = (GetNumber(course_info.getClass_()) - 1) * 7;
                        mLastList.set(position1 + position2, course_info);
                        if (course_info.getClass_count().contains("4")) {
                            mLastList.set(position1 + position2 + 7, course_info);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        R.string.error_get_data,Toast.LENGTH_LONG).show();
            }


        }

        //周筛选
        for(int e=0;e<mLastList.size();e++){
            if(mLastList.get(e)!=null) {
                if (mLastList.get(e).getWeek().contains("-")) {

                    //正则解析，提取数字
                    List<String> week_range = PatternUtil.getNumber(mLastList.get(e).getWeek());

                    for(int i= Integer.parseInt(week_range.get(0));i<=Integer.parseInt(week_range.get(1));i++){
                       number_b+=(i+",");
                    }
                    if(!(number_b.contains(","+choose_week+","))){
                        mLastList.set(e, null);
                    }
                    number_b = ",";
                } else {
                    String help_week = (","+mLastList.get(e).getWeek().substring(2,mLastList.get(e).getWeek().length()-2)+",");
                    if (!(help_week.contains(","+choose_week+","))) {
                        mLastList.set(e, null);
                    }
                }
            }
        }
    }

    private void load_image() {
        String image_path = SharedPreferencesUtil.Read(getActivity(),"my_picture_path",null);
        if(image_path != null){
            Glide.with(this).load(image_path).into(mImageView);
        }
    }

    //Automatic display week
    private int SetWeek(int choose_week) {
        Boolean change = SharedPreferencesUtil.Read(getActivity(),"is_change_week",false);
        if(TimeUtil.GetWeek().equals("周一")){
           if(!change) {
               SharedPreferencesUtil.Save(getActivity(),"is_change_week", true);
               SharedPreferencesUtil.Save(getActivity(),"choice_week", choose_week);
               return choose_week;
           }
       }
       else if(TimeUtil.GetWeek().equals("周日")){
           SharedPreferencesUtil.Save(getActivity(),"is_change_week", false);
       }
       return choose_week;
    }


    //init Week view
    private void initWeek() {
       String week = TimeUtil.GetWeek();
        if(week.equals("周一")||week.equals("Mon")){
            //set the today color
            tex1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            //give a flag
            week_number_for_todays_course = 1;
        }else if(week.equals("周二")||week.equals("Tue")){
            tex2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            week_number_for_todays_course = 2;

        }else if(week.equals("周三")||week.equals("Wed")){
            tex3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            week_number_for_todays_course = 3;

        }else if(week.equals("周四")||week.equals("Thu")){
            tex4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            week_number_for_todays_course = 4;

        }else if(week.equals("周五")||week.equals("Sat")){
            tex5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            week_number_for_todays_course = 5;

        }else if(week.equals("周六")||week.equals("Sun")){
            tex6.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            week_number_for_todays_course = 6;

        }else if(week.equals("周日")||week.equals("Mon")){
            tex7.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            week_number_for_todays_course = 7;

        }
    }


    //get number
    public int GetNumber(String word){
        if(word.contains("一")){
            number = 1;
        }else if(word.contains("二")){
            number = 2;
        }else if(word.contains("三")){
            number = 3;
        }else if(word.contains("四")){
            number = 4;
        }else if(word.contains("五")){
            number = 5;
        }else if(word.contains("六")){
            number = 6;
        }else if(word.contains("七")){
            number = 7;
        }else if(word.contains("八")){
            number = 8;
        }else if(word.contains("九")){
            number = 9;
        }else if(word.contains("十")){
            number = 10;
        }
        return number;
    }
}
