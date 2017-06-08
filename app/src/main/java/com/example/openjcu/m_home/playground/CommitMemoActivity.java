package com.example.openjcu.m_home.playground;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.example.openjcu.R;
import com.example.openjcu.login.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.example.openjcu.tool.NetRequest.getLogin;
import static com.example.openjcu.tool.NetRequest.getUserId;
import static com.example.openjcu.tool.NetRequest.isOnline;

public class CommitMemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_memo);
        initView();
    }





    String app_url;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:     // 0是网络超时发过来的msg
                    Toast.makeText(CommitMemoActivity.this, "网络错误(超时)", Toast.LENGTH_SHORT).show();
                    break;
                case 1:     // 1是无网络发过来的msg
//                    progressDialog.dismiss();
                    Toast.makeText(CommitMemoActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };




    EditText memo_content;
    Button commit;
    ImageView previewUploadPic1,previewUploadPic2,previewUploadPic3;
    String memoId;
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.memo_commit_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        app_url = getResources().getString(R.string.app_url);
        memo_content=(EditText) findViewById(R.id.memo_content);
        commit = (Button) findViewById(R.id.commit);
        previewUploadPic1 = (ImageView) findViewById(R.id.previewUploadPic1);
        previewUploadPic2 = (ImageView) findViewById(R.id.previewUploadPic2);
        previewUploadPic3 = (ImageView) findViewById(R.id.previewUploadPic3);

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
            case R.id.login_from_memo:
                Intent in = new Intent(this, LoginActivity.class); startActivity(in); break;
            case 16908332:  finish(); break;
            default: break;
        }
        return true;
    }
    //为toolbar 指定menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commitmemotoolbar, menu);
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commit:
                if(getLogin()) {
                    String memo_contentS = memo_content.getText().toString();
                    if(memo_contentS.equals("") ){
                        Toast.makeText(CommitMemoActivity.this,"请输入内容",Toast.LENGTH_SHORT).show(); break;
                    }else {
                        commitTheme(memo_contentS);
                    }
                }else {
                    Toast.makeText(CommitMemoActivity.this, "请先登录", Toast.LENGTH_SHORT).show();

                }   break;

            case R.id.previewUploadPic1: handlePicture(1); break;
            case R.id.previewUploadPic2: handlePicture(2); break;
            case R.id.previewUploadPic3: handlePicture(3); break;


            default: break;
        }
    }

    String jsondata;
    Response response;
    ProgressDialog progressDialog;
    public void commitTheme(final String memo_contentS){
//        progressDialog=new ProgressDialog(this);
//        progressDialog.setTitle("正在发布");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        if (!isOnline(this)) {Toast.makeText(CommitMemoActivity.this, "请连接网络", Toast.LENGTH_SHORT).show(); return;}
        if(p1) picsQuantity+=1;
        if(p2) picsQuantity+=1;
        if(p3) picsQuantity+=1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client;                                                                                                                        //$_GET['username']、$_GET['pwd']、$_GET['title']、$_GET['content']、$_GET['category']
                client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
                Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"colorfulwall/insertMemo.php?colorfulWallContent="+memo_contentS+"&userId="+getUserId()+"&colorfulWallPic="+getPicsString()).build();
