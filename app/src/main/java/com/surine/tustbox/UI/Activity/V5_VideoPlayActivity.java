package com.surine.tustbox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class V5_VideoPlayActivity extends TustBaseActivity {

    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;

    Intent intent;
    private String url = null;
    private String name = null;
    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        SystemUI.hideStatusbar(this);
        ButterKnife.bind(this);
        intent = getIntent();
        if(intent == null){
            finishWithDataError();
        }
        //视频连接和名字
        url = intent.getStringExtra(FormData.VIDEO_URL);
        name = intent.getStringExtra(FormData.VIDEO_NAME);

        if(url == null || name == null ||url.isEmpty() || name.isEmpty()){
           finishWithDataError();
        }

        //开始播放
        videoPlayer.setUp(url, true, name);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置全屏按钮隐藏
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);

        //流量提示
        videoPlayer.setNeedShowWifiTip(true);

        //关闭自动旋转
        videoPlayer.setRotateViewAuto(false);
        videoPlayer.setLockLand(true);
//        //设置旋转
//        orientationUtils = new OrientationUtils(this, videoPlayer);
//
//        //强制横屏
//        orientationUtils.setScreenType(SCREEN_ORIENTATION_NOSENSOR);

        //强制全屏
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }


    //不正常启动
    private void finishWithDataError() {
        Toast.makeText(this, "参数错误，启动失败！", Toast.LENGTH_SHORT).show();
        finish();
    }
}
