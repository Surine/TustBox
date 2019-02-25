package com.surine.tustbox.Adapter.Recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadGroupEntity;
import com.arialyy.aria.core.inf.AbsEntity;
import com.arialyy.aria.core.inf.IEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.surine.tustbox.R;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Surine on 2019/1/7.
 */

public class DownLoadListAdapter extends BaseQuickAdapter<DownloadEntity,BaseViewHolder> {
    private Map<String, Integer> mPositions = new ConcurrentHashMap<>();

    public DownLoadListAdapter(int layoutResId, @Nullable List<DownloadEntity> data) {
        super(layoutResId, data);
        int i = 0;
        for (AbsEntity entity : data) {
            mPositions.put(getKey(entity), i);
            i++;
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, DownloadEntity item) {
        //服务器文件名字
        helper.setText(R.id.serverName,item.getFileName());
        //文件大小
        helper.setText(R.id.serverFileSize, "文件大小:"+item.getConvertFileSize());
        //当前进度
        helper.setProgress(R.id.downloadProgressBar, item.getPercent());
        //当前速度
        helper.setText(R.id.downloadSpeed, item.getConvertSpeed());

        //显示下载完成
        Button status = helper.getConvertView().findViewById(R.id.openFile);
        Button downloadController = helper.getConvertView().findViewById(R.id.downController);
        ProgressBar progressBar = helper.getConvertView().findViewById(R.id.downloadProgressBar);

        //UI控制
        switch (item.getState()){
            //任务失败
            case 0:
                downloadController.setText("重试");  //设置重试
                downloadController.setClickable(true); //设置可点击
                status.setVisibility(View.GONE);  //关闭可打开
                progressBar.setVisibility(View.GONE);  //进度条关闭
                break;
            //已经完成
            case 1:
                downloadController.setText("完成");  //设置完成
                downloadController.setClickable(false);  //设置不能点击
                status.setVisibility(View.VISIBLE);  //显示可以打开的按钮
                progressBar.setVisibility(View.GONE);  //进度条关闭
                break;
            //停止，未完成
            case 2:
                downloadController.setText("继续");  //提供继续进行的操作
                downloadController.setClickable(true);  //开放点击
                status.setVisibility(View.GONE);  //不能打开
                progressBar.setVisibility(View.VISIBLE); //进度条显示
                break;
            //进行中
            case 4:
                downloadController.setText("暂停"); //提供暂停的操作
                downloadController.setClickable(true); //开放点击
                status.setVisibility(View.GONE);   //不能打开
                progressBar.setVisibility(View.VISIBLE);  //进度条显示
                break;
        }


        helper.addOnClickListener(R.id.openFile);
        helper.addOnClickListener(R.id.downController);
        helper.addOnClickListener(R.id.deleteDownloadTask);
    }

    public void updateState(DownloadEntity entity) {
        if (entity.getState() == IEntity.STATE_CANCEL) {
            mData.remove(entity);
            mPositions.clear();
            int i = 0;
            for (AbsEntity entity_1 : mData) {
                mPositions.put(getKey(entity_1), i);
                i++;
            }
            notifyDataSetChanged();
        } else {
            int position = indexItem(getKey(entity));
            if (position == -1 || position >= mData.size()) {
                return;
            }
            mData.set(position, entity);
            notifyItemChanged(position);
        }
    }

    public void setProgress(DownloadEntity entity) {
        String url = entity.getKey();
        int position = indexItem(url);
        if (position == -1 || position >= mData.size()) {
            return;
        }
        mData.set(position, entity);
        notifyItemChanged(position, entity);
    }

    private synchronized int indexItem(String url) {
        Set<String> keys = mPositions.keySet();
        for (String key : keys) {
            if (key.equals(url)) {
                return mPositions.get(key);
            }
        }
        return -1;
    }

    private String getKey(AbsEntity entity) {
        if (entity instanceof DownloadEntity) {
            return ((DownloadEntity) entity).getUrl();
        } else if (entity instanceof DownloadGroupEntity) {
            return ((DownloadGroupEntity) entity).getGroupName();
        }
        return "";
    }


    /**
     * 暂停执行
     * @param entity 被暂停的任务实体
     * @param context 上下文
     * @param position 实体位置
     *
     * */
    public void pause(AbsEntity entity,Context context,int position){
        //启动下载
        Aria.download(context).load((DownloadEntity) entity).pause();
        //通知刷新按钮
        notifyItemChanged(position);
    }


    /**
     * 恢复下载
     * @param entity 被恢复的实体类
     * @param context 上下文
     * @param position 实体位置
     * */
    public void resume(AbsEntity entity,Context context,int position){
        //启动下载
        Aria.download(context).load((DownloadEntity) entity).start();
        //通知刷新按钮
        notifyItemChanged(position);
    }


}