//                Request request1 = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(R.string.bbsAddr+"insert_theme.php?username="+username+"&pwd="+pwd+"&title="+textTitle+"&content="+textContent+"&category=1").build();

                try {
                    response = client.newCall(request).execute();

                    String mm=response.body().string();
                    Log.e("Open", mm);
                    String temp[] = mm.split(",");
                    memoId = temp[1];
                    //jsondata=response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message=new Message();
                    message.what=0;
                    handler.sendMessage(message);
                }
                requestForUpLoadFile();

                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);

            }
        }).start();
    }

    public String getPicsString() {
        String d=null;
        if (picsQuantity == 1)
            d="1";
        if(picsQuantity==2)
            d = "1,2";
        if(picsQuantity==3)
            d = "1,2,3";
        if(picsQuantity==0)
            d = "0";
        return d;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void handlePicture(int picFlag) {

        if (ContextCompat.checkSelfPermission(CommitMemoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CommitMemoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum(picFlag);
        }
    }
    public void openAlbum(int picFlag){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, picFlag); // 打开相册
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(1);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(2);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum(3);
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default: break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data,1);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data,1);
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data,2);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data,2);
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data,3);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data,3);
                    }
                }
                break;
            default:
                break;
        }
    }
    String imagePath;
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data,int picFlag) {
        //  String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        // displayImage(imagePath); // 根据图片路径显示图片
        displayImageWithGilde(uri,picFlag);
    }

    private void handleImageBeforeKitKat(Intent data,int picFlag) {
        Uri uri = data.getData();
        //String
        imagePath = getImagePath(uri, null);
        //displayImage(imagePath);
        displayImageWithGilde(uri,picFlag);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImageWithGilde(final Uri uri, final int picFlag){

        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(CommitMemoActivity.this).load(uri).downloadOnly(200, 200);
                switch (picFlag) {
                    case 1:
                        try {
                            file1Uri=uri;
                            cacheFile1 = future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e("OpenJCU", "1111111111111111111111111");
                        } catch (ExecutionException e) {
                            e.printStackTrace();

                            Log.e("OpenJCU", "2222222222222222222222222");
                        }
                        p1=true;
                        break;
                    case 2:
                        try {
                            file2Uri=uri;
                            cacheFile2 = future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e("OpenJCU", "1111111111111111111111111");
                        } catch (ExecutionException e) {
                            e.printStackTrace();

                            Log.e("OpenJCU", "2222222222222222222222222");
                        }
                        p2=true;
                        break;
                    case 3:
                        try {
                            file3Uri=uri;
                            cacheFile3 = future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e("OpenJCU", "1111111111111111111111111");
                        } catch (ExecutionException e) {
                            e.printStackTrace();

                            Log.e("OpenJCU", "2222222222222222222222222");
                        }
                        p3=true;
                        break;
                }
            }
        }).start();

        switch (picFlag) {
            case 1:
                Glide.with(this)
                        .load(uri)
                        .override(100,100) //图片大小
                        .centerCrop()   //等比缩放
                        .error(R.drawable.bgi1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(previewUploadPic1);
                break;
            case 2:
                Glide.with(this)
                        .load(uri)
                        .override(100,100) //图片大小
                        .centerCrop()   //等比缩放
                        .error(R.drawable.bgi1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(previewUploadPic2);
                break;
            case 3:
                Glide.with(this)
                        .load(uri)
                        .override(100,100) //图片大小
                        .centerCrop()   //等比缩放
                        .error(R.drawable.bgi1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(previewUploadPic3);
                break;
        }


    }





    File cacheFile1,cacheFile2,cacheFile3;
    Uri file1Uri,file2Uri,file3Uri;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    上传文件
     */
    int picsQuantity=0; Boolean p1=false,p2=false,p3=false;
    public interface FileUploadService {
        // 上传单个文件
        @Multipart
        @POST("colorfulwall/process_memo_pics.php")
        Call<ResponseBody> uploadFile(
                @Part("memoId") RequestBody description,
                @Part MultipartBody.Part file);

        //上传多个文件
        @Multipart
        @POST("colorfulwall/process_memo_pics.php")
        Call<ResponseBody> uploadMultipleTwoFile(
                @Part("memoId") RequestBody description,
                @Part MultipartBody.Part file1,
                @Part MultipartBody.Part file2);
        @Multipart
        @POST("colorfulwall/process_memo_pics.php")
        Call<ResponseBody> uploadMultipleThreeFile(
                @Part("memoId") RequestBody description,
                @Part MultipartBody.Part file1,
                @Part MultipartBody.Part file2,
                @Part MultipartBody.Part file3);
    }


    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }
    @NonNull  // MultipartBody.Part 用于填充 name filename file Content-Type
    private MultipartBody.Part prepareFilePart(String partName, File cacheFile) {
        File file = new File(imagePath);
        // 为file建立RequestBody实例
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), cacheFile);
        // MultipartBody.Part借助文件名完成最终的上传
        return MultipartBody.Part.createFormData(partName, cacheFile.getName(), requestFile);  //包含 name filename file
    }
    //正式上传的代码
    public void requestForUpLoadFile() { //指定url
        int picsQuantity=0;
        if(file1Uri==null&&file2Uri==null&file3Uri==null) return;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(app_url).addConverterFactory(GsonConverterFactory.create()).build();
        CommitMemoActivity.FileUploadService service = retrofit.create(CommitMemoActivity.FileUploadService.class); // 创建上传的service实例
        MultipartBody.Part body[]=new MultipartBody.Part[3];
        if(file1Uri!=null) {
            body[picsQuantity] = prepareFilePart("themePic1", cacheFile1);  // 创建文件的part (photo, video, ...)
            picsQuantity+=1;
        }if(file2Uri!=null) {
            body[picsQuantity] = prepareFilePart("themePic2", cacheFile2);
            picsQuantity+=1;
        }if(file3Uri!=null) {
            body[picsQuantity] = prepareFilePart("themePic3", cacheFile3);
            picsQuantity+=1;
        }
        //     MultipartBody.Part body2 = prepareFilePart("thumbnail", file2Uri);
        RequestBody description = createPartFromString(memoId); // 添加其他的part
        Call<ResponseBody> call=null;
        switch (picsQuantity) {
            case 1:
                call = service.uploadFile(description, body[0]);
                break;
            case 2:
                call = service.uploadMultipleTwoFile(description, body[0],body[1]);
                break;
            case 3:
                call = service.uploadMultipleThreeFile(description, body[0],body[1],body[2]);
                break;
            default: break;
        }
        //    Call<ResponseBody> call = service.uploadFile(description, body1); // 最后执行异步请求操作
        try {
            retrofit2.Response<ResponseBody> requestBody=call.execute();
            //Log.e("OpenJCU", "Secucces"+requestBody.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OpenJCU", "cfffffefwsdw");
        }
    }











}
