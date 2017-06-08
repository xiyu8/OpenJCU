package com.example.openjcu.m_home.playground;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;
import com.example.openjcu.m_home.communication.PictureDetailActivity;

public class MemoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        initView();
    }




    TextView d_l_f_title,d_l_f_content,d_l_f_time;
    LinearLayout l_f_showPicDiv;
    ImageView d_l_f_flag;
    String L_f_content,L_f_id,L_f_pics,L_f_time,L_f_title,L_f_flag;

    public static final String FRUIT_NAME = "fruit_name";

    public static final String FRUIT_IMAGE_ID = "fruit_image_id";

    String app_url;
   public void initView(){
       app_url = getResources().getString(R.string.app_url);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("UserId");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView = (ImageView) findViewById(R.id.fruit_image_view);
        TextView fruitContentText = (TextView) findViewById(R.id.fruit_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(userName);
        Glide.with(this).load(app_url+"playgroundPics/"+intent.getStringExtra("ColorfulWallId")+"/1.jpg").into(fruitImageView);
        String fruitContent = intent.getStringExtra("ColorfulWallContent");
        fruitContentText.setText(fruitContent);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.theme_pic1: Intent intent=new Intent(this, PictureDetailActivity.class);
                intent.putExtra("picUrl",app_url + "lostPics/" + L_f_id + "/" + "1" + ".jpg"); startActivity(intent); break;

            case R.id.theme_pic2: Intent intent1=new Intent(this, PictureDetailActivity.class);
                intent1.putExtra("picUrl",app_url + "lostPics/" + L_f_id + "/" + "2" + ".jpg"); startActivity(intent1); break;

            case R.id.theme_pic3: Intent intent2=new Intent(this, PictureDetailActivity.class);
                intent2.putExtra("picUrl",app_url + "lostPics/" + L_f_id + "/" + "3" + ".jpg"); startActivity(intent2); break;

        }
    }
























}
