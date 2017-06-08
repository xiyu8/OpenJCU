package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: getName();  checkLogin();
                break;
            default:break;
        }
    }

    String name,pwd;
    protected void getName(){

        EditText t1=(EditText)findViewById(R.id.editText); name=t1.getText().toString();
        EditText t2=(EditText)findViewById(R.id.editText2); pwd=t2.getText().toString();

    }


    private android.os.Handler mHandler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:     // 0是网络超时发过来的msg
                    Toast.makeText(Main2Activity.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                 //   swp.setRefreshing(false);
                    progressDialog.dismiss();
                    break;
                case 1:     // 1是无网络发过来的msg
                    Toast.makeText(Main2Activity.this, "成功", Toast.LENGTH_SHORT).show();
                //    swp.setRefreshing(false);

                    progressDialog.dismiss();
                    break;
                case 2:     // 1是无网络发过来的msg
                    Toast.makeText(Main2Activity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    //    swp.setRefreshing(false);

                    progressDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    ProgressDialog progressDialog;
    String data;
    public Boolean checkLogin() {
        if(isOnline(Main2Activity.this)){
            progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("正在登录");
            progressDialog.setCancelable(false);
            progressDialog.show();



            new Thread(new Runnable() {
                @Override
                public void run() {

                    OkHttpClient client;  Request request; Response response;
                    client=new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
                    request= new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url("http://192.168.155.1/www/OpenJCU/login/login.php?username="+name+"&pwd="+pwd).build();
                    try {
                        response=client.newCall(request).execute();
                        Log.e("OpnJCU",""+data);
                        data=response.body().string();
                        Log.e("OpnJCU",""+data);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Message message=new Message();
                        message.what=0;
                        mHandler.sendMessage(message);
                        return;
                    }


                    if(data.equals("1")) {
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                    else {

                        Message message = new Message();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    }

                }

            }).start();




        }else {
            Toast.makeText(this, "请检查网络",Toast.LENGTH_SHORT).show();
        }
        return false;
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
}
