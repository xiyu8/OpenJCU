package com.example.openjcu.m_home.communication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openjcu.R;
import com.example.openjcu.m_home.communication.forgson.ThemeResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.androidinject.annotation.present.AIActionBarActivity;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.listener.OnRapidFloatingButtonSeparateListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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


public class CommunicationMainAct extends AIActionBarActivity implements OnRapidFloatingButtonSeparateListener {
    String app_url;
    private RapidFloatingActionButton rfab;
    public void initFB() {

        rfab=(RapidFloatingActionButton) findViewById(R.id.separate_rfab_sample_rfab);
        rfab.setOnRapidFloatingButtonSeparateListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_main);
        initFB();
        initView();
    }


    //////////////////////////////ViewPager////////////////////////
    private ViewPager viewPager;
    PagerTabStrip pagerTabStrip;
    SwipeRefreshLayout view1,view2,view3,view4,view5,view6,view7,view8,view9;
    SwipeRefreshLayout pagers[]=new SwipeRefreshLayout[9];
    ArrayList<SwipeRefreshLayout> viewList;
    ArrayList<ListView> listViews;
    ArrayList<String> titleList;
    ////////////////////////////////List//////////////////////////////////

    public ArrayList<List<ThemeResource>> resourceGroup= new ArrayList<List<ThemeResource>>();
    ThemeAdapter themeAdapters[];
    //////////////////////////////////////////////////////////////////////
    private void initView() {
        first=new Boolean[9];
        myScrollListener = new MyScrollListener[9];
        for (int i=0;i<9;i++) {
            myScrollListener[i]=new MyScrollListener();
            first[i]=true;
        }

        themeAdapters = new ThemeAdapter[9];
        for (int i=0;i<9;i++){
            List<ThemeResource> ll=new ArrayList<>();
            resourceGroup.add(ll);
            tempResourceGroup.add(ll);
        }
        app_url=getResources().getString(R.string.app_url);
 /*********************************************ViewPager************************************************************/
        viewPager = (ViewPager) findViewById(R.id.rfab_group_sample_vp);
        //pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.rfab_group_sample_pts);
        pagerTabStrip.setTabIndicatorColor(Color.parseColor("#ffffff"));
        //pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextSize(0,45); pagerTabStrip.setTextSpacing(10);
        pagerTabStrip.setTextColor(Color.parseColor("#ffffff"));//修改选中tab项字体的颜色/
        pagerTabStrip.setNonPrimaryAlpha(0.2f);  //通过设置透明度来修改未选中tab项的字体颜色/
        LayoutInflater lf = getLayoutInflater().from(this);

        view1 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager1, null);view2 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager2, null);
        view3 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager3, null);view4 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager4 , null);
        view5= (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager5, null);view6 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager6, null);
        view7 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager7, null);view8 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager8, null);
        view9 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager9, null);
        pagers[1]=view2; pagers[2]=view3; pagers[3]=view4; pagers[4]=view5; pagers[5]=view6; pagers[6]=view7; pagers[7]=view8; pagers[8]=view9; pagers[0]=view1;

        viewList = new ArrayList<SwipeRefreshLayout>();// 将要分页显示的View装入数组中
        viewList.add(view1); viewList.add(view2);viewList.add(view3);viewList.add(view4);viewList.add(view5);viewList.add(view6);viewList.add(view7);viewList.add(view8);viewList.add(view9);

        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("电子");titleList.add("计科");titleList.add("机械");titleList.add("文传");titleList.add("土木");titleList.add("建管");titleList.add("艺术");titleList.add("外语");titleList.add("财会");

        PagerAdapter pagerAdapter = new PagerAdapter() {


            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));

            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titleList.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
/*********************************************************************************************************************/


