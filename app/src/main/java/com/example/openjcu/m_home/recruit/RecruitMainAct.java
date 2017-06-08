package com.example.openjcu.m_home.recruit;

import android.content.Intent;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.openjcu.R;
import com.example.openjcu.m_home.playground.CommitMemoActivity;
import com.example.openjcu.m_home.recruit.adapter.JobsListAdapter;
import com.example.openjcu.m_home.recruit.forjson.JobsBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.openjcu.tool.NetRequest.isOnline;

public class RecruitMainAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_main);
        initView();
        requestLost();
    }










    String app_url;
    private android.os.Handler handler = new android.os.Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    reflash_l_f.setRefreshing(false);
                    Toast.makeText(RecruitMainAct.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    wait=true;
                    break;
                case 1:
                    for(int i=0;i<tempResoureGroup.size();i++){
                        resoureGroup.add(tempResoureGroup.get(i));
                    }
                    wait=true;
                    if(bl){// 如果是第一次请求，需要设置ListView相关
                        jobsListAdapter = new JobsListAdapter(RecruitMainAct.this, R.layout.job_item,resoureGroup);
                        listView.setAdapter(jobsListAdapter);
                        bl=false;
                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView absListView, int i) { }
                            @Override   //处理滚动冲突
                            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                //firstVisibleItem：屏幕中能看到的第一个item
                                //visibleItemCount：屏幕中能看到的item的总数
                                //totalItemCount：ListView包含的item的总数
                                if (firstVisibleItem == 0)
                                    reflash_l_f.setEnabled(true);
                                else
                                    reflash_l_f.setEnabled(false);
                                if(swipeReflash){
                                    requestLost();
                                }else {
                                    if (firstVisibleItem + visibleItemCount == resoureGroup.size() && resoureGroup.size() == 10) {
                                        requestLost();
                                    }
                                }
                            }
                        });

                    }else {
                        jobsListAdapter.notifyDataSetChanged();  //调用adapter的通知方法告诉listview数据已经改变
                        bl=false;
                        wait=true;
                    }
                    reflash_l_f.setRefreshing(false);
                    break;
                case 2:
//                    Toast.makeText(RecruitMainAct.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    reflash_l_f.setRefreshing(false);
                    wait = true; break;

            }
            super.handleMessage(msg);
        }
    };


    

    ListView listView;
    List<JobsBean> resoureGroup;
    SwipeRefreshLayout reflash_l_f;
    JobsListAdapter jobsListAdapter;
    public void initView(){

        app_url = getResources().getString(R.string.app_url);
        resoureGroup=new ArrayList<>();
        tempResoureGroup=new ArrayList<>();
//        for(int i=0;i<9;i++){
//            resoureGroup
//        }
        reflash_l_f=(SwipeRefreshLayout)findViewById(R.id.nomalJobReflash);
        reflash_l_f.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reflash();
                    }
                }).start();
            }
        });


        listView=(ListView)findViewById(R.id.jobItemList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JobsBean themeItem = resoureGroup.get(position);
                Intent in = new Intent(RecruitMainAct.this, JobDetailActivity.class);
                in.putExtra("FirmDescription",themeItem.getFirmDescription());
                in.putExtra("FirmName",themeItem.getFirmName());
                in.putExtra("FirmPics",themeItem.getFirmPics());
                in.putExtra("FlagPerson",themeItem.getFlagPerson());
                in.putExtra("JobFlag",themeItem.getJobFlag());
                in.putExtra("JobId",themeItem.getJobId());
                in.putExtra("JobPublishTime",themeItem.getJobPublishTime());
                in.putExtra("Jobs",themeItem.getJobs());
                in.putExtra("JobsDescription",themeItem.getJobsDescription());
                in.putExtra("LinkWay",themeItem.getLinkWay());
                in.putExtra("WorkPlace",themeItem.getWorkPlace());
                in.putExtra("WorkPlacePosition",themeItem.getWorkPlacePosition());
                startActivity(in);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.memo_commit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    Toolbar toolbar;

    //    toolbar = (Toolbar) findViewById(R.id.memo_commit_toolbar);
//    setSupportActionBar(toolbar);
//
//    ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

    //menu点击事件的处理
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.addmemo:
//                Intent in = new Intent(this, CommitMemoActivity.class); startActivity(in); break;
            case 16908332:  finish(); break;
            default: break;
        }
        return true;
    }
    //为toolbar 指定menu
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.playgroudtoolbar, menu);
        return true;
    }


    public void onClick(View v){
        switch (v.getId()) {


            default:break;
        }
    }

    public void reflash(){
        if(resoureGroup!=null) {
            resoureGroup.clear();
        }
        if(tempResoureGroup!=null) {
            tempResoureGroup.clear();
        }
        swipeReflash=true;
        requestLost();
    }



    Boolean wait=true,bl=true,swipeReflash=false;
    Response response;
    String jsondata;
    public void requestLost() {
        if(!isOnline(RecruitMainAct.this)){
            reflash_l_f.setRefreshing(false);
            Toast.makeText(RecruitMainAct.this, "请连接网络", Toast.LENGTH_SHORT).show();
            return;
        }
        while (wait) {
            wait=false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client;
                    Request request;
                    client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
                    if (bl || swipeReflash) {
                        request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"recruit/obtain_job_list.php").build();
                        swipeReflash=false;
                    }else {
                        request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"recruit/obtain_job_list.php?time=" + getMinTime()).build();
                    }

                    try {
                        response = client.newCall(request).execute();
                        jsondata = response.body().string();
                        if(jsondata.contains("无记录")){
                            Message message=new Message();
                            message.what=2;
                            handler.sendMessage(message);
                        }else {
                            parseArry(jsondata);
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }

                }
            }).start();
        }
    }


    List<JobsBean> tempResoureGroup;
    public void parseArry(String data) {
        Gson gson = new Gson();
        //ThemeResource[] themeResourcesArray = gson.fromJson(data, ThemeResource[].class);
        tempResoureGroup = gson.fromJson(data, new TypeToken<List<JobsBean>>() {}.getType());

    }




    public String getMinTime() {
        String minTime=resoureGroup.get(0).getJobPublishTime();;
        for (int i=1;i<resoureGroup.size();i++) {
            String temp=resoureGroup.get(i).getJobPublishTime();
            if((minTime.compareTo(temp))>0)
                minTime=temp;
        }
        return  minTime;
    }












}
