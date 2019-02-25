package com.surine.tustbox.Mvp.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.Helper.Interface.UpdateUIListenter;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Helper.Utils.RunOnUiThread;
import com.surine.tustbox.Mvp.base.BaseMvpActivity;
import com.surine.tustbox.Pojo.Cookies;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.V5_WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Surine on 2018/9/2.
 */

public class LoginMvpActivity extends BaseMvpActivity implements LoginView {

    @BindView(R.id.login_number_edit)
    EditText loginNumberEdit;
    @BindView(R.id.login_pswd_edit)
    EditText loginPswdEdit;

    @BindView(R.id.dknowtustNumber)
    TextView dknowtustNumber;
    @BindView(R.id.userl)
    TextView userl;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.verifyCodeInput)
    EditText verifyCodeInput;
    @BindView(R.id.verifyCode)
    ImageView verifyCode;

    private LoginPresenter mPresenter;
    private String TAG = this.getClass().getName();
    private String verifyCookie = "";
    private Context myContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        myContext = this;

        mPresenter = new LoginPresenter(this);
        //绑定view
        mPresenter.attachView(this);

        //加载验证码
        mPresenter.getVerifyCode(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mPresenter.detachView();
    }

    @Override
    public void showData(String data) {

    }

    //显示验证码
    @Override
    public void getVerifyData(Cookies cookies) {
        //显示数据及cookie
        final byte[] picBt = (byte[]) cookies.getData();
        verifyCookie = cookies.getMsg();
        LogUtil.d("曲奇"+verifyCookie);
        RunOnUiThread.updateUi(myContext, new UpdateUIListenter() {
            @Override
            public void update() {
                Bitmap bitmap = BitmapFactory.decodeByteArray(picBt, 0, picBt.length);
                verifyCode.setImageBitmap(bitmap);
            }
        });
    }


    @OnClick({R.id.dknowtustNumber, R.id.userl, R.id.btn_login,R.id.verifyCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dknowtustNumber:
                startActivity(new Intent(getContext(), V5_WebViewActivity.class).putExtra(Constants.TITLE, "说明").putExtra(Constants.URL, UrlData.cantLoginHelp));
                break;
            case R.id.userl:
                //  startActivity(new Intent(getContext(), SettingActivity.class).putExtra("set_", 5));
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.verifyCode:
                //加载验证码
                mPresenter.getVerifyCode(this);
                break;
        }
    }

    //登录逻辑
    private void login() {
        String tustNumber = loginNumberEdit.getText().toString();
        String pswd = loginPswdEdit.getText().toString();
        String verifyCode = verifyCodeInput.getText().toString();
        mPresenter.startLogin(tustNumber, pswd,verifyCode,verifyCookie);
    }


}
