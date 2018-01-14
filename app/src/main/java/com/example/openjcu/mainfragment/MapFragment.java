package com.example.openjcu.mainfragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.example.openjcu.MainActivity;
import com.example.openjcu.R;
import com.example.openjcu.m_home.home_map.PositionGeo;
import com.example.openjcu.m_home.home_map.RoutePlanActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.openjcu.tool.Localtion.myListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    //fragment 相关方法
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String app_url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


















    MainActivity A;
    MapView mapView;
    BaiduMap mBaiduMap;
    LatLng longLL;
    @Override    //相当于在Activity的setContentView之前写的内容
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SDKInitializer.initialize(
                getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        return view;

    }




    @Override   //在Activity的onCreateView中写的在Fragment的这里写
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        A= (MainActivity) getActivity();   //在Fragment中找到活动
        init();
        initListener();
    }

    MyOnClickListener myOnClickListener;
    EditText edit_chat_item;
    Button publish_chat_item;
    Button hideChatArea;
    LinearLayout chatItemArea;
    LinearLayout chatArea;
    public void init(){
        //prepare for chat area
        app_url = A.getResources().getString(R.string.app_url);
        myOnClickListener=new MyOnClickListener();
        edit_chat_item = (EditText) A.findViewById(R.id.edit_chat_item);
        publish_chat_item = (Button) A.findViewById(R.id.publish_chat_item);
        hideChatArea = (Button) A.findViewById(R.id.hideChatArea);
        chatItemArea = (LinearLayout) A.findViewById(R.id.chatItemArea);
        chatArea = (LinearLayout) A.findViewById(R.id.chatArea);
        publish_chat_item.setOnClickListener(myOnClickListener);
        hideChatArea.setOnClickListener(myOnClickListener);


        //prepare for controlling map which is include formula route and pop window etc.
        mapView=(MapView)A.findViewById(R.id.bmapView);
        LayoutInflater inflater=A.getLayoutInflater();
        v1 = inflater.inflate(R.layout.fragment_map_pop_window, null);
        Button vvv=(Button)v1.findViewById(R.id.go);
        vvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.go:
                        Intent in = new Intent(A, RoutePlanActivity.class);
                        in.putExtra("lat", longLL.latitude);   in.putExtra("lng", longLL.longitude); startActivity(in);  break;

                    default:  break;
                }
            }
        });

        initMap();
        Toast.makeText(getActivity().getApplicationContext(),"22222222222",LENGTH_SHORT).show(); PositionGeo p=new PositionGeo();

    }

    int i=0;                //chat area click event:hidding and sendding
    public class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hideChatArea:
                    if(i%2==0) {chatArea.setVisibility(View.GONE); i++; }else{  chatArea.setVisibility(View.VISIBLE); i++; }

                    break;
                case R.id.publish_chat_item:
                    if(A.teamFragment!=null) if(((A.teamFragment).groupId)!=null) sendChatItem();
                    break;
                default: break;
            }
        }
    }

    String tmp;
    public void sendChatItem() {
        String s=edit_chat_item.getText().toString();
        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).cache(new Cache(new File(A.getExternalCacheDir(), "okhttpcache"), 1 * 1024 * 1024)).build();
        Request request = new Request.Builder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .url(app_url+"group/chat_item_insert.php?memberId="+A.teamFragment.IMME+"&chatContent="+s).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                tmp = response.body().string();
                A.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edit_chat_item.setText("");
                        Log.e("OpenJCU", tmp);
//                LayoutInflater lf = A.getLayoutInflater().from(A);

                    }
                });

            }
        });
    }































    //init map condition
    public void initMap(){
        mBaiduMap=mapView.getMap();
       // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//卫星地图

        //缩放地图
        MapStatusUpdate mSup = MapStatusUpdateFactory.zoomTo(19f);  //zoomBy zoomIn zoomOut
        mBaiduMap.animateMapStatus(mSup);
        //移动到指定的经纬度
        LatLng ll = new LatLng(30.731, 103.957);
        MapStatusUpdate upd = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(upd);
    }

    public void controlmap(){

    }







    View v1;  //mapPopWindow
    // /MapView 是地图主控件
    private MapView mMapView;
    //当前地点击点
    private LatLng currentPt;
    //控制按钮
    private Button zoomButton;
    private Button rotateButton;
    private Button overlookButton;
    private Button saveScreenButton;

    private String touchType;

    //用于显示地图状态的面板
    private TextView mStateBar;
    InfoWindow mInfoWindow;
    //设置地图的各种事件
    private void initListener() {

        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {

            }
        });


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 单击地图
             */
            public void onMapClick(LatLng point) {
//                touchType = "单击地图";
//                currentPt = point;
              //  updateMapState();
                mBaiduMap.hideInfoWindow();
            }

            /**
             * 单击地图中的POI点
             */
            public boolean onMapPoiClick(MapPoi poi) {
//                touchType = "单击POI点";
//                currentPt = poi.getPosition();
              //  updateMapState();
                return false;
            }
        });
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            /**
             * 长按地图
             */
            public void onMapLongClick(LatLng point) {
                touchType = "长按";
                currentPt = point;
                longLL=point;
              //  updateMapState();
                //在地图上覆盖View :跟随地图移动、不放缩


                mInfoWindow = new InfoWindow(v1, point, -30); //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                mBaiduMap.showInfoWindow(mInfoWindow);   //显示InfoWindow

            }
        });
        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            /**
             * 双击地图
             */
            public void onMapDoubleClick(LatLng point) {
//                touchType = "双击";
//                currentPt = point;
              //  updateMapState();
            }
        });

        /**
         * 地图状态发生变化
         */
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
               // updateMapState();
            }

            public void onMapStatusChangeFinish(MapStatus status) {
               // updateMapState();
            }

            public void onMapStatusChange(MapStatus status) {
              //  updateMapState();
            }
        });
