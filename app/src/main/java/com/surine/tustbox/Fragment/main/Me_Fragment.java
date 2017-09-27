package com.surine.tustbox.Fragment.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Bean.Book_Info;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.Score_Info;
import com.surine.tustbox.Bean.Student_info;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.LoginActivity;
import com.surine.tustbox.UI.SettingActivity;
import com.surine.tustbox.UI.ToolbarActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/3/28.
 */

public class Me_Fragment extends Fragment {
    private static final String ARG_ = "Me_Fragment";
    private ImageView head;
    private TextView mTextView2;
    private TextView mTextView3;
    private CardView card_view_about_me;
    private LinearLayout setting;
    private LinearLayout exit;
    public static Me_Fragment getInstance(String title) {
        Me_Fragment fra = new Me_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_me, container, false);

        head  = (ImageView) v.findViewById(R.id.head);
        mTextView2 = (TextView) v.findViewById(R.id.textView2);
        mTextView3 = (TextView) v.findViewById(R.id.textView3);
        loadHead();
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        mTextView2.setText(pref.getString("stu_name","未知用户"));
        mTextView3.setText(pref.getString("part","未知学院"));


        card_view_about_me = (CardView) v.findViewById(R.id.card_about_me);
        card_view_about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ToolbarActivity.class);
                intent.putExtra("activity_flag",1);
                startActivity(intent);
            }
        });

        setting = (LinearLayout) v.findViewById(R.id.setting_linear);
        exit = (LinearLayout) v.findViewById(R.id.exit_linear);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exit();
            }
        });
        return v;
    }

    //exit()
    private void Exit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                DataSupport.deleteAll(Course_Info.class);
                DataSupport.deleteAll(Student_info.class);
                DataSupport.deleteAll(Score_Info.class);
                DataSupport.deleteAll(Book_Info.class);
                File file = new File(String.valueOf(getActivity().getFilesDir()+"/head.jpg"));
                deletefile(file);
                file = new File("/data/data/com.surine.tustbox/shared_prefs/data.xml");
                deletefile(file);
                startActivity(new Intent(getActivity(),LoginActivity.class));
                Toast.makeText(getActivity(),
                        R.string.clear_success,
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
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


    private void loadHead() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        boolean value = prefs.getBoolean("setting_close_show_picture", false);
        if(value) {
            Glide.with(getActivity()).load(R.drawable.head).into(head);
        }else{
            Glide.with(getActivity()).load(String.valueOf(getActivity().getFilesDir()+"/head.jpg")).into(head);
        }
    }

    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==1){
            loadHead();
        }
    }
}
