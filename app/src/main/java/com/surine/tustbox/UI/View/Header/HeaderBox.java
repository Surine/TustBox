package com.surine.tustbox.UI.View.Header;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.Helper.Interface.UpdateUIListenter;
import com.surine.tustbox.Helper.Utils.AppUtil;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.Helper.Utils.JsonUtil;
import com.surine.tustbox.Helper.Utils.RunOnUiThread;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.PanLoginActivity;
import com.surine.tustbox.UI.Activity.ScoreActiviy;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.UI.Activity.V5_MessageActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static com.surine.tustbox.App.Data.JCode.ERROR;
import static com.surine.tustbox.App.Data.JCode.J400;

/**
 * Created by Surine on 2019/1/28.
 */

public class HeaderBox {
    private static LinearLayout box1;
    private static LinearLayout box2;
    private static LinearLayout box3;
    private static LinearLayout box4;
    private static Badge badge;
    static TustBoxUtil tustBoxUtil;

    public static View getInstance(final Context context, RecyclerView r) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_box, (ViewGroup) r.getParent(), false);
        box1 = view.findViewById(R.id.box1);
        box2 = view.findViewById(R.id.box2);
        box3 = view.findViewById(R.id.box3);
        box4 = view.findViewById(R.id.box4);
        tustBoxUtil = new TustBoxUtil(context);
        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.launchMiniProgramByWxAppId(context,"wx5f22b78c34909007","gh_7153347c2d45","/pages/index/index",0);
            }
        });
        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ScoreActiviy.class));
            }
        });
        box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PanLoginActivity.class));
            }
        });
        box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, V5_MessageActivity.class));
            }
        });

        getUnReadNum(context);

        return view;
    }

    /**
     * 获取未读数目
     * */
    private static int getUnReadNum(final Context context) {
        //获取未读数量
        //取得token和学号
        String token = tustBoxUtil.getToken();
        String uid = tustBoxUtil.getUid();
        String buildUrl = UrlData.getMessageNum + "?" +
                FormData.toUser + "=" + uid + "&" + FormData.token + "=" + token;

        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String serverData = response.body().string();
                RunOnUiThread.updateUi(context, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        try {
                            //获取状态
                            int status = JsonUtil.getStatus(serverData);
                            int unNum = 0;
                            JSONObject jsonObject = new JSONObject(serverData);
                            unNum = jsonObject.getInt(FormData.JDATA);
                            switch (status) {
                               case J400:
                                   //隐藏
                                   if (badge != null) {
                                       badge.hide(true);
                                   }

                                   badge = new QBadgeView(context).bindTarget(box4).setBadgeNumber(unNum).setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                                       @Override
                                       public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                                           if (dragState == 5) {
                                               //拖拽全部已读
                                               AllRead(context);
                                           }
                                       }
                                   });
                                   badge.isDraggable();
                                   break;
                                case ERROR:
                                    //隐藏
                                    if (badge != null) {
                                        badge.hide(true);
                                    }
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return 0;
    }

    private static void AllRead(final Context context) {
        //全部已读
        //取得token和学号
        String token = tustBoxUtil.getToken();
        String uid = tustBoxUtil.getUid();
        String buildUrl = UrlData.readAll + "?" + FormData.toUser + "=" +
                uid + "&" + FormData.token + "=" + token;

        HttpUtil.get(buildUrl).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String serverData = response.body().string();
                RunOnUiThread.updateUi(context, new UpdateUIListenter() {
                    @Override
                    public void update() {
                        int status = JsonUtil.getStatus(serverData);
                        switch (status){
                            case J400:
                                ToastUtil.show(R.string.MainActivityAllRead);
                                break;
                            case ERROR:
                                ToastUtil.jsonError();
                                break;
                        }
                    }
                });
            }
        });


    }
}