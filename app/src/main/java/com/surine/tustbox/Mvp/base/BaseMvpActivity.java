package com.surine.tustbox.Mvp.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

/**
 * Created by Surine on 2018/9/2.
 */

public abstract class BaseMvpActivity extends TustBaseActivity implements BaseView {
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder alertDialog;
    private Dialog dialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //初始化对话框
        mProgressDialog = new ProgressDialog(this);
        //误触开关
        mProgressDialog.setCancelable(false);

        //初始化提示对话框
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);

    }

    @Override
    public void showLoading(final String title, final String msg) {
        //显示加载对话框
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!mProgressDialog.isShowing()){
                    mProgressDialog.setTitle(title);
                    mProgressDialog.setMessage(msg);
                    mProgressDialog.show();
                }
            }
        });
    }

    @Override
    public void setLoadingText(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.setMessage(msg);
            }
        });
    }

    @Override
    public void hideLoading() {
        //隐藏对话框
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void showFailMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        });
    }

    @Override
    public void showDialog(String title, String msg, String p, String n, final DialogEvent dialogEvent) {
        if(dialog == null || !dialog.isShowing()) {
            alertDialog.setTitle(title).setMessage(msg)
                    .setPositiveButton(p, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogEvent.p();
                        }
                    })
                    .setNegativeButton(n, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogEvent.n();
                        }
                    });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = alertDialog.create();
                    dialog.show();
                }
            });
        }
    }

    @Override
    public void hideDialog() {
       //隐藏对话框
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void showErrorMessage() {
        //显示请求错误信息
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(getResources().getString(R.string.api_error_msg));
            }
        });
    }



    @Override
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //toast
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Context getContext() {
        return context;
    }
}
