package com.surine.tustbox.Fragment.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.surine.tustbox.Activity.SplashActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.ClearCacheUtil;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/4/8.
 */

public class Set_BackgroundFragment extends TakePhotoFragment {
    ImageView imageview;
    Button restart;
    Button reset;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_set_background,container,false);

        TakePhoto takePhoto = getTakePhoto();
        Config_compress(takePhoto);
        configTakePhotoOption(takePhoto);
        takePhoto.onPickFromDocumentsWithCrop(Config_the_file(),getCropOptions());

        initView(view);
        return view;
    }



    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
        Snackbar.make(imageview,"图片储存成功",Snackbar.LENGTH_SHORT).show();
    }

    private void showImg(ArrayList<TImage> images) {
        Log.d("show_url", "showImg: "+images.get(0).getCompressPath());
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("my_picture_path",images.get(0).getCompressPath());
        editor.apply();
        Glide.with(this).load(new File(images.get(0).getCompressPath())).into(imageview);
    }

    private void initView(View view) {
        imageview = (ImageView) view.findViewById(R.id.my_choose_image);
        restart = (Button) view.findViewById(R.id.restart);
        reset = (Button) view.findViewById(R.id.reset);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartMethod();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cachePath2 =  getActivity().getCacheDir().getPath();
                ClearCacheUtil.delAllFile(cachePath2,getActivity());
                restartMethod();
            }
        });
    }

    private void restartMethod() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        android.os.Process.killProcess(android.os.Process.myPid());
        getActivity().startActivity(intent);
    }

    private void configTakePhotoOption(TakePhoto takePhoto){
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
            builder.setWithOwnGallery(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    private void Config_compress(TakePhoto takePhoto) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight()-140;
        int maxSize = 102400;
        CompressConfig config=new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width>=height? width:height)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config,false);
     }

    private Uri Config_the_file() {
        File file=new File(Environment.getExternalStorageDirectory(), "/surine/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    private CropOptions getCropOptions(){

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight()-140;
        CropOptions.Builder builder=new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(true);
        return builder.create();
    }


}
