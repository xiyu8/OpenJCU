package com.example.openjcu.m_home.playground;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;
import com.example.openjcu.m_home.playground.forgson.MemoBean;

import java.util.List;

public class MemoBeanAdapter extends RecyclerView.Adapter<MemoBeanAdapter.ViewHolder>{

    String app_url;
    private List<MemoBean> mMemoBeanList;


    public String[] color={"#FF4500","#F5DEB3","#FFF8DC","#FFFFE0","#6B8E23","#20B2AA","#87CEFA","#FFB6C1","#f6e1a0"};
//Holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        View memoView;
        ImageView memoImageView;
        TextView memoContentView;

        public ViewHolder(View view) {
            super(view);
            memoView = view;
            memoImageView = (ImageView) view.findViewById(R.id.fruit_image);
            memoContentView = (TextView) view.findViewById(R.id.fruit_name);
        }
    }
//要显示的资源
    public MemoBeanAdapter(List<MemoBean> fruitList) {
        mMemoBeanList = fruitList;

    }

    //指定 item 布局，为item添加事件
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        app_url = view.getContext().getResources().getString(R.string.app_url);
        final ViewHolder holder = new ViewHolder(view);



        holder.memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition(); //找到是哪个item
                MemoBean themeItem = mMemoBeanList.get(position);
                Intent in = new Intent(v.getContext(), MemoDetailActivity.class);
                in.putExtra("ColorfulWallContent",themeItem.getColorfulWallContent());
                in.putExtra("ColorfulWallId",themeItem.getColorfulWallId());
                in.putExtra("ColorfulWallPic",themeItem.getColorfulWallPic());
                in.putExtra("MemoCreatTime",themeItem.getMemoCreatTime());
                in.putExtra("UserId",themeItem.getUserId());
                v.getContext().startActivity(in);
            }
        });
        holder.memoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MemoBean fruit = mMemoBeanList.get(position);
                Toast.makeText(v.getContext(), "you clicked image " + fruit.getColorfulWallPic(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    //指定资源的具体呈现
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemoBean fruit = mMemoBeanList.get(position);
        //holder.memoImageView.setImageResource(fruit.getImageId());
        Glide.with(holder.memoView.getContext())
                .load(app_url+"playgroundPics/" + fruit.getColorfulWallId() + "/1.jpg")
                .override(100,100)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .placeholder(R.drawable.bgi1)
                .error(R.drawable.bgi1)
                .into(holder.memoImageView);
        holder.memoContentView.setText(fruit.getColorfulWallContent());
        int n = 0 + (int) (Math.random() * 9);
        //lesson.setBackground(R.drawable.text_view_border);
        holder.memoView.setBackgroundColor(Color.parseColor(color[n]));
    }

    @Override
    public int getItemCount() {
        return mMemoBeanList.size();
    }

}