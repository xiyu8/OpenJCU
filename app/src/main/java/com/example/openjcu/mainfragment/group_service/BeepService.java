package com.example.openjcu.mainfragment.group_service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.openjcu.MainActivity;
import com.example.openjcu.R;
import com.example.openjcu.mainfragment.gson_bean.ChatBean;
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

import static com.example.openjcu.tool.NetRequest.isApplicationBackground;
import static com.example.openjcu.tool.NetRequest.isSleeping;

public class BeepService extends Service {
    String positionJsonData;

    List<ChatBean> newChatItems = new ArrayList<>();
    public MyBinder myBinder=new MyBinder();
    Boolean first = true;

    String sss=null;
    @Override
    public IBinder onBind(Intent intent) {
        sss = "dvd";
        return myBinder;
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////        updateWeather();
////        updateBingPic();
//
//        return super.onStartCommand(intent, flags, startId);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (myThread == null) {
//            myThread = new MyThread();
//        }
//        myThread.start();
        if(sss==null){  //本service是否和binder绑定
            stopSelf();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {// 开始执行后台任务
                    beep();
                }
            }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE); //倒计时
        int fourSecond = 4* 1000; // 这是10 秒的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + fourSecond;
        Intent in = new Intent(BeepService.this, BeepService.class);
        in.putExtra("lauchFromNoti", "yes");
        in.putExtra("groupName", groupName);
        in.putExtra("IMME", IMME);
        PendingIntent pi = PendingIntent.getService(BeepService.this, 0, in, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi); //1、把service放入intent  2、把intent放入PendingIntent  3、pendingIntent放入AlarmManager
        }

        return super.onStartCommand(intent, flags, startId);
    }

    MyThread myThread;
    public class MyThread implements Runnable {
        @Override
        public void run() {
            // 开始执行后台任务
            beep();
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int anHour = 4* 1000; // 这是10 秒的毫秒数
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
            Intent i = new Intent(BeepService.this, BeepService.class);
            i.putExtra("lauchFromNoti", "yes");
            i.putExtra("groupName", groupName);
            i.putExtra("IMME", IMME);
            PendingIntent pi = PendingIntent.getService(BeepService.this, 0, i, 0);
            manager.cancel(pi);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
    }

    public class MyBinder extends Binder {

        public void startDownload() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 执行具体的下载任务
                }
            }).start();
        }

        public String getPositionString(){
            while (positionJsonData==null){ }
            return positionJsonData;
        }
        public void setBeepInfo(String groupNames,String IMMEs,String positions){
            groupName=groupNames; IMME=IMMEs; position=positions;
        }

        public List<ChatBean> getNewChatItems() {
            return newChatItems;
        }
    }
    String groupName;String IMME;String position;

    public void beep(){
        while (groupName==null || IMME==null || position==null){ }
        Log.e("OpenJCU","上传和下载位置信息"+groupName+"___"+IMME+"__"+position);
        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 1 * 1024 * 1024)).build();
        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK)
                .url(app_url+"group/beep.php?groupName="+groupName+"&IMME="+IMME+"&position="+position).build();
        Request request1 = null;
        if(first){
                request1 = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"group/chat_item_all.php?groupId="+groupName).build();
        }else {
                request1 = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url + "group/chat_item_obtain.php?groupId=" + groupName + "&chatItemDate=" + getLatestItemTime()).build();
        }
        try {
            Response responseVersion=client.newCall(request).execute();
            String data=responseVersion.body().string();
            positionJsonData=data;
        } catch (IOException e) {
            //   e.printStackTrace();
            Log.e("OpenJCU","超时ccccccccccccccccc");
        }
        try {
            Response responseVersion=client.newCall(request1).execute(); first=false;
            String data=responseVersion.body().string();
            parseChatItemsJson(data);
        } catch (IOException e) {
            //   e.printStackTrace();
            Log.e("OpenJCU","超时ccccccccccccccccc");
        }

    }
    public String getLatestItemTime(){
        String time="2017-04-11 20:20:20";
        String tmp;
        int n=newChatItems.size();
        if(n!=0) {
            for (int i = 0; i < n; i++) {
                tmp = ((newChatItems.get(i)).getChatItemDate());
                if (tmp.compareTo(time) > 0)
                    time = tmp;
            }
            return time;
        }else
            return latstChatItem;
    }
    String latstChatItem="2017-04-11 20:20:20";
    public void saveLatestItemTime(){
        String time="2017-04-11 20:20:20";
        String tmp;

        int n=newChatItems.size();
        if(n!=0) {
            for (int i = 0; i < n; i++) {
                tmp = ((newChatItems.get(i)).getChatItemDate());
                if (tmp.compareTo(time) > 0)
                    time = tmp;
            }
            latstChatItem=time;
        }
    }

    public void parseChatItemsJson(String data) {
        Gson gson = new Gson();
        //ThemeResource[] themeResourcesArray = gson.fromJson(data, ThemeResource[].class);
        newChatItems = gson.fromJson(data, new TypeToken<List<ChatBean>>() {}.getType());


        if (newChatItems.size() != 0) {
            saveLatestItemTime();
            /////////////////////////////////////////////////
            if (isSleeping(BeepService.this) || isApplicationBackground(BeepService.this)) {
                Intent intent = new Intent(this, MainActivity.class);
               // PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
                PendingIntent pi = PendingIntent.getActivity(BeepService.this, (int) (Math.random() * 1000) + 1,intent, 0);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle(newChatItems.get(0).getMemberId())
                        .setContentText(newChatItems.get(0).getChatContent())
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pi)
                        //        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                               .setVibrate(new long[]{0, 1000, 1000, 1000})
                        //        .setLights(Color.GREEN, 1000, 1000)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        //        .setStyle(new NotificationCompat.BigTextStyle().bigText("Learn how to build notifications, send and sync data, and use voice actions. Get the official Android IDE and developer tools to build apps for Android."))
                       // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.bgi1)))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .build();
                manager.notify(1, notification);
            }
        }
    }

    String app_url;
    @Override
    public void onCreate() {
        super.onCreate();
        app_url = getResources().getString(R.string.app_url);
        local();
    }

    ////////////////////////////////////////////////////////////////////////////////
    public LatLng ll=null;
//    public BaiduMap mBaidumap;
//    public MapView mMapView;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    boolean isFirstLoc = true; // 是否首次定位

    LocationClientOption option;
    public void local() {

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        // 开启定位图层
      //  mBaidumap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(BeepService.this);
        mLocClient.registerLocationListener(myListener);
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }
    @Override
    public void onDestroy() {
        Log.e("OpenJCU", "STOPSTOPSTOPSTOPSTOPSTOPSTOP");

        sss = null;
        mLocClient.unRegisterLocationListener(myListener);
        mLocClient.stop();
        super.onDestroy();
    }
    /**
     * 定位SDK监听的回调
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //////////////////////////////////////////////////////
            // map view 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null) {
//                return;
//            }
            Log.e("OpenJCU","获取本地位置信息"+location.getLatitude()+"__"+location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
//            mBaidumap.setMyLocationData(locData);
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                ll = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(19f);
//                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//            }
                ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
            position=""+location.getLatitude()+","+location.getLongitude();

            ////////////////////////////////////////////////////////////

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            Log.i("BaiduLocationApiDem", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
