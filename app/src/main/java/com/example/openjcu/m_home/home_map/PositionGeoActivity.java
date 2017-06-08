package com.example.openjcu.m_home.home_map;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.openjcu.R;
import com.baidu.mapapi.search.geocode.GeoCodeOption;

import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.example.openjcu.tool.NetRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.openjcu.tool.NetRequest.isOnline;
import static java.lang.Thread.sleep;


public class PositionGeoActivity extends AppCompatActivity {

    TextView showjson;
    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_geo);
//        geoCeder();   //地理位置编码相关
     //   init();
     //   sendRequestWithOkhttp();  //网络请求相关
 //       ss();
    //    requestLogic();
     //   requestImage();
//        //定位相关
//        location();
//        initLocation();
//        mLocationClient.start();


    }

//
//    public void init() {
//        showjson = (TextView) findViewById(R.id.showjson);
//        im = (ImageView) findViewById(R.id.imageView2);
//
//    }
//
//    /***********************************网络请求**********************************************/
//
//    public void requestLogic() {
//        //是否有网络连接
//        if (isOnline(PositionGeoActivity.this)) {  //如果有
//
//            // 获取SharedPreferences中的OpenJCU文件的Editor
//            SharedPreferences preferences = getSharedPreferences("OpenJCU", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//
//            // 看fragment_home_ver字段中是否有值、有什么值    //读取fragment_home_ver字段，如果没有这个字段，创建，并给这个字段赋值0.00
//            String haveFragment_home_ver = preferences.getString("fragment_home_ver", "0.00");
//
//            // 如果没有这个字段的值
//            if (haveFragment_home_ver.equals("0.00")) {  //请求版本，更新本地版本，请求资源，更新呈现资源
//                request_ParseJson();  while (s1==null) {   } //时间不够会返回null
//                editor.putString("fragment_home_ver",s1);  editor.apply();//更新本地版本；
//                requestImageAndUpdateUI();
//                //requestVertion\如果没有这个字段的值，直接请求网络的数据，并写入值的信息作为本地版本号
//            } else {  //1请求版本，比较版本信息，2请求版本，比较版本信息，更新本地版本，请求资源，更新呈现资源
//                //如果有这个字段的值，请求网络，比较这个值和网络请求下来的版本信息
//                request_ParseJson();    while (s1==null) {   }
//                if(s1.equals(haveFragment_home_ver))
//                    requestCacheImageAndUpdateUI();
//                else {
//                    editor.putString("fragment_home_ver",s1);  editor.apply();  //更新本地版本；
//                    requestImageAndUpdateUI();
//                }
//
//            }
//        } else {        //如果没有
//            Toast.makeText(this, "请连接网络", Toast.LENGTH_SHORT).show();
//            requestCacheImageAndUpdateUI();
//        }
//    }
//
//
//
//    String s1;  //用来存解析Json的结果
//    //rrequest for json
//    public String request_ParseJson() {
//        NetRequest.sendRequest("http://192.168.155.1/www/OpenJCU/update.json",new okhttp3.Callback(){
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                s1 =parseJSON(response.body().string());
//            }
//        },PositionGeoActivity.this);
//        return s1;
//    }   //static工具发请求
//
//
////request for image
//    public void requestImageAndUpdateUI() {
//
//        NetRequest.sendRequest("http://192.168.155.1/www/OpenJCU/p1.png",new okhttp3.Callback(){
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final byte[] Picture_bt = response.body().bytes();
//                //通过handler更新UI
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        //使用BitmapFactory工厂，把字节数组转化为bitmap
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
//                        //通过imageview，设置图片
//                        im.setImageBitmap(bitmap);
//
//                    }
//                });
//
//            }
//        },PositionGeoActivity.this);
//    }
//
//    //request cache for image
//    public void requestCacheImageAndUpdateUI() {
//
//        NetRequest.sendForceCacheRquest("http://192.168.155.1/www/OpenJCU/p1.png",new okhttp3.Callback(){
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final byte[] Picture_bt = response.body().bytes();
//                //通过handler更新UI
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        //使用BitmapFactory工厂，把字节数组转化为bitmap
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
//                        //通过imageview，设置图片
//                        im.setImageBitmap(bitmap);
//
//                    }
//                });
//
//            }
//        },PositionGeoActivity.this);
//    }
//
//    public String parseJSON(String data) {
//        final FragmentHome fg;
//        String ver = null;
//        try {
//            Gson gson = new Gson();
////            List<FragmentHome> gonList = gson.fromJson(data, new TypeToken<List<FragmentHome>>(){}.getType()); //解析Json数组
////            for(FragmentHome fg : gonList){
////            Log.e("Act","***********"+fg.getVersion());}
//            fg = gson.fromJson(data, FragmentHome.class);
//            ver=fg.getFragment_home_ver();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    showjson.setText("" + fg.getFragment_home_ver());
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Act", "+++++");
//        }
//        return ver;
//    }
//
//    class FragmentHome {
//        public String fragment_home_ver;
//        public String go_pager_ver;
//        public String go_list_ver1;
//        public String go_list_ver2;
//        public String go_list_ver3;
//
//        public String getFragment_home_ver() {
//            return fragment_home_ver;
//        }
//
//        public void setFragment_home_ver(String fragment_home_ver) {
//            this.fragment_home_ver = fragment_home_ver;
//        }
//
//        public String getGo_pager_ver() {
//            return go_pager_ver;
//        }
//
//        public void setGo_pager_ver(String go_pager_ver) {
//            this.go_pager_ver = go_pager_ver;
//        }
//
//        public String getGo_list_ver1() {
//            return go_list_ver1;
//        }
//
//        public void setGo_list_ver1(String go_list_ver1) {
//            this.go_list_ver1 = go_list_ver1;
//        }
//
//        public String getGo_list_ver2() {
//            return go_list_ver2;
//        }
//
//        public void setGo_list_ver2(String go_list_ver2) {
//            this.go_list_ver2 = go_list_ver2;
//        }
//
//        public String getGo_list_ver3() {
//            return go_list_ver3;
//        }
//
//        public void setGo_list_ver3(String go_list_ver3) {
//            this.go_list_ver3 = go_list_ver3;
//        }
//
//        public String getGo_list_ver4() {
//            return go_list_ver4;
//        }
//
//        public void setGo_list_ver4(String go_list_ver4) {
//            this.go_list_ver4 = go_list_ver4;
//        }
//
//        public String go_list_ver4;
//
//    }  //用来存Json解析出来的数据
//    /*****************************************************************************************/
//
//
//    /***********************************地理编码**********************************************/
//    public void geoCeder() {
//
//        //第一步，创建地理编码检索实例；
//        GeoCoder mSearch = GeoCoder.newInstance();
//        //第二步，创建地理编码检索监听者；
//        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
//            public void onGetGeoCodeResult(GeoCodeResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    //没有检索到结果
//                }
//                //获取地理编码结果:得到LatLng
//                double ln = result.getLocation().latitude;
//                double lt = result.getLocation().longitude;
//                LatLng ll = result.getLocation();
//            }
//
//            @Override
//            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    //没有找到检索结果
//                }
//                //获取反向地理编码结果：得到String
//                String add = result.getAddress();
//                Toast.makeText(PositionGeoActivity.this, add, Toast.LENGTH_SHORT).show();
//            }
//        };
//        //第三步，设置地理编码检索监听者；
//        mSearch.setOnGetGeoCodeResultListener(listener);
//        //第四步，发起地理编码检索、反向地理编码检索；
//        //   mSearch.geocode(new GeoCodeOption().city("北京").address("海淀区上地十街10号"));
//        LatLng ptCenter = new LatLng(30.73184, 103.957475);
//        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
//        //第五步，释放地理编码检索实例；
//        //     mSearch.destroy();
//    }
//    /*****************************************************************************************/
//
//
//    /***********************************定位**********************************************/
//    public LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();
//
//    public void location() {
//
//        mLocationClient = new LocationClient(getApplicationContext());
//        //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);
//        //注册监听函数
//    }
//
//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//
//        option.setCoorType("bd09ll");
//        //可选，默认gcj02，设置返回的定位结果坐标系
//
////        int span=1000;
////        option.setScanSpan(span);
////        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//
//        option.setIsNeedAddress(true);
//        //可选，设置是否需要地址信息，默认不需要
//
//        option.setOpenGps(true);
//        //可选，默认false,设置是否使用gps
//
//        option.setLocationNotify(true);
//        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//
//        option.setIsNeedLocationDescribe(true);
//        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//
//        option.setIsNeedLocationPoiList(true);
//        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//
//        option.setIgnoreKillProcess(false);
//        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//
//        option.SetIgnoreCacheException(false);
//        //可选，默认false，设置是否收集CRASH信息，默认收集
//
//        option.setEnableSimulateGps(false);
//        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
//
//        mLocationClient.setLocOption(option);
//    }
//
//    /*********************************************************************************/
//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//
//            //获取定位结果
//            StringBuffer sb = new StringBuffer(256);
//
//            sb.append("time : ");
//            sb.append(location.getTime());    //获取定位时间
//
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());    //获取类型类型
//
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());    //获取纬度信息
//
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());    //获取经度信息
//
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());    ///获取定位精准度
//
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//
//                // GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());    // 单位：公里每小时
//
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());    //获取卫星数
//
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());    //获取海拔高度信息，单位米
//
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());    //获取方向信息，单位度
//
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());    //获取地址信息
//
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//
//                // 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());    //获取地址信息
//
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());    //获取运营商信息
//
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
//
//                // 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//
//            }
//
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());    //位置语义化信息
//
//            List<Poi> list = location.getPoiList();    // POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
//
//            Log.i("BaiduLocationApiDem", sb.toString());
//        }
//    }









