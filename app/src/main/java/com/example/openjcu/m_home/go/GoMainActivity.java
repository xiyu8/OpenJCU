package com.example.openjcu.m_home.go;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.example.openjcu.R;
import com.example.openjcu.m_home.Tab1Activity;
import com.example.openjcu.m_home.go.dinner.FoodDetailAct;
import com.example.openjcu.m_home.home_map.HomeMapActivity;
import com.example.openjcu.mainfragment.homefragment_toolclass.ParseJsonForResource;
import com.example.openjcu.mainfragment.homefragment_toolclass.ParseJsonForVersion;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
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

import static com.example.openjcu.tool.NetRequest.isOnline;

public class GoMainActivity extends AppCompatActivity {

    /****************************************ViewPager 相关**********************************************/  //
    public ViewPager viewPager;  //viewpager
    View v1,v2,v3,v4,v5; //Pager显示的view
    ArrayList<View> viewList; //装每个viewlayout的容器
    private PagerTabStrip pagerTabStrip; //pager头
    private List<View> dots;
    TextView tx;  String[] food={"推荐菜品：鱼香肉丝","推荐菜品：回锅肉","推荐菜品：宫保鸡丁","推荐菜品：回锅肉","推荐菜品：宫保鸡丁"};
    MyViewPagerAdapter pagerAdapter;
    private ScheduledExecutorService scheduledExecutorService;  //定时器
    private int currentItem = 5000;
    boolean nowAction = false;// 当前用户正在滑动视图
    private Handler handler = new Handler() {      //定时器用于更新dots的handler
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);
        };
    };
   /*************************************************************************************************/












    /****************************************ViewPager 相关**********************************************/
    public void init() {

    /*****************************ViewPager相关init************************************************* *//*  */
        //ViewPager Titil相关
        pagerTabStrip=(PagerTabStrip)findViewById(R.id.pagertab);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextSpacing(50);
        pagerTabStrip.setVisibility(View.VISIBLE);
        //为ViewPager设置View
        viewList = new ArrayList<View>();// 将要分页显示的layoutView装入数组中
        LayoutInflater inflater=getLayoutInflater();  //找到要在viewpager中显示的layout
        v1 = inflater.inflate(R.layout.go_pager1, null);
        v2 = inflater.inflate(R.layout.go_pager2,null);
        v3 = inflater.inflate(R.layout.go_pager3, null);
        v4 = inflater.inflate(R.layout.go_pager4, null);
        v5 = inflater.inflate(R.layout.go_pager5, null);
        View.OnClickListener onPageListener = new OnPageClickListener();
        viewList.add(v1); v1.setOnClickListener(onPageListener);
        viewList.add(v2); v2.setOnClickListener(onPageListener);
        viewList.add(v3); v3.setOnClickListener(onPageListener);
        viewList.add(v4); v4.setOnClickListener(onPageListener);
        viewList.add(v5); v5.setOnClickListener(onPageListener);
        //所有指示点
        dots = new ArrayList<View>();
        tx = (TextView) findViewById(R.id.pageText);
        dots.add(findViewById(R.id.v_dot0));
        dots.add(findViewById(R.id.v_dot1));
        dots.add(findViewById(R.id.v_dot2));
        dots.add(findViewById(R.id.v_dot3));
        dots.add(findViewById(R.id.v_dot4));
        //为pager设置adapter和listener
        viewPager = (ViewPager)findViewById(R.id.viewpager); //找到viewpager
        pagerAdapter=new MyViewPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setCurrentItem(Integer.MAX_VALUE / 4);
    /**********************************************************************************************************/


        positions[0] = new LatLng(30.73186, 103.957486);
        positions[1]=new LatLng(30.730815,103.955334);
        positions[2]=new LatLng(30.73386,103.9565);
        positions[3]=new LatLng(30.728035,103.954518);



    //swipe reflash 准备client和request
        client=new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();

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
        bl=isOnline(GoMainActivity.this);

    }

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
    private void changeDotsBg(int currentitem){
        for(int i = 0; i < dots.size();i++){
            dots.get(i).setBackgroundResource(R.drawable.dot_normal);
        }
        dots.get(currentitem).setBackgroundResource(R.drawable.dot_focused);
        tx.setText(food[currentitem]);
    }

    public class OnPageClickListener implements View.OnClickListener {
        Intent in = new Intent(GoMainActivity.this, Tab1Activity.class);
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pg1:  startActivity(in);  break;
                case R.id.pg2:  startActivity(in);  break;
                case R.id.pg3:  startActivity(in);  break;
                case R.id.pg4:  startActivity(in);  break;
                case R.id.pg5:  startActivity(in);  break;
                default:  break;
            }
        }
    }
    //ViewPagerAdapter 构造函数参数viewlist
    public class MyViewPagerAdapter extends PagerAdapter {
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

        public MyViewPagerAdapter(ArrayList<View> viewList) { this.mListViews = viewList; }
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_main);
        init();   aboutSwipeReflash();   initExpandableListView();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View v) {

    }
    /****************************************************************************************************/








    /***************************ExpandableList********************************************************/
    public String[] groupStrings = {"一、二、三食堂", "四、五食堂", "六食堂", "杏岛餐厅"};
    public String[][] childStrings = {{"鱼香肉丝", "回锅肉", "菜品", "菜品"}, {"菜品", "菜品", "菜品", "菜品"}, {"菜品", "菜品", "菜品", "菜品", "菜品"}, {"菜品", "菜品", "菜品", "菜品"}};
    public ExpandableListView expandableListView;

    public void initExpandableListView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expand_list);
        expandableListView.setAdapter(new MyExpandableListAdapter());

        //setOnChildClickListener  setOnGroupClickListener ◦setOnGroupCollapseListener ◦setOnGroupExpandListener

        //        设置分组项的点击监听事件
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), groupStrings[i], Toast.LENGTH_SHORT).show();
                return false;
            }});

