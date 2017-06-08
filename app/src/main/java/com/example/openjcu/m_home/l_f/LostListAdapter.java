package com.example.openjcu.m_home.communication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 晞余 on 2017/3/22.
 */

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;
import com.example.openjcu.m_home.communication.forgson.ThemeResource;

public class ThemeAdapter  extends ArrayAdapter<ThemeResource> {


        String app_url;
        private int resourceId;
    long ll=System.currentTimeMillis();

        public ThemeAdapter(Context context, int textViewResourceId, List<ThemeResource> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
            if (app_url == null) {
                app_url = getContext().getResources().getString(R.string.app_url);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (app_url == null) {
                app_url = getContext().getResources().getString(R.string.app_url);
            }
            ThemeResource themeItem = getItem(position); // 获取当前项的Fruit实例
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.usericon = (ImageView) view.findViewById (R.id.usericon);
                viewHolder.name = (TextView) view.findViewById (R.id.username);
                viewHolder.title = (TextView) view.findViewById (R.id.themeTitle);
                viewHolder.content = (TextView) view.findViewById (R.id.themeContent);
                view.setTag(viewHolder); // 将ViewHolder存储在View中
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
            }
        //    viewHolder.usericon.setImageResource(themeItem.getUsericon());
            if(themeItem.getUserId().equals("1")) {
                Glide.with(getContext())
                        .load(app_url+"userIcon/" + themeItem.getUserId() + ".gif")
                        .override(100,100)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .placeholder(R.drawable.bgi1)
                        .error(R.drawable.bgi1)
                        .into(viewHolder.usericon);
            }else {

                Glide.with(getContext())
                        .load(app_url+"userIcon/" + themeItem.getUserId() + ".jpg"+"?"+ll)
                        .placeholder(R.drawable.bgi1)
                        .error(R.drawable.bgi1)
                        .into(viewHolder.usericon);
            }
            viewHolder.name.setText(themeItem.getUserName());
            viewHolder.title.setText(themeItem.getTitle());
            viewHolder.content.setText(themeItem.getContent());
            return view;
        }

        class ViewHolder {

            ImageView usericon;

            TextView name;
            TextView title;
            TextView content;

        }



}
