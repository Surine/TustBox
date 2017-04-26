package com.surine.tustbox.Adapter.Recycleview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.surine.tustbox.Bean.Box;
import com.surine.tustbox.R;
import com.surine.tustbox.Activity.Box_info_Activty;

import java.util.List;

/**
 * Created by surine on 2017/3/29.
 */

public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder> {
    private List<Box> mBoxes;
    private Context mContext;

    public BoxAdapter(List<Box> boxes,Context context) {
        mBoxes = boxes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_box,parent,false);
        final ViewHolder holder = new ViewHolder(v);

        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if(pos == 0){
                    //score
                    mContext.startActivity(new Intent(mContext, Box_info_Activty.class).putExtra("item_box",0));
                }else if(pos == 1){

                }else if(pos == 2){
                    mContext.startActivity(new Intent(mContext, Box_info_Activty.class).putExtra("item_box",2));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Box box = mBoxes.get(position);
        holder.mTextView.setText(box.getBox_name());
        holder.mImageView.setImageResource(box.getImage_id());
        holder.info.setText(box.getInfo());
        holder.background.setBackgroundColor(mContext.getResources().getColor(box.getColor_id()));

    }
    @Override
    public int getItemCount() {
        return mBoxes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        RelativeLayout background;
        TextView info;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView  = (ImageView) itemView.findViewById(R.id.box_image);
            mTextView = (TextView) itemView.findViewById(R.id.box_text);
            info = (TextView) itemView.findViewById(R.id.info);
            background = (RelativeLayout) itemView.findViewById(R.id.box_background);
        }
    }
}
