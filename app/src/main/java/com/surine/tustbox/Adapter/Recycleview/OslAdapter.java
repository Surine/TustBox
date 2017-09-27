package com.surine.tustbox.Adapter.Recycleview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surine.tustbox.Bean.Osl_info;
import com.surine.tustbox.R;

import java.util.List;

/**
 * Created by surine on 2017/9/13.
 */

public class OslAdapter extends RecyclerView.Adapter<OslAdapter.ViewHolder>{
    private Context mContext;
    private List<Osl_info> mOsl_infos;

    public OslAdapter(Context context, List<Osl_info> osl_infos) {
        mContext = context;
        mOsl_infos = osl_infos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_osl,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = viewHolder.getAdapterPosition();
                mContext.startActivity(new Intent().setAction(Intent.ACTION_VIEW)
                .setData(Uri.parse(mOsl_infos.get(postion).getContent())));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Osl_info osl_info = mOsl_infos.get(position);
        holder.title.setText(osl_info.getTitle());
        holder.content.setText(osl_info.getContent());
    }

    @Override
    public int getItemCount() {
        return mOsl_infos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;
        private CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.osl_title);
            content = (TextView) itemView.findViewById(R.id.osl_url);
            card = (CardView) itemView.findViewById(R.id.osl_card);
        }
    }
}
