package com.surine.tustbox.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.ScratchTextView;
import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Bean.ScoreInfo;

import com.surine.tustbox.Fragment.score.ScoreNewTermFragment;
import com.surine.tustbox.Fragment.score.ScoreDbFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Surine on 2018/3/14.
 */

public class ScoreActiviy extends TustBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    double score;
    double credit_add;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private ScratchTextView scratchTextView;
    private TextView sur;
    private List<ScoreInfo> mscore_infos = new ArrayList<>();
    private List<ScoreInfo> mLastScore_infos = new ArrayList<>();
    private String my_score_report = "成绩列表：";
    public static final String DIALOGFORSCORE = "DIALOGFORSCORE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.score));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(!SharedPreferencesUtil.Read(this,DIALOGFORSCORE,false)){
            showNotic();
        }

        //加载Viewpager
        initViewPager();
    }

    private void showNotic() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        StringBuilder sb = new StringBuilder();
        sb.append("1.成绩获取必须链接校园网，没有校园网的时候，您可以通过本地数据查看您的成绩");
        sb.append("\n");
        sb.append("2.抓取成绩面逻辑比较复杂，所以加载失败也是很常见的，比较有效的方式是返回到首页重新进入多次尝试");
        sb.append("\n");
        sb.append("3.由于成绩是通过教务处系统来获取的，而教务处成绩系统很有可能在某个时间段关闭，所以抓不到成绩的时候，可以通过本地备份查看");
        sb.append("\n");
        builder.setTitle("成绩使用说明");
        builder.setMessage(sb.toString());
        builder.setPositiveButton("不再提醒", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtil.Save(ScoreActiviy.this,DIALOGFORSCORE,true);
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void initViewPager() {
        fragments.add(ScoreNewTermFragment.getInstance("NOW"));
        fragments.add(ScoreDbFragment.getInstance("ALL"));
        fragments.add(ScoreDbFragment.getInstance("2"));
        titles.add("本学期");
        titles.add("全部成绩");
        titles.add("本地数据");
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        //7.关联viewpager
        tabs.setupWithViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {

                }
                else if (position==1)
                {

                }
                else if(position==2)
                {

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.gpa:
                //GPA
                showGPADialog();
                break;
            case R.id.share:
                //分享
                shareMyGrade();
                break;
            case R.id.file:
                showFileDialog();
                break;
            case R.id.learn_info:
                showLearnInfoDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLearnInfoDialog() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.learn_info);
        builder.setMessage(pref.getString("learn_info",getString(R.string.no_more_info)));
        builder.setPositiveButton(R.string.ok,null);
        builder.show();
    }

    private void showFileDialog() {
        View view = LayoutInflater.from(ScoreActiviy.this).inflate(R.layout.dialog_view_gpa_calculate_file,null);
        WebView webView = (WebView) view.findViewById(R.id.gpa_file_Webview);
        //WebView :load url at assets
        webView.loadUrl("file:///android_asset/Html/about_me.html");
        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActiviy.this);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void shareMyGrade() {
        List<ScoreInfo> mscore_infos = DataSupport.findAll(ScoreInfo.class);
        for(int i = 0;i<mscore_infos.size();i++) {
            my_score_report =my_score_report + mscore_infos.get(i).getName()
                    +":"+mscore_infos.get(i).getScore()+"\n";
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, my_score_report);
        intent.setType("text/plain");
        //setting the share title
        startActivity(Intent.createChooser(intent, getString(R.string.share_your_score_to)));
        my_score_report = "";
    }

    //显示gpa对话框
    private void showGPADialog() {
        View view = LayoutInflater.from(ScoreActiviy.this).inflate(R.layout.dialog_view_gpa_view,null);
        scratchTextView = (ScratchTextView) view.findViewById(R.id.gpa_guaguaka);
        sur = (TextView) view.findViewById(R.id.surprise);
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("gpa", String.valueOf(getGPA()));
        editor.apply();
        scratchTextView.setText(getString(R.string.this_term_gpa)+ getGPA());
        scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
            @Override
            public void onRevealed(ScratchTextView tv) {
                sur.setVisibility(View.GONE);
            }


            @Override
            public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
                sur.setVisibility(View.GONE);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActiviy.this);
        builder.setView(view);
        builder.show();
    }

    private double getGPA() {
        try {
            mscore_infos = DataSupport.findAll(ScoreInfo.class);
            for(ScoreInfo score:mscore_infos){
                if(score.getType().equals("THIS")){
                    mLastScore_infos.add(score);
                }
            }
            for(int i = 0;i<mLastScore_infos.size();i++){
                if(mLastScore_infos.get(i).getScore().contains(getString(R.string.f1))){
                    score += 4.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
                }else if(mLastScore_infos.get(i).getScore().contains(getString(R.string.f2))){
                    score += 3.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
                }else if(mLastScore_infos.get(i).getScore().contains(getString(R.string.f3))){
                    score += 2.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
                }else if(mLastScore_infos.get(i).getScore().equals(getString(R.string.f4))){
                    score += 1.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
                }else if(mLastScore_infos.get(i).getScore().equals(getString(R.string.f5))){
                    score += 0*Double.parseDouble(mLastScore_infos.get(i).getCredit());
                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
                }else{
                    score += (Double.parseDouble(mLastScore_infos.get(i).getScore())-50)/10
                            *Double.parseDouble(mLastScore_infos.get(i).getCredit());
                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(ScoreActiviy.this,"出了一些不可描述的错误",Toast.LENGTH_SHORT).show();
        }
        return score/credit_add;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.score_menu,menu);
        return true;
    }


}
