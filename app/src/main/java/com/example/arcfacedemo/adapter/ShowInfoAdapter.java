package com.example.arcfacedemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arcfacedemo.model.ItemShowInfo;
import com.example.arcfacedemo.R;

import java.util.List;



public class ShowInfoAdapter extends BaseRecyclerViewAdapter<ItemShowInfo> {


    public ShowInfoAdapter(Context mContext, List<ItemShowInfo> mList) {
        super(mContext, mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateMyViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(inflater.inflate(R.layout.item_show_info, parent, false));
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder holder, int position) {
        setData((ContentViewHolder) holder, position);
    }


    //设置相关数据
    private void setData(ContentViewHolder holder, int position) {
        holder.tvNotification.setText(mList.get(position).toString());
        holder.ivHeadImage.setImageBitmap(mList.get(position).getBitmap());
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHeadImage;
        TextView tvNotification;
        public ContentViewHolder(View itemView) {
            super(itemView);
             ivHeadImage = itemView.findViewById(R.id.iv_item_head_img);
             tvNotification = itemView.findViewById(R.id.tv_item_notification);

        }
    }
}
