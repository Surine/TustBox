package com.surine.tustbox.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.JwcUserInfo;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Init.SystemUI;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.EncryptionUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TustBoxUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends TustBaseActivity {
    public static final String TAG = "LoginActivity";
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
    public static final int[] colors = new int[]{
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
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.textView22)
    TextView textView22;
    @BindView(R.id.dknowtustNumber)
    TextView dknowtustNumber;
    @BindView(R.id.userl)
    TextView userl;
    @BindView(R.id.login_number_edit)
    EditText loginNumberEdit;
    @BindView(R.id.login_pswd_edit)
    EditText loginPswdEdit;
    @BindView(R.id.loginWeb)
    WebView webView;
    @BindView(R.id.help_login)
    Button helpLogin;
    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
    private int slec_color = 0;
    int user = 0;
    private Context context;
    private String tustNumber;  //科大帐号
    private String passWord;  //科大密码
    private int loginStatus = 0;  //记录登录状态0为未成功
    private OkHttpClient mOkHttpClient;
    private String backupStr = "";
    private int isBack = 0; //0为解析教务处，1为解析备份



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //hidden the  stautusbar
        SystemUI.hide_statusbar(this);
        //set up the database
        Connector.getDatabase();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("登录提示").setMessage(getResources().getString(R.string.loginJwcFail_login_notice))
                .setPositiveButton(R.string.ok,null);
        builder.show();

        //初始化
        initOkhttp();
        //initWebViewOkhttp();
    }

    private void initWebViewOkhttp() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private CookieManager mCookieManager = CookieManager.getInstance();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        String urlString = url.toString();
                        for (Cookie cookie : cookies) {
                            mCookieManager.setCookie(urlString, cookie.toString());
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        String urlString = url.toString();
                        String cookiesString = mCookieManager.getCookie(urlString);
                        if (cookiesString != null && !cookiesString.isEmpty()) {
                            String[] cookieHeaders = cookiesString.split(";");
                            List<Cookie> cookies = new ArrayList<>(cookieHeaders.length);

                            for (String header : cookieHeaders) {
                                cookies.add(Cookie.parse(url, header));
                            }
                            return cookies;
                        }
                        return Collections.emptyList();
                    }
                })
                .build();
    }

    //加载简洁版本请求
    private void initOkhttp() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        Log.d(TAG, url.toString());
                        cookieStore.put(url.host(), cookies);
                        Log.d(TAG, cookies.toString());
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
    }


    //监听事件
    @OnClick({R.id.btn_login, R.id.dknowtustNumber, R.id.userl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                readyLoginJwcNew();  //登录教务处
                break;
            case R.id.dknowtustNumber:
                startActivity(new Intent(context, WebViewActivity.class).putExtra(Constants.TITLE, "说明").putExtra(Constants.URL, UrlData.login_introduce));
                break;
            case R.id.userl:
                startActivity(new Intent(context, SettingActivity.class).putExtra("set_", 5));
                break;
        }
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_a_text_notice, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        TextView label = (TextView) view.findViewById(R.id.label);
        TextView message = (TextView) view.findViewById(R.id.message);

        ok.setText("开始验证");
        label.setText("提示");
        message.setText(R.string.loginError);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jwcCheckWebView();
                dialog.dismiss();
            }
        });

    }

    //准备登录教务处
    private void readyLoginJwcNew() {
        //获取字符串
        tustNumber = loginNumberEdit.getText().toString();
        passWord = loginPswdEdit.getText().toString();
        //判空
        if (tustNumber.equals("") || passWord.equals("")) {
            Toast.makeText(context,R.string.null_number_and_pswd,Toast.LENGTH_SHORT).show();
            return;
        }

        //saveUserInfo(tustNumber,passWord);
       // showErrorDialog();
        //登录
        try {
            //显示对话框
            setDialog(getString(R.string.login), getString(R.string.login_info));
            //登录
            loginJWCNew(tustNumber, passWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //显示对话框
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


    private int loginJWCNew(final String tustNumber, final String passWord) {
        //准备登录
        FormBody formBody = new FormBody.Builder()
                .add(FormData.login_id_new, tustNumber)
                .add(FormData.login_pswd_new, passWord)
                .add(FormData.help_var_new, "error")
                .build();

        Request request = new Request.Builder()
                .url(UrlData.login_post_url_new)
                .post(formBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pg.dismiss();
                        pg.cancel();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_net_error))
                                .setPositiveButton(R.string.ok,null).setCancelable(false)
                                .setNegativeButton("使用课表云备份", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getBackup();
                                    }
                                });
                        builder.show();

                        loginStatus = 0;
                        Toast.makeText(context,R.string.fail,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string().toString();
                Log.d(TAG, str);
                //登录成功储存帐号密码
                saveUserInfo(tustNumber, passWord);
                //JSOUP解析
                final Document doc = Jsoup.parse(str);
                Log.d(TAG, doc.title());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (doc.title().equals(getString(R.string.manager_))) {
                            Snackbar.make(btnLogin, R.string.success, Snackbar.LENGTH_SHORT).show();
                            pg.setMessage(getString(R.string.readyGetCourse));
                            getCourseJWC();
                        } else {
                            pg.dismiss();
                            pg.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                    .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_pass_error))
                                    .setPositiveButton(R.string.ok,null);
                            builder.show();
                            Toast.makeText(context,R.string.wrong,Toast.LENGTH_SHORT).show();
                            loginStatus = 0;
                        }
                    }
                });
            }
        });
        return 0;
    }

    //获取云备份TODO：
    private void getBackup() {
        try {
            pg.setMessage("正在下载云备份，请保持网络畅通");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = UrlData.downloadSchedule+"?uid="+tustNumber+"&pass="+passWord;
        HttpUtil.get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pg.dismiss();
                        pg.cancel();
                        Toast.makeText(context, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG,res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    if(jsonObject.getInt(FormData.JCODE) == 2000){
                        final String data = jsonObject.getString(FormData.JDATA);
                        final String courseStr = new JSONObject(data).getString(FormData.VALUE);
                        Log.d(TAG,EncryptionUtil.base64_de(courseStr));
                        isBack = 1; //标志解析备份
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Pattern r = Pattern.compile("&quot;");
                                Matcher m = r.matcher(EncryptionUtil.base64_de(courseStr));
                                parseCourse(m.replaceAll("\""));
                               // parseCourse(EncryptionUtil.base64_de(courseStr));
                            }
                        });
                    }else{
                        throw new JSONException("JCODE ！= 2000");
                    }
                } catch (JSONException e) {
                    //取消
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pg.dismiss();
                            pg.cancel();
                            Toast.makeText(context, "下载课表失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCourseJWC() {
        Request request = new Request.Builder()
                .url(UrlData.getCourseInfoNew)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_net_error))
                                .setPositiveButton(R.string.ok,null);
                        builder.show();
                        loginStatus = 0;
                        Toast.makeText(context,R.string.fail,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string().toString();
                Log.d(TAG, str);
                if(str.contains("登录")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_cookie_error))
                            .setPositiveButton(R.string.ok,null);
                    builder.show();
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //拿到课表，json解析
                        pg.setMessage(getString(R.string.parseCourse));
                        backupStr = str;
                        parseCourse(str);
                    }
                });
            }
        });
    }

    //解析课表
    private void parseCourse(String str) {
        try {
            pg.setMessage("正在解析课程表……");
            JSONObject jsonObject = new JSONObject(str);
            String dataList = jsonObject.getString("dateList");
            Log.d(TAG, dataList);
            /*json字符串最外层是方括号时：*/
            JSONArray jsonArray = new JSONArray(dataList);
            JSONObject j2 = new JSONObject(jsonArray.get(0).toString());
            String selectCourseList = j2.getString("selectCourseList");

            //准备添加数据
            String courseName;  //课程名
            String attendClassTeacher;  //教师
            String studyModeName;//课程状态（正常or重修）
            int jwColor;  //颜色
            String programPlanName; //计划方案
            String unit; //学分
            JSONArray tapListArray = null;
            String weekDescription = null; //哪几周上课
            String classDay = null; //周几上
            String classSessions = null; //第几节
            String continuingSession = null; //小课还是大课（2或者4）
            String coureNumber = null; //课序号
            String teachingBuildingName = null; //教学楼
            String campusName = null; //校园
            String classroomName = null; //教室

            JSONArray selectCourseListArray = new JSONArray(selectCourseList);
            for (int i = 0; i < selectCourseListArray.length(); i++) {
                Log.d(TAG, "数据"+selectCourseListArray.get(i).toString());
                //首先取得这一节课的全部信息
                String courseInfoJson = selectCourseListArray.get(i).toString();
                JSONObject courseInfoJsonObject = new JSONObject(courseInfoJson);

                //然后获取重要字段,课程名，老师，课程状态
                courseName = courseInfoJsonObject.getString("courseName");
                attendClassTeacher = courseInfoJsonObject.getString("attendClassTeacher");
                studyModeName = courseInfoJsonObject.getString("studyModeName");
                unit = courseInfoJsonObject.getString("unit");
                programPlanName = courseInfoJsonObject.getString("programPlanName");

                //选择颜色
                slec_color = colors[i % 15];
                if (slec_color == 0) {
                   slec_color = colors[0];
                }
                jwColor = slec_color;


                //判断是否两节课
                //取得上课时间地点
                String tapList = courseInfoJsonObject.getString("timeAndPlaceList");

                //Log.d(TAG,"时间表"+tapList);

                if(tapList.equals("null")){
                   continue;
                }

                tapListArray = new JSONArray(tapList);

                //缓存
                saveCache(courseName,
                        attendClassTeacher,
                        studyModeName,
                        unit,
                        programPlanName,
                        jwColor);

                if(tapListArray.length() > 1){
                    //如果这门课的上课时间地点有两个，那么先储存第一个
                    for (int j = 0; j < tapListArray.length(); j++) {
                        String tapArrayValue2 = tapListArray.get(j).toString();
                        JSONObject tapJsonObject2 = new JSONObject(tapArrayValue2);
                        //注意下面这块，获取使用0000111表示的格式
                        weekDescription = tapJsonObject2.getString("classWeek");
                        classDay = tapJsonObject2.getString("classDay");
                        classSessions = tapJsonObject2.getString("classSessions");
                        continuingSession = tapJsonObject2.getString("continuingSession");
                        coureNumber = tapJsonObject2.getString("coureNumber");
                        teachingBuildingName = tapJsonObject2.getString("teachingBuildingName");
                        campusName = tapJsonObject2.getString("campusName");
                        classroomName = tapJsonObject2.getString("classroomName");

                        courseName = SharedPreferencesUtil.Read(context,"courseName","");
                        attendClassTeacher = SharedPreferencesUtil.Read(context,"attendClassTeacher","");
                        studyModeName = SharedPreferencesUtil.Read(context,"studyModeName","");
                        unit = SharedPreferencesUtil.Read(context,"unit","");
                        programPlanName = SharedPreferencesUtil.Read(context,"programPlanName","");
                        jwColor = SharedPreferencesUtil.Read(context,"jwColor",0);
                        //存储
                        saveCourse(weekDescription,
                                classDay,
                                classSessions,
                                continuingSession,
                                coureNumber,
                                teachingBuildingName,
                                campusName,
                                classroomName,
                                courseName,
                                attendClassTeacher,
                                studyModeName,
                                unit,
                                programPlanName,
                                jwColor);
                    }
                }else{
                    String tapArrayValue = tapListArray.get(0).toString();
                    JSONObject tapJsonObject = new JSONObject(tapArrayValue);
                    //注意下面这块，获取使用0000111表示的格式
                    weekDescription = tapJsonObject.getString("classWeek");
                    classDay = tapJsonObject.getString("classDay");
                    classSessions = tapJsonObject.getString("classSessions");
                    continuingSession = tapJsonObject.getString("continuingSession");
                    coureNumber = tapJsonObject.getString("coureNumber");
                    teachingBuildingName = tapJsonObject.getString("teachingBuildingName");
                    campusName = tapJsonObject.getString("campusName");
                    classroomName = tapJsonObject.getString("classroomName");
                    //存储
                    saveCourse(weekDescription,
                            classDay,
                            classSessions,
                            continuingSession,
                            coureNumber,
                            teachingBuildingName,
                            campusName,
                            classroomName,
                            courseName,
                            attendClassTeacher,
                            studyModeName,
                            unit,
                            programPlanName,
                            jwColor);
                }

            }

            //如果从服务器解析备份，解析完直接进入主页，否则加载教务处个人信息
            if(isBack == 1){
                intentMain();
            }else{
                pg.setMessage("开始获取个人信息……");
                getUserInfo();
            }

        } catch (JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_json_error))
                    .setPositiveButton(R.string.ok,null).setNegativeButton("反馈", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          //TODO 发送服务器反馈
                            pg.dismiss();
                            pg.cancel();
                          Toast.makeText(context,"反馈成功",Toast.LENGTH_SHORT).show();
                        }
                    }).setCancelable(false)
                    .setNeutralButton("使用课表云备份", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getBackup();
                        }
                    });
            builder.show();
            e.printStackTrace();
            return;
        }
    }

    private void saveCache(String courseName, String attendClassTeacher, String studyModeName, String unit, String programPlanName, int jwColor) {
        SharedPreferencesUtil.Save(context,"courseName",courseName);
        SharedPreferencesUtil.Save(context,"attendClassTeacher",attendClassTeacher);
        SharedPreferencesUtil.Save(context,"studyModeName",studyModeName);
        SharedPreferencesUtil.Save(context,"unit",unit);
        SharedPreferencesUtil.Save(context,"programPlanName",programPlanName);
        SharedPreferencesUtil.Save(context,"jwColor",jwColor);
    }

    private void saveCourse(String weekDescription, String classDay,
                            String classSessions, String continuingSession,
                            String coureNumber, String teachingBuildingName,
                            String campusName, String classroomName,
                            String courseName, String attendClassTeacher,
                            String studyModeName, String unit,
                            String programPlanName, int jwColor) {
        CourseInfoHelper courseInfoHelper = new CourseInfoHelper();
        courseInfoHelper.setStudyModeName(studyModeName);
        courseInfoHelper.setWeekDescription(weekDescription);
        courseInfoHelper.setClassDay(classDay);
        courseInfoHelper.setClassSessions(classSessions);
        courseInfoHelper.setContinuingSession(continuingSession);
        courseInfoHelper.setCoureNumber(coureNumber);
        courseInfoHelper.setTeachingBuildingName(teachingBuildingName);
        courseInfoHelper.setCampusName(campusName);
        courseInfoHelper.setClassroomName(classroomName);
        courseInfoHelper.setCourseName(courseName);
        courseInfoHelper.setAttendClassTeacher(attendClassTeacher);
        courseInfoHelper.setUnit(unit);
        courseInfoHelper.setProgramPlanName(programPlanName);
        courseInfoHelper.setJwColor(jwColor);
        if(courseInfoHelper.save()){
            Log.d(TAG,"存储成功");
        }
    }

    //获取个人信息
    private void getUserInfo() {
        Request request = new Request.Builder()
                .url(UrlData.user_info_url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_net_error))
                            .setPositiveButton(R.string.ok,null);
                    builder.show();
                    loginStatus = 0;
                    Toast.makeText(context,R.string.fail,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string().toString();
                Log.d(TAG, "个人信息字符"+str);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(str.contains("登录")||str.contains("500错误")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                    .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_user_error))
                                    .setPositiveButton(R.string.ok,null);
                            builder.show();
                            loginStatus = 0;
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    intentMain();
                                }
                            },2000);
                        }
                        try {
                            pg.setMessage("开始解析个人信息……");
                            parseUserInfo(str);
                        } catch (Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                    .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_user_parse_error))
                                    .setPositiveButton(R.string.ok,null);
                            builder.show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //解析个人数据
    private void parseUserInfo(String str) throws Exception{
        Document doc = Jsoup.parse(str);
        Elements elements = doc.getElementsByClass("self profile-user-info profile-user-info-striped");
        for (Element element: elements) {
            Elements tab = element.getElementsByClass("profile-info-row");
            for (Element tabInfo : tab) {
                Elements infoName = tabInfo.getElementsByClass("profile-info-name");
                Elements infoValue = tabInfo.getElementsByClass("profile-info-value");
                for (int i = 0; i < infoName.size(); i++) {
                    if(!infoValue.get(i).text().equals("")){
                        JwcUserInfo jwcUserInfo = new JwcUserInfo();
                        jwcUserInfo.setJwcName(infoName.get(i).text());
                        jwcUserInfo.setJwcValue(infoValue.get(i).text());
                        jwcUserInfo.save();
                        Log.d(TAG,"已存"+jwcUserInfo.toString());
                    }
                }
            }
        }

        loginStatus = 1;
        Snackbar.make(userl, R.string.save_success, Snackbar.LENGTH_SHORT).show();
        //取消
        pg.dismiss();
        pg.cancel();

        cloudBackup();
    }

    private void cloudBackup() {
         /*课表云备份 2018年8月29日23点34分*/
        pg = new ProgressDialog(context);
        pg.setTitle("提示");
        pg.setMessage("正在课表云备份，请保持网络畅通");
        pg.setCancelable(false);
        pg.show();

//        try {
//            backupStr = EncryptionUtil.base64_en(backupStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //组装表单
        FormBody formBody = new FormBody.Builder()
                .add(FormData.uid, tustNumber)
                .add(FormData.pass_server, passWord)
                .add(FormData.VALUE,backupStr)
                .build();
        //发起POST请求
        HttpUtil.post(UrlData.uploadSchedule,formBody).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //取消
                        pg.dismiss();
                        pg.cancel();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setTitle("错误").setMessage(getResources().getString(R.string.loginJwcFail_backup_error))
                                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cloudBackup();
                                    }
                                }).setNegativeButton("取消云备份", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       intentMain();
                                    }
                                }).setCancelable(false);
                        builder.show();
                       }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d(TAG,res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    if(jsonObject.getInt(FormData.JCODE) == 2000){
                        //取消
                        pg.dismiss();
                        pg.cancel();

                    }else{
                        throw new JSONException("JCODE ！= 2000");
                    }
                } catch (JSONException e) {
                    //取消
                    pg.dismiss();
                    pg.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "云备份失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
                //成功
               intentMain();
            }
        });
    }

    private void intentMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    //储存信息
    private void saveUserInfo(String tust_number_string, String pswd_string) {
        // 对密码编码
        String enToStr = EncryptionUtil.base64_en(pswd_string);
        SharedPreferencesUtil.Save(LoginActivity.this, FormData.tust_number_server, tust_number_string);
        SharedPreferencesUtil.Save(LoginActivity.this, FormData.pswd, enToStr);
    }

    private void jwcCheckWebView() {
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(UrlData.id_tust_url);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                webview.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);
                String[] array = cookieStr.split(";");
                super.onPageFinished(view, url);
            }
        });
    }

    //辅助登录教务处
    @OnClick(R.id.help_login)
    public void onViewClicked() {
        setDialog("获取","您是否已经进行了统一认证登录，如果没有请先进行认证！");
        getCourseJWC();
    }
}
