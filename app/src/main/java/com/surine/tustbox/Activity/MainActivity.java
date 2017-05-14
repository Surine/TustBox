package com.surine.tustbox.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.surine.tustbox.Bean.Book_Info;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.Score_Info;
import com.surine.tustbox.Bean.Student_info;
import com.surine.tustbox.Fragment.course.Course_Fragment;
import com.surine.tustbox.Fragment.main.Box_Fragment;
import com.surine.tustbox.Fragment.main.Me_Fragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;

import org.litepal.crud.DataSupport;

import java.io.File;

public class MainActivity extends TustBaseActivity {
    int yourChoice;
    private int Flag = 1;
    Fragment mFragment1  = null;
    Fragment mFragment2 = Box_Fragment.getInstance("2");
    Fragment mFragment3 = Me_Fragment.getInstance("3");
    final String[] str={"第一周","第二周",
            "第三周","第四周",
            "第五周","第六周",
            "第七周","第八周",
            "第九周","第十周",
            "第十一周","第十二周",
            "第十三周","第十四周",
            "第十五周","第十六周",
            "第十七周","第十八周",
            "第十九周","第二十周",
            "第二十一周","第二十二周",
            "第二十三周","第二十四周",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init the first fragment
        initFragmentOne("1");

        //iadd all of the fragment to fragmenttransaction
        setFragment();

        //set the normal toolbar title
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        setTitle("课表 [第"+(pref.getInt("choice_week",0)+1)+"周]");


        //init the BottomNavigationView and set linstener
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //set the toolbar title
                        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                        setTitle("课表 [第"+(pref.getInt("choice_week",0)+1)+"周]");
                            //show the fragment
                        Flag = 1;
                        setFragment_Show(Flag);
                        //update the menu
                        supportInvalidateOptionsMenu();
                        return true;
                    case R.id.navigation_dashboard:
                        setTitle(R.string.box);
                        Flag = 2;
                        setFragment_Show(Flag);
                        supportInvalidateOptionsMenu();
                        return true;
                    case R.id.navigation_notifications:
                        setTitle(R.string.me);
                        Flag = 3;
                        setFragment_Show(Flag);
                        supportInvalidateOptionsMenu();
                        return true;
                }

                return false;
            }
        });


        if(!pref.getBoolean("is_login",false)) {
            show_Dialog(); //show the dialog(choose week)
            //save the login status
            SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
            editor.putBoolean("is_login",true);
            editor.apply();
        }
        //set first_fragment
        setFragment_Show(Flag);
    }

    private void initFragmentOne(String flag) {
        /*if fragment is null ,create it directly
        * Otherwise,we have to remove the old one and create a new one
        * */
        if (mFragment1 == null) {
            mFragment1 = Course_Fragment.getInstance(flag);
        }else{
            FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
            tran.remove(mFragment1);
            mFragment1 = Course_Fragment.getInstance(flag);
            tran.add(R.id.content,mFragment1);
            tran.commit();
        }
    }

    //iadd all of the fragment to fragmenttransaction
    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        tran.add(R.id.content, mFragment1);
        tran.add(R.id.content, mFragment2);
        tran.add(R.id.content, mFragment3);
        tran.commit();
    }


    //the method of show Fragment
    private void setFragment_Show(int fragment) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        if(fragment == 1){
            tran.show(mFragment1);
            tran.hide(mFragment2);
            tran.hide(mFragment3);
        }else if(fragment == 2){
            tran.show(mFragment2);
            tran.hide(mFragment1);
            tran.hide(mFragment3);
        }else{
            tran.show(mFragment3);
            tran.hide(mFragment1);
            tran.hide(mFragment2);
        }
        tran.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.week:
                show_Dialog(); //show the dialog(choose week)
                break;
            case R.id.exit:
                Exit();
                break;
            case R.id.setting:
                //go to the setting activity
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.other_user:
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                if(!pref.getBoolean("other_user_is_login",false)){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.putExtra("other_user","other_user");
                    startActivity(intent);
                }else{
                    initFragmentOne("other_user_signal");
                }
                break;
        }
        return true;
    }

    //exit()
    private void Exit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit);
        builder.setMessage(R.string.exit_info);
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               //1:delete all of the database
               //2:delete SharedPreferences
                //3.start the login activty
                //4. make a toast

                //TODO:我们期望在这里，删除所有用户数据，防止下面的用户登录产生的不同影响
                DataSupport.deleteAll(Course_Info.class);
                DataSupport.deleteAll(Student_info.class);
                DataSupport.deleteAll(Score_Info.class);
                DataSupport.deleteAll(Book_Info.class);
                File file = new File(String.valueOf(getFilesDir()+"/head.jpg"));
                deletefile(file);
                file = new File("/data/data/com.surine.tustbox/shared_prefs/data.xml");
                deletefile(file);
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                Toast.makeText(MainActivity.this,
                        R.string.clear_success,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.show();
    }

    private void deletefile(File file) {
        if(file.exists()){
            if(file.isFile()){
                file.delete();   //delete the head or SharedPreferences
            }
        }
    }

    private void show_Dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_week);
        yourChoice = -1;
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        //default selection
        builder.setSingleChoiceItems(str, pref.getInt("choice_week",0), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                yourChoice = i;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (yourChoice != -1) {

                    //1.choose the week and make a SharedPreferences
                    //2.send a broadcase to the widget(update the widget)
                    //3. load fragment
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putInt("choice_week",yourChoice);
                    editor.apply();
                    Toast.makeText(MainActivity.this,
                            getString(R.string.choose_) + str[yourChoice],
                            Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                    setTitle("课表 [第"+(pref.getInt("choice_week",0)+1)+"周]");
                    initFragmentOne("1");
                    Intent updateIntent = new Intent("com.widget.surine.WidgetProvider.MY_UPDATA_CHANGE");
                    sendBroadcast(updateIntent);
                }
            }
        });
        builder.show();
    }

    //the method is a control menu update
    //we can use the supportInvalidateOptionsMenu() to update our menu
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem week = menu.findItem(R.id.week);
        MenuItem exit = menu.findItem(R.id.exit);
        MenuItem setting = menu.findItem(R.id.setting);
        MenuItem other_user = menu.findItem(R.id.other_user);
        if(Flag == 1) {
            week.setVisible(true);
            exit.setVisible(false);
            setting.setVisible(false);
            other_user.setVisible(true);
        }else if(Flag == 2){
            week.setVisible(false);
            exit.setVisible(false);
            setting.setVisible(false);
            other_user.setVisible(false);
        }else{
            week.setVisible(false);
            exit.setVisible(true);
            setting.setVisible(true);
            other_user.setVisible(false);
        }
        return true;
    }
}
