package com.example.openjcu;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.example.openjcu.Base.BaseActivity;
import com.example.openjcu.login.LoginActivity;
import com.example.openjcu.login.UserDetailActivity;
import com.example.openjcu.m_home.home_map.PositionGeo;
import com.example.openjcu.mainfragment.HomeFragment;
import com.example.openjcu.mainfragment.MapFragment;
import com.example.openjcu.mainfragment.TeamFragment;
import com.example.openjcu.mainfragment.group_service.BeepService;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.openjcu.tool.NetRequest.getLogin;
import static com.example.openjcu.tool.NetRequest.getUserId;
import static com.example.openjcu.tool.NetRequest.getUsername;
import static com.example.openjcu.tool.NetRequest.getPwd;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.openjcu.tool.NetRequest.goHome;


public class MainActivity extends BaseActivity implements View.OnClickListener,TeamFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener{

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        // 第一次启动时选中第0个tab

        initViews();
        setTabSelection(0);
    }


    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    public DrawerLayout mDrawerLayout;
    public TextView drawername;
    public Button drawerlogin;
    public ImageView userMainIcon;
    private void initViews() {

        home = findViewById(R.id.message_layout);
        map = findViewById(R.id.contacts_layout);
        team = findViewById(R.id.team);

        homeIcon = (ImageView) findViewById(R.id.message_image);
        mapIcon = (ImageView) findViewById(R.id.contacts_image);
        teamIcon=(ImageView) findViewById(R.id.team_image);
        homeText = (TextView) findViewById(R.id.message_text);
        mapText = (TextView) findViewById(R.id.contacts_text);
        teamText = (TextView) findViewById(R.id.team_text);
        home.setOnClickListener(this);
        map.setOnClickListener(this);
        team.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawername = (TextView) findViewById(R.id.drawername);
        drawerlogin = (Button) findViewById(R.id.login);
        userMainIcon = (ImageView) findViewById(R.id.userMainIcon);
        Glide.with(this).load(R.drawable.bgi)
                .bitmapTransform(new CropCircleTransformation(this))
//                .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                .into((ImageView) findViewById(R.id.userMainIcon));
        if(getLogin()) {
            Glide.with(this)
                    .load("http://192.168.155.1/www/OpenJCU/userIcon/" + getUserId()+".jpg")
                   // .override(100, 100) //图片大小
                    //.centerCrop()   //等比缩放
                    .bitmapTransform(new CropCircleTransformation(this))
                    .placeholder(R.drawable.bgi1) //
                    .error(R.drawable.bgi1)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userMainIcon);
            userMainIcon.setClickable(true);

        }else {
            userMainIcon.setClickable(false);
        }

        setTabSelection(1);
        setTabSelection(2);


        Intent in = getIntent();
        String ln=in.getStringExtra("lauchFromNoti");

        Log.e("OpenJCU", "GGGGGGGGGGGG"+ln);
        if (ln!=null) {
            teamFragment.requestForJoinGroup(in.getStringExtra("groupName"),in.getStringExtra("IMME"),"30.733399,103.955637");
        }

        //z轴阴影
//        ViewOutlineProvider viewOutlineProvider=new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                int size=10;
//                outline.setOval(0,0,size,size);
//            }
//        };
//
//        homeIcon.setOutlineProvider(viewOutlineProvider);
//        int shapeSize = 10;
//
///* Create a circular outline. */
//        Outline mOutlineCircle = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            mOutlineCircle = new Outline();
//
//        mOutlineCircle.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize / 2);
//
///* Create a rectangular outline. */
//        Outline mOutlineRect = new Outline();
//        mOutlineRect.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize / 10);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_layout:  setTabSelection(0);  break;
            case R.id.contacts_layout:  setTabSelection(1);  break;
            case R.id.finish: finish();  break;
            case R.id.login:
                Intent in = new Intent(this, LoginActivity.class);
                startActivityForResult(in,1);
//                startActivity(in);
                break;
            case R.id.userMainIcon:
                Intent in1 = new Intent(this, UserDetailActivity.class);
                startActivityForResult(in1,2); break;
            case R.id.team: setTabSelection(2);  break;
            default: break;
        }
    }
