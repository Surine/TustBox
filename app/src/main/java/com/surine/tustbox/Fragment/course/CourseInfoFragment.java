package com.surine.tustbox.Fragment.course;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.EditCourseActivity;
import com.surine.tustbox.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by surine on 2017/4/5.
 */

public class CourseInfoFragment extends Fragment {
    int id;
    @BindView(R.id.ivBackColor)
    ImageView ivBackColor;
    @BindView(R.id.tvCourseName)
    TextView tvCourseName;
    @BindView(R.id.tvCourseScore)
    TextView tvCourseScore;
    @BindView(R.id.tvTeacher)
    TextView tvTeacher;
    @BindView(R.id.tvCourseInfo)
    TextView tvCourseInfo;
    @BindView(R.id.tvCourseExamScore)
    TextView tvCourseExamScore;
    @BindView(R.id.tvCourseOther)
    TextView tvCourseOther;
    @BindView(R.id.btnScore)
    Button btnScore;
    @BindView(R.id.btnRecommand)
    Button btnRecommand;
    Unbinder unbinder;
    @BindView(R.id.edit_course)
    Button editCourse;
    private String course_number;
    private FragmentActivity mContext;

    public static CourseInfoFragment getInstance(String title) {
        CourseInfoFragment fra = new CourseInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("String", title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_info, container, false);
        unbinder = ButterKnife.bind(this, v);
//        initView(v);   //初始化控件
//        initData();
        try {
            initCourseData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    private void initCourseData() throws Exception {
        id = getActivity().getIntent().getIntExtra("course_id", -1);
        if (id == -1) {
            Toast.makeText(getActivity(), getResources().getString(R.string.CourseInfoFragment_load_error), Toast.LENGTH_SHORT).show();
        } else {
            Course_Info course_info = DataSupport.find(Course_Info.class, id);
            course_number = course_info.getCourse_number();

            if(course_info.getCourse_name() != null)
            tvCourseName.setText(course_info.getCourse_name());

            if(course_info.getTeacher() != null)
            tvTeacher.setText("教师：" + course_info.getTeacher());

            if(course_info.getWeek_number() != null && course_info.getClass_() != null)
                tvCourseInfo.setText("节次：星期" + course_info.getWeek_number() + "  " + "第" + course_info.getClass_() + "节");

            if(course_info.getSchool() != null && course_info.getBuilding() != null && course_info.getClassroom() != null)
            tvCourseOther.setText("位置：" + course_info.getSchool() + course_info.getBuilding() + course_info.getClassroom());

            if(course_info.getScore() != null){
                tvCourseExamScore.setText("学分：" + course_info.getScore());
            }
            if(course_info.getColor() != 0){
                ivBackColor.setBackgroundColor(getResources().getColor(course_info.getColor()));
            }
        }

        initScore();

    }

    private void initScore() throws Exception{
        HttpUtil.get(UrlData.getScore + "?" + FormData.courseNumber + "=" + course_number).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                try {
                    if(mContext != null){
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(s == null||s.equals("")){
                                    Toast.makeText(getActivity(), "数据获取错误", Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int code = jsonObject.getInt("jcode");
                                    if (code == 400) {
                                        JSONObject data = jsonObject.getJSONObject("jdata");
                                        float score = (float) data.getDouble(FormData.courseScore);
                                        if(tvCourseScore != null)
                                        tvCourseScore.setText(score + "");
                                    } else {
                                        if(tvCourseScore != null)
                                        tvCourseScore.setText("0");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnScore, R.id.edit_course})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnScore:
                //UI展示
                showScoreDialog();
                break;
            case R.id.edit_course:
                startActivity(new Intent(getActivity(), EditCourseActivity.class).putExtra(FormData.id,id));
                break;
        }
    }


    private void showScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.CourseInfoFragmentsetScoreToTheCourse);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_ratingview, null);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float star = ratingBar.getRating();
                sendService(star);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    private void sendService(float star) {
        if (star == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.CourseInfoFragmentHelpMe), Toast.LENGTH_SHORT).show();
            return;
        }
        FormBody formbody = new FormBody.Builder()
                .add(FormData.courseNumber, course_number)
                .add(FormData.courseScore, String.valueOf(star))
                .build();
        HttpUtil.post(UrlData.setScore, formbody).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(mContext == null){
                    return;
                }
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getResources().getString(R.string.network_no_connect), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                if(mContext != null){
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("jcode");
                                if (code == 400) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.CourseInfoFragmentSetScoreSuccess), Toast.LENGTH_SHORT).show();
                                    JSONObject data = jsonObject.getJSONObject("jdata");
                                    float score = (float) data.getDouble(FormData.courseScore);
                                    tvCourseScore.setText(score + "");
                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

}
