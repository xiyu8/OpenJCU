package com.example.openjcu.mainfragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openjcu.MainActivity;
import com.example.openjcu.R;
import com.example.openjcu.m_home.Tab1Activity;
import com.example.openjcu.m_home.abroad.AbroadMainAct;
import com.example.openjcu.m_home.communication.CommunicationMainAct;
import com.example.openjcu.m_home.home_map.BNDemoMainActivity;
import com.example.openjcu.m_home.home_map.PositionGeoActivity;
import com.example.openjcu.m_home.home_map.RoutePlanActivity;
import com.example.openjcu.m_home.info_search.InfoSearchMainAct;
import com.example.openjcu.m_home.l_f.LostfoundMainAct;
import com.example.openjcu.m_home.playground.PlaygroundMainAct;
import com.example.openjcu.m_home.position.PositionMainAct;
import com.example.openjcu.m_home.go.GoMainActivity;
import com.example.openjcu.m_home.postgraduate.PostgraduateMainAct;
import com.example.openjcu.m_home.recruit.RecruitMainAct;
import com.example.openjcu.mainfragment.homefragment_toolclass.ParseJsonForResource;
import com.example.openjcu.mainfragment.homefragment_toolclass.ParseJsonForVersion;
import com.example.openjcu.tool.NetRequest;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.openjcu.tool.NetRequest.isOnline;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
















    //fragment 需要事件的View
    Button b;
    MainActivity A;

    /*********************************ViewPager 相关*************************************************/
    public ViewPager viewPager;  //viewpager
    View v1,v2,v3,v4,v5; //Pager显示的view
    ArrayList viewList; //装每个viewlayout的容器
    private PagerTabStrip pagerTabStrip; //pager头
    private List<View> dots;
    String[] home_pager_title={"","","","",""};
    String[] home_pager_surl={"","","","",""};
    TextView pageText;
    MyViewPagerAdapter pagerAdapter;
    private ScheduledExecutorService scheduledExecutorService;  //定时器
    private int currentItem = 5000;
    boolean nowAction = false;// 当前用户正在滑动视图
    private Handler handler = new Handler() {      //定时器用于更新dots的handler
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);
        };
    };
    /************************************************************************************************/


    /*********************************homefragment界面的点击事件相关*************************************************/
    View v;
    LinearLayout go,infoSearch,communication,recruit,abroad,postgraduate,playground,l_f,position;
    OnFragViewClickListener lst;
    /************************************************************************************************************/


    @Override   //在Activity的onCreateView中写的在Fragment的这里写
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        A= (MainActivity) getActivity();   //在Fragment中找到活动
        init(A);
      //  viewPager.setCurrentItem(currentItem+1);

    }

    private void init(Activity A) {
        /* **************************ViewPager相关init***************************************** *//*  */
        //ViewPager Titil相关
        pagerTabStrip=(PagerTabStrip) A.findViewById(R.id.pagertab);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextSpacing(50);
        pagerTabStrip.setVisibility(View.VISIBLE);
        //为ViewPager设置View
        viewList = new ArrayList<View>();// 将要分页显示的layoutView装入数组中
        LayoutInflater inflater=A.getLayoutInflater();  //找到要在viewpager中显示的layout
        v1 = inflater.inflate(R.layout.fragment_pager1, null);
        v2 = inflater.inflate(R.layout.fragment_pager2,null);
        v3 = inflater.inflate(R.layout.fragment_pager3, null);
        v4 = inflater.inflate(R.layout.fragment_pager4, null);
        v5 = inflater.inflate(R.layout.fragment_pager5,null);
        View.OnClickListener onPageListener = new OnPageClickListener();
        viewList.add(v1); v1.setOnClickListener(onPageListener);
        viewList.add(v2); v2.setOnClickListener(onPageListener);
        viewList.add(v3); v3.setOnClickListener(onPageListener);
        viewList.add(v4); v4.setOnClickListener(onPageListener);
        viewList.add(v5); v5.setOnClickListener(onPageListener);
        //所有指示点
        dots = new ArrayList<View>();
        dots.add(A.findViewById(R.id.v_dot0));
        dots.add(A.findViewById(R.id.v_dot1));
        dots.add(A.findViewById(R.id.v_dot2));
        dots.add(A.findViewById(R.id.v_dot3));
        dots.add(A.findViewById(R.id.v_dot4));
        pageText = (TextView) A.findViewById(R.id.pageText);
        //为pager设置adapter和listener
        viewPager = (ViewPager)A. findViewById(R.id.viewpager); //找到viewpager
        pagerAdapter=new MyViewPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(Integer.MAX_VALUE / 4);
        /* *************************************************************************************** */

        lst=new OnFragViewClickListener();
        A.findViewById(R.id.map_test).setOnClickListener(lst);
        A.findViewById(R.id.map_test1).setOnClickListener(lst);
        A.findViewById(R.id.map_test2).setOnClickListener(lst);
        A.findViewById(R.id.map_test3).setOnClickListener(lst);
        go=(LinearLayout)A.findViewById(R.id.go); go.setOnClickListener(lst);
        position=(LinearLayout)A.findViewById(R.id.position); position.setOnClickListener(lst);
        infoSearch=(LinearLayout)A.findViewById(R.id.info_search); infoSearch.setOnClickListener(lst);
        communication=(LinearLayout)A.findViewById(R.id.conmmunication); communication.setOnClickListener(lst);
        recruit=(LinearLayout)A.findViewById(R.id.recruit); recruit.setOnClickListener(lst);
        abroad=(LinearLayout)A.findViewById(R.id.abroad); abroad.setOnClickListener(lst);
        postgraduate=(LinearLayout)A.findViewById(R.id.postgraduate); postgraduate.setOnClickListener(lst);
        playground=(LinearLayout)A.findViewById(R.id.playground); playground.setOnClickListener(lst);
        l_f=(LinearLayout)A.findViewById(R.id.l_f); l_f.setOnClickListener(lst);


        //swipeReflash
        aboutSwipeReflash();
        client=new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).cache(new Cache(new File(A.getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();

        request[0]= new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(addr1).build();
        request[1] = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(addr+"p1.png").build();
        request[2] = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(addr+"p2.png").build();
        request[3] = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(addr+"p3.png").build();
        request[4] = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(addr+"p4.png").build();
        request[5] = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(addr+"p5.png").build();
        request1[0]= new Request.Builder().cacheControl(CacheControl.FORCE_CACHE).url(addr1).build();
        request1[1] = new Request.Builder().cacheControl(CacheControl.FORCE_CACHE).url(addr+"p1.png").build();
        request1[2] = new Request.Builder().cacheControl(CacheControl.FORCE_CACHE).url(addr+"p2.png").build();
        request1[3] = new Request.Builder().cacheControl(CacheControl.FORCE_CACHE).url(addr+"p3.png").build();
        request1[4] = new Request.Builder().cacheControl(CacheControl.FORCE_CACHE).url(addr+"p4.png").build();
        request1[5] = new Request.Builder().cacheControl(CacheControl.FORCE_CACHE).url(addr+"p5.png").build();
    }

    /***************************************下拉刷新************************************************************/
    public SwipeRefreshLayout swp;

    public void aboutSwipeReflash() {

        swp = (SwipeRefreshLayout) A.findViewById(R.id.h_swp_reflash);
        //      swp.setColorSchemeColors(Color.argb(1,200,1,1));
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ref();
            }
        });
    }

    //    public void ref(){
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////
////                try {
////                    Thread.sleep(500);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                A.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        swp.setRefreshing(false);
////                    }
////                });
////
////
////            }
////        }).start();
//
//
//
//
////        NetRequest.sendRequest("http://192.168.155.1/www/OpenJCU/p1.png",new okhttp3.Callback(){
////
////            @Override
////            public void onFailure(Call call, IOException e) {
////
////            }
////
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////                final byte[] Picture_bt = response.body().bytes();
////                //通过handler更新UI
////                A.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////
////                        //使用BitmapFactory工厂，把字节数组转化为bitmap
////                        Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
////                        //通过imageview，设置图片
////                        ((ImageView)v2.findViewById(R.id.pg_im2)).setImageBitmap(bitmap);
////                        swp.setRefreshing(false);
////                    }
////                });
////
////            }
////        },A);
//    }
    public void ref() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        swp.setRefreshing(false);
