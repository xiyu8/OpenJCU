package com.example.openjcu.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.openjcu.tool.NetRequest.isOnline;
import static com.example.openjcu.tool.NetRequest.loginStatus;
import static com.example.openjcu.tool.NetRequest.setLogin;
import static com.example.openjcu.tool.NetRequest.setUserId;
import static com.example.openjcu.tool.NetRequest.setUsername;
import static com.example.openjcu.tool.NetRequest.setPwd;
import static com.example.openjcu.tool.NetRequest.tureName;


public class LoginActivity extends AppCompatActivity {

    String app_url;
    OkHttpClient client; Request request; Response response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        client=new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
        initView();


      //  laodCheckIamge();
    }
    Button login,register,confirm;
    EditText usernameEdit,pwdEidt,checkingCodeEdit;
    TextView checkingCode;
    ImageView checkImage;
    public void initView(){
        app_url=getResources().getString(R.string.app_url);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register); register.setVisibility(View.GONE);
        confirm = (Button) findViewById(R.id.confirm);
        usernameEdit = (EditText) findViewById(R.id.editText);
        pwdEidt = (EditText) findViewById(R.id.editText2);
        checkingCodeEdit = (EditText) findViewById(R.id.checkingCodeEdit);
        checkingCode = (TextView) findViewById(R.id.checkingCode);
        checkImage=(ImageView) findViewById(R.id.checkImage);



        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        checkingCode.setVisibility(View.GONE);
        checkImage.setVisibility(View.GONE);
        checkingCodeEdit.setVisibility(View.GONE);


    }

    Boolean firstLogin=true;
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                checkMyService();
                break;
