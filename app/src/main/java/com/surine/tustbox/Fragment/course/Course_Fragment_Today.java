package com.surine.tustbox.Fragment.course;

/**
 * Created by surine on 2017/9/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Today_Course_Adapter;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.TodayCardActivity;
import com.surine.tustbox.UI.ToolbarActivity;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by surine on 2017/6/3.
 */

public class Course_Fragment_Today extends Fragment{
    public static final String ARG = "Fragment";
    private RecyclerView mRecyclerView;
    private List<Course_Info> mCourse_infos = new ArrayList<>();
    private List<Course_Info> mLast_infos = new ArrayList<>();
    View v;
    private Today_Course_Adapter today_adapter;
    private List<String> week_range;
    private int week;
    private View noView;
    private View headView;
    private String info_message;
    private String title;
    private String picture_url;
    private String web_url;
    private ImageView imageView;
    private JSONObject jsonObject;
    private TextView textview;
    private TextView textview_date;

    public static Course_Fragment_Today getInstance(String title){
        Course_Fragment_Today fragment = new Course_Fragment_Today();
        Bundle bundle = new Bundle();
        bundle.putString(ARG,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_course_today,container,false);
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initTodayInfo();
            }
        }).start();
        return v;
    }

    private void initTodayInfo() {
        HttpUtil.get(UrlData.get_today_url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                info_message = response.body().string();
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Log.d("SDC",info_message);
                       try {
                           jsonObject = new JSONObject(info_message);
                           if (jsonObject.getString("jcode").equals("200")) {
                               JSONObject data = jsonObject.getJSONObject("jdata");
                               title = data.getString("title");
                               picture_url = data.getString("picture_url");
                               web_url = data.getString("web_url");

                               if(SharedPreferencesUtil.Read(getActivity(),"BIRTHDAY","0000").substring(4).equals(TimeUtil.GetDate())){
                                   title = "小天祝你生日快乐！";
                                   Glide.with(getActivity()).
                                           load(R.drawable.birthday).into(imageView);
                               }else{
                                   Glide.with(getActivity()).
                                           load(picture_url).into(imageView);
                               }

                               textview.setText(title);
                               imageView.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent = new Intent(getActivity(),TodayCardActivity.class).
                                               putExtra("ART",web_url).putExtra("ART_TITLE",title).putExtra("ART_IMAGE",picture_url);
//                                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                           startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
//                                                   getActivity(),view,"art_image").toBundle());
//                                       }else{
                                           startActivity(intent);
                                       //}
                                   }
                               });
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               });
                   }
               });

    }


    private void init() {
                try {
                    initData();
                    initView();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "今日课表加载错误，请联系开发者", Toast.LENGTH_SHORT).show();
                }
            }

            private void initData() throws Exception {
                //初始化
                mCourse_infos.clear();
                mLast_infos.clear();
                //查询周
                week = SharedPreferencesUtil.Read(getActivity(), "choice_week", 1);
                //查询周？所上的全部课按照顺序排序
                mCourse_infos = DataSupport.where("week_number like ?", "%" + TimeUtil.GetWeekNumber() + "%").order("class_ asc").find(Course_Info.class);
                //遍历提取本周所上的课
                for (Course_Info course_info : mCourse_infos) {
                    week_range = PatternUtil.getNumber(course_info.getWeek());
                    if (course_info.getWeek().contains("-")) {
                        if (week >= Integer.parseInt(week_range.get(0)) &&
                                week <= Integer.parseInt(week_range.get(1))) {
                            mLast_infos.add(course_info);
                        }
                    } else {
                        for (String a : week_range) {
                            if (Integer.parseInt(a) == week) {
                                mLast_infos.add(course_info);
                            }
                        }
                    }
                }

            }

            private void initView() {
                imageView = (ImageView) v.findViewById(R.id.back);
                textview = (TextView) v.findViewById(R.id.text_title);
                mRecyclerView = (RecyclerView) v.findViewById(R.id.rec_today_course);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                today_adapter = new Today_Course_Adapter(R.layout.item_course_today_new, mLast_infos);
                mRecyclerView.setAdapter(today_adapter);

                noView = getActivity().getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mRecyclerView.getParent(), false);
                noView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                    }
                });
                today_adapter.setEmptyView(noView);


                today_adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        startActivity(new Intent(getActivity(), ToolbarActivity.class).putExtra("course_id",mCourse_infos.get(position).getId()));
                    }
                });
            }

            @Subscribe
            public void GetMessage(SimpleEvent event) {
                if (event.getId() == 5) {
                    update();
                }
            }

    private void update() {
       //更新数据
        try {
            initData();
            today_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
