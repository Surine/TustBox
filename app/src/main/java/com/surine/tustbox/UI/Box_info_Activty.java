package com.surine.tustbox.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.surine.tustbox.Bean.ScoreInfo;
import com.surine.tustbox.Fragment.gp_download.GP_download_Fragment;
import com.surine.tustbox.Fragment.score.ScoreNewTermFragment;
import com.surine.tustbox.Init.SystemUI;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/6.
 */

public class Box_info_Activty extends TustBaseActivity {
    ScratchTextView scratchTextView;
    TextView sur;
    double score;
    double credit_add;
    private String my_score_report = "成绩列表：";
    private List<ScoreInfo> mscore_infos = new ArrayList<>();
    private List<ScoreInfo> mLastScore_infos = new ArrayList<>();
    private int Flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_box_info);

        //init the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.box_info_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SystemUI.color_toolbar(this,actionBar);
        //Load the Fragment according to the intent
        initFragment(getIntent().getStringExtra("item_box"));
    }

    private void initFragment(String box_item) {
       switch (box_item){
           case "成绩":
               Flag = 0;
               //loading score fragment_task
               setTitle(getString(R.string.loading));
               replaceFragment(ScoreNewTermFragment.getInstance("School_Score"));
               break;
           case "图书":
               Flag = 1;
               Toast.makeText(Box_info_Activty.this,"图书馆系统不稳定，开发哥哥正在努力修复！",Toast.LENGTH_SHORT).show();
               break;
           case "GP":
               Flag = 2;
               setTitle(getString(R.string.loading));
               replaceFragment(GP_download_Fragment.getInstance("GP_download"));
               break;
           case "网络":
               Flag = 3;
               setTitle(getString(R.string.school_network));
               startActivity(new Intent(Box_info_Activty.this,NetWorkActivity.class));
               break;
       }
    }


    //load the fragment_task
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        tran.replace(R.id.box_info_fragment, fragment);
        tran.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gpa,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.gpa:
                showDialog();
                break;
            case R.id.share:
                share_my_grade();
                break;
            case R.id.file:
                show_File_Dialog();
                break;
            case R.id.learn_info:
                show_learn_info_Dialog();
                break;
            case R.id.gp_info:
                show_gp_info_Dialog();
                break;
        }
        return true;
    }

    private void show_gp_info_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_gp);
        builder.setMessage(
                getString(R.string.info_gp));
        builder.setPositiveButton(R.string.ok,null);
        builder.show();
    }

    private void show_learn_info_Dialog() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.learn_info);
        builder.setMessage(pref.getString("learn_info",getString(R.string.no_more_info)));
        builder.setPositiveButton(R.string.ok,null);
        builder.show();
    }

    //show the gpa calculate file
    private void show_File_Dialog() {
        View view = LayoutInflater.from(Box_info_Activty.this).inflate(R.layout.dialog_view_gpa_calculate_file,null);
        WebView webView = (WebView) view.findViewById(R.id.gpa_file_Webview);
        //WebView :load url at assets
        webView.loadUrl("file:///android_asset/Html/about_me.html");
        AlertDialog.Builder builder = new AlertDialog.Builder(Box_info_Activty.this);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
     }

    //share my score to mom
    private void share_my_grade() {
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


    //show the  GPA dialog
    private void showDialog() {
        View view = LayoutInflater.from(Box_info_Activty.this).inflate(R.layout.dialog_view_gpa_view,null);
        scratchTextView = (ScratchTextView) view.findViewById(R.id.gpa_guaguaka);
        sur = (TextView) view.findViewById(R.id.surprise);
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("gpa", String.valueOf(GetGPA()));
        editor.apply();
        scratchTextView.setText(getString(R.string.this_term_gpa)+ GetGPA());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Box_info_Activty.this);
        builder.setView(view);
        builder.show();
    }


    //calculate the GPA
    private double GetGPA() {
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
            Toast.makeText(Box_info_Activty.this,"出了一些不可描述的错误",Toast.LENGTH_SHORT).show();
        }
        return score/credit_add;
    }


    //the method is a control menu update
    //we can use the supportInvalidateOptionsMenu() to update our menu
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem gpa = menu.findItem(R.id.gpa);
        MenuItem share = menu.findItem(R.id.share);
        MenuItem file = menu.findItem(R.id.file);
        MenuItem learn_info = menu.findItem(R.id.learn_info);
        MenuItem gp_info = menu.findItem(R.id.gp_info);
        if(Flag == 0) {
            gp_info.setVisible(false);
        }else if(Flag == 1){

        }else if(Flag == 2){
            gpa.setVisible(false);
            share.setVisible(false);
            file.setVisible(false);
            learn_info.setVisible(false);
        }else if(Flag == 3){

        }
        return true;
    }
}