/****************************************************************************************************************/
    private FragmentManager fragmentManager;
    public HomeFragment homeFragment;
    public MapFragment mapFragment;
    public TeamFragment teamFragment;

    private View home;
    private View map;
    private View team;
    private ImageView homeIcon;
    private ImageView mapIcon;
    private ImageView teamIcon;
    private TextView homeText;
    private TextView mapText;
    private TextView teamText;

    /**
     * 根据传入的index参数来设置选中的tab页。
     * @param index
     *            每个tab页对应的下标。，1，2，3。
     */
    public void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态

        Log.e("OpenJCU", "$$$$$$$$$$$$$"+getLogin()+getUsername()+getPwd());
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                homeIcon.setImageResource(R.drawable.home_ac1);
                homeText.setTextColor(Color.parseColor("#47c6fc"));
                if (homeFragment == null) {
                    // 如果homeFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content,homeFragment);
                } else {
                    // 如果homeFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                mapIcon.setImageResource(R.drawable.map_ac1);
                mapText.setTextColor(Color.parseColor("#47c6fc"));
                if (mapFragment == null) {
                    // 如果mapFragment为空，则创建一个并添加到界面上
                    mapFragment = new MapFragment();
                    transaction.add(R.id.content, mapFragment);
                } else {
                    // 如果mapFragment不为空，则直接将它显示出来
                    transaction.show(mapFragment);
                }
                break;
            case 2:
                teamIcon.setImageResource(R.drawable.team_ac1);
                teamText.setTextColor(Color.parseColor("#47c6fc"));
                if (teamFragment == null) {
                    teamFragment = new TeamFragment();
                    transaction.add(R.id.content, teamFragment);
                } else {
                    transaction.show(teamFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        homeIcon.setImageResource(R.drawable.home_b1);
        homeText.setTextColor(Color.parseColor("#82858b"));
        mapIcon.setImageResource(R.drawable.map_b1);
        mapText.setTextColor(Color.parseColor("#82858b"));
        teamIcon.setImageResource(R.drawable.team_b1);
        teamText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        if (teamFragment!=null){
            transaction.hide(teamFragment);
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) { }
/****************************************************************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1: //login
                if(resultCode==RESULT_OK){
                    String da=data.getStringExtra("name");
                    drawername.setText(da);
                    Glide.with(this)
                            .load("http://192.168.155.1/www/OpenJCU/userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis())
                            //.override(100, 100) //图片大小
                            .centerCrop()   //等比缩放
                            //.bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                            .bitmapTransform(new CropCircleTransformation(this))
                            .placeholder(R.drawable.bgi1) //
                            .error(R.drawable.bgi1)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(userMainIcon);
                    drawerlogin.setText("切换账号");
                }else{
                }

            userMainIcon.setClickable(true);
                break;
            case 2: //detail
                if(requestCode==RESULT_OK) {
                    if (getLogin()) {
                        Glide.with(MainActivity.this)
                                .load("http://192.168.155.1/www/OpenJCU/userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis())
                                //.override(100, 100) //图片大小
                                .centerCrop()   //等比缩放
                                //.bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                                .bitmapTransform(new CropCircleTransformation(this))
                                .placeholder(R.drawable.bgi1) //
                                .error(R.drawable.bgi1)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(userMainIcon);

                        Glide.with(MainActivity.this)
                                .load("http://192.168.155.1/www/OpenJCU/userIcon/" + getUserId() + ".jpg"+"?"+System.currentTimeMillis())
                                .placeholder(R.drawable.bgi1)
                                .error(R.drawable.bgi1)
                                .into(userMainIcon);
                    }
                }
                break;
            default: break;
        }
//        if(getLogin()) {
//            Glide.with(this)
//                    .load("http://192.168.155.1/www/OpenJCU/userIcon/" + getUserId()+".jpg")
//                    .override(100, 100) //图片大小
//                    .centerCrop()   //等比缩放
//                    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
//                    .placeholder(R.drawable.bgi1) //
//                    .error(R.drawable.bgi1)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(userMainIcon);
//            userMainIcon.setClickable(true);
//
//        }else {
//            userMainIcon.setClickable(false);
//        }


        Glide.with(MainActivity.this)
                .load("http://192.168.155.1/www/OpenJCU/userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis())
                //.override(100, 100) //图片大小
                .centerCrop()   //等比缩放
                //.bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                .bitmapTransform(new CropCircleTransformation(this))
                .placeholder(R.drawable.bgi1) //
                .error(R.drawable.bgi1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(userMainIcon);

    }


    @Override
    public void onBackPressed() {
        goHome(MainActivity.this);

    }

//    public ServiceConnection connection;
//
//    public BeepService.MyBinder bindIntent=null;
    @Override
    protected void onDestroy() {
//        if(teamFragment!=null) {
//            if (teamFragment.connection != null) {
//                unbindService(teamFragment.connection);
//            }
//            if (teamFragment.bindIntent != null) {
//                stopService(teamFragment.bindIntent);
//            }
//        }
        super.onDestroy();
    }
}