//        zoomButton = (Button) findViewById(R.id.zoombutton);
//        rotateButton = (Button) findViewById(R.id.rotatebutton);
//        overlookButton = (Button) findViewById(R.id.overlookbutton);
//        saveScreenButton = (Button) findViewById(R.id.savescreen);
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (view.equals(zoomButton)) {
//                    perfomZoom();
//                } else if (view.equals(rotateButton)) {
//                    perfomRotate();
//                } else if (view.equals(overlookButton)) {
//                    perfomOverlook();
//                } else if (view.equals(saveScreenButton)) {
//                    // 截图，在SnapshotReadyCallback中保存图片到 sd 卡
//                    mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
//                        public void onSnapshotReady(Bitmap snapshot) {
//                            File file = new File("/mnt/sdcard/test.png");
//                            FileOutputStream out;
//                            try {
//                                out = new FileOutputStream(file);
//                                if (snapshot.compress(
//                                        Bitmap.CompressFormat.PNG, 100, out)) {
//                                    out.flush();
//                                    out.close();
//                                }
//                                Toast.makeText(A,
//                                        "屏幕截图成功，图片存在: " + file.toString(),
//                                        Toast.LENGTH_SHORT).show();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    Toast.makeText(A, "正在截取屏幕图片...",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//              //  updateMapState();
//            }
//
//        };
//        zoomButton.setOnClickListener(onClickListener);
//        rotateButton.setOnClickListener(onClickListener);
//        overlookButton.setOnClickListener(onClickListener);
//        saveScreenButton.setOnClickListener(onClickListener);
    }

    /**
     * 处理缩放 sdk 缩放级别范围： [3.0,19.0]
     */
    private void perfomZoom() {
        //EditText t = (EditText) findViewById(R.id.zoomlevel);
        try {
            float zoomLevel = 17; //Float.parseFloat(t.getText().toString());
            MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(zoomLevel);
            mBaiduMap.animateMapStatus(u);
        } catch (NumberFormatException e) {
            Toast.makeText(A, "请输入正确的缩放级别", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理旋转 旋转角范围： -180 ~ 180 , 单位：度 逆时针旋转
     */
    private void perfomRotate() {
     //   EditText t = (EditText) findViewById(R.id.rotateangle);
        try {
            int rotateAngle = 4; //Integer.parseInt(t.getText().toString());
            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).rotate(rotateAngle).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u);
        } catch (NumberFormatException e) {
            Toast.makeText(A, "请输入正确的旋转角度", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理俯视 俯角范围： -45 ~ 0 , 单位： 度
     */
    private void perfomOverlook() {
       // EditText t = (EditText) findViewById(R.id.overlookangle);
        try {
            int overlookAngle = 40; //Integer.parseInt(t.getText().toString());
            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(overlookAngle).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u);
        } catch (NumberFormatException e) {
            Toast.makeText(A, "请输入正确的俯角", Toast.LENGTH_SHORT).show();
        }
    }



//    更新地图状态显示面板
//    private void updateMapState() {
//        if (mStateBar == null) {
//            return;
//        }
//        String state = "";
//        if (currentPt == null) {
//            state = "点击、长按、双击地图以获取经纬度和地图状态";
//        } else {
//            state = String.format(touchType + ",当前经度： %f 当前纬度：%f",
//                    currentPt.longitude, currentPt.latitude);
//        }
//        state += "\n";
//        MapStatus ms = mBaiduMap.getMapStatus();
//        state += String.format(
//                "zoom=%.1f rotate=%d overlook=%d",
//                ms.zoom, (int) ms.rotate, (int) ms.overlook);
//        mStateBar.setText(state);
//    }






















    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {        //核心方法，避免因Fragment跳转导致地图崩溃
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            // if this view is visible to user, start to request user location
            startRequestLocation();
        } else if (isVisibleToUser == false) {
            // if this view is not visible to user, stop to request user
            // location
            stopRequestLocation();
        }
    }
    private void stopRequestLocation() {
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
            mLocClient.stop();
        }
    }

    long startTime;
    long costTime;

    private void startRequestLocation() {
        // this nullpoint check is necessary
        if (mLocClient != null) {
            mLocClient.registerLocationListener(myListener);
            mLocClient.start();
            mLocClient.requestLocation();
            startTime = System.currentTimeMillis();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Bug:从点了mapfragment再点下一个活动，崩溃
    //    mMapView.onSaveInstanceState(outState);

    }


    LocationClient mLocClient;




    //fragment关联map生面周期
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