//        设置子选项点击监听事件
         expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    Toast.makeText(getApplicationContext(), childStrings[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(GoMainActivity.this,FoodDetailAct.class);
                    in.putExtra("index",""+groupPosition);
                    startActivity(in);
                    return true;}
            });
    }

    public LatLng positions[]=new LatLng[4];
    public class MyExpandableListAdapter implements ExpandableListAdapter {



        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }


        //        获取分组的个数
        @Override
        public int getGroupCount() {
            return groupStrings.length;
        }

        //        获取指定分组中的子选项的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childStrings[groupPosition].length;
        }

        //        获取指定的分组数据
        @Override
        public Object getGroup(int groupPosition) {
            return groupStrings[groupPosition];
        }

        //        获取指定分组中的指定子选项数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childStrings[groupPosition][childPosition];
        }

        //        获取指定分组的ID, 这个ID必须是唯一的
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //        获取子选项的ID, 这个ID必须是唯一的
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
        @Override
        public boolean hasStableIds() {
            return true;
        }
        //        获取显示指定分组的视图
        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder groupViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(GoMainActivity.this).inflate(R.layout.expandablelist_titleview, parent, false);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
                groupViewHolder.target = (ImageView) convertView.findViewById(R.id.target);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }
            groupViewHolder.tvTitle.setText(groupStrings[groupPosition]);
            groupViewHolder.target.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(GoMainActivity.this, HomeMapActivity.class);
                    in.putExtra("lat",positions[groupPosition].latitude);
                    in.putExtra("lng",positions[groupPosition].longitude);
                    startActivity(in);
                }
            });
            return convertView;
        }

        //        获取显示指定分组中的指定子选项的视图
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(GoMainActivity.this).inflate(R.layout.expandablelist_childview, parent, false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.tvTitle.setText(childStrings[groupPosition][childPosition]);
            return convertView;
        }

        //        指定位置上的子元素是否可选中
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
        class GroupViewHolder {
            TextView tvTitle;
            ImageView target;
        }
       class ChildViewHolder {
            TextView tvTitle;
        }
    }

    /****************************************************************************************************/







    /***************************************下拉刷新************************************************************/
    public SwipeRefreshLayout swp;
    public void aboutSwipeReflash() {
        swp = (SwipeRefreshLayout) findViewById(R.id.swp_reflash);
      //  swp.setColorSchemeColors(Color.argb(1,200,1,1));
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                ref();
            }
        });
    }
    public void ref(){
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


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:     // 0是网络超时发过来的msg
                       Toast.makeText(GoMainActivity.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    swp.setRefreshing(false);
                    break;
                case 1:     // 1是无网络发过来的msg
                       Toast.makeText(GoMainActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                    swp.setRefreshing(false);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 请求逻辑：什么时候，该发出什么样的请求
     * 1、判断是否有网络连接
     * 2.1、有网络，读取本地资源的版本信息。如果没有本地资源的版本信息
     * 2.2、无网络，
     *
     */

    Boolean bl;
    public int requestLogic() {
        String netVersion=null;
        Boolean bl=isOnline(GoMainActivity.this);
        //是否有网络连接
        if (bl) {  //如果有

            // 获取SharedPreferences中的OpenJCU文件的Editor
            SharedPreferences preferences = getSharedPreferences("OpenJCU", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            // 看localVersion字段中是否有值、有什么值    //读取fragment_home_ver字段，如果没有这个字段，创建，并给这个字段赋值0.00
            String localVersion = preferences.getString("go_pager_ver", "0.00");

            // 如果没有这个字段的值
            if (localVersion.equals("0.00")) {  //请求版本，更新本地版本，请求资源，更新呈现资源
                netVersion=requestVersion();  if(netVersion=="exp"){   /* bl=false; requestLogic();*/ return 0;}
                editor.putString("go_pager_ver",netVersion);  editor.apply();//更新本地版本；
                requestResourceAndUpdateUI(0);
                //requestVertion\如果没有这个字段的值，直接请求网络的数据，并写入值的信息作为本地版本号
            } else {  //1请求版本，比较版本信息，2请求版本，比较版本信息，更新本地版本，请求资源，更新呈现资源
                //如果有这个字段的值，请求网络，比较这个值和网络请求下来的版本信息
                netVersion=requestVersion();
                if(netVersion=="exp"){   /* bl=false; requestLogic();*/ return 0;}
                editor.putString("go_pager_ver",netVersion);  editor.apply();//更新本地版本；
                Log.e("OpenJCU","gg99999999999999999999999999999"+netVersion);
                if(netVersion.equals(localVersion)){
                    requestResourceAndUpdateUI(1);
                } else {
                    editor.putString("go_pager_ver",netVersion);  editor.apply();  //更新本地版本；
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



   // String s1;  //用来存解析vresion_Json的结果
    //request resource's version
   // Response responseVersion;
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
            s1=pj.parseGoPagerVersionJSON(data);
        } catch (IOException e) {
         //   e.printStackTrace();
            Log.e("OpenJCU","超时ccccccccccccccccc");
            return "exp";
        }

        while (s1==null) {   //接口的回调为异步，在接口回调中操作的变量时间不够可能没来得及赋值
        }
        return s1;
    }


    String[] go_pager_url=new String[5];
    OkHttpClient client;
    Request request[]=new Request[6];
    Request request1[]=new Request[6];   int flag=1;
    String addr = "http://192.168.155.1/www/OpenJCU/go/";
    String addr1 = "http://192.168.155.1/www/OpenJCU/go/go_view_pager_string.json";
    Bitmap bitmap[]=new Bitmap[5];

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
                            if(j<5)  food[j] = resouce[j];
                            else {
                                go_pager_url[j - 5] = resouce[j];
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
                runOnUiThread(new Runnable() {
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
                            if(j<5)  food[j] = resouce[j];
                            else {
                                go_pager_url[j - 5] = resouce[j];
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

            runOnUiThread(new Runnable() {
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



//    public int requestLogic() {
//        String versionResult=null;
//        //是否有网络连接
//        if (isOnline(GoMainActivity.this)) {  //如果有
//
//            // 获取SharedPreferences中的OpenJCU文件的Editor
//            SharedPreferences preferences = getSharedPreferences("OpenJCU", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//
//            // 看go_pager_ver字段中是否有值、有什么值    //读取fragment_home_ver字段，如果没有这个字段，创建，并给这个字段赋值0.00
//            String go_pager_ver = preferences.getString("go_pager_ver", "0.00");
//
//            // 如果没有这个字段的值
//            if (go_pager_ver.equals("0.00")) {  //请求版本，更新本地版本，请求资源，更新呈现资源
//                versionResult=requestVersion();  if(versionResult=="exp"){  return 0;}
//                editor.putString("go_pager_ver",s1);  editor.apply();//更新本地版本；
//                requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/go/","http://192.168.155.1/www/OpenJCU/go/go_view_pager_string.json",0);
//                //requestVertion\如果没有这个字段的值，直接请求网络的数据，并写入值的信息作为本地版本号
//            } else {  //1请求版本，比较版本信息，2请求版本，比较版本信息，更新本地版本，请求资源，更新呈现资源
//                //如果有这个字段的值，请求网络，比较这个值和网络请求下来的版本信息
//                versionResult=requestVersion();
//                if(versionResult=="exp"){  return 0;}
//                Log.e("OpenJCU","gg99999999999999999999999999999"+versionResult);
//                if(versionResult.equals(go_pager_ver)){
//                    requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/go/","http://192.168.155.1/www/OpenJCU/go/go_view_pager_string.json",1);
//                } else {
//                    editor.putString("go_pager_ver",versionResult);  editor.apply();  //更新本地版本；
//                    requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/go/","http://192.168.155.1/www/OpenJCU/go/go_view_pager_string.json",0);
//                }
//
//            }
//        } else {        //如果没有
//            Message msg=new Message(); msg.what=1;
//            mHandler.sendMessage(msg);
//            requestResourceAndUpdateUI("http://192.168.155.1/www/OpenJCU/go/","http://192.168.155.1/www/OpenJCU/go/go_view_pager_string.json",1);
//        }
//        return 1;
//    }
//
//
//
////    String s1;  //用来存解析vresion_Json的结果
////    //request resource's version
////    Response responseVersion;
////    public String requestVersion() {
////        s1=null;
////        Request request = new Request.Builder()
////                //强制使用网络
////                .cacheControl(CacheControl.FORCE_NETWORK)
////                //强制使用缓存
////                // .cacheControl(CacheControl.FORCE_CACHE)
////                .url("http://192.168.155.1/www/OpenJCU/update.json").build();
////        try {
////            responseVersion=client.newCall(request).execute();
////            String data=responseVersion.body().string();
////            ParseJsonForVersion pj=new ParseJsonForVersion();
////            s1=pj.parseGoPagerVersionJSON(data);
////        } catch (IOException e) {
////            //   e.printStackTrace();
////            Log.e("OpenJCU","超时ccccccccccccccccc");
////            return "exp";
////        }
////
////        while (s1==null) {   //接口的回调为异步，在接口回调中操作的变量时间不够可能没来得及赋值
////        }
////        return s1;
////    }
////
////
////    String[] go_pager_url=new String[5];
////    OkHttpClient client;
////    Request request[]=new Request[6];
////    Request request1[]=new Request[6];   int flag=1;
////    String addr = "http://192.168.155.1/www/OpenJCU/go/";
////    String addr1 = "http://192.168.155.1/www/OpenJCU/go/go_view_pager_string.json";
////    Bitmap bitmap[]=new Bitmap[5];
////
////    Response responseResource;
////
////    //缓存路径、缓存区的大小(没网时读缓存)
////    public void requestResourceAndUpdateUI(String addr,String addr1, int formWhere){
////        if (formWhere == 0) {
////            for (int i=0;i<=5;i+=1) {  //六个链接，第一个为json，其它为image
////                try {
////                    responseResource = client.newCall(request[i]).execute();
////
////                    if (i == 0) {
////                        String data=responseResource.body().string();
////                        String[] resouce=(new ParseJsonForResource()).parseStringJSON(data);
////                        for(int j=0;j<10;j++){
////                            if(j<5)  food[j] = resouce[j];
////                            else {
////                                go_pager_url[j - 5] = resouce[j];
////                            }
////                        }
////                    } else {
////                        final byte[] Picture_bt1 = responseResource.body().bytes();
////                        bitmap[i-1] = BitmapFactory.decodeByteArray(Picture_bt1, 0, Picture_bt1.length);
////                    }
////                    flag=1;
////
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////            runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    ((ImageView) v1.findViewById(R.id.pg_im1)).setImageBitmap(bitmap[0]);
////                    ((ImageView) v2.findViewById(R.id.pg_im2)).setImageBitmap(bitmap[1]);
////                    ((ImageView) v3.findViewById(R.id.pg_im3)).setImageBitmap(bitmap[2]);
////                    ((ImageView) v4.findViewById(R.id.pg_im4)).setImageBitmap(bitmap[3]);
////                    ((ImageView) v5.findViewById(R.id.pg_im5)).setImageBitmap(bitmap[4]);
////
////                    swp.setRefreshing(false);
////                }
////            });
////
////
////        } else {
////            for (int i=0;i<=5;i+=1) {
////                try {
////                    responseResource = client.newCall(request1[i]).execute();
////
////                    if (i == 0) {
////                        String data=responseResource.body().string();
////                        String[] resouce=(new ParseJsonForResource()).parseStringJSON(data);
////                        for(int j=0;j<10;j++){
////                            if(j<5)  food[j] = resouce[j];
////                            else {
////                                go_pager_url[j - 5] = resouce[j];
////                            }
////                        }
////                    } else {
////                        final byte[] Picture_bt1 = responseResource.body().bytes();
////                        bitmap[i-1] = BitmapFactory.decodeByteArray(Picture_bt1, 0, Picture_bt1.length);
////                    }
////                    flag=1;
////
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////
////            runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    ((ImageView) v1.findViewById(R.id.pg_im1)).setImageBitmap(bitmap[0]);
////                    ((ImageView) v2.findViewById(R.id.pg_im2)).setImageBitmap(bitmap[1]);
////                    ((ImageView) v3.findViewById(R.id.pg_im3)).setImageBitmap(bitmap[2]);
////                    ((ImageView) v4.findViewById(R.id.pg_im4)).setImageBitmap(bitmap[3]);
////                    ((ImageView) v5.findViewById(R.id.pg_im5)).setImageBitmap(bitmap[4]);
////
////                    swp.setRefreshing(false);
////                }
////            });
////        }
////    }



    /*****************************************************************************************/
    /***********************************************************************************************************/


    /***********************************************************************************************************/
    //添加toolbar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //menu点击事件的处理
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_menu1:
                Toast.makeText(this, "sdvgwsv", Toast.LENGTH_SHORT).show(); break;
            case R.id.home:  finish(); break;
            default: break;
        }
        finish();
        return true;
    }







    /***********************************************************************************************************/








    //定时器相关   onStart()   onStop()  ScrollTask
    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new GoMainActivity.ScrollTask(), 1, 3,
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
