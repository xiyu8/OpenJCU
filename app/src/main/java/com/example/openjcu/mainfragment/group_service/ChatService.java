package com.example.openjcu.mainfragment.group_service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.example.openjcu.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatService extends Service {
    String positionJsonData;
    public MyBinder myBinder=new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开始执行后台任务
                beep();
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                int anHour = 10 * 1000; // 这是10 秒的毫秒数
                long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
                Intent i = new Intent(ChatService.this, BeepService.class);
                PendingIntent pi = PendingIntent.getService(ChatService.this, 0, i, 0);
                manager.cancel(pi);
                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
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
    }
    String groupName;String IMME;String position;

    public void beep(){
        while (groupName==null || IMME==null || position==null){ }
        Log.e("OpenJCU","上传和下载位置信息"+groupName+"___"+IMME+"__"+position);

        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 1 * 1024 * 1024)).build();
        Request request = new Request.Builder()
                //强制使用网络
                .cacheControl(CacheControl.FORCE_NETWORK)
                //强制使用缓存
                // .cacheControl(CacheControl.FORCE_CACHE)
                .url(app_url+"group/beep.php?groupName="+groupName+"&IMME="+IMME+"&position="+position).build();
        try {
            Response responseVersion=client.newCall(request).execute();
            String data=responseVersion.body().string();
            positionJsonData=data;
        } catch (IOException e) {
            //   e.printStackTrace();
            Log.e("OpenJCU","超时ccccccccccccccccc");
        }

    }

    String app_url;
    @Override
    public void onCreate() {
        super.onCreate();
        app_url = getResources().getString(R.string.app_url);

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
