package com.example.openjcu.mainfragment.homefragment_toolclass;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by 晞余 on 2017/3/11.
 */

public class ParseJsonForVersion {
    //Gson解析资源版本
    String ver;   //这个仅是homeFragment's viewpagerResouce version
    ResourceVersion resourceVersion;
    public String parseJSON(String data) { //返回homeFragment's viewpagerResouce version。参数为：version的jsonString
        try {
            Gson gson = new Gson();
            resourceVersion = gson.fromJson(data, ResourceVersion.class);
            ver=resourceVersion.getFragment_home_ver();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Act", "+++++++Json解析资源版本出错");
        }
        return ver;
    }
    public String parseGoPagerVersionJSON(String data) { //返回homeFragment's viewpagerResouce version。参数为：version的jsonString
        try {
            Gson gson = new Gson();
            resourceVersion = gson.fromJson(data, ResourceVersion.class);
            ver=resourceVersion.getGo_pager_ver();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Act", "+++++++Json解析资源版本出错");
        }
        return ver;
    }
}
