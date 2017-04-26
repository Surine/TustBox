package com.surine.tustbox.Fragment.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.surine.tustbox.Adapter.Recycleview.LibraryAdapter;
import com.surine.tustbox.Bean.Book_Info;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;

import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/11.
 */

public class Borrow_book_fragment extends Fragment{
    private RecyclerView mRecyclerView;
    private List<Book_Info> mBook_infos = new ArrayList<>();
    private static final String ARG_ ="Borrow_book_fragment" ;
    View v;
    public static Borrow_book_fragment getInstance(String title) {
        Borrow_book_fragment fra = new Borrow_book_fragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_viewpager_and_list, container, false);
        //初始化数据
        initData();

        return v;

    }

    private void initData() {
        mBook_infos = DataSupport.findAll(Book_Info.class);
        if(mBook_infos.size()!=0){
            initView();   //初始化view
        }else{
            Toast.makeText(getActivity(),"本地没有数据哦！",Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.score_rec_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new LibraryAdapter(mBook_infos,getActivity()));
    }


    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==1){
//            initData();
//            initView();
        }
    }
}