//    public void sendRequestWithOkhttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder().url("http://192.168.155.1/www/OpenJCU/update.json").build();
//                    Response response = client.newCall(request).execute();
//                    String data = response.body().string();
//                    Log.e("Act", "####");
//                    parseJSON(data);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }   //  本地类请求




//
//    /***********************************网络请求**********************************************/
//    public void sendRequestWithOkhttp() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder().url("http://192.168.155.1/www/OpenJCU/update.json").build();
//                    Response response = client.newCall(request).execute();
//                    String data = response.body().string();
//                    Log.e("Act", "####");
//                    parseJSON(data);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }   //
//
//    public void requestLogic() {
//
//        // 获取SharedPreferences中的fragment_home_ver字段
//        SharedPreferences preferences = getSharedPreferences("OpenJCU", MODE_PRIVATE);
//        SharedPreferences.Editor editor=preferences.edit();
//
//        // 看fragment_home_ver字段中是否有值    //读取fragment_home_ver字段，如果没有这个字段，创建，并给这个字段赋值0.00
//        String  haveFragment_home_ver= preferences.getString("fragment_home_ver", "0.00");
//
//        // 如果没有这个字段的值
//        if (haveFragment_home_ver.equals("0.00")) {  //请求版本，更新本地版本，请求资源，更新呈现资源
//            editor.putString("fragment_home_ver", "1.00"); editor.apply();
//            //requestVertion\如果没有这个字段的值，直接请求网络的数据，并写入值的信息作为本地版本号
//        } else {  //1请求版本，比较版本信息，2请求版本，比较版本信息，更新本地版本，请求资源，更新呈现资源
//            //如果有这个字段的值，请求网络，比较这个值和网络请求下来的版本信息
//        }
//
//        Log.e("Act", haveFragment_home_ver);
//    }
//
//    public void ss() {
//        NetRequest.sendRquest("http://192.168.155.1/www/OpenJCU/update.json",new okhttp3.Callback(){
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                parseJSON(response.body().string());
//            }
//        });
//    };   //static工具发请求
//
//    public void parseJSON(String data) {
//        try {
//            Gson gson = new Gson();
//            Log.e("Act", "+++++" + data);
////            List<FragmentHome> gonList = gson.fromJson(data, new TypeToken<List<FragmentHome>>(){}.getType());
////            Log.e("Act","+++++"+gonList.size());
////            for(FragmentHome fg : gonList){
////            Log.e("Act","***********"+fg.getVersion());}
//            final FragmentHome fg = gson.fromJson(data, FragmentHome.class);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    showjson.setText("" + fg.getFragment_home_ver());
//                }
//            });
//            Log.e("Act", "+++++" + fg.getFragment_home_ver());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Act", "+++++");
//        }
//    }
//
//    class FragmentHome {
//        public String fragment_home_ver;
//        public String go_pager_ver;
//        public String go_list_ver1;
//        public String go_list_ver2;
//        public String go_list_ver3;
//
//        public String getFragment_home_ver() {
//            return fragment_home_ver;
//        }
//
//        public void setFragment_home_ver(String fragment_home_ver) {
//            this.fragment_home_ver = fragment_home_ver;
//        }
//
//        public String getGo_pager_ver() {
//            return go_pager_ver;
//        }
//
//        public void setGo_pager_ver(String go_pager_ver) {
//            this.go_pager_ver = go_pager_ver;
//        }
//
//        public String getGo_list_ver1() {
//            return go_list_ver1;
//        }
//
//        public void setGo_list_ver1(String go_list_ver1) {
//            this.go_list_ver1 = go_list_ver1;
//        }
//
//        public String getGo_list_ver2() {
//            return go_list_ver2;
//        }
//
//        public void setGo_list_ver2(String go_list_ver2) {
//            this.go_list_ver2 = go_list_ver2;
//        }
//
//        public String getGo_list_ver3() {
//            return go_list_ver3;
//        }
//
//        public void setGo_list_ver3(String go_list_ver3) {
//            this.go_list_ver3 = go_list_ver3;
//        }
//
//        public String getGo_list_ver4() {
//            return go_list_ver4;
//        }
//
//        public void setGo_list_ver4(String go_list_ver4) {
//            this.go_list_ver4 = go_list_ver4;
//        }
//
//        public String go_list_ver4;
//
//    }  //用来存Json解析出来的数据
//    /*****************************************************************************************/





}