/********************************************List*************************************************************/

        listViews = new ArrayList<ListView>();
        for(int i=0;i<9;i++) {
            listViews.add((ListView)(viewList.get(i)).findViewById(R.id.themeItemList));
            listViews.get(i).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ThemeResource themeItem = (resourceGroup.get(flagPage)).get(position);
                    Intent in = new Intent(CommunicationMainAct.this, ThemeDetailActivity.class);
                    in.putExtra("userName",themeItem.getUserName());
                    in.putExtra("title",themeItem.getTitle());
                    in.putExtra("content",themeItem.getContent());
                    in.putExtra("time",themeItem.getTime());
                    in.putExtra("themeId",themeItem.getThemeId());
                    in.putExtra("pics",themeItem.getThemePics());
                    startActivity(in);
                }

            });
        }

        requestTheme();


    /************************************************************************************************************/
        aboutSwipeReflash();
    }





    //用来标记当前在哪个page
    int flagPage=0;
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        public void onPageSelected(int position) {
            flagPage=position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    @Override
    public void onRFABClick() {
        Intent in = new Intent(this, CommitContentActivity.class);
        startActivity(in);
    }

    /*************************************reflash*******************************************************/
    Boolean swipeReflash=false;
    public void aboutSwipeReflash() {
        //  swp.setColorSchemeColors(Color.argb(1,200,1,1));
        for (int i=0;i<9;i++){
            pagers[i].setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                @Override
                public void onRefresh() {
                    ref();
                }
            });
        }
        view1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                ref();
            }
        });
    }
    public void ref(){
        if(resourceGroup.get(flagPage)!=null) {
            (resourceGroup.get(flagPage)).clear();
        }
        if(tempResourceGroup.get(flagPage)!=null) {
            (tempResourceGroup.get(flagPage)).clear();
        }
        swipeReflash=true;
        requestTheme();

    }
    Boolean first[];
    Boolean wait=true;
    private android.os.Handler handler = new android.os.Handler() {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pagers[flagPage].setRefreshing(false);
                    Toast.makeText(CommunicationMainAct.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    wait=true;
                    break;
                case 1:
//                    ((TextView)findViewById(R.id.testlist)).setText(themeResourcesList.get(8).getContent());
                    for(int i=0;i<(tempResourceGroup.get(flagPage)).size();i++){
                        (resourceGroup.get(flagPage)).add((tempResourceGroup.get(flagPage)).get(i));
                    }
                    wait=true;
                    if(first[flagPage]){// 如果是第一次请求，需要设置ListView相关


                        for(int i=0;i<9;i++){
                                themeAdapters[i] = new ThemeAdapter(CommunicationMainAct.this, R.layout.bbs_item, resourceGroup.get(i));
                                (listViews.get(i)).setAdapter(themeAdapters[i]);
                                (listViews.get(i)).setOnScrollListener(myScrollListener[i]);
                        }
                        for (int i=0; i<9;i++) {
                        }

                        first[flagPage]=false;

                    }else {
                        themeAdapters[flagPage].notifyDataSetChanged();   //调用adapter的通知方法告诉listview数据已经改变
                        first[flagPage]=false;
                    }
                    pagers[flagPage].setRefreshing(false);
                    break;
                case 2:
                    //Toast.makeText(CommunicationMainAct.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    wait=true; break;

            }
            super.handleMessage(msg);
        }
    };


    MyScrollListener myScrollListener[];
    public class MyScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }
        @Override   //处理滚动冲突
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //firstVisibleItem：屏幕中能看到的第一个item
            //visibleItemCount：屏幕中能看到的item的总数
            //totalItemCount：ListView包含的item的总数
            if (firstVisibleItem == 0)
                pagers[flagPage].setEnabled(true);
            else
                pagers[flagPage].setEnabled(false);
            if(firstVisibleItem+visibleItemCount==(resourceGroup.get(flagPage)).size()){
//                ThemeResource th7 = new ThemeResource("1","title1","444444444444","1","2017-3-23 19:20:00","1");
//                themeList.add(th7);  //先改变数据对象data
//                themeAdapter.notifyDataSetChanged();  //调用adapter的通知方法告诉listview数据已经改变
                requestTheme();
            }
//                                Toast.makeText(CommunicationMainAct.this, "firstVisibleItem:"+firstVisibleItem+"__visibleItemCount:"+visibleItemCount+"__totalItemCount"+totalItemCount+"__"+themeList.size(), Toast.LENGTH_SHORT).show();
//                                Log.e("OpenJCU","firstVisibleItem:"+firstVisibleItem+"__visibleItemCount:"+visibleItemCount+"__totalItemCount"+totalItemCount+"__"+themeList.size());
        }
    }




    String jsondata;
    Response response;
    Boolean contentNull;
    public void requestTheme() {
        if(!isOnline(CommunicationMainAct.this)){

            pagers[flagPage].setRefreshing(false);
            Toast.makeText(CommunicationMainAct.this, "请连接网络", Toast.LENGTH_SHORT).show();
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
                    if (first[flagPage]) {
                        request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"bbs/obtain_theme_list.php?category="+(flagPage+1)).build();
                    } else {
                        if(swipeReflash) {
                            request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"bbs/obtain_theme_list.php?category="+(flagPage+1)).build();
                            swipeReflash=false;
                        }else {
                            request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"bbs/obtain_theme_list.php?time=" + getMinTime()+"&category="+(flagPage+1)).build();
                        }
                    }

                    try {
                        response = client.newCall(request).execute();
                        jsondata = response.body().string();
                        if(jsondata.contains("无记录")){
                            Message message=new Message();
                            message.what=2;
                            handler.sendMessage(message);
                        }else
                        parseArry(jsondata);

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

    //获取themeResourcesList中的最小时间
    public String getMinTime(){
        String minTime=((resourceGroup.get(flagPage)).get(0)).getTime();;
        for (int i=1;i<(resourceGroup.get(flagPage)).size();i++) {
            String temp=(resourceGroup.get(flagPage)).get(i).getTime();
            if((minTime.compareTo(temp))>0)
            minTime=temp;
        }
        return  minTime;
    }


    public ArrayList<List<ThemeResource>> tempResourceGroup= new ArrayList<List<ThemeResource>>();

    public void parseArry(String data) {
        Gson gson = new Gson();
        //ThemeResource[] themeResourcesArray = gson.fromJson(data, ThemeResource[].class);
        tempResourceGroup.set(flagPage,((List<ThemeResource>) gson.fromJson(data, new TypeToken<List<ThemeResource>>() {}.getType())));

        Message message=new Message();
        message.what=1;
        handler.sendMessage(message);
    }


    /********************************************************************************************/


    /*************************************List*******************************************************/


    /*******************************************************************************************************/
}
