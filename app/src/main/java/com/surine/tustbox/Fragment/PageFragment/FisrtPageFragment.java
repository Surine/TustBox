package com.surine.tustbox.Fragment.PageFragment;

/**
 * Created by surine on 2017/9/16.
 */

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Action_List_Adapter;
import com.surine.tustbox.Adapter.Recycleview.Box_new_adapter;
import com.surine.tustbox.Adapter.Recycleview.Today_Course_Adapter;
import com.surine.tustbox.Bean.Action;
import com.surine.tustbox.Bean.ActionInfo;
import com.surine.tustbox.Bean.Box;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.Image_Grid_Info;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.ActionInfoActivity;
import com.surine.tustbox.UI.Box_info_Activty;
import com.surine.tustbox.UI.NetWorkActivity;
import com.surine.tustbox.UI.ScoreActiviy;
import com.surine.tustbox.UI.ToolbarActivity;
import com.surine.tustbox.UI.UserInfoActivity;
import com.surine.tustbox.Util.GsonUtil;
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
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.surine.tustbox.Fragment.PageFragment.SencondPageFragment.c_info;


public class FisrtPageFragment extends Fragment {
    public static final String ARG = "Fragment";

    //今日课程列表
    @BindView(R.id.rec_today_course)
    RecyclerView recTodayCourse;
    //今日课程标题
    @BindView(R.id.tv_today_course_title)
    TextView tvTodayCourseTitle;

    @BindView(R.id.box_rec)
    RecyclerView boxRec;

    @BindView(R.id.rec_today_message)
    RecyclerView recTodayMessage;


    private List<Course_Info> mCourse_infos = new ArrayList<>();
    private List<Course_Info> mLast_infos = new ArrayList<>();
    View v;
    private Today_Course_Adapter today_adapter;
    private List<String> week_range;
    private int week;
    private View noView;
    private List<Box> mboxs = new ArrayList<>();
    private Box_new_adapter adapter;
    private List<Action> data = new ArrayList<>();
    private Action_List_Adapter actionAdapter;
    private List<Action> actionsFromServer = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private int positionInActions = 1;
    private String[] items;
    private String picIdsInAction;
    private List<Course_Info> mCourse_infos_From_db = new ArrayList<>();
    private Course_Info cInfoFromDB;

    public static FisrtPageFragment getInstance(String title) {
        FisrtPageFragment fragment = new FisrtPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
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
        v = inflater.inflate(R.layout.fragment_index_page, container, false);
        ButterKnife.bind(this, v);

        //初始化课程部分
        initBlockCourse();

        //初始化工具部分
        initBlockTools();

        //初始化动态部分
        initBlockMessage();

        return v;
    }