//                    }
//                });
                int a=requestLogic();
                Log.e("OpenJCU",")))))))))))))))))))))"+a);
                if(a==0){
                    Message msg=new Message();
                    msg.what=0;
                    mHandler.sendMessage(msg);
                    return;
                }
            }

        }).start();
    }


    /***********************************网络请求**********************************************/

//    ParseJsonForVersion parseJsonForVersion=new ParseJsonForVersion();
//    ParseJsonForResource parseJsonForResource=new ParseJsonForResource();
//    public void requestLogic() {
//        //是否有网络连接
//        if (isOnline(A)) {  //如果有
//
//            // 获取SharedPreferences中的OpenJCU文件的Editor
//            SharedPreferences preferences = A.getSharedPreferences("OpenJCU", A.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//
//            // 看fragment_home_ver字段中是否有值、有什么值    //读取fragment_home_ver字段，如果没有这个字段，创建，并给这个字段赋值0.00
//            String haveFragment_home_ver = preferences.getString("fragment_home_ver", "0.00");
//
//            // 如果没有这个字段的值
//            if (haveFragment_home_ver.equals("0.00")) {  //请求版本，更新本地版本，请求资源，更新呈现资源
//                s1=requestVersion();
//                Log.e("OpenJCU","dd99999999999999999999999999999"+s1);
//                editor.putString("fragment_home_ver",s1);  editor.apply();//更新本地版本；
//                requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/","http://192.168.155.1/www/OpenJCU/fragment_viewpager_string.json",0);
//                //requestVertion\如果没有这个字段的值，直接请求网络的数据，并写入值的信息作为本地版本号
//            } else {  //1请求版本，比较版本信息，2请求版本，比较版本信息，更新本地版本，请求资源，更新呈现资源
//                //如果有这个字段的值，请求网络，比较这个值和网络请求下来的版本信息
//                s1=requestVersion();
//                Log.e("OpenJCU","99999999999999999999999999999"+s1);
//                Log.e("OpenJCU","99999999999999999999999999999"+haveFragment_home_ver);
//                if(s1.equals(haveFragment_home_ver)){
//                    requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/","http://192.168.155.1/www/OpenJCU/fragment_viewpager_string.json",1);
//                } else {
//                    editor.putString("fragment_home_ver",s1);  editor.apply();  //更新本地版本；
//                    requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/","http://192.168.155.1/www/OpenJCU/fragment_viewpager_string.json",0);
//                }
//
//            }
//        } else {        //如果没有
//            Toast.makeText(A, "请连接网络", Toast.LENGTH_SHORT).show();
//            requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/","http://192.168.155.1/www/OpenJCU/fragment_viewpager_string.json",1);
//        }
//    }



