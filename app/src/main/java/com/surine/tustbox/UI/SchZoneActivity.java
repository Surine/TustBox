package com.surine.tustbox.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SchZoneActivity extends TustBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.addClass)
    LinearLayout addClass;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sch_zone);
        ButterKnife.bind(this);
        context = this;
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.sch_zone));
        mToolbar.setTitleTextAppearance(context, R.style.ToolbarTitle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                finishWithAnimation();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finishWithAnimation();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishWithAnimation() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.task_enter_anim,
                R.anim.task_exit_anim);
        finish();
    }

    @OnClick(R.id.addClass)
    public void onViewClicked() {
        startActivity(new Intent(this,EditCourseActivity.class).putExtra(FormData.CREATE,FormData.CREATE));
    }
}
