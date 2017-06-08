package com.example.openjcu.mainfragment.homefragment_toolclass;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by 晞余 on 2017/3/11.
 */

public class ParseJsonForResource {
        //Gson解析pager string资源
    ViewPagerTitleAndUrl viewPagerTitleAndUrl;
    String[] resource=new String[10];  //前5个为pager的title，后五个为pager的url
    public String[] parseStringJSON(String data) {  //返回字符串数组：前5个为pager的title，后五个为pager的url。参数:resource的jsonString
        try {
            Gson gson = new Gson();
            viewPagerTitleAndUrl = gson.fromJson(data, ViewPagerTitleAndUrl.class);
            resource[0]=viewPagerTitleAndUrl.home_pager1_string;
            resource[1]=viewPagerTitleAndUrl.home_pager2_string;
            resource[2]=viewPagerTitleAndUrl.home_pager3_string;
            resource[3]=viewPagerTitleAndUrl.home_pager4_string;
            resource[4]=viewPagerTitleAndUrl.home_pager5_string;
            resource[5]=viewPagerTitleAndUrl.getHome_pager1_url;
            resource[6]=viewPagerTitleAndUrl.getHome_pager2_url;
            resource[7]=viewPagerTitleAndUrl.getHome_pager3_url;
            resource[8]=viewPagerTitleAndUrl.getHome_pager4_url;
            resource[9]=viewPagerTitleAndUrl.getHome_pager5_url;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Act", "+++++++Json解析Pager_String资源出错");
        }
        return resource;
    }
}
