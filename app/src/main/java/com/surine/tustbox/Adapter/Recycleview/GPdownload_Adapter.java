package com.surine.tustbox.Adapter.Recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Bean.GpInfo;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/24.
 */

public class GPdownload_Adapter extends RecyclerView.Adapter<GPdownload_Adapter.ViewHolder> {
    private List<GpInfo> mGpInfos = new ArrayList<>();
    private Context mContext;

    public GPdownload_Adapter(List<GpInfo> gpInfos, Context context) {
        mGpInfos = gpInfos;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gp,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GpInfo gpInfo = mGpInfos.get(position);
        Glide.with(mContext).load(gpInfo.getImageUrl()).asBitmap().into(holder.gp_iamge);
        holder.gp_name.setText(gpInfo.getGp_name());
        holder.gp_content.setText(gpInfo.getGp_content());
    }

    @Override
    public int getItemCount() {
        return mGpInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gp_iamge;
        TextView gp_name;
        TextView gp_content;

        public ViewHolder(View itemView) {
            super(itemView);
            gp_iamge = (ImageView) itemView.findViewById(R.id.gp_image);
            gp_name = (TextView) itemView.findViewById(R.id.gp_name);
            gp_content = (TextView) itemView.findViewById(R.id.gp_content);
        }
    }
}
