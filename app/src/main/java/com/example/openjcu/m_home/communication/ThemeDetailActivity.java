package com.example.openjcu.m_home.communication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.example.openjcu.R;
import com.example.openjcu.login.LoginActivity;
import com.example.openjcu.m_home.communication.forgson.ThemeDetailResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

import static com.example.openjcu.tool.NetRequest.login;
import static com.example.openjcu.tool.NetRequest.pwd;
import static com.example.openjcu.tool.NetRequest.username;


public class ThemeDetailActivity extends AppCompatActivity {

    String app_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_detail);
        initView();
        refreshLayout.setRefreshing(true);
        ref();
    }

    TextView v_username,v_title,v_content,v_time;
    SwipeRefreshLayout refreshLayout;
    Intent in;
    RelativeLayout[] commentItemView;
    LinearLayout comment_area;
    ///////////////////////commit content///////////////////////////////
    Button publish;
    EditText pulish_content;
    String content_text;
    ImageView perViewCommentPic;
    String supusername;
    /////////////////////////////////////////////////////
    String themeId;
    LinearLayout showPicDiv;
    public void initView() {
        app_url=getResources().getString(R.string.app_url);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.detail_reflash);
        comment_area=(LinearLayout)findViewById(R.id.comment_area);
        v_content = (TextView) findViewById(R.id.theme_content);
        v_title = (TextView) findViewById(R.id.theme_title);
        v_username = (TextView) findViewById(R.id.user_name);
        v_time = (TextView) findViewById(R.id.timeStampe);
        showPicDiv = (LinearLayout) findViewById(R.id.showPicDiv);
        in = getIntent();
        String username = in.getStringExtra("userName");
        supusername=username;
        String title = in.getStringExtra("title");
        String content = in.getStringExtra("content");
        String time = in.getStringExtra("time");
        String pics = in.getStringExtra("pics");
        themeId = in.getStringExtra("themeId");
        v_username.setText(username);
        v_content.setText(content);
        v_time.setText(time);
        v_title.setText(title);
        if(pics==null){
        }else {
            String eachPic[] = pics.split(",");
            ImageView picsView[] = new ImageView[3];
            picsView[0] = (ImageView) findViewById(R.id.theme_pic1);
            picsView[1] = (ImageView) findViewById(R.id.theme_pic2);
            picsView[2] = (ImageView) findViewById(R.id.theme_pic3);
            for (int i = 0; i < eachPic.length; i++) {
                picsView[i].setVisibility(View.VISIBLE);
                showPicDiv.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(app_url+"themePics/" + themeId + "/" + eachPic[i] + ".jpg")
                        // .override(100,100) //图片大小
                        .fitCenter()//等比例缩放图片，宽或者是高等于ImageView的宽或者是高。
                        //.centerCrop()   //等比缩放
                        //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                        .placeholder(R.drawable.bgi1) //
                        .error(R.drawable.bgi1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(picsView[i]);
            }
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                        ref();

            }
        });


        client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).cache(new Cache(new File(getExternalCacheDir(), "okhttpcache"), 100 * 1024 * 1024)).build();
/////////////////////////////////////////////////////////////////
        publish = (Button)findViewById(R.id.publish);
        pulish_content=(EditText)findViewById(R.id.publish_content);

