package com.surine.tustbox.UI.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.DownLoadListAdapter;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.IOUtil;
import com.surine.tustbox.UI.View.LittleProgramToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadPageActivity extends TustBaseActivity {

    @BindView(R.id.topbar)
    LittleProgramToolbar topbar;
    @BindView(R.id.downloadList)
    RecyclerView downloadList;
    private List<DownloadEntity> downLoadDataList ;
    private DownLoadListAdapter downloadListAdapter;
    private Context context;
    private String[] group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(R.layout.activity_download_page);
        ButterKnife.bind(this);
        context = this;
        topbar.setTitle(getString(R.string.panDownload))
        .setNoteText("请用校园网下载,下载路径: /sdcard/Download");
        Aria.download(this).register();
        final List<DownloadEntity> list = Aria.download(this).getTaskList();

        if(list == null){
            //获取数据
            downLoadDataList =  new ArrayList<>();
        }else{
            downLoadDataList = list;
        }

        downloadListAdapter = new DownLoadListAdapter(R.layout.item_download_list,downLoadDataList);
        downloadList.setLayoutManager(new LinearLayoutManager(this));
        downloadList.setAdapter(downloadListAdapter);
        downloadListAdapter.setEmptyView(R.layout.view_empty_2,downloadList);
        downloadListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if(view == adapter.getViewByPosition(downloadList,position,R.id.downController)){
                    //获取当前位置实体
                    DownloadEntity downloadEntity = downLoadDataList.get(position);

                    //4是正在执行
                    if(downloadEntity.getState() == 4){
                        downloadListAdapter.pause(downloadEntity,context,position);
                    }else if(downloadEntity.getState() == 2 || downloadEntity.getState() == 0){
                        downloadListAdapter.resume(downloadEntity,context,position);
                    }
                }else if(view == adapter.getViewByPosition(downloadList,position,R.id.deleteDownloadTask)){
                    //获取当前位置实体
                    final DownloadEntity downloadEntity = downLoadDataList.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("删除下载");
                    builder.setMessage("是否删除？");
                    builder.setPositiveButton("仅删除任务", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Aria.download(context).load(downloadEntity).cancel();
                            downLoadDataList.remove(position);
                            downloadListAdapter.notifyItemRemoved(position);
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("删除任务及本地文件", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Aria.download(context).load(downloadEntity).cancel(true);
                            downLoadDataList.remove(position);
                            downloadListAdapter.notifyItemRemoved(position);
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                }else if(view == adapter.getViewByPosition(downloadList,position,R.id.openFile)){
                    final DownloadEntity downloadEntity = downLoadDataList.get(position);
                    //暂时用全部来打开
                    IOUtil.openFile(downloadEntity.getDownloadPath(),"",context);
                }
            }
        });

    }

    @Download.onPre void onPre(DownloadTask task) {
        downloadListAdapter.updateState(task.getEntity());
    }

    @Download.onTaskStart void taskStart(DownloadTask task) {
        downloadListAdapter.updateState(task.getEntity());
    }

    @Download.onTaskResume void taskResume(DownloadTask task) {
        downloadListAdapter.updateState(task.getEntity());
    }

    @Download.onTaskStop void taskStop(DownloadTask task) {
        downloadListAdapter.updateState(task.getEntity());
    }

    @Download.onTaskCancel void taskCancel(DownloadTask task) {
        downloadListAdapter.updateState(task.getEntity());
    }

    @Download.onTaskFail void taskFail(DownloadTask task) {
        downloadListAdapter.updateState(task.getEntity());
    }

    @Download.onTaskComplete void taskComplete(DownloadTask task) {
        downloadListAdapter.updateState(task.getEntity());
    }

    @Download.onTaskRunning() void taskRunning(DownloadTask task) {
        downloadListAdapter.setProgress(task.getEntity());
    }
}