//                getName();
//                if(name.isEmpty() || pwd.isEmpty()){
//                    Toast.makeText(LoginActivity.this, "请输入用户名及密码", Toast.LENGTH_SHORT).show();
//                    break;
//                }else {
//                    checkLogin();
////                    checkMyService();
//                    break;
//                }
            case R.id.register:   getName();
                if(name.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入用户名及密码", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    register();
                    break;
                }
            case R.id.confirm:
                checkCode=(((EditText)findViewById(R.id.checkingCodeEdit)).getText()).toString();
                loginjcjw();
                break;
            case R.id.checkingCode: laodCheckIamge();
                break;
            default:break;
        }
    }

    String name,pwd;
    protected void getName(){
        name=usernameEdit.getText().toString();
        pwd=pwdEidt.getText().toString();
    }


    public void checkMyService() {

        getName();
        if(name.isEmpty() || pwd.isEmpty()){
            Toast.makeText(LoginActivity.this, "请输入用户名及密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isOnline(LoginActivity.this)){
            Toast.makeText(LoginActivity.this, "请连接网络", Toast.LENGTH_SHORT).show(); return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                request= new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"bbs/check_user.php?username="+name+"&pwd="+pwd).build();
                try {
                    response=client.newCall(request).execute();
                    data=response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=new Message();
                    message.what=10;
                    mHandler.sendMessage(message);
                    return;
                }

                if(data.contains("验证用户正确")) {
                    setUserId((data.split(","))[1]);
                    Message message = new Message();
                    message.what = 8;
                    mHandler.sendMessage(message);
                } else if(data.contains("无此用户")){
                    Message message = new Message();
                    message.what = 9;
                    mHandler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what = 10;
                    mHandler.sendMessage(message);
                }

            }

        }).start();
    }


    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:     // 0是网络超时发过来的msg
                    Toast.makeText(LoginActivity.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    //   swp.setRefreshing(false);
                    progressDialog.dismiss();
                    break;
                case 1:     // 1是登录成功发过来的msg
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    setLogin(true); setPwd(pwd); setUsername(name);
                    progressDialog.dismiss();
                    Intent intent2 = new Intent();
                    intent2.putExtra("name", name);
                    setResult(RESULT_OK,intent2);
                    finish();
                    break;
                case 2:     // 1是无网络发过来的msg
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    setLogin(false); setPwd(null); setUsername(null);

                    progressDialog.dismiss();
                    break;
                case 3:     // 1是无网络发过来的msg
                    Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
                case 4:
                    Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent1 = new Intent();
                    intent1.putExtra("name", name);
                    setUsername(name);
                    setPwd(pwd);
                    setLogin(true);
                    setResult(RESULT_OK,intent1);
                    finish();
                    break;
                //////////////////for jcjw////////////////////////////////
                case 5:  //教务网验证成功
                    ((TextView) findViewById(R.id.showhtml)).setText(responseString);
                    setLogin(true); setPwd(pwd); setUsername(name);
                    register(); // 验证成功后注册本服务器账号
                    break;
                case 105:  //教务网验证失败
                    ((TextView) findViewById(R.id.showhtml)).setText(responseString);
                    login.setVisibility(View.VISIBLE);
                    register.setVisibility(View.GONE);
                    confirm.setVisibility(View.GONE);
                    checkingCode.setVisibility(View.GONE);
                    checkImage.setVisibility(View.GONE);
                    checkingCodeEdit.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                case 106:  //教务网验证异常
                    Log.e("AAAAAA", "rrrrrrrrrrrrrrrrrrrr" + responseString);
                    ((TextView) findViewById(R.id.showhtml)).setText(responseString);
                    break;
                case 6:
                    ((TextView) findViewById(R.id.showhtml)).setText("Error"); break;
                case 7://
                    ImageView checkImage=(ImageView)findViewById(R.id.checkImage);
                    checkImage.setImageBitmap(bitmap);
                    break;
                /////////////////////////for checking user from my server////////////////////////////////
                case 8:  // 用户已在本服务器上，登录成功，本地记住登录信息
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    setLogin(true); setPwd(pwd); setUsername(name);
                    Intent intent = new Intent();
                    intent.putExtra("name", name);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case 9:  // 用户不在本服务器上，要连接教务网验证
                   // Toast.makeText(LoginActivity.this, "用户不在本服务器上", Toast.LENGTH_SHORT).show();
                    laodCheckIamge();
                    break;
                case 10:  // 异常
                    Toast.makeText(LoginActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                    break;


                default: break;
            }
            super.handleMessage(msg);
        }
    };


    ProgressDialog progressDialog;
    String data;
    public Boolean checkLogin() {
        if(isOnline(LoginActivity.this)){
            progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("正在登录");
            progressDialog.setCancelable(false);
            progressDialog.show();



            new Thread(new Runnable() {
                @Override
                public void run() {
                    client=new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
                    request= new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"login/login.php?username="+name+"&pwd="+pwd).build();
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


                    if(data.length()<15) {
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

    public void register(){
        if(isOnline(LoginActivity.this)){
            progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("正在注册");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    request= new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"login/register.php?username="+name+"&pwd="+pwd).build();
                    try {
                        response=client.newCall(request).execute();
                        data=response.body().string();
                        String temp[] = data.split(",");
                        setUserId(temp[1]);
                        Log.e("OpnJCU",""+data);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Message message=new Message();
                        message.what=0;
                        mHandler.sendMessage(message);
                        return;
                    }


                    if(data.length()<25) {
                        Message message = new Message();
                        message.what = 4;
                        mHandler.sendMessage(message);
                    }
                    else {

                        Message message = new Message();
                        message.what = 3;
                        mHandler.sendMessage(message);
                    }

                }

            }).start();




        }else {
            Toast.makeText(this, "请检查网络",Toast.LENGTH_SHORT).show();
        }
    }















    String responseString="";
    String checkCode;
    public void loginjcjw(){

        if(!isOnline(LoginActivity.this)){
            Toast.makeText(LoginActivity.this, "请连接网络", Toast.LENGTH_SHORT).show(); return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //http协议：响应头中的Set-Cookie字段用于:服务器向客户端写cookie；请求头中的cookie字段用于:客户端向服务器发本地cookie信息
                FormBody requestBody=new FormBody.Builder()
                        .add("__VIEWSTATE",sesion_IDLink)  //标志连接状态(包含在登录界面的响应html正文中)，添加在post参数中
                        .add("Button1","")
                        .add("hidPdrs","")
                        .add("hidsc","")
                        .add("lbLanguage","")
                        .add("RadioButtonList1","学生")
                        .add("Textbox1","")
                        .add("TextBox2",pwd)
                        .add("txtSecretCode",checkCode)
                        .add("txtUserName",name)
                        .build();
                 request= new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url("http://jwweb.scujcc.cn/default2.aspx").post(requestBody)
                       // .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
                        .addHeader("cookie","ASP.NET_SessionId="+sesion_ID).build(); // 标志验证码的cookie

                try {
                    response=client.newCall(request).execute();
                    responseString=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();

                    Message message = new Message();
                    message.what = 6;
                    mHandler.sendMessage(message);
                }

                if(responseString.contains("欢迎使用正方教务") && responseString.contains("验证码")){ //教务网用户名或密码错误
                    Message message = new Message();
                    message.what = 105;
                    mHandler.sendMessage(message);
                }else if(responseString.contains("欢迎您") && responseString.contains("退出")){ //教务网登录成功
                    String temp[]=responseString.split("id=\"xhxm\">|同学</span>");
                    tureName=temp[1];
                    Log.e("OpenJCU", "BBBBBBBBBBBBBBBBBBB"+temp[1]);
                    Message message = new Message();
                    message.what = 5;
                    mHandler.sendMessage(message);
                }else {   //异常 ：可能是教务网改动造成
                    Message message = new Message();
                    message.what = 106;
                    mHandler.sendMessage(message);
                }

            }
        }).start();


    }


    String sesion_IDLink; // 1、在登录界面的请求 的HTML正文中 找到 __VIEWSTATE 对应的值
    Bitmap bitmap; // 2、请求 验证码图片的地址，找到验证码图片
    String sesion_ID; //3、在加载 验证码图片的请求 头 中，找到验证码对应的cookie
    public void laodCheckIamge() {
        //g改变UI
        login.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        confirm.setVisibility(View.VISIBLE);
        checkingCode.setVisibility(View.VISIBLE);
        checkImage.setVisibility(View.VISIBLE);
        checkingCodeEdit.setVisibility(View.VISIBLE);

        if(!isOnline(LoginActivity.this)){
            Toast.makeText(LoginActivity.this, "请连接网络", Toast.LENGTH_SHORT).show(); return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                /**************************************************************************/
                request= new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url("http://jwweb.scujcc.cn").build();

                try {
                    response=client.newCall(request).execute();
                    responseString=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();

                    Message message = new Message();
                    message.what = 10;
                    mHandler.sendMessage(message);

                    return;
                }
                Headers headers=response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                String temp[]= session.split("Id=|;");
                sesion_IDLink  =temp[1];
                temp=responseString.split("name=\"__VIEWSTATE\" value=\"|\" />");
                sesion_IDLink=temp[1];
                /**************************************************************************/

                //http协议：服务器往客户端写 cookie信息 包含在 响应头的 Set-Cookie 字段中
                /***************************每个验证码对应一个cookie***********************************************/
                request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url("http://jwweb.scujcc.cn/CheckCode.aspx").build();
                byte[] Picture_bt1 = null;
                try {
                    response = client.newCall(request).execute();
                    Picture_bt1 = response.body().bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeByteArray(Picture_bt1, 0, Picture_bt1.length);

                headers=response.headers();
                cookies = headers.values("Set-Cookie");
                session = cookies.get(0);
                Log.e("OpenJCU", "响应头中完整的“Set-Cookie”,即响应头中包含服务器想要在客户端写的cookie：" + cookies);
                temp= session.split("Id=|;");

                loginStatus = session.substring(0, session.indexOf(";"));
                sesion_ID =temp[1];
                /**************************************************************************/



                Message message = new Message();
                message.what = 7;
                mHandler.sendMessage(message);

            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("d","d");
        setResult(RESULT_CANCELED,intent);
        finish();
    }


    //        /* 响应头
//        	Request request = new Request.Builder()
//	    .url("https://api.github.com/repos/square/okhttp/issues")
//	    .header("User-Agent", "OkHttp Headers.java")
//	    .addHeader("Accept", "application/json; q=0.5")
//	    .addHeader("Accept", "application/vnd.github.v3+json")
//	    .build();
//
//	Response response = client.newCall(request).execute();
//	if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//	System.out.println("Server: " + response.header("Server"));
//	System.out.println("Date: " + response.header("Date"));
//	System.out.println("Vary: " + response.headers("Vary"));
//         */
//        /* 请求头
//
//    private final OkHttpClient client = new OkHttpClient();
//
//public void run() throws Exception {
//	Request request = new Request.Builder()
//	    .url("https://api.github.com/repos/square/okhttp/issues")
//	    .header("User-Agent", "OkHttp Headers.java")
//	    .addHeader("Accept", "application/json; q=0.5")
//	    .addHeader("Accept", "application/vnd.github.v3+json")
//	    .build();
//
//	Response response = client.newCall(request).execute();
//	if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//	System.out.println("Server: " + response.header("Server"));
//	System.out.println("Date: " + response.header("Date"));
//	System.out.println("Vary: " + response.headers("Vary"));
//}
//
//         */
}
