package com.example.openjcu.m_home.l_f;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;
import com.example.openjcu.m_home.communication.PictureDetailActivity;

public class ThingDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_detail);
        initView();
    }

    TextView d_l_f_title,d_l_f_content,d_l_f_time;
    LinearLayout l_f_showPicDiv;
    ImageView d_l_f_flag;
    String L_f_content,L_f_id,L_f_pics,L_f_time,L_f_title,L_f_flag,app_url;
    public void initView(){
        app_url = getResources().getString(R.string.app_url);
        d_l_f_flag=(ImageView)findViewById(R.id.d_l_f_flag);
        d_l_f_title=(TextView)findViewById(R.id.d_l_f_title);
        d_l_f_time=(TextView)findViewById(R.id.d_l_f_time);
        d_l_f_content=(TextView)findViewById(R.id.d_l_f_content);
        l_f_showPicDiv=(LinearLayout)findViewById(R.id.l_f_showPicDiv);
        Intent intent=getIntent();
        L_f_content=intent.getStringExtra("L_f_content");
        L_f_id=intent.getStringExtra("L_f_id");
        L_f_pics=intent.getStringExtra("L_f_pics");
        L_f_time=intent.getStringExtra("L_f_time");
        L_f_title=intent.getStringExtra("L_f_title");
        L_f_flag=intent.getStringExtra("L_f_flag");
        if (L_f_flag .equals("1")) {
            Glide.with(ThingDetailActivity.this).load(R.drawable.found).error(R.drawable.bgi1).into(d_l_f_flag);
        }
        d_l_f_content.setText(L_f_content);
        d_l_f_title.setText(L_f_title);
        d_l_f_time.setText(L_f_time);
        if(L_f_pics!=null){
            l_f_showPicDiv.setVisibility(View.VISIBLE);
            String eachPic[] = L_f_pics.split(",");
            ImageView picsView[] = new ImageView[3];
            picsView[0] = (ImageView) l_f_showPicDiv.findViewById(R.id.theme_pic1);
            picsView[1] = (ImageView) l_f_showPicDiv.findViewById(R.id.theme_pic2);
            picsView[2] = (ImageView) l_f_showPicDiv.findViewById(R.id.theme_pic3);
            for (int i = 0; i < eachPic.length; i++) {
                picsView[i].setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(app_url+"lostPics/" + L_f_id + "/" + eachPic[i] + ".jpg")
                        .fitCenter()//等比例缩放图片，宽或者是高等于ImageView的宽或者是高。/
                        .error(R.drawable.bgi1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(picsView[i]);
            }
        }

            toolbar = (Toolbar) findViewById(R.id.memo_commit_toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

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
//            case R.id.addmemo:
//                Intent in = new Intent(this, CommitMemoActivity.class); startActivity(in); break;
            case 16908332:  finish(); break;
            default: break;
        }
        return true;
    }
    //为toolbar 指定menu
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.playgroudtoolbar, menu);
        return true;
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