//    String s1;  //用来存解析vresion_Json的结果
//    //request resource's version
//    public String requestVersion() {
//        s1=null;
//        Request request = new Request.Builder()
//                    //强制使用网络
//                       .cacheControl(CacheControl.FORCE_NETWORK)
//                       //强制使用缓存
//                      // .cacheControl(CacheControl.FORCE_CACHE)
//                       .url("http://192.168.155.1/www/OpenJCU/update.json").build();
//        client.newCall(request).enqueue(new okhttp3.Callback(){
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("OpenJCU", "请求资源版本信息失败");
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //s1 =parseJSON(response.body().string());
//                String data=response.body().string();
//                ParseJsonForVersion pj=new ParseJsonForVersion();
//                s1=pj.parseJSON(data);
//            }
//        });
//        while (s1==null) {   //接口的回调为异步，在接口回调中操作的变量时间不够可能没来得及赋值
//
//        }
//        return s1;
//    }


//    OkHttpClient client;
//    Request request[]=new Request[6];
//    Request request1[]=new Request[6];   int flag=1;
//    String addr = "http://192.168.155.1/www/OpenJCU/";
//    String addr1 = "http://192.168.155.1/www/OpenJCU/fragment_viewpager_string.json";
//    Bitmap bitmap[]=new Bitmap[5];
//    //缓存路径、缓存区的大小(没网时读缓存)
//    public void requestResourceAndUpdateUI(String addr,String addr1, int formWhere){
//        if(formWhere==0){
//            //强制使用网络
//            for(int i=0;i<=5;i+=1) {
//                final int j=i;
////                while (flag==0){  }
////                    flag=0;
//
//                Log.e("OpenJCU","44444444444444444444444444444444444");
//                client.newCall(request[i]).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e("OpenJCU","请求资源失败");
//                    }
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("OpenJCU","66666666666666666666666666666666666");
//
//                        if (j == 0) {
//                            String data=response.body().string();
//                            String[] resouce=(new ParseJsonForResource()).parseStringJSON(data);
//                            for(int i=0;i<10;i++){
//                                if(i<5)  home_pager_title[i] = resouce[i];
//                                else {
//                                    home_pager_surl[i - 5] = resouce[i];
//                                }
//                            }
//                        } else {
//                            final byte[] Picture_bt1 = response.body().bytes();
//                            bitmap[j-1] = BitmapFactory.decodeByteArray(Picture_bt1, 0, Picture_bt1.length);
//                        }
//                        flag=1;
//                    }
//                });
//            }
//            while (flag==0) {  }
//                ((ImageView) v1.findViewById(R.id.pg_im1)).setImageBitmap(bitmap[0]);
//                ((ImageView) v2.findViewById(R.id.pg_im2)).setImageBitmap(bitmap[1]);
//                ((ImageView) v3.findViewById(R.id.pg_im3)).setImageBitmap(bitmap[2]);
//                ((ImageView) v4.findViewById(R.id.pg_im4)).setImageBitmap(bitmap[3]);
//                ((ImageView) v5.findViewById(R.id.pg_im5)).setImageBitmap(bitmap[4]);
//
//            swp.setRefreshing(false);
//
//        }else{
//            //强制使用缓存
//        //    flag=0;
//            for(int i=0;i<=5;i+=1) {
//                final int j=i;
////                while (flag==0){   }
////                flag=0;
//
//                Log.e("OpenJCU","44444444444444444444444444444444444");
//                client.newCall(request1[i]).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {  Log.e("OpenJCU","请求缓存资源失败");   }
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("OpenJCU","66666666666666666666666666666666666");
//
//                        if (j == 0) {
//                            String data=response.body().string();
//                            String[] resouce=(new ParseJsonForResource()).parseStringJSON(data);
//                            for(int i=0;i<10;i++){
//                                if(i<5)  home_pager_title[i] = resouce[i];
//                                else {
//                                    home_pager_surl[i - 5] = resouce[i];
//                                }
//                            }
//                        } else {
//                            final byte[] Picture_bt1 = response.body().bytes();
//                            bitmap[j-1] = BitmapFactory.decodeByteArray(Picture_bt1, 0, Picture_bt1.length);
//                        }
//                        if(j==5){
//                            Log.e("OpenJCU","aaaaaaaaaaaaaaa");
//                        flag=1;}
//                    }
//                });
//            }
//            while (flag==0) {  }
//            ((ImageView) v1.findViewById(R.id.pg_im1)).setImageBitmap(bitmap[0]);
//            ((ImageView) v2.findViewById(R.id.pg_im2)).setImageBitmap(bitmap[1]);
//            ((ImageView) v3.findViewById(R.id.pg_im3)).setImageBitmap(bitmap[2]);
//            ((ImageView) v4.findViewById(R.id.pg_im4)).setImageBitmap(bitmap[3]);
//            ((ImageView) v5.findViewById(R.id.pg_im5)).setImageBitmap(bitmap[4]);
//
//            swp.setRefreshing(false);
//        }
//
//    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:     // 0是网络超时发过来的msg
                    Toast.makeText(A, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    swp.setRefreshing(false);
                    break;
                case 1:     // 1是无网络发过来的msg
                    Toast.makeText(A, "请连接网络", Toast.LENGTH_SHORT).show();
                    swp.setRefreshing(false);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public int requestLogic() {
        String netVersion=null;
        Boolean bl=isOnline(A);
        //是否有网络连接
        if (bl) {  //如果有

            // 获取SharedPreferences中的OpenJCU文件的Editor
            SharedPreferences preferences = A.getSharedPreferences("OpenJCU", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            // 看localVersion字段中是否有值、有什么值    //读取fragment_home_ver字段，如果没有这个字段，创建，并给这个字段赋值0.00
            String localVersion = preferences.getString("fragment_home_ver", "0.00");

            // 如果没有这个字段的值
            if (localVersion.equals("0.00")) {  //请求版本，更新本地版本，请求资源，更新呈现资源
                netVersion=requestVersion();  if(netVersion=="exp"){   /* bl=false; requestLogic();*/ return 0;}
                editor.putString("fragment_home_ver",netVersion);  editor.apply();//更新本地版本；
                requestResourceAndUpdateUI(0);
                //requestVertion\如果没有这个字段的值，直接请求网络的数据，并写入值的信息作为本地版本号
            } else {  //1请求版本，比较版本信息，2请求版本，比较版本信息，更新本地版本，请求资源，更新呈现资源
                //如果有这个字段的值，请求网络，比较这个值和网络请求下来的版本信息
                netVersion=requestVersion();
                if(netVersion=="exp"){   /* bl=false; requestLogic();*/ return 0;}
                editor.putString("fragment_home_ver",netVersion);  editor.apply();//更新本地版本；
                Log.e("OpenJCU","gg99999999999999999999999999999"+netVersion);
                if(netVersion.equals(localVersion)){
                    requestResourceAndUpdateUI(1);
                } else {
                    editor.putString("fragment_home_ver",netVersion);  editor.apply();  //更新本地版本；
                    requestResourceAndUpdateUI(0);
                }

            }
        } else {        //如果没有
            Message msg=new Message(); msg.what=1;
            mHandler.sendMessage(msg);
            requestResourceAndUpdateUI(1);
        }
        return 1;
    }

    public String requestVersion() {   //请求异常时，返回 "exp"
        String s1=null;
        Request request = new Request.Builder()
                //强制使用网络
                .cacheControl(CacheControl.FORCE_NETWORK)
                //强制使用缓存
                // .cacheControl(CacheControl.FORCE_CACHE)
                .url("http://192.168.155.1/www/OpenJCU/update.json").build();
        try {
            Response responseVersion=client.newCall(request).execute();
            String data=responseVersion.body().string();
            ParseJsonForVersion pj=new ParseJsonForVersion();
            s1=pj.parseJSON(data);
        } catch (IOException e) {
            //   e.printStackTrace();
            Log.e("OpenJCU","超时ccccccccccccccccc");
            return "exp";
        }

        while (s1==null) {   //接口的回调为异步，在接口回调中操作的变量时间不够可能没来得及赋值
        }
        return s1;
    }



    OkHttpClient client;
    Request request[]=new Request[6];
    Request request1[]=new Request[6];   int flag=1;
    String addr = "http://192.168.155.1/www/OpenJCU/";
    String addr1 = "http://192.168.155.1/www/OpenJCU/fragment_viewpager_string.json";
    Bitmap bitmap[]=new Bitmap[5];
    String pagerText[]=new String[5];
    String pagerUrl[]=new String[5];
    Response responseResource;

    //缓存路径、缓存区的大小(没网时读缓存)
    public void requestResourceAndUpdateUI( int formWhere){
        if (formWhere == 0) {
            for (int i=0;i<=5;i+=1) {  //六个链接，第一个为json，其它为image
                try {
                    responseResource = client.newCall(request[i]).execute();

                    if (i == 0) {
                        String data=responseResource.body().string();
                        String[] resouce=(new ParseJsonForResource()).parseStringJSON(data);
                        for(int j=0;j<10;j++){
                            if(j<5)  pagerText[j] = resouce[j];
                            else {
                                pagerUrl[j - 5] = resouce[j];
                            }
                        }
                    } else {
                        final byte[] Picture_bt1 = responseResource.body().bytes();
                        bitmap[i-1] = BitmapFactory.decodeByteArray(Picture_bt1, 0, Picture_bt1.length);
                    }
                    flag=1;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            A.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) v1.findViewById(R.id.pg_im1)).setImageBitmap(bitmap[0]);
                    ((ImageView) v2.findViewById(R.id.pg_im2)).setImageBitmap(bitmap[1]);
                    ((ImageView) v3.findViewById(R.id.pg_im3)).setImageBitmap(bitmap[2]);
                    ((ImageView) v4.findViewById(R.id.pg_im4)).setImageBitmap(bitmap[3]);
                    ((ImageView) v5.findViewById(R.id.pg_im5)).setImageBitmap(bitmap[4]);

                    swp.setRefreshing(false);
                }
            });


        } else {
            for (int i=0;i<=5;i+=1) {
                try {
                    responseResource = client.newCall(request1[i]).execute();

                    if (i == 0) {
                        String data=responseResource.body().string();
                        String[] resouce=(new ParseJsonForResource()).parseStringJSON(data);
                        for(int j=0;j<10;j++){
                            if(j<5)  pagerText[j] = resouce[j];
                            else {
                                pagerUrl[j - 5] = resouce[j];
                            }
                        }
                    } else {
                        final byte[] Picture_bt1 = responseResource.body().bytes();
                        bitmap[i-1] = BitmapFactory.decodeByteArray(Picture_bt1, 0, Picture_bt1.length);
                    }
                    flag=1;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            A.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ImageView) v1.findViewById(R.id.pg_im1)).setImageBitmap(bitmap[0]);
                    ((ImageView) v2.findViewById(R.id.pg_im2)).setImageBitmap(bitmap[1]);
                    ((ImageView) v3.findViewById(R.id.pg_im3)).setImageBitmap(bitmap[2]);
                    ((ImageView) v4.findViewById(R.id.pg_im4)).setImageBitmap(bitmap[3]);
                    ((ImageView) v5.findViewById(R.id.pg_im5)).setImageBitmap(bitmap[4]);

                    swp.setRefreshing(false);
                }
            });
        }
    }



    /*****************************************************************************************/

    /*******************************************************************************************************************/

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageSelected(int position) {
        currentItem = position;
        changeDotsBg(currentItem % viewList.size());
        }

        // 其中arg0这个参数
        // 有三种状态（0，1，2）。
        // arg0 == 1的时辰默示正在滑动，
        // arg0 == 2的时辰默示滑动完毕了，
        // arg0 == 0的时辰默示什么都没做。
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 0) { nowAction = false; }
            if (arg0 == 1) { nowAction = true; }
            if (arg0 == 2) { }
            }
        public void onPageScrolled(int arg0, float arg1, int arg2) { }
    }

    private void changeDotsBg(int currentitem) {
        for(int i = 0; i < dots.size();i++){
            dots.get(i).setBackgroundResource(R.drawable.dot_normal);
        }
        dots.get(currentitem).setBackgroundResource(R.drawable.dot_focused);
        pageText.setText(pagerText[currentitem]);
    }

    public class OnPageClickListener implements View.OnClickListener {
        Intent in = new Intent(getActivity(), Tab1Activity.class);
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pg1:  getActivity().startActivity(in);  break;
                case R.id.pg2:  getActivity().startActivity(in);  break;
                case R.id.pg3:  getActivity().startActivity(in);  break;
                case R.id.pg4:  getActivity().startActivity(in);  break;
                case R.id.pg5:  getActivity().startActivity(in);  break;
                default:  break;
            }
        }
    }


    //ViewPagerAdapter 构造函数参数viewlist
    public class MyViewPagerAdapter extends PagerAdapter{
        //不循环的Adapter

        //        private List<View> mListViews;
//
//        public MyViewPagerAdapter(List<View> mListViews) {
//            this.mListViews = mListViews;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) 	{
//            container.removeView(mListViews.get(position));
//        }
//
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(mListViews.get(position), 0);
//            return mListViews.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return  mListViews.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0==arg1;
//        }
        //ViewPager实现循环播放的Adapter
        private List<View> mListViews;

        public MyViewPagerAdapter(ArrayList<View> viewlist) { this.mListViews = viewlist; }
        @Override
        public int getCount() {//设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Warning：不要在这里调用removeView
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项View view = null;
            View view=null;
            if (position % mListViews.size() < 0) {
                view = (View) mListViews.get(mListViews.size() + position);
            } else {
                view = (View) mListViews.get(position % mListViews.size());
            }
//            position %= mListViews.size();
//            if (position<0){
//                position = mListViews.size()+position;
//            }
            //View view = mListViews.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp =view.getParent();
            if (vp!=null){
                ViewGroup parent = (ViewGroup)vp;
                parent.removeView(view);
            }
            container.addView(view);
            //add listeners here if necessary
            return view;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) { return arg0==arg1; }
    }



    public class OnFragViewClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.map_test:
                    A.setTabSelection(1); break;
                case R.id.map_test1:
                    Intent in = new Intent(A, BNDemoMainActivity.class); A.startActivity(in); break;
                case R.id.map_test2:
                    Intent in9 = new Intent(A, RoutePlanActivity.class); A.startActivity(in9); break;
                case R.id.map_test3:
                    Intent in11 = new Intent(A, PositionGeoActivity.class); A.startActivity(in11); break;

                case R.id.go:
                    Intent in1 = new Intent(A, GoMainActivity.class); A.startActivity(in1); break;
                case R.id.info_search:
                    Intent in2 = new Intent(A, InfoSearchMainAct.class); A.startActivity(in2); break;
                case R.id.conmmunication:
                    Intent in3 = new Intent(A, CommunicationMainAct.class); A.startActivity(in3); break;
                case R.id.recruit:
                    Intent in4 = new Intent(A, RecruitMainAct.class); A.startActivity(in4); break;
                case R.id.abroad:
                    Intent in5 = new Intent(A, AbroadMainAct.class); A.startActivity(in5); break;
                case R.id.postgraduate:
                    Intent in6 = new Intent(A, PostgraduateMainAct.class); A.startActivity(in6); break;
                case R.id.playground:
                    Intent in7 = new Intent(A, PlaygroundMainAct.class); A.startActivity(in7); break;
                case R.id.position:
                    Intent in10 = new Intent(A, PositionMainAct.class); A.startActivity(in10); break;
                case R.id.l_f:
                    Intent in8 = new Intent(A, LostfoundMainAct.class); A.startActivity(in8); break;
                default: break;
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //   b =(Button) view.findViewById(R.id.v1);

        return view;
    }























    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
//                TimeUnit.SECONDS);
//    }
//    @Override
//    public void onStop() {
//        scheduledExecutorService.shutdown();
//        super.onStop();
//    }
//    private class ScrollTask implements Runnable {  //定时轮询
//
//        public void run() {
//            synchronized (viewPager) {
//                if (!nowAction) {
//                    currentItem = currentItem+1;
//                    handler.obtainMessage().sendToTarget();
//                }
//            }
//        }
//    }


    //定时器相关   onStart()   onStop()  ScrollTask
    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
                TimeUnit.SECONDS);
        super.onStart();
    }

    @Override
    public void onStop() {
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                if (!nowAction) {
                    currentItem = currentItem+1;
                    handler.obtainMessage().sendToTarget();
                }
            }
        }
    }
}
