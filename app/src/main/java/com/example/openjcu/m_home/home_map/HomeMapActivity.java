package com.example.openjcu.m_home.home_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.openjcu.R;

public class HomeMapActivity extends AppCompatActivity {

    MapView mapView;
    BaiduMap baiduMap;
    LatLng targetAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());  //初始化地图
        setContentView(R.layout.map_home_activity);
//        getSupportActionBar().hide();
        initMap();
        testMap();
     //   controlMap();
    }

    public void initMap() {
        mapView = (MapView) findViewById(R.id.homeMapView);
        baiduMap = mapView.getMap();
        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//卫星地图
        Intent in=getIntent();
        targetAdd=new LatLng(in.getDoubleExtra("lat",0),in.getDoubleExtra("lng",0)
        );

    }

    public void controlMap() {
        //显示我的位置 点
        MyLocationData.Builder localtion = new MyLocationData.Builder();  //MyLocationData用于封装位置信息(经纬度)
        localtion.latitude(30.731);
        localtion.longitude(103.957);

        baiduMap.setMyLocationEnabled(true);
        MyLocationData myLocationData = localtion.build();   //在地图上显示位置
        baiduMap.setMyLocationData(myLocationData);

        //缩放地图
        MapStatusUpdate mSup = MapStatusUpdateFactory.zoomTo(17.5f);  //zoomBy zoomIn zoomOut
        baiduMap.animateMapStatus(mSup);
        //移动到指定的经纬度
        LatLng ll = new LatLng(30.731, 103.957);
        MapStatusUpdate upd = MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus(upd);
//        //在地图上覆盖View
//        Button button = new Button(getApplicationContext()); //创建InfoWindow展示的view
//        button.setText("Button"); button.setOnClickListener(new MapEvent()); //button.setWidth(30); button.setHeight(30);
//        LatLng pt = new LatLng(30.731, 103.957);//定义用于显示该InfoWindow的坐标点
//        InfoWindow mInfoWindow = new InfoWindow(button, pt, -30); //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//        baiduMap.showInfoWindow(mInfoWindow);   //显示InfoWindow



        //重绘地图图层
        LatLng southwest = new LatLng(30.727688, 103.952729); //定义Ground的显示地理范围
        LatLng northeast = new LatLng(30.735907, 103.9614);    //LatLng northeast = new LatLng(30.729683,103.956606);LatLng northeast = new LatLng(30.73184,103.957475);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.drawable.jcu); //定义Ground显示的图片
        OverlayOptions ooGround = new GroundOverlayOptions().positionFromBounds(bounds).image(bdGround).transparency(0.8f); //定义Ground覆盖物选项
        baiduMap.addOverlay(ooGround); //在地图中添加Ground覆盖物
        baiduMap.showMapPoi(false); //隐藏标注

    }
    public class MapEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(HomeMapActivity.this,"sssss",Toast.LENGTH_SHORT).show();
            Log.e("Map", "*****************************************");
        }
    }

    View v1;
    public void testMap() {

        //缩放地图
        MapStatusUpdate mSup = MapStatusUpdateFactory.zoomTo(19f);  //zoomBy zoomIn zoomOut
        baiduMap.animateMapStatus(mSup);
        //移动到指定的经纬度
        LatLng ll = new LatLng(30.731, 103.957);
        MapStatusUpdate upd = MapStatusUpdateFactory.newLatLng(targetAdd);
        baiduMap.animateMapStatus(upd);

//        //重绘地图图层
//        LatLng southwest = new LatLng(30.727688, 103.952729); //定义Ground的显示地理范围
//        LatLng northeast = new LatLng(30.735907, 103.9614);
//        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();
//        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.drawable.jcu); //定义Ground显示的图片
//        OverlayOptions ooGround = new GroundOverlayOptions().positionFromBounds(bounds).image(bdGround).transparency(0.8f); //定义Ground覆盖物选项
//        baiduMap.addOverlay(ooGround); //在地图中添加Ground覆盖物
//        baiduMap.showMapPoi(false); //隐藏标注



        //在地图上覆盖View :跟随地图移动、不放缩
        LayoutInflater inflater=getLayoutInflater();
        v1 = inflater.inflate(R.layout.map_info_window, null);
        LatLng pt = new LatLng(30.731, 103.957);//定义用于显示该InfoWindow的坐标点
        InfoWindow mInfoWindow = new InfoWindow(v1, targetAdd, -30); //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        baiduMap.showInfoWindow(mInfoWindow);   //显示InfoWindow
    }
    public void onClick(View view) {
        Toast.makeText(HomeMapActivity.this,"sssss",Toast.LENGTH_SHORT).show();
        Log.e("Map","*****************************************");
        switch (view.getId()) {
            case R.id.go:
                Intent in = new Intent(this, RoutePlanActivity.class);
                in.putExtra("lat", targetAdd.latitude);   in.putExtra("lng", targetAdd.longitude); startActivity(in);  break;

            default:  break;
        }
    }
}
