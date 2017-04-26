package com.surine.tustbox.Adapter.Recycleview;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.surine.tustbox.Bean.Book_Info;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/11.
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private List<Book_Info> mbook_infos =new ArrayList<>();
    private Context mContext;

    public LibraryAdapter(List<Book_Info> mbook_infos, Context context) {
        this.mbook_infos = mbook_infos;
        mContext = context;
    }

    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library,parent,false);
        ViewHolder holder = new ViewHolder(v);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"续借提醒！",Snackbar.LENGTH_SHORT).setAction("确定续借？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext,"续借",Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(LibraryAdapter.ViewHolder holder, int position) {
        Book_Info book_info = mbook_infos.get(position);
       holder.book_name.setText(book_info.getBook_name());
       holder.author.setText(book_info.getAuthor());
       holder.date.setText(book_info.getDead_line());
       holder.status.setText(book_info.getStatus());
       holder.money.setText(book_info.getMoney());
    }

    @Override
    public int getItemCount() {
        return mbook_infos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_name;
        TextView author;
        TextView date;
        TextView status;
        TextView money;
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            book_name = (TextView) itemView.findViewById(R.id.book_name);
            author = (TextView) itemView.findViewById(R.id.author);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
            money = (TextView) itemView.findViewById(R.id.money);
            button = (Button) itemView.findViewById(R.id.renew);
        }
    }
}
