package com.surine.tustbox.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cooltechworks.views.ScratchTextView;
import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Pojo.ScoreInfo;

import com.surine.tustbox.Pojo.ScoreInfoHelper;
import com.surine.tustbox.UI.Fragment.score.ScoreNewTermFragment;
import com.surine.tustbox.UI.Fragment.score.ScoreDbFragment;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;

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
    private Context context;
    private List<ScoreInfoHelper> mScoreFromDB = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(R.layout.activity_score);
        context = this;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.score));
        toolbar.setTitleTextAppearance(context,R.style.ToolbarTitle);

        if(!SharedPreferencesUtil.Read(this,DIALOGFORSCORE,false)){
            showNotic();
        }

        //加载Viewpager
        initViewPager();
    }

    private void showNotic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_a_text_notice,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        TextView label = (TextView) view.findViewById(R.id.label);
        TextView message = (TextView) view.findViewById(R.id.message);

        label.setText("成绩使用说明");
        StringBuilder sb = new StringBuilder();
        sb.append("1.成绩获取必须链接校园网，没有校园网的时候，您可以通过本地数据查看您的成绩");
        sb.append("\n");
        sb.append("2.抓取成绩面逻辑比较复杂，所以加载失败也是很常见的，比较有效的方式是返回到首页重新进入多次尝试");
        sb.append("\n");
        sb.append("3.由于成绩是通过教务处系统来获取的，而教务处成绩系统很有可能在某个时间段关闭，所以抓不到成绩的时候，可以通过本地备份查看");
        sb.append("\n");
        message.setText(sb.toString());
       // message.setText("好尴尬，成绩现在不能用哦。\n教务处改版了而且处于不稳定状态，成绩功能暂时不能使用，恢复日期请关注首页公告！");
        ok.setText("不再提醒");
        cancel.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.save(ScoreActiviy.this,DIALOGFORSCORE,true);
                dialog.dismiss();
            }
        });
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
        tabs.setupWithViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.score_menu, menu);
        return true;
    }


    private void shareMyGrade() {
        List<ScoreInfoHelper> mscore_infos = DataSupport.findAll(ScoreInfoHelper.class);
        for(int i = 0;i<mscore_infos.size();i++) {
            my_score_report =my_score_report + mscore_infos.get(i).getCourseName()
                    +":"+mscore_infos.get(i).getCj()+"\n";
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

        double gpa = getGPANew();
        String gpaString = "未计算";
        if(gpa == -1){
            gpaString = "计算错误，某些成绩信息有误";
        }else{
            gpaString = String.valueOf(gpa);
        }

        scratchTextView.setText(getString(R.string.this_term_gpa)+ gpaString);
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


    private double getGPANew(){
        ScoreInfoHelper scoreInfoHelper = DataSupport.findLast(ScoreInfoHelper.class);
        if(scoreInfoHelper == null){
            return 0;
        }
        mScoreFromDB = DataSupport.where("termInfo = ?",scoreInfoHelper.getTermInfo()).find(ScoreInfoHelper.class);

        double scoreSum = 0;
        double gradePointSum = 0;
        try {
            for(ScoreInfoHelper score:mScoreFromDB){
                //Log.d("测试",score.toString());
                //获取绩点和学分绩字符串
                String creditString = score.getCredit();
                String gradePointScoreString = score.getGradePointScore();
                if(creditString == null){
                   throw new NumberFormatException();
                }
                if(gradePointScoreString.equals("null")){
                    //手动计算学分绩
                    gradePointScoreString = String.valueOf((Double.parseDouble(score.getCj()) - 50) / 10);
                }
                //强转
                double credit = Double.parseDouble(creditString);
                double gradePointScore = Double.parseDouble(gradePointScoreString);
                //计算
                gradePointSum = gradePointSum + (credit * gradePointScore);
                scoreSum = scoreSum + credit;
            }
        } catch (NumberFormatException e) {
            scoreSum = -1;  //计算异常
            e.printStackTrace();
        }
        //返回绩点
        try {
            return (gradePointSum / scoreSum) < 0 ? -1 : (gradePointSum / scoreSum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


//    private double getGPA() {
//        try {
//            mscore_infos = DataSupport.findAll(ScoreInfo.class);
//            for(ScoreInfo score:mscore_infos){
//                if(score.getType().equals("THIS")){
//                    mLastScore_infos.add(score);
//                }
//            }
//            for(int i = 0;i<mLastScore_infos.size();i++){
//                if(mLastScore_infos.get(i).getScore().contains(getString(R.string.f1))){
//                    score += 4.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                }else if(mLastScore_infos.get(i).getScore().contains(getString(R.string.f2))){
//                    score += 3.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                }else if(mLastScore_infos.get(i).getScore().contains(getString(R.string.f3))){
//                    score += 2.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                }else if(mLastScore_infos.get(i).getScore().equals(getString(R.string.f4))){
//                    score += 1.5*Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                }else if(mLastScore_infos.get(i).getScore().equals(getString(R.string.f5))){
//                    score += 0*Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                }else{
//                    score += (Double.parseDouble(mLastScore_infos.get(i).getScore())-50)/10
//                            *Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                    credit_add+=Double.parseDouble(mLastScore_infos.get(i).getCredit());
//                }
//            }
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            Toast.makeText(ScoreActiviy.this,"出了一些不可描述的错误",Toast.LENGTH_SHORT).show();
//        }
//        return score/credit_add;
//    }



}
