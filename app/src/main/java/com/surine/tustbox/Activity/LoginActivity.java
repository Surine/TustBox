package com.surine.tustbox.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.Student_info;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Init.SystemUI;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends TustBaseActivity {
    private static final String TAG = "LoginActivity";  //tag for log
    private static final int CONNECT_TIMEOUT = 10;   //network connect timeout(10s)
    private static final String EXTRA = "other_user";   //more user intent flag
    private EditText tust_number;    //input tust number
    private EditText pswd;      //input tust password
    private TextView warning;
    private Button login_btn;
    String tust_number_string = null;
    String pswd_string = null;
    ProgressDialog pg;
    String my_student_info_str;
    String my_course_info_str;
    int status_login = 0;
    Elements content2;
    Elements content;
    Elements content_Text;
    Document doc;
    Course_Info course_info;
    Course_Info help_course;
    int times = 1;
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

    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;
    private int slec_color;
    String intent_string = "-1";
    int user=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        intent_string = intent.getStringExtra(EXTRA);

        //hidden the  stautusbar
        SystemUI.hide_statusbar(this);

        //set up the database
        Connector.getDatabase();

        //initview
        initView();

        //init the okhttp
        okHttpClient = HttpUtil.initOkhttp(CONNECT_TIMEOUT);

        //set the listener
        set_listen();
    }

    private void initView() {
        tust_number = (EditText) findViewById(R.id.tust_number);
        pswd = (EditText) findViewById(R.id.pswd);
        login_btn = (Button) findViewById(R.id.btn_login);
        warning = (TextView) findViewById(R.id.warning);
    }

    private void set_listen() {
        //the listener for login_btn
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get input text
                tust_number_string = tust_number.getText().toString();
                pswd_string = pswd.getText().toString();

                //check null
                if (tust_number_string.equals("") || pswd_string.equals("")) {
                    Snackbar.make(login_btn, R.string.null_number_and_pswd, Snackbar.LENGTH_SHORT).show();
                } else {
                    //login
                    try {
                        //set progress
                        setDialog(getString(R.string.login), getString(R.string.login_info));
                        login(tust_number_string, pswd_string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //warning
        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(R.string.warning_text)
                        .setMessage(R.string.note)
                        .setPositiveButton(R.string.ok, null).show();
            }
        });
    }


    //set dialog
    private void setDialog(String string, String s) {
        try {
            //create the dialog
            pg = new ProgressDialog(LoginActivity.this);
            pg.setTitle(string);
            pg.setMessage(s);
            pg.setCancelable(false);
            pg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //login
    private int login(final String tust_number_string, final String pswd_string) throws Exception {
        //create the formbody
        FormBody formBody = new FormBody.Builder()
                .add(FormData.login_id, tust_number_string)
                .add(FormData.login_pswd, pswd_string)
                .build();

        //the callback of the request
        okHttpClient
                .newCall(HttpUtil.HttpConnect(UrlData.login_post_url,formBody)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //callback:failure
                pg.dismiss();
                pg.cancel();
                status_login = 0;
                Snackbar.make(login_btn, R.string.fail, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //save the pswd and id
                if(!intent_string.equals(EXTRA)) {
                    saveUserInfo(tust_number_string, pswd_string);
                }
                String str = response.body().string().toString();
                Document doc = Jsoup.parse(str);
                String back_title = doc.title();
                if (back_title.equals(getString(R.string.manager_))) {
                    //request success
                    Snackbar.make(login_btn, R.string.success, Snackbar.LENGTH_SHORT).show();
                    //if the request is successful we should start the new same request
                    //it can solve the problem about the cookies mismatching
                    if (times == 2) {
                        status_login = 1;
                        Message mess = new Message();
                        mess.what = 1;
                        myHandler.sendMessage(mess);
                    } else {
                        try {
                            //dismiss
                            pg.dismiss();
                            pg.cancel();
                            login(tust_number_string, pswd_string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        times = times + 1;
                    }
                } else {
                    //pswd is wrong
                    Snackbar.make(login_btn, R.string.wrong, Snackbar.LENGTH_SHORT).show();
                    status_login = 0;
                }
                //dismiss
                pg.dismiss();
                pg.cancel();
            }
        });
        return status_login;
    }

    //save user information
    private void saveUserInfo(String tust_number_string, String pswd_string) {
        // encodeToString will return string value
        String enToStr = Base64.encodeToString(pswd_string.getBytes(), Base64.DEFAULT);
        //save
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("tust_number", tust_number_string);
        editor.putString("pswd", enToStr);
        editor.apply();
    }


    private void GetStudentInfo() {
        //the method is a way to get data
        //and we should send an url and a flag to it,so it can use the method（savedata） to save
        //the data.
        get_Info(UrlData.get_stu_info_url, 1);
    }

    private void saveInfo(int save_tag) {
        /*
        * Tag = 1 :student information
        * Tag = 2 :course information
        *
        * */
        if (save_tag == 1) {
            if(!intent_string.equals(EXTRA)) {
                //get the save_tag and use the Jsoup to analyze the html  (td)
                Document doc2 = Jsoup.parse(my_student_info_str);
                content = doc2.select("td");
                //save the name in another way
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("stu_name", this.content.get(20).text());
                //save the school
                editor.putString("part", this.content.get(70).text());
                editor.apply();
                SaveStudentInfo(this.content.get(19).text(), this.content.get(20).text());
                SaveStudentInfo(this.content.get(17).text(), this.content.get(18).text());
                SaveStudentInfo(this.content.get(25).text(), this.content.get(26).text());

                SaveStudentInfo(this.content.get(31).text(), this.content.get(32).text());
                SaveStudentInfo(this.content.get(33).text(), this.content.get(34).text());
                SaveStudentInfo(this.content.get(37).text(), this.content.get(38).text());
                SaveStudentInfo(this.content.get(41).text(), this.content.get(42).text());
                SaveStudentInfo(this.content.get(45).text(), this.content.get(46).text());
                SaveStudentInfo(this.content.get(47).text(), this.content.get(48).text());
                SaveStudentInfo(this.content.get(49).text(), this.content.get(50).text());
                SaveStudentInfo(this.content.get(67).text(), this.content.get(68).text());
                SaveStudentInfo(this.content.get(69).text(), this.content.get(70).text());
                SaveStudentInfo(this.content.get(71).text(), this.content.get(72).text());
                SaveStudentInfo(this.content.get(77).text(), this.content.get(78).text());
                SaveStudentInfo(this.content.get(79).text(), this.content.get(80).text());
                SaveStudentInfo(this.content.get(83).text(), this.content.get(84).text());
                SaveStudentInfo(this.content.get(115).text(), this.content.get(119).text());
            }
        } else if (save_tag == 2) {

            //get the tr tag
            Document doc2 = Jsoup.parse(my_course_info_str);
            content2 = doc2.select("tr");

            for (int i = 22; i < content2.size(); i++) {

                //set the value of user(int)
                if(intent_string.equals(EXTRA)) {
                    user = 1;
                }else if(intent_string.equals("no")){
                    user = 0;
                }
               /*
               *the data obtained are classifiled into two cases
               * full line & half a line
               * （if you want to get more information ,please visit the tust official website）
               * if the string doesn't contains the "培养方案", and we can count it as second cases
               * so we can use the SharedPreferences to load the data for it
               * and the other case is normal loading,but we should save the data by SharedPreferences
               * */
                if (!(content2.get(i).text().contains(getString(R.string.plan)))) {
                    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                    content_Text = content2.get(i).select("td");
                    //load courese object
                    help_course = creat_course(
                            pref.getString("0", "null"),
                            pref.getString("1", "null"),
                            pref.getString("2", "null"),
                            pref.getString("3", "null"),
                            pref.getString("4", "null"),
                            pref.getString("5", "null"),
                            pref.getString("6", "null"),
                            pref.getString("7", "null"),
                            pref.getString("9", "null"),
                            pref.getString("10", "null"),

                            content_Text.get(0).text(),
                            content_Text.get(1).text(),
                            content_Text.get(2).text(),
                            content_Text.get(3).text(),
                            content_Text.get(4).text(),
                            content_Text.get(5).text(),
                            content_Text.get(6).text(),
                            pref.getInt("18_color", 0),
                            user
                    );

                } else {
                    //color
                    slec_color = colors[i % 15];
                    content_Text = content2.get(i).select("td");
                    help_course = creat_course(
                            content_Text.get(0).text(),
                            content_Text.get(1).text(),
                            content_Text.get(2).text(),
                            content_Text.get(3).text(),
                            content_Text.get(4).text(),
                            content_Text.get(5).text(),
                            content_Text.get(6).text(),
                            content_Text.get(7).text(),
                            content_Text.get(9).text(),
                            content_Text.get(10).text(),
                            content_Text.get(11).text(),
                            content_Text.get(12).text(),
                            content_Text.get(13).text(),
                            content_Text.get(14).text(),
                            content_Text.get(15).text(),
                            content_Text.get(16).text(),
                            content_Text.get(17).text(),
                            slec_color,
                            user
                    );

                    //save the data
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    for (int share = 0; share <= 17; share++) {
                        editor.putString(share + "", content_Text.get(share).text());
                    }
                    editor.putInt("18_color", slec_color);
                    editor.apply();
                }
            }

        }
        Snackbar.make(login_btn, R.string.save_success, Snackbar.LENGTH_SHORT).show();
        //intent
        Intent_Activity();
    }

    private void SaveStudentInfo(String text, String text1) {
      Student_info stu_info = new Student_info(text,text1);
        stu_info.save();
    }


    private Course_Info creat_course(String dev,
                                     String course_number,
                                     String course_name,
                                     String class_number,
                                     String score,
                                     String tag,
                                     String exm,
                                     String teacher,
                                     String method,
                                     String status,
                                     String week,
                                     String week_number,
                                     String class_,
                                     String class_count,
                                     String school,
                                     String building,
                                     String classroom,
                                     int color,
                                     int user
    ) {
        course_info = new Course_Info();
        course_info.setDev(dev);
        course_info.setCourse_number(course_number);
        course_info.setCourse_name(course_name);
        course_info.setClass_number(class_number);
        course_info.setScore(score);
        course_info.setTag(tag);
        course_info.setExm(exm);
        course_info.setTeacher(teacher);
        course_info.setMethod(method);
        course_info.setStatus(status);
        course_info.setWeek(week);
        course_info.setWeek_number(week_number);
        course_info.setClass_(class_);
        course_info.setClass_count(class_count);
        course_info.setSchool(school);
        course_info.setBuilding(building);
        course_info.setClassroom(classroom);
        course_info.setColor(color);
        course_info.setUser(user);
        course_info.save();
        return course_info;
    }

    private void GetCourseInfo() {
        //get info method
        get_Info(UrlData.get_Course_info_url, 2);
    }

    private void get_Info(String get_info_url, final int i) {
        //get
        Request request = new Request.Builder().
                url(get_info_url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Snackbar.make(login_btn, R.string.fail, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (i == 1) {
                    //jsoup
                    my_student_info_str = response.body().string().toString();
                    doc = Jsoup.parse(my_student_info_str);
                } else if (i == 2) {
                    my_course_info_str = response.body().string().toString();
                    doc = Jsoup.parse(my_course_info_str);
                }

                //according to the title of the html to determine whether the success of loading
                String back_title = doc.title();
                if (back_title.equals(getString(R.string.error_information))) {
                    Snackbar.make(login_btn, R.string.get_error, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(login_btn, R.string.get_success, Snackbar.LENGTH_SHORT).show();
                    //save
                    try {
                        saveInfo(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                pg.dismiss();
            }
        });

    }

    //start intent
    private void Intent_Activity() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        if(!intent_string.equals(EXTRA)) {
            intent.putExtra("other_user","no");
            editor.apply();
            startActivity(intent);
            finish();
        }else{
            intent.putExtra("other_user","other_user");
            editor.putBoolean("other_user_is_login",true);
            editor.apply();
            startActivity(intent);
            finish();
        }
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //show another dialog
                    setDialog(getString(R.string.get), getString(R.string.get_info));
                    //get
                    GetStudentInfo();
                    //get
                    GetCourseInfo();
                    if(!intent_string.equals(EXTRA)) {
                        //get
                        GetHead();
                    }
                    break;
            }
        }
    };



    private void GetHead() {

        okHttpClient.newCall(HttpUtil.HttpConnect(UrlData.get_head,null)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Snackbar.make(login_btn, R.string.wrong, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //把请求成功的response转为字节流
                InputStream inputStream = response.body().byteStream();
                //在这里用到了文件输出流
                FileOutputStream fileOutputStream = new FileOutputStream(new File(String.valueOf(getFilesDir()+"/head.jpg")));
                //定义一个字节数组
                byte[] buffer = new byte[2048];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    //写出到文件
                    fileOutputStream.write(buffer, 0, len);
                }
                //关闭输出流
                fileOutputStream.flush();
            }
        });
    }

}

