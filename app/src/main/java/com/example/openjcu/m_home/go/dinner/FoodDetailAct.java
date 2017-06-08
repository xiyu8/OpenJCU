package com.example.openjcu.m_home.go.dinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.example.openjcu.R;
import com.example.openjcu.m_home.home_map.HomeMapActivity;

public class FoodDetailAct extends AppCompatActivity {

    String addCode;  //地址代码：0 是一食堂，1是 四  2是六  3是杏岛
    int intaddCode;
    LatLng[] dnr1,dnr2,dnr3,dnr4;
    LatLng ad1,ad2,ad3,ad4;
    LatLng currentAdd;
    LatLng targetAdd;  double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        addCode=getIntent().getStringExtra("index");
        currentAdd = new LatLng(30.729712, 103.956601);
//        dnr1[0]=new LatLng(30.73186,103.957486);
//        dnr2[0]=new LatLng(30.730815,103.955334);
//        dnr3[0]=new LatLng(30.73386,103.9565);
//        dnr4[0]=new LatLng(30.728035,103.954518);

        ad1=new LatLng(30.73186,103.957486);
        ad2=new LatLng(30.730815,103.955334);
        ad3=new LatLng(30.73386,103.9565);
        ad4=new LatLng(30.728035,103.954518);

        try {
        intaddCode = Integer.parseInt(addCode);

        } catch (NumberFormatException e) {
             e.printStackTrace();
             }
        switch (intaddCode) {
            case 0:    targetAdd=ad1;    break;
            case 1:    targetAdd=ad2;     break;
            case 2:    targetAdd=ad3;     break;
            case 3:    targetAdd=ad4;     break;
        }
        lat=  targetAdd.latitude; lng=  targetAdd.longitude;
    }

    public void onClick(View v) {
        Intent in = new Intent(this, HomeMapActivity.class);
        in.putExtra("lat", lat);
        in.putExtra("lng", lng);

        startActivity(in);
    }
}
