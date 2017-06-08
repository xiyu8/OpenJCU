package com.example.openjcu.m_home.home_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.openjcu.R;
import com.baidu.mapapi.search.geocode.GeoCodeOption;

import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;

/**
 * Created by 晞余 on 2017/3/1.
 */

public class PositionGeo {

     public String positionReGeo(LatLng ll){
        final String[] s = {null};
        //第一步，创建地理编码检索实例；
        GeoCoder mSearch = GeoCoder.newInstance();
        //第二步，创建地理编码检索监听者；
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果:得到LatLng
                double ln=result.getLocation().latitude; double lt=result.getLocation().longitude;
                LatLng ll=result.getLocation();
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }
                //获取反向地理编码结果：得到String
                String add=result.getAddress();  s[0] =add;
            }
        };
        //第三步，设置地理编码检索监听者；
        mSearch.setOnGetGeoCodeResultListener(listener);
        //第四步，发起地理编码检索、反向地理编码检索；
  //      mSearch.geocode(new GeoCodeOption().city("北京").address("海淀区上地十街10号"));
  //      LatLng ptCenter = new LatLng(21.344,21.344);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        //第五步，释放地理编码检索实例；
   //     mSearch.destroy();

    return s[0];
    }

     public LatLng positionGeo(String add){
        final LatLng[] ll = new LatLng[1];
        //第一步，创建地理编码检索实例；
        GeoCoder mSearch = GeoCoder.newInstance();
        //第二步，创建地理编码检索监听者；
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果:得到LatLng
                double ln=result.getLocation().latitude; double lt=result.getLocation().longitude;
                ll[0] =result.getLocation();
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }
                //获取反向地理编码结果：得到String
                String add=result.getAddress();
            }
        };
        //第三步，设置地理编码检索监听者；
        mSearch.setOnGetGeoCodeResultListener(listener);
        //第四步，发起地理编码检索、反向地理编码检索；
        mSearch.geocode(new GeoCodeOption().city("成都").address(add));
 //       LatLng ptCenter = new LatLng(21.344,21.344);
 //       mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
        //第五步，释放地理编码检索实例；
  //      mSearch.destroy();

        return ll[0];
    }

}
