package com.example.openjcu.m_home.communication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;
import com.gloomyer.zoomimageview.ZoomImageView;

public class PictureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        getSupportActionBar().hide();
        initView();
    }

    public void initView() {



        LinearLayout ly = (LinearLayout) findViewById(R.id.pictureDetailActivityLayout);
        ly.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        Intent in=getIntent();
        String picUrl = in.getStringExtra("picUrl");
//        Glide.with(this)
//                .load(picUrl)
//                .fitCenter()//等比例缩放图片，宽或者是高等于ImageView的宽或者是高。
//                //    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
//                .placeholder(R.drawable.bgi1) //
//                .error(R.drawable.bgi1)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into((ImageView)findViewById(R.id.showPictureDetail));



        Glide.with(this)
                .load(picUrl)
                .asBitmap()
                .placeholder(R.drawable.bgi1)
                .into((ZoomImageView) findViewById(R.id.showPictureDetail));

    }
}
