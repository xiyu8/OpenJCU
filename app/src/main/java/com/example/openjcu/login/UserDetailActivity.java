package com.example.openjcu.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.example.openjcu.R;
import com.example.openjcu.m_home.communication.PictureDetailActivity;
import com.example.openjcu.mainfragment.homefragment_toolclass.ResourceVersion;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.openjcu.tool.NetRequest.getUserId;

public class UserDetailActivity extends AppCompatActivity {
    String app_url;
    Boolean haveResult=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getSupportActionBar().hide();
        initView();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                requestWithRetrofit();
//            }
//        }).start();
    }

    ImageView userIcon;
    public void initView() {
        app_url = getResources().getString(R.string.app_url);
        userIcon=(ImageView)findViewById(R.id.detailUserIcon);
        Glide.with(this)
                .load(app_url+"userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis())
                //.override(100, 100) //图片大小
                .centerCrop()   //等比缩放
                //.bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                .bitmapTransform(new CropCircleTransformation(this))
                .placeholder(R.drawable.bgi1) //
                .error(R.drawable.bgi1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(userIcon);
    }

    @Override
    public void onBackPressed() {
        if (haveResult) {
//            Intent intent = new Intent();
//            intent.putExtra("name", name);
            setResult(RESULT_OK);
            finish();
        }else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.getUserIcon: handlePicture(); break;
            case R.id.detailUserIcon:
                Intent intent = new Intent(this, PictureDetailActivity.class);
                intent.putExtra("picUrl", app_url+"userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis());
                startActivity(intent);

                break;
            default: break;
        }
    }


    /******************************************从相册加载图片*************************************************/
    public void handlePicture() {

        if (ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }
    public void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1); // 打开相册
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
//            case TAKE_PHOTO:
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
    @TargetApi(19)
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

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            userIcon.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayImageWithGilde(final Uri uri){

        //  拿到file的uri 后，利用glide拿到file，拿到file之后再上传file
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(UserDetailActivity.this)
                        .load(uri)
                        .downloadOnly(200, 200);
                try {
                    cacheFile = future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
            Log.e("OpenJCU", "1111111111111111111111111");
                } catch (ExecutionException e) {
                    e.printStackTrace();

                    Log.e("OpenJCU", "2222222222222222222222222");
                }
                requestForUpLoadFile(uri);

            }
        }).start();

        Glide.with(this)
                .load(uri)
                .override(100,100) //图片大小
                .centerCrop()   //等比缩放，imageView占满
            //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                .placeholder(R.drawable.bgi1) //
                .error(R.drawable.bgi1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(userIcon);
    }
    File cacheFile;
    /*******************************************************************************************/



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 /*
Retrofit 使用Get请求
*/
    public interface BlueServiceApi {
        @GET("testJson.php")
        Call<List<ResourceVersion>> getSearchBooks(@Query("name") String name); //指定请求的api，指定请求结果(http响应的body)的存放类
    }

    public void requestWithRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()  //指定url
                .baseUrl(app_url+"")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BlueServiceApi blueService = retrofit.create(BlueServiceApi.class);

        //指定请求参数的值
        Call<List<ResourceVersion>> call = blueService.getSearchBooks("cc"); //"小王子", "", 0, 3

        List<ResourceVersion> resourceVersionList=null;
        Response<List<ResourceVersion>> response1=null;
        try {
            response1=call.execute();  //获得http完整的Response
            if(response1==null){
                Log.e("OenJCU", "DDDDDDDDDDDDDDDDDDDDDD");
            }else {
                resourceVersionList = response1.body(); //获得http完整的Response的Body部分
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OenJCU", "CCCCCCCCCCCCCCCC"+response1);
        }
//        String ss=response.fragment_home_ver;
        if (resourceVersionList != null) {
            Log.e("OenJCU", "WWWWWWWWWWWWWWWW" + resourceVersionList.get(1).fragment_home_ver);

        }
    }

