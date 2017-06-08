package com.example.openjcu.m_home.l_f;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;
import com.example.openjcu.m_home.communication.forgson.ThemeResource;
import com.example.openjcu.m_home.l_f.forJson.L_F_Bean;

import java.util.List;

/**
 * Created by 晞余 on 2017/3/22.
 */

public class LostListAdapter extends ArrayAdapter<L_F_Bean> {


        String app_url;
        private int l_f_itemView;
    long ll=System.currentTimeMillis();

        public LostListAdapter(Context context, int l_f_itemView, List<L_F_Bean> objects) {
            super(context, l_f_itemView, objects);
            this.l_f_itemView = l_f_itemView;
            if (app_url == null) {
                app_url = getContext().getResources().getString(R.string.app_url);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (app_url == null) {
                app_url = getContext().getResources().getString(R.string.app_url);
            }
            L_F_Bean lostItem = getItem(position); // 获取当前项的Fruit实例
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(l_f_itemView, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.l_f_flag = (ImageView) view.findViewById (R.id.l_f_flag);
                viewHolder.l_f_title = (TextView) view.findViewById (R.id.l_f_title);
                viewHolder.l_f_content = (TextView) view.findViewById (R.id.l_f_content);
                viewHolder.l_f_time = (TextView) view.findViewById (R.id.l_f_time);
                view.setTag(viewHolder); // 将ViewHolder存储在View中
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
            }
        //    viewHolder.usericon.setImageResource(themeItem.getUsericon());
            if((lostItem.getL_f_flag()).equals("1")) {
                Glide.with(getContext())
                        .load(R.drawable.lost)
                        .override(100,100)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .placeholder(R.drawable.bgi1)
                        .error(R.drawable.bgi1)
                        .into(viewHolder.l_f_flag);
            }else {

                Glide.with(getContext())
                        .load(R.drawable.found)
                        .placeholder(R.drawable.bgi1)
                        .error(R.drawable.bgi1)
                        .into(viewHolder.l_f_flag);
            }
            viewHolder.l_f_title.setText(lostItem.getL_f_title());
            viewHolder.l_f_content.setText(lostItem.getL_f_content());
            viewHolder.l_f_time.setText(lostItem.getL_f_time());
            return view;
        }

        class ViewHolder {

            ImageView l_f_flag;

            TextView l_f_title;
            TextView l_f_content;
            TextView l_f_time;

        }



}
