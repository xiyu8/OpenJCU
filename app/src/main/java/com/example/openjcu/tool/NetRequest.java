package com.example.openjcu.tool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 晞余 on 2017/3/9.
 */

public class NetRequest {
//    public static void sendRequest(String urlString, okhttp3.Callback callback, Activity A) {
//        OkHttpClient client= new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).cache(new Cache(new File(A.getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
//        //缓存路径、缓存区的大小(没网时读缓存)
//
//
//        Request request = new Request.Builder()
//                    //强制使用网络
//                       .cacheControl(CacheControl.FORCE_NETWORK)
//                       //强制使用缓存
//                      // .cacheControl(CacheControl.FORCE_CACHE)
//                       .url(urlString).build();
//
// //       Request request = new Request.Builder().url(urlString).build();
//        client.newCall(request).enqueue(callback);
//    }
//
//    public static void sendForceCacheRquest(String urlString, okhttp3.Callback callback, Activity A) {
//        OkHttpClient client= new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).cache(new Cache(new File(A.getExternalCacheDir(), "okhttpcache"), 10 * 1024 * 1024)).build();
//        //缓存路径、缓存区的大小(没网时读缓存)
//
//
//        Request request = new Request.Builder()
//                    //强制使用网络
//        //                .cacheControl(CacheControl.FORCE_NETWORK)
//                       //强制使用缓存
//                       .cacheControl(CacheControl.FORCE_CACHE)
//                       .url(urlString).build();
//
////        Request request = new Request.Builder().url(urlString).build();
//        client.newCall(request).enqueue(callback);
//    }

    static public Boolean login=false;
    static public String username=null;
    static public String pwd=null;
    static public String tureName=null;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        NetRequest.userId = userId;
    }

    static public String userId=null;

    public static String getTureName() { return tureName; }

    public static void setTureName(String tureName) { NetRequest.tureName = tureName; }

    public static Boolean getLogin() {
        return login;
    }

    public static void setLogin(Boolean login) {
        NetRequest.login = login;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        NetRequest.username = username;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(String pwd) {
        NetRequest.pwd = pwd;
    }











    public static OkHttpClient client;
    public static Response[] okhttpRequest(Activity A,Request... requests) {
        Response[] responses = new Response[requests.length];
        client=new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(A.getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
        for(int i=0;i<requests.length;i++){
            try {
                responses[i]=client.newCall(requests[i]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }




    //判断当前是否有网络连接
    public static boolean isOnline(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context

                .getSystemService(Activity.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {

            return true ;

        }

        return false ;

    }

    //判断当前设备是否为手机
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return false ;
        } else {
            return true ;
        }
    }

    //获取当前设备的IMEI，需要与上面的isPhone()一起使用
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getDeviceIMEI(Context context) {
        String deviceId;
        if (isPhone(context)) {
            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephony.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }






}