package com.surine.tustbox.Adapter.Recycleview;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Pojo.GpInfo;
import com.surine.tustbox.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

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
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Toast.makeText(mContext,
                        "下载详情信息已经复制到剪贴板，请发送至电脑访问!\n"+mGpInfos.get(position).getGp_download_url(),
                        Toast.LENGTH_SHORT).show();

                ClipboardManager cmb = (ClipboardManager)mContext.getSystemService(CLIPBOARD_SERVICE);
                cmb.setText(mGpInfos.get(position).getGp_download_url());
            }
        });
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
        CardView mCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            gp_iamge = (ImageView) itemView.findViewById(R.id.gp_image);
            gp_name = (TextView) itemView.findViewById(R.id.gp_name);
            gp_content = (TextView) itemView.findViewById(R.id.gp_content);
            mCardView = (CardView) itemView.findViewById(R.id.gp_copy);
        }
    }
}
