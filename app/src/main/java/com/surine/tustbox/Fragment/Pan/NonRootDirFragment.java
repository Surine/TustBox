package com.surine.tustbox.Fragment.Pan;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.NonRootDirAdapter;
import com.surine.tustbox.Bean.EventBusBean.TustPanBus;
import com.surine.tustbox.Bean.NonRootDir;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.FunctionTag;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.R;
import com.surine.tustbox.Service.DownloadService;
import com.surine.tustbox.UI.VideoPlayActivity;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.IOUtil;
import com.surine.tustbox.Util.LogUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Surine on 2019/1/2.
 */

public class NonRootDirFragment extends Fragment {

    private static final String ARG = "NonRootDirFragment";
    @BindView(R.id.group)
    RecyclerView group;
    private View v;
    private List<NonRootDir> list = new ArrayList<>();
    private NonRootDirAdapter nonRootDirAdapter;
//    private DownloadService.DownloadBinder downloadBinder;
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            downloadBinder = (DownloadService.DownloadBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    public static NonRootDirFragment getInstance(String title) {
        NonRootDirFragment fragment = new NonRootDirFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String s = (String) bundle.get(ARG);
        LogUtil.d(s);
        try {
            list = GsonUtil.parseJsonWithGsonToList(s,NonRootDir.class);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "该文件/目录无法访问", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_folder, container, false);
        ButterKnife.bind(this, v);
        group.setLayoutManager(new LinearLayoutManager(getActivity()));
        nonRootDirAdapter = new NonRootDirAdapter(R.layout.item_root_dir,list);
        group.setAdapter(nonRootDirAdapter);
        nonRootDirAdapter.setEmptyView(R.layout.view_empty_2,group);

        nonRootDirAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(list.get(position).getIs_dir().equals("false")){
                    String root = list.get(position).getRoot_id();
                    String fileId = list.get(position).getId();
                    String fileName = list.get(position).getName();
                    String opUrl = getOpUrl(root,fileId);
                    //如果是文件
                    if(IOUtil.isVideo(list.get(position).getName())){
                        //播放
                        Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                        intent.putExtra(FormData.VIDEO_URL,opUrl);
                        intent.putExtra(FormData.VIDEO_NAME,fileName);
                        startActivity(intent);
                    }else{
                        //发送文件信息到下载控制器（PanActivity），由PanActivity负责启动Service下载
                        EventBus.getDefault().post(new TustPanBus(FunctionTag.PAN_DOWNLOAD,opUrl,fileName));
                    }
                }else {
                    //是文件夹，通知（PanActivity）来加载下一级别碎片
                    EventBus.getDefault().post(new TustPanBus(FunctionTag.PAN_CODE, list.get(position).getRoot_id(), list.get(position).getPath()));
                }
            }
        });

        return v;
    }


    /**
     * 获取操作url，根据目录id和文件id得到拼装后的url，用于视频播放，或者下载文件
     * @param root 根目录id
     * @param fileId 文件id
     * @return url 拼装后的url
     * */
    private String getOpUrl(String root, String fileId) {
        String token = SharedPreferencesUtil.Read(getActivity(), FormData.TOKEN,null);
        if(token == null){
            Toast.makeText(getActivity(), "本地无Token,请重新登录！", Toast.LENGTH_SHORT).show();
            return "";
        }
        //组合url
        return UrlData.downloadUrl+root+"/files/"+fileId+"?token="+token;

    }

}