/*
Retrofit 使用POST请求
 */
    public interface ServicePostApi {
    @FormUrlEncoded
    @POST("book/reviews")
    Call<List<ResourceVersion>> gainVersion(@Field("book") String bookId, @Field("title") String title, //指定请求的api，指定请求结果(http响应的body)的存放类
                                            @Field("content") String content, @Field("rating") String rating);
//        @POST("testJson.php")
//        Call<List<ResourceVersion>> getSearchBooks(@Query("name") String name); //指定请求的api，指定请求结果(http响应的body)的存放类
}
    public void requestWithRetrofitPost() {
        Retrofit retrofit = new Retrofit.Builder()  //指定url
                .baseUrl(app_url+"")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicePostApi serviceApi = retrofit.create(ServicePostApi.class); //指定请求的api

        //指定请求参数的值
        Call<List<ResourceVersion>> call = serviceApi.gainVersion("cc","","cc",""); //指定Api的参数

        List<ResourceVersion> resourceVersionList=null;
        Response<List<ResourceVersion>> response1=null;
        try {
            response1=call.execute();                   //获得http完整的Response
            if(response1==null){
                Log.e("OenJCU", "DDDDDDDDDDDDDDDDDDDDDD");
            }else {
                resourceVersionList = response1.body(); //获得http完整的Response的Body部分
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OenJCU", "CCCCCCCCCCCCCCCC"+response1);
        }
        if (resourceVersionList != null) {
            Log.e("OenJCU", "WWWWWWWWWWWWWWWW" + resourceVersionList.get(1).fragment_home_ver);

        }
    }

/*
上传文件
 */
    public interface FileUploadService {
        // 上传单个文件
        @Multipart
        @POST("processResouce/process_user_icon.php")
        Call<ResponseBody> uploadFile(
                @Part("userId") RequestBody description,
                @Part MultipartBody.Part file);
        // 上传多个文件
//        @Multipart
//        @POST("upload")
//        Call<ResponseBody> uploadMultipleFiles(
//                @Part("description") RequestBody description,
//                @Part MultipartBody.Part file1,
//                @Part MultipartBody.Part file2);
    }


    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }
    @NonNull  // MultipartBody.Part 用于填充 name filename file Content-Type
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), cacheFile);
        // MultipartBody.Part借助文件名完成最终的上传
        return MultipartBody.Part.createFormData(partName, cacheFile.getName(), requestFile);  //包含 name filename file
    }
    //正式上传的代码
    public void requestForUpLoadFile(Uri file1Uri) { //指定url
        Retrofit retrofit = new Retrofit.Builder().baseUrl(app_url).addConverterFactory(GsonConverterFactory.create()).build();
        FileUploadService service = retrofit.create(FileUploadService.class); // 创建上传的service实例

        MultipartBody.Part body1 = prepareFilePart("userIcon", file1Uri);  // 创建文件的part (photo, video, ...)
   //     MultipartBody.Part body2 = prepareFilePart("thumbnail", file2Uri);
        RequestBody description = createPartFromString(getUserId()); // 添加其他的part

        Call<ResponseBody> call = service.uploadFile(description, body1); // 最后执行异步请求操作
        try {
            Response<ResponseBody> requestBody=call.execute();
            haveResult=true;
            Log.e("OpenJCU", "Secucces"+requestBody.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OpenJCU", "cfffffefwsdw");
        }
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call,
//                                   Response<ResponseBody> response) {
//                Log.v("Upload", "success");
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());
//            }
//        });
    }






















    /*
    封装后的getRealFilePathFromURI
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] {MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    //将uri 解析成 file path
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();    return res;
    }



/*  异步请求

call.enqueue(new Callback<BookSearchResponse>() {
  @Override
  public void onResponse(Call<BookSearchResponse> call, 		Response<BookSearchResponse> response) {
  	asyncText.setText("异步请求结果: " + response.body().books.get(0).altTitle);
  }
  @Override
  public void onFailure(Call<BookSearchResponse> call, Throwable t) {

  }
});


 */


























}
