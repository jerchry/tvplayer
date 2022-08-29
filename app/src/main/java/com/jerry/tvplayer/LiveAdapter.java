package com.jerry.tvplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry
 */
public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.MyHolder> implements View
        .OnClickListener {


    private LayoutInflater layoutInflater;
    private List<String> items;
    private OnItemClickListener mItemClickListener;


    public LiveAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_room, viewGroup, false);
        //点击
        view.setOnClickListener(this);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String items = this.items.get(i);
        myHolder.title.setText(items);
//        Glide.with(myHolder.picture).load(items.getPictures().getImg()).into(myHolder.picture);
        //设置标签
        myHolder.itemView.setTag(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * 设置新数据
     *
     * @param liveList
     */
    public void setLiveList(List<String> liveList) {
        items.clear();
        items.addAll(liveList);
    }

    /**
     * 点击回调
     *
     * @param mItemClickListener
     */
    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((String) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