    private void initBlockMessage() {
        //初始化视图
        initMessageView();
        //初始化数据
        initMessageData();

        actionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), ActionInfoActivity.class).putExtra(FormData.did,actions.get(position).getId()));
            }
        });
        actionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view == adapter.getViewByPosition(recTodayMessage,position,R.id.love)){
                    loveThisAction(actions.get(position).getId(),position);
                }else if(view == adapter.getViewByPosition(recTodayMessage,position,R.id.more)){
                    //展示更多对话框
                    positionInActions = position;
                    showMoreDialog();
                }else if(view == adapter.getViewByPosition(recTodayMessage,position,R.id.action_info_head)){
                    startActivity(new Intent(getActivity(), UserInfoActivity.class).putExtra(FormData.uid,actions.get(position).getUid()));
                }
            }
        });

    }

    private void showMoreDialog() {
        final String items_with_permission[] = {"复制文本", "删除动态"};
        final String items_no_permission[] = {"复制文本"};
        String tust_number = SharedPreferencesUtil.Read(getActivity(), FormData.tust_number_server, "");
        String type = SharedPreferencesUtil.Read_safe(getActivity(), FormData.USER_TYPE, "0");
        if(actions.get(positionInActions).getUid().equals(tust_number)||type.equals("1")){
            items = items_with_permission;
        }else{
            items = items_no_permission;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(which == 0){
                    //复制内容
                    ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                    cmb.setText(actions.get(positionInActions).getMessages_info());
                    Toast.makeText(getActivity(), R.string.ReplyInCommentActivityClipSuccess, Toast.LENGTH_SHORT).show();
                }else if(which == 1){
                    picIdsInAction = actions.get(positionInActions).getPic_ids();
                    deleteAction();
                }
            }
        });
        builder.create().show();
    }

    private void deleteAction() {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(getActivity(), FormData.TOKEN, "");
        String tust_number = SharedPreferencesUtil.Read(getActivity(), FormData.tust_number_server, "");
        HttpUtil.get(UrlData.deleteAction+"?"+FormData.token+"="+token+"&"+FormData.uid+"="+tust_number+"&"+FormData.id+"="+actions.get(positionInActions).getId()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG",x);
                try {
                    JSONObject jsonObject = new JSONObject(x);
                    if(jsonObject.getInt(FormData.JCODE) == 400){
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //删除成功
                                //通知自身列表更新
                                updateList(positionInActions);
                                positionInActions = 0;
                                //TODO：删除七牛云图片
                            }
                        });

                    }else{
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), R.string.ReplyInCommentActivityDeleteFail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateList(int positionInActions) {
        actions.remove(positionInActions);
        actionAdapter.notifyDataSetChanged();
        actionAdapter.notifyItemRemoved(positionInActions);
    }

    //点赞
    private void loveThisAction(int id, final int postion) {
        //取得token和学号
        String token = SharedPreferencesUtil.Read_safe(getActivity(),FormData.token,"");
        String tust_number = SharedPreferencesUtil.Read(getActivity(),FormData.tust_number_server,"");
        String buildUrl = UrlData.loveAction+"?"+FormData.token+"="+token+"&"+FormData.did+"="+
                id+"&"+FormData.uid+"="+tust_number;
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), R.string.network_no_connect, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String x = response.body().string();
                Log.d("TAG",x);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(x);
                    if (jsonObject.getInt(FormData.JCODE) == 400) {
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //点赞成功
                                actions.get(postion).setIslove(true);
                                actionAdapter.notifyItemChanged(postion);
                            }
                        });
                    }else if(jsonObject.getInt(FormData.JCODE) == 401){
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), R.string.null_input, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(jsonObject.getInt(FormData.JCODE) == 500){
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //取消赞
                                actions.get(postion).setIslove(false);
                                actionAdapter.notifyItemChanged(postion);
                            }
                        });
                    }else{
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //其他错误
                                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMessageData() {
        String token = SharedPreferencesUtil.Read_safe(getActivity(), FormData.token,"");
        String tust_number = SharedPreferencesUtil.Read(getActivity(),FormData.tust_number_server,"");
        String buildUrl = UrlData.getAction+"?"+FormData.uid+"="+tust_number+"&"+FormData.page+"="+1;
        Log.d("TAG",buildUrl);
        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                Log.d("TAG",s);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("jcode") == 400) {
                        String s1 = jsonObject.getString("jdata");
                        actionsFromServer.clear();
                        actionsFromServer = GsonUtil.parseJsonWithGsonToList(s1, Action.class);
                        if(getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               updateAction();
                            }
                        });

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateAction() {
        actions.clear();
        if(actionsFromServer.get(0) != null){
            actions.add(actionsFromServer.get(0));
        }
        if(actionsFromServer.get(1) != null){
            actions.add(actionsFromServer.get(1));
        }
        //通知适配器更新数据
        actionAdapter.notifyDataSetChanged();
    }


    private void initMessageView() {
        actionAdapter = new Action_List_Adapter(R.layout.item_action,actions);
        recTodayMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        recTodayMessage.setAdapter(actionAdapter);
    }


    private void initBlockTools() {
        //初始化数据
        initToolData();
        //初始化视图
        initToolView();
    }

    private void initToolData() {
        Box box = new Box(R.drawable.ic_action_score, "成绩", null, R.color.Tust_Green);
        Box box4 = new Box(R.drawable.ic_action_network, "网络", null, R.color.colorPrimary);
        Box box3 = new Box(R.drawable.ic_action_library, "图书", null, R.color.Tust_Red);
        Box box5 = new Box(R.drawable.ic_action_gp, "下载", null, R.color.Tust_more_color_1);
        mboxs.add(box);
        mboxs.add(box3);
        mboxs.add(box5);
        mboxs.add(box4);
    }


    private void initToolView() {
        boxRec.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new Box_new_adapter(R.layout.item_box, mboxs);
        boxRec.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(position == 3){  //3是网络
                    startActivity(new Intent(getActivity(), NetWorkActivity.class));
                }else if(position == 0){ //0是成绩
                    startActivity(new Intent(getActivity(), ScoreActiviy.class));
                }else
                {
                    startActivity(new Intent(getActivity(), Box_info_Activty.class).putExtra("item_box", mboxs.get(position).getBox_name()));
                }
            }
        });
    }


    private void initBlockCourse() {
        try {
            //初始化数据
            initCourseData();
            //初始化视图
            initCourseView();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "今日课表加载错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCourseData() throws Exception{
        //初始化
        mCourse_infos_From_db.clear();
        mCourse_infos.clear();
        mLast_infos.clear();
        //查询周
        week = SharedPreferencesUtil.Read(getActivity(), "choice_week", 1);
        //查询周？所上的全部课
        mCourse_infos_From_db = DataSupport.where("week_number like ?", "%" + TimeUtil.GetWeekNumber() + "%").order("class_ asc").find(Course_Info.class);
//
//        for (Course_Info course_info: mCourse_infos_From_db) {
//            Log.d("DEBUG", "数据库读取数据"+course_info.toString());
//        }

        //周筛选(此时mCourse_infos_From_db是数据库所有数据，mLastList是c_info)
        for (int e = 0; e < mCourse_infos_From_db.size(); e++) {
            if (mCourse_infos_From_db.get(e) != null) {
                //正则解析:提取数字(上课周)
                week_range = PatternUtil.getNumber(mCourse_infos_From_db.get(e).getWeek());
                if (mCourse_infos_From_db.get(e).getWeek().contains("-")) {
                    //连周上课（当上课周不在这个区间）
                    if (!(Integer.parseInt(week_range.get(0)) <= week && Integer.parseInt(week_range.get(1)) >= week)) {
                        mCourse_infos_From_db.set(e, c_info);
                    }
                } else {
                    if (week_range.indexOf(week + "") == -1) {
                        mCourse_infos_From_db.set(e,c_info);
                    }
                }
            }
        }

//        for (Course_Info course_info: mCourse_infos_From_db) {
//            if(course_info != null)
//            Log.d("DEBUG", "数据库处理后数据"+course_info.toString());
//        }

        //初始化
        Course_Info[] coursesArray = new Course_Info[6];  //每天6节课
        for(int i = 0;i <= 5;i++){
            coursesArray[i] = c_info;
        }
        for (Course_Info c: mCourse_infos_From_db) {
            if(c != null){
                if(c.getClass_().contains("一")){
                    coursesArray[0] = c;
                }else if(c.getClass_().contains("二")){
                    coursesArray[1] = c;
                }else if(c.getClass_().contains("三")){
                    coursesArray[2] = c;
                }else if(c.getClass_().contains("四")){
                    coursesArray[3] = c;
                }else if(c.getClass_().contains("五")){
                    coursesArray[4] = c;
                }else if(c.getClass_().contains("六")){
                    coursesArray[5] = c;
                }
            }
        }
        mCourse_infos = Arrays.asList(coursesArray);

        for (Course_Info course_info: mCourse_infos) {
         //  Log.d("DEBUG", "最终数据"+course_info.toString());
            if(!course_info.getCourse_number().equals("*")){
                mLast_infos.add(course_info);
            }
        }

//        for (Course_Info course_info: mLast_infos) {
//            if(course_info != null)
//            Log.d("DEBUG", "最终数据"+course_info.toString());
//        }

        tvTodayCourseTitle.setText(TimeUtil.GetWeek()+"课程");


    }

    private void initCourseView() {

        recTodayCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        today_adapter = new Today_Course_Adapter(R.layout.item_course_today_new, mLast_infos);
        recTodayCourse.setAdapter(today_adapter);

        noView = getActivity().getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) recTodayCourse.getParent(), false);
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
                if(mLast_infos.get(position) != null){
                    startActivity(new Intent(getActivity(), ToolbarActivity.class).putExtra("course_id", mLast_infos.get(position).getId()));
                }
            }
        });
    }

    @Subscribe
    public void GetMessage(SimpleEvent event) {
        if (event.getId() == 0) {
            update();
        }
    }

    private void update() {
        //更新数据
        try {
            initCourseData();
            initCourseView();
          } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
