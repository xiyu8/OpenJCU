package com.example.openjcu.m_home.recruit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.openjcu.R;
import com.example.openjcu.m_home.recruit.forjson.JobsBean;

import java.util.List;

/**
 * Created by 晞余 on 2017/4/22.
 */

public class JobsListAdapter extends ArrayAdapter<JobsBean> {

    String app_url;
    private int job_item_iew;
    long ll=System.currentTimeMillis();

    public JobsListAdapter(Context context, int job_item_iew, List<JobsBean> objects) {
        super(context, job_item_iew, objects);
        this.job_item_iew = job_item_iew;
        if (app_url == null) {
            app_url = getContext().getResources().getString(R.string.app_url);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (app_url == null) {
            app_url = getContext().getResources().getString(R.string.app_url);
        }
        JobsBean lostItem = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(job_item_iew, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.jobName = (TextView) view.findViewById (R.id.jobName);
            viewHolder.jobDescription = (TextView) view.findViewById (R.id.jobDescription);
            viewHolder.jobPublishTime = (TextView) view.findViewById (R.id.jobPublishTime);
            viewHolder.firmName = (TextView) view.findViewById (R.id.firmName);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        //    viewHolder.usericon.setImageResource(themeItem.getUsericon());

        viewHolder.jobName.setText(lostItem.getJobs());
        viewHolder.jobDescription.setText(lostItem.getJobsDescription());
        viewHolder.jobPublishTime.setText(lostItem.getJobPublishTime());
        viewHolder.firmName.setText(lostItem.getFirmName());
        return view;
    }

    class ViewHolder {
        TextView jobName;
        TextView jobDescription;
        TextView jobPublishTime;
        TextView firmName;

    }





}
