package com.surine.tustbox.Mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Mvp.base.BaseMvpActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.SettingActivity;
import com.surine.tustbox.UI.WebViewActivity;
import com.surine.tustbox.Util.LogUtil;

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

    private LoginPresenter mPresenter;
    private String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPresenter = new LoginPresenter(this);
        //绑定view
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mPresenter.detachView();
    }

    @Override
    public void showData(String data) {
        LogUtil.d(TAG,data);
    }


    @OnClick({R.id.dknowtustNumber, R.id.userl, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dknowtustNumber:
                startActivity(new Intent(getContext(), WebViewActivity.class).putExtra(Constants.TITLE, "说明").putExtra(Constants.URL, UrlData.login_introduce));
                break;
            case R.id.userl:
                startActivity(new Intent(getContext(), SettingActivity.class).putExtra("set_", 5));
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    //登录逻辑
    private void login() {
        String tustNumber = loginNumberEdit.getText().toString();
        String pswd = loginPswdEdit.getText().toString();
        mPresenter.startLogin(tustNumber,pswd);
    }


}
