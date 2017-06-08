package com.example.openjcu.m_home.playground;

import android.content.Intent;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.openjcu.R;
import com.example.openjcu.m_home.playground.forgson.MemoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.openjcu.tool.NetRequest.isOnline;

public class PlaygroundMainAct extends AppCompatActivity {



    String app_url;
    private android.os.Handler handler = new android.os.Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    reflash_l_f.setRefreshing(false);
                    Toast.makeText(PlaygroundMainAct.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    wait=true;
                    break;
                case 1:
                    for(int i=0;i<tempResoureGroup.size();i++){
                        resoureGroup.add(tempResoureGroup.get(i));
                    }
                    wait=true;
                    if(bl){// 如果是第一次请求，需要设置ListView相关
                        adapter = new MemoBeanAdapter(resoureGroup);
                        recyclerView.setAdapter(adapter);
                        bl=false;

                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if(swipeReflash){
                                    requestLost();
                                }else {
                                    if ((recyclerView != null) && (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())){
                                        requestLost();
                                    }
                                }
                            }
                        });


                    }else {
                        adapter.notifyDataSetChanged();  //调用adapter的通知方法告诉listview数据已经改变
                        bl=false;
                        wait=true;
                    }
                    reflash_l_f.setRefreshing(false);
                    break;
                case 2:
//                    Toast.makeText(PlaygroundMainAct.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    reflash_l_f.setRefreshing(false);
                    wait = true; break;

            }
            super.handleMessage(msg);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground_main);
        initView();

    }


    RecyclerView recyclerView;
    List<MemoBean> resoureGroup;
    SwipeRefreshLayout reflash_l_f;
    MemoBeanAdapter adapter;
    Toolbar toolbar;
    public void initView(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        app_url = getResources().getString(R.string.app_url);
        resoureGroup=new ArrayList<MemoBean>();
        tempResoureGroup=new ArrayList<MemoBean>();
        reflash_l_f=(SwipeRefreshLayout)findViewById(R.id.memo_reflash);
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        new Thread(new Runnable() {
            @Override
            public void run() {
                reflash();
            }
        }).start();

    }

    //menu点击事件的处理
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.addmemo:
                Intent in = new Intent(this, CommitMemoActivity.class); startActivity(in); break;
            case 16908332:  finish(); break;
            default: break;
        }
        return true;
    }
    //为toolbar 指定menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playgroudtoolbar, menu);
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
        if(!isOnline(PlaygroundMainAct.this)){
            reflash_l_f.setRefreshing(false);
            Toast.makeText(PlaygroundMainAct.this, "请连接网络", Toast.LENGTH_SHORT).show();
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
                        request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"colorfulwall/obtainMemo.php").build();
                        swipeReflash=false;
                    }else {
                        request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"colorfulwall/obtainMemo.php?time=" + getMinTime()).build();
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


    List<MemoBean> tempResoureGroup;
    public void parseArry(String data) {
        Gson gson = new Gson();
        //ThemeResource[] themeResourcesArray = gson.fromJson(data, ThemeResource[].class);
        tempResoureGroup = gson.fromJson(data, new TypeToken<List<MemoBean>>() {}.getType());

    }




    public String getMinTime() {
        String minTime=resoureGroup.get(0).getMemoCreatTime();;
        for (int i=1;i<resoureGroup.size();i++) {
            String temp=resoureGroup.get(i).getMemoCreatTime();
            if((minTime.compareTo(temp))>0)
                minTime=temp;
        }
        return  minTime;
    }






    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }



}
