package com.surine.tustbox.Mvp.login;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.JwcUserInfo;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Manager.OkhttpManager;
import com.surine.tustbox.Mvp.Dao.CourseInfoDao;
import com.surine.tustbox.Mvp.Dao.UserInfoDao;
import com.surine.tustbox.Mvp.base.BaseCallBack;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.EncryptionUtil;
import com.surine.tustbox.Util.LogUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TustBoxUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.surine.tustbox.Data.Constants.colors;

/**
 * Created by Surine on 2018/9/2.
 * mvp:loginmodel
 */

public class LoginImpl implements LoginModel{
    private final String TAG = this.getClass().getName();
    private final CourseInfoDao courseDao;
    private final UserInfoDao userDao;
    private Context context;
    private Resources r;
    private int tag = 0;


    public LoginImpl(Context context) {
        this.context = context;
        r = context.getResources();
        courseDao = new CourseInfoDao();
        userDao = new UserInfoDao();
    }

    @Override
    public void loginJwc(final String tustNumber, final String pswd, final BaseCallBack<String> baseCallBack) {
        //准备登录
        FormBody formBody = new FormBody.Builder()
                .add(FormData.login_id_new, tustNumber)
                .add(FormData.login_pswd_new, pswd)
                .add(FormData.help_var_new, "error")
                .build();
        OkhttpManager.post(UrlData.login_post_url_new,formBody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseCallBack.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                LogUtil.d(TAG,str);
                final Document doc = Jsoup.parse(str);
                if (doc.title().equals(r.getString(R.string.manager_))){

                    saveUserInfo(tustNumber,pswd);

                    baseCallBack.onSuccess(str);
                }else{
                    baseCallBack.onFail(r.getString(R.string.wrong));
                }
                baseCallBack.onComplete();
            }
        });

    }

    private void saveUserInfo(String tustNumber, String pswd) {
        // 对密码编码并保存帐号密码
        String enToStr = EncryptionUtil.base64_en(pswd);
        SharedPreferencesUtil.save(context, FormData.tust_number_server, tustNumber);
        SharedPreferencesUtil.save(context, FormData.pswd, enToStr);
    }

    @Override
    public void getJwCourse(final BaseCallBack<String> baseCallBack){
        OkhttpManager.get(UrlData.getCourseInfoNew).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseCallBack.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                //临时存储课表
                SharedPreferencesUtil.save(context, Constants.COURSE_DATA,str);
                tag = 0; //普通教务标志

                baseCallBack.onSuccess(str);
                baseCallBack.onComplete();
            }
        });
    }


    @Override
    public void parseCourse(String json,BaseCallBack<String> callBack) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String dataList = jsonObject.getString("dateList");
            LogUtil.d(TAG, dataList);

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
                int slec_color = colors[i % 15];
                if (slec_color == 0) {
                    slec_color = colors[0];
                }
                jwColor = slec_color;


                //判断是否两节课
                //取得上课时间地点
                String tapList = courseInfoJsonObject.getString("timeAndPlaceList");

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

            //安全处理结束，若从云备份解析成功后返回TAG1
            callBack.onSuccess("TAG"+tag);
            callBack.onComplete();

        } catch (JSONException e) {
            callBack.onFail(r.getString(R.string.parse_error));
            e.printStackTrace();
            return;
        }
    }


    @Override
    public void getJwInfo(final BaseCallBack<String> baseCallBack) {

        OkhttpManager.get(UrlData.user_info_url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                baseCallBack.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                LogUtil.d(TAG,str);

                if(str.contains("登录")||str.contains("500错误")){
                   baseCallBack.onFail(r.getString(R.string.cookie_error));
                }else{
                   baseCallBack.onSuccess(str);
                }
                baseCallBack.onComplete();
            }
        });

    }

    @Override
    public void parseUserInfo(String json, BaseCallBack<String> baseCallBack) {
        try {
            Document doc = Jsoup.parse(json);
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
                            userDao.add(jwcUserInfo);
                        }
                    }
                }
            }

            baseCallBack.onSuccess(null);
        } catch (Exception e) {
            baseCallBack.onFail(context.getString(R.string.parse_info_error));
            e.printStackTrace();
        }

    }


    @Override
    public void backUpCourse(final BaseCallBack<String> baseCallBack) {

        String tustNumber = new TustBoxUtil(context).getUid();
        String pswd = new TustBoxUtil(context).getPswd();

        pswd = EncryptionUtil.base64_de(pswd);

        String backStr = SharedPreferencesUtil.Read(context, Constants.COURSE_DATA,"");

        if(tustNumber.isEmpty() || pswd.isEmpty() || backStr.isEmpty()){
            baseCallBack.onFail(r.getString(R.string.mvp_empty_field));
            return;
        }

        //组装表单
        FormBody formBody = new FormBody.Builder()
                .add(FormData.uid, tustNumber)
                .add(FormData.pass_server, pswd)
                .add(FormData.VALUE,backStr)
                .build();
        //发起POST请求
        OkhttpManager.post(UrlData.uploadSchedule,formBody).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                //重试，对话框（重试，取消备份）
                baseCallBack.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtil.d(TAG,res);

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    if(jsonObject.getInt(FormData.JCODE) == 2000){
                       baseCallBack.onSuccess(null);
                    }else{
                        throw new JSONException("JCODE ！= 2000");
                    }
                } catch (JSONException e) {
                    baseCallBack.onFail("服务器访问异常。");
                    e.printStackTrace();
                }finally {
                    baseCallBack.onComplete();
                }
            }
        });
    }

    @Override
    public void getBackUp(final String tustNumber, final String passWord, final BaseCallBack<String> baseCallBack) {

        if(tustNumber.isEmpty() || passWord.isEmpty()){
            baseCallBack.onFail(r.getString(R.string.mvp_empty_field));
            return;
        }

//        //密码编码
//        passWord = EncryptionUtil.base64_en(passWord);

        String url = UrlData.downloadSchedule+"?uid="+tustNumber+"&pass="+passWord;
        Log.d(TAG,url);
        OkhttpManager.get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               baseCallBack.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                LogUtil.d(TAG,res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    if(jsonObject.getInt(FormData.JCODE) == 2000){
                        final String data = jsonObject.getString(FormData.JDATA);
                        final String courseStr = new JSONObject(data).getString(FormData.VALUE);
                        SharedPreferencesUtil.save(context,Constants.COURSE_DATA,courseStr);
                        Pattern r = Pattern.compile("&quot;");
                        Matcher m = r.matcher(EncryptionUtil.base64_de(courseStr));
                        tag = 1; //云备份标志
                        saveUserInfo(tustNumber,passWord);
                        baseCallBack.onSuccess(m.replaceAll("\""));
                    }else{
                        throw new JSONException("JCODE ！= 2000");
                    }
                } catch (JSONException e) {
                    baseCallBack.onFail(context.getString(R.string.null_course_or_401));
                }
            }
        });
    }


    //临时缓存
    private void saveCache(String courseName, String attendClassTeacher, String studyModeName, String unit, String programPlanName, int jwColor) {
        SharedPreferencesUtil.save(context,"courseName",courseName);
        SharedPreferencesUtil.save(context,"attendClassTeacher",attendClassTeacher);
        SharedPreferencesUtil.save(context,"studyModeName",studyModeName);
        SharedPreferencesUtil.save(context,"unit",unit);
        SharedPreferencesUtil.save(context,"programPlanName",programPlanName);
        SharedPreferencesUtil.save(context,"jwColor",jwColor);
    }


    //组装存储
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
        courseDao.add(courseInfoHelper);

    }

}