/////////////////////////////////////////////////////////////////

        perViewCommentPic = (ImageView) findViewById(R.id.perViewCommentPic);


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ThemeDetailActivity.this,"获取评论失败1",Toast.LENGTH_SHORT).show(); break;
                case 1:
                    putComment();
                    refreshLayout.setRefreshing(false);  break;
                case 2:
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(ThemeDetailActivity.this,"获取评论失败2",Toast.LENGTH_SHORT).show(); break;
                case 3:
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(ThemeDetailActivity.this,"没有评论",Toast.LENGTH_SHORT).show(); break;
                case 10:
                    Toast.makeText(ThemeDetailActivity.this,"评论失败",Toast.LENGTH_SHORT).show(); break;
                case 11:
                    Toast.makeText(ThemeDetailActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                    pulish_content.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pulish_content.getWindowToken(), 0);
                    perViewCommentPic.setWillNotDraw(false);
                    break;
                case 12:
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(ThemeDetailActivity.this);
//                    dialog.setTitle("请登录");
//                    dialog.setMessage("是否登录？");
//                    dialog.setPositiveButton("登录",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent in = new Intent(ThemeDetailActivity.this, LoginActivity.class);
//                            startActivity(in);
//                        }
//                    });
//                    dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    dialog.show();
                    Toast.makeText(ThemeDetailActivity.this,"请先登录",Toast.LENGTH_SHORT).show();  break;
                default: break;
            }

            super.handleMessage(msg);
        }
    };

    OkHttpClient client;
    public void ref() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"bbs/obtain_theme_detail.php?id=" + in.getStringExtra("themeId")).build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsondata = response.body().string();
                    Log.e("OpenJCU", jsondata);
                    if (jsondata.contains("无记录")) {  //当用户发的包含 "无记录" 此处有Bug
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                    } else {
                        parseArry(jsondata);
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


    List<ThemeDetailResource> themeDetailResourceList;
    public void parseArry(String data) {
        Gson gson = new Gson();
      //  ThemeDetailResource[] themeResourcesArray = gson.fromJson(data, ThemeDetailResource[].class);
        themeDetailResourceList = gson.fromJson(data, new TypeToken<List<ThemeDetailResource>>() {}.getType());

        Message message=new Message();
        message.what=1;
        handler.sendMessage(message);
    }

    int i;
    public void putComment() {   //已完成评论呈现picture
        int size=themeDetailResourceList.size();
        LayoutInflater lf = getLayoutInflater().from(this);
//        view1 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager1, null);view2 = (SwipeRefreshLayout)lf.inflate(R.layout.communication_pager2, null);
        commentItemView = new RelativeLayout[size];
        comment_area.removeAllViews();
        for (int i=0;i<size;i++) {
            commentItemView[i]=(RelativeLayout)lf.inflate(R.layout.comment_item, null);
            Log.e("OpenJCU",themeDetailResourceList.get(i).getUserName());
            TextView comment_username=(TextView) commentItemView[i].findViewById(R.id.comment_username); comment_username.setText(themeDetailResourceList.get(i).getUserName());
            TextView comment_content=(TextView) commentItemView[i].findViewById(R.id.comment_content); comment_content.setText(themeDetailResourceList.get(i).getCommentContent());
            TextView comment_time=(TextView) commentItemView[i].findViewById(R.id.comment_time); comment_time.setText(themeDetailResourceList.get(i).getCommentTime());
            if(themeDetailResourceList.get(i).getCommentPics()!=null) {
                ImageView img=(ImageView) (commentItemView[i].findViewById(R.id.comment_pic));
                img.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(app_url+"commentPics/" + themeDetailResourceList.get(i).getCommentId()+"/1.jpg")
                        // .override(100,100) //图片大小
                        .fitCenter()//等比例缩放图片，宽或者是高等于ImageView的宽或者是高。
                        //.centerCrop()   //等比缩放
                        //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                        .placeholder(R.drawable.bgi1) //
                        .error(R.drawable.bgi1)
                        .into(img);
            }
            comment_area.addView(commentItemView[i]);
            final int finalI = i;
            (commentItemView[i].findViewById(R.id.comment_pic)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ThemeDetailActivity.this, PictureDetailActivity.class);
                    in.putExtra("picUrl",app_url+"commentPics/" + themeDetailResourceList.get(finalI).getCommentId()+"/1.jpg");
                    startActivity(in);
                }
            });
        }

    }


    public void onClick(View v) {
        Intent in = new Intent(this, PictureDetailActivity.class);
        String ss = app_url+"themePics/"+themeId+"/";
        switch (v.getId()) {
            case R.id.publish:  publishComment();break;
            case R.id.perViewCommentPic:  perViewCommentPic.setWillNotDraw(true); handlePicture();  break;
            case R.id.comment_pic:  in.putExtra("picUrl",ss+"1.jpg"); startActivity(in); break;
            case R.id.theme_pic1:
                in.putExtra("picUrl",ss+"1.jpg");

                startActivity(in);

                break;
            case R.id.theme_pic2: in.putExtra("picUrl",ss+"2.jpg"); startActivity(in);  break;
            case R.id.theme_pic3: in.putExtra("picUrl",ss+"3.jpg"); startActivity(in); break;
            default: break;
        }
    }



    String commentId;
    public void publishComment(){
        if(login) {
            content_text = pulish_content.getText().toString();
            if(content_text.equals("")) {
                Toast.makeText(ThemeDetailActivity.this,"评论不能为空",Toast.LENGTH_SHORT).show();
            }else {
                new Thread(new Runnable() {  //先提交评论文本内容，再提交评论的图片
                    Request request;
                    @Override
                    public void run() {  //http://localhost/www/OpenJCU/bbs/insert_comment.php?username=xiyu&pwd=12345&supusername=xiyu&content=commentContent&themeid=1
                        if(cacheFile==null){
                            request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"bbs/insert_comment.php?username=" + username + "&pwd=" + pwd+"&supusername="+supusername + "&content=" + content_text + "&themeid=" + in.getStringExtra("themeId")).build();
                            String s=getResources().getString(R.string.app_url);
                        }else {
                            request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(app_url+"bbs/insert_comment.php?username=" + username + "&pwd=" + pwd +"&supusername="+supusername+ "&content=" + content_text + "&themeid=" + in.getStringExtra("themeId")+"&commentPics=1").build();
                        }
                        try {
                            Response response = client.newCall(request).execute();
                            String ss=response.body().string();
                            String temp[] = ss.split(",");
                            Log.e("OpenJCU", "VVVVVVVVVV" + ss);
                            if(cacheFile!=null) {
                                commentId = temp[1];
                                Log.e("OpenJCU",ss+ "VVVVVVVVVV" + commentId);
                                requestForUpLoadFile();
                            }
                            if (ss.contains("新记录插入成功")) {
                                Log.e("OpenJCU", "VVVVVVVVVV" + ss);
                                Message message = new Message();
                                message.what = 11;
                                handler.sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Message message = new Message();
                            message.what = 10;
                            handler.sendMessage(message);
                        }

                    }
                }).start();
            }
        }else {
            Message message=new Message();
            message.what=12;
            handler.sendMessage(message);
        }
    }

    //////////////////////////////////////////从手机相册获得图片//////////////////////////////////////////////////////
    public void handlePicture() {
        if (ContextCompat.checkSelfPermission(ThemeDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ThemeDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }
    public void openAlbum() {

        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,1); // 打开相册
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
//            case TAKE_PHOTO:   //从camera中拿到图片
//                if (resultCode == RESULT_OK) {
//                    try {
//                        // 将拍摄的照片显示出来
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        picture.setImageBitmap(bitmap);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    String imagePath;
    @TargetApi(19)   //拿到图片的路径
    private void handleImageOnKitKat(Intent data) {
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
        displayImageWithGilde(uri);
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        //String
        imagePath = getImagePath(uri, null);
        //displayImage(imagePath);
        displayImageWithGilde(uri);
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
    File cacheFile=null;
    private void displayImageWithGilde(final Uri uri) {

        //获取到图片的缓存文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(ThemeDetailActivity.this)
                        .load(uri)
                        .downloadOnly(200, 200);
                try {
                    cacheFile = future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            //     requestForUpLoadFile(uri);
        }).start();
        //预览获取到的图片
        Glide.with(this)
                .load(uri)
                .override(100, 100) //图片大小
                .centerCrop()   //等比缩放
                //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                .placeholder(R.drawable.bgi1) //
                .error(R.drawable.bgi1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(perViewCommentPic);
    }
        //将图片上传至服务器
        //   requestForUpLoadFile(uri);



//    public void requestForUpLoadFile(){
//
//    }

//////////////////////////////////////////////////////////
    public interface FileUploadService {
        // 上传单个文件
        @Multipart
        @POST("processResouce/process_comment_pics.php")
        Call<ResponseBody> uploadFile(
                @Part("commentId") RequestBody description,
                @Part MultipartBody.Part file);
//        //上传多个文件
//        @Multipart
//        @POST("processResouce/process_theme_pics.php")
//        Call<ResponseBody> uploadMultipleTwoFile(
//                @Part("themeId") RequestBody description,
//                @Part MultipartBody.Part file1,
//                @Part MultipartBody.Part file2);
//        @Multipart
//        @POST("processResouce/process_theme_pics.php")
//        Call<ResponseBody> uploadMultipleThreeFile(
//                @Part("themeId") RequestBody description,
//                @Part MultipartBody.Part file1,
//                @Part MultipartBody.Part file2,
//                @Part MultipartBody.Part file3);
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


        Retrofit retrofit = new Retrofit.Builder().baseUrl(app_url).addConverterFactory(GsonConverterFactory.create()).build();
        FileUploadService service = retrofit.create(FileUploadService.class); // 创建上传的service实例
        MultipartBody.Part body= prepareFilePart("commentPic", cacheFile);  // 创建文件的part (photo, video, ...)
        RequestBody description = createPartFromString(commentId); // 添加其他的part
        Call<ResponseBody> call=null;
        call = service.uploadFile(description, body);
        try {
            retrofit2.Response<ResponseBody> requestBody=call.execute();
            Log.e("OpenJCU", "Secucces"+requestBody.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OpenJCU", "cfffffefwsdw");
        }
    }





}
