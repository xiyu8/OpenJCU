package com.example.openjcu.m_home.recruit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.openjcu.R;
import com.example.openjcu.m_home.communication.PictureDetailActivity;

public class JobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        initView();
    }
    

    TextView detailFirmName,detailJob,detailJobTime,detailJobDescripetion,detailJobTel,detailJobWorkPlace;
    ImageView showJobDetailInMap;
    LinearLayout jobPicsDiv;
    ImageView d_l_f_flag;
    String FirmDescription,FirmName,FirmPics,FlagPerson,JobFlag,JobId,JobPublishTime,JobsDescription,Jobs,LinkWay,WorkPlace,WorkPlacePosition,app_url;
    public void initView(){
        app_url = getResources().getString(R.string.app_url);
        d_l_f_flag=(ImageView)findViewById(R.id.d_l_f_flag);
        detailFirmName=(TextView)findViewById(R.id.detailFirmName);
        detailJobTime=(TextView)findViewById(R.id.detailJobTime);
        detailJob=(TextView)findViewById(R.id.detailJob);
        detailJobDescripetion=(TextView)findViewById(R.id.detailJobDescripetion);
        detailJobTel=(TextView)findViewById(R.id.detailJobTel);
        detailJobWorkPlace=(TextView)findViewById(R.id.detailJobWorkPlace);
        showJobDetailInMap=(ImageView)findViewById(R.id.showJobDetailInMap);
        jobPicsDiv=(LinearLayout)findViewById(R.id.jobPicsDiv);
        Intent intent=getIntent();
        FirmDescription=intent.getStringExtra("FirmDescription");
        FirmName=intent.getStringExtra("FirmName");
        FirmPics=intent.getStringExtra("FirmPics");
        FlagPerson=intent.getStringExtra("FlagPerson");
        JobFlag=intent.getStringExtra("JobFlag");
        JobId=intent.getStringExtra("JobId");
        JobPublishTime=intent.getStringExtra("JobPublishTime");
        Jobs=intent.getStringExtra("Jobs");
        JobsDescription=intent.getStringExtra("JobsDescription");
        LinkWay=intent.getStringExtra("LinkWay");
        WorkPlace=intent.getStringExtra("WorkPlace");
        WorkPlacePosition=intent.getStringExtra("WorkPlacePosition");

        ///显示图片
//        if (L_f_flag .equals("1")) {
//            Glide.with(JobDetailActivity.this).load(R.drawable.found).error(R.drawable.bgi1).into(d_l_f_flag);
//        }
        detailJob.setText(Jobs);
        detailFirmName.setText(FirmName);
        detailJobTime.setText(JobPublishTime);
        detailJobDescripetion.setText(JobsDescription);
        detailJobTel.setText(LinkWay);
        detailJobWorkPlace.setText(WorkPlace);
        if(FirmPics!=null){
            jobPicsDiv.setVisibility(View.VISIBLE);
            String eachPic[] = FirmPics.split(",");
            ImageView picsView[] = new ImageView[3];
            picsView[0] = (ImageView) jobPicsDiv.findViewById(R.id.theme_pic1);
            picsView[1] = (ImageView) jobPicsDiv.findViewById(R.id.theme_pic2);
            picsView[2] = (ImageView) jobPicsDiv.findViewById(R.id.theme_pic3);
            for (int i = 0; i < eachPic.length; i++) {
                picsView[i].setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(app_url+"lostPics/" + JobId + "/" + eachPic[i] + ".jpg")
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
//            case R.id.login_from_memo:
//                Intent in = new Intent(this, LoginActivity.class); startActivity(in); break;
            case 16908332:  finish(); break;
            default: break;
        }
        return true;
    }
    //为toolbar 指定menu
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.commitmemotoolbar, menu);
        return true;
    }






    public void onClick(View v){
        switch (v.getId()) {
            case R.id.theme_pic1: Intent intent=new Intent(this, PictureDetailActivity.class);
                intent.putExtra("picUrl",app_url + "lostPics/" + JobId + "/" + "1" + ".jpg"); startActivity(intent); break;

            case R.id.theme_pic2: Intent intent1=new Intent(this, PictureDetailActivity.class);
                intent1.putExtra("picUrl",app_url + "lostPics/" + JobId + "/" + "2" + ".jpg"); startActivity(intent1); break;

            case R.id.theme_pic3: Intent intent2=new Intent(this, PictureDetailActivity.class);
                intent2.putExtra("picUrl",app_url + "lostPics/" + JobId + "/" + "3" + ".jpg"); startActivity(intent2); break;

        }
    }


























}
