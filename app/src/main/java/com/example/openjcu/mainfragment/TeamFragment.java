package com.example.openjcu.mainfragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.openjcu.MainActivity;
import com.example.openjcu.R;
import com.example.openjcu.mainfragment.group_service.BeepService;
import com.example.openjcu.mainfragment.gson_bean.ChatBean;
import com.example.openjcu.mainfragment.gson_bean.GroupPosition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.openjcu.tool.NetRequest.getDeviceIMEI;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false);
    }









































    MainActivity A;
    String app_url;
    @Override   //在Activity的onCreateView中写的在Fragment的这里写
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        A= (MainActivity) getActivity();   //在Fragment中找到活动
        app_url = A.getResources().getString(R.string.app_url);
        init();

    }

    TextView joinGroup,creatGroup;
    LinearLayout aboutGroupArea;
    public Intent bindIntent;
    OkHttpClient client = new OkHttpClient();
    LinearLayout groupArea;
    LinearLayout groupMembers;
    Button exitOrRemove,groupInMap;
    MyOnClickListener myOnClickListener;
    String IMME;
    public void init() {
        ////////////////get mapfragment in this fragment(Teamfragment)////////////////

        MapFragment mapFragment=A.mapFragment;
        baiduMap=mapFragment.mBaiduMap;
        baiduMap.setOnMarkerClickListener(new MyOnMarkerClickListener());
        /////////////////////////////

        IMME = getDeviceIMEI(A);

        Log.e("OpenJCU","LLLLLLLLLLLL"+IMME);
        groupArea = (LinearLayout) A.findViewById(R.id.groupArea);
        joinGroup = (TextView) A.findViewById(R.id.joinGroup);
        creatGroup = (TextView) A.findViewById(R.id.creatGroup);
        aboutGroupArea = (LinearLayout) A.findViewById(R.id.aboutGroupArea);

        // for team info inflater
        LayoutInflater lf = A.getLayoutInflater().from(A);
        ly=(LinearLayout) lf.inflate(R.layout.group_item,null);
        groupMembers = (LinearLayout) ly.findViewById(R.id.group_members);
        exitOrRemove = (Button) ly.findViewById(R.id.exitOrRemove);
        groupInMap = (Button) ly.findViewById(R.id.groupInMap);
        //for Teamfragment's views
        myOnClickListener=new MyOnClickListener();
        joinGroup.setOnClickListener(myOnClickListener);
        creatGroup.setOnClickListener(myOnClickListener);
        exitOrRemove.setOnClickListener(myOnClickListener);
        groupInMap.setOnClickListener(myOnClickListener);
//        while (downloadBinder==null){ }
//        Log.e("OpenJCU","HHHHHHHHHHHHHHHHHHHHHHH"+ downloadBinder.getPositionString());

    }

    ProgressDialog progressDialog;
    public class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.joinGroup:
                    progressDialog=new ProgressDialog(A);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    firstBeep = true;
                    requestForJoinGroup((((EditText)A.findViewById(R.id.group_name)).getText()).toString(),IMME,"30.733399,103.955637");break;
                case R.id.creatGroup:
                    progressDialog=new ProgressDialog(A);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    firstBeep = true;
                    requestForCreatGroup(IMME,"30.733399,103.955637");break;
                case R.id.groupInMap:
                    A.setTabSelection(1); break;
                case R.id.exitOrRemove:

                    progressDialog=new ProgressDialog(A);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if (groupCategory) requestForDeleteGroup(groupId); else  requestForQuitGroup(groupId,IMME);
                    break;
                default:break;
            }
        }
    }

    String groupId;
    public void requestForCreatGroup(String IMME,String ownerPosition){
        groupCategory=true;
        Request request = new Request.Builder().url(app_url+"group/creat_group.php?IMME="+IMME+"&ownerPosition="+ownerPosition).build();
        client.newCall(request).enqueue( new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (responseText.contains("出错")) {//出错
                    progressDialog.dismiss();
                    return;
                }
                groupId = responseText;
                A.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processUI(1);
                        // 处理UI，添加一个新的小分队，显示小分队口令，开始心跳包：显示成员，显示成员位置
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void requestForDeleteGroup(String groupName){
//        String ss=downloadBinder.requestForDeleteGroup(groupName);
        Request request = new Request.Builder().url(app_url+"group/delete_group.php?groupName="+groupName).build();
        client.newCall(request).enqueue( new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (!responseText.contains("OK")) {//出错
                    A.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    return;
                }
                A.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processUI(2);
                        // 处理变量:positions,marks,定时器,beepService
                        // 处理UI，删除一个新的小分队
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

    }
    public void requestForJoinGroup(String groupName,String IMME,String position){
        groupId = groupName;
        groupCategory=false;
        Request request = new Request.Builder().url(app_url+"group/join_group.php?IMME="+IMME+"&position="+position+"&groupName="+groupName).build();
        client.newCall(request).enqueue( new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (responseText.contains("小分队不存在")) {
                    return;
                }
                A.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processUI(1);
                        // 处理UI，添加一个新的小分队，显示小分队口令，开始心跳包：显示成员，显示成员位置
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

    }
    public void requestForQuitGroup(String groupName,String IMME){
//        String ss=downloadBinder.requestForQuitGroup(groupName,IMME);
        Request request = new Request.Builder().url(app_url+"group/qiut_group.php?groupName="+groupName+"&IMME="+IMME).build();
        client.newCall(request).enqueue( new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (!responseText.contains("退出成功")) {//出错
                    A.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    return;
                }
                A.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processUI(2);
                        // 处理变量:positions,marks,定时器,beepService
                        // 处理UI，删除一个新的小分队
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    LinearLayout ly;
    Boolean groupCategory;
    //after Creat or join group，what we should do: update teamfragment views 、start service、set scheduledExecutorService
    public void processUI(int m) {
        if (m == 1) {
            groupArea.removeAllViews();

            bindIntent = new Intent(A, BeepService.class);   // 绑定服务、启动服务
            A.bindService(bindIntent, connection, BIND_AUTO_CREATE);
            A.startService(bindIntent);

            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();//定时循环的任务：scrolltask
            scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
            //tackle mapfragment's views which is about team
            aboutGroupArea.setVisibility(View.GONE);
            A.mapFragment.chatArea.setVisibility(View.VISIBLE);
            A.mapFragment.hideChatArea.setVisibility(View.VISIBLE);
            A.mapFragment.chatArea.setVisibility(View.VISIBLE);
            ((TextView)ly.findViewById(R.id.group_key)).setText(groupId);
            if(groupCategory)((TextView)ly.findViewById(R.id.exitOrRemove)).setText("删除"); else ((TextView)ly.findViewById(R.id.exitOrRemove)).setText("退出");
            groupArea.addView(ly);

        } else if(m==2){  //quit or delete group

            scheduledExecutorService.shutdown();
            A.unbindService(connection);
            A.stopService(bindIntent);

            markers.clear();
            A.mapFragment.chatArea.setVisibility(View.GONE);
            A.mapFragment.hideChatArea.setVisibility(View.GONE);
            A.mapFragment.chatArea.setVisibility(View.GONE);
            positions.clear();
            baiduMap.clear();
            baiduMap.setMyLocationEnabled(false);
            progressDialog.dismiss();
            groupArea.removeAllViews();
            A.mapFragment.chatItemArea.removeAllViews();

            aboutGroupArea.setVisibility(View.VISIBLE);

        }
//        if (n == 1) {
//
//        }
    }
//    public void requestForBeep(String groupName,String IMME,String position){
////        String ss=downloadBinder.requestForBeep(groupName,IMME,position);
//
//    }

    BeepService.MyBinder downloadBinder;
    String positionJson;
    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (BeepService.MyBinder) service;  //由Service回调，传过来的方法参数，有Service的binder
            downloadBinder.setBeepInfo(groupId,IMME,"30.753404,103.974621");  //这些本地信息都要beep给服务器(在service中)
//            String ss=downloadBinder.getPositionString();
//            Log.e("OpenJCU","HHHHHHHHHHHHHHHHHHHHHHH"+ ss);
            positionJson = downloadBinder.getPositionString();  //获取service从服务器获得的信息
            A.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    A.mapFragment.hideChatArea.setVisibility(View.VISIBLE);
                }
            });

        }
    };
/*
        LayoutInflater lf = getLayoutInflater().from(this);
            commentItemView[i]=(RelativeLayout)lf.inflate(R.layout.comment_item, null);
 */
    //定时器相关   onStart()   onStop()  ScrollTask
    private ScheduledExecutorService scheduledExecutorService;  //定时器
    @Override
    public void onStart() {
//        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 8, TimeUnit.SECONDS);
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    private class ScrollTask implements Runnable { //scheduledExecutorService的定时任务(in a new thread)
        public void run() {                        //定时读获取的位置信息，并在地图上更新
                        A.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    positionJson = downloadBinder.getPositionString();
                    processMap(positionJson);
                    Log.e("OpenJCU", "本地UI更新" + positionJson);
                }
            });
        }
    }


    BaiduMap baiduMap;
    Boolean firstBeep=false;
    //定时更新成员位置、聊天记录
    public void processMap(String data){
        if (firstBeep) {
            progressDialog.dismiss();
            firstBeep = false;
        }
        if (data.contains("出错")){ return; }
        if (data.contains("小分队已被删除")){
            processUI(2);
            return; }
        //定时更新mapfragment 的 每个team成员位置
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        OverlayOptions option;//构建MarkerOption，用于在地图上添加Marker
        Gson gson = new Gson();
        //ThemeResource[] themeResourcesArray = gson.fromJson(data, ThemeResource[].class);
        positions = gson.fromJson(data, new TypeToken<List<GroupPosition>>() {}.getType());
        LatLng[] latLngs; String[] names;
        markers.clear();
        baiduMap.clear();
        groupMembers.removeAllViews();
        TextView tx;
        for(GroupPosition tmp:positions){
            String ss=tmp.getMemberPosition();
            String[] tt = ss.split(",");
            LatLng ll = new LatLng(Float.parseFloat(tt[0]),Float.parseFloat(tt[1]));
            option= new MarkerOptions()
                    .position(ll)
                    .icon(bitmap);
            Marker marker=(Marker) baiduMap.addOverlay(option);
            markers.add(marker);

            tx = new TextView(A);
            tx.setText(tmp.getMemberId());
            groupMembers.addView(tx);
        }


        /////////////////Update team menbers chat items in mapfragment///////////////
        List<ChatBean> tempchatItems=downloadBinder.getNewChatItems();
        if(tempchatItems.size()!=0){
            LinearLayout ly[] = new LinearLayout[tempchatItems.size()];
            if(chatItems.size()!=0 ) {
                if(getLatestItemTime(chatItems)!=getLatestItemTime(tempchatItems)){
                    LayoutInflater lf = A.getLayoutInflater().from(A);
                    for (int i = 0; i < tempchatItems.size(); i++) {
                        ly[i]=new LinearLayout(A);
                        ly[i]= (LinearLayout) lf.inflate(R.layout.chat_item, null);
                        ((TextView)(ly[i].findViewById(R.id.chatMemberName))).setText(tempchatItems.get(i).getMemberId());
                        ((TextView)(ly[i].findViewById(R.id.memberChatContent))).setText(tempchatItems.get(i).getChatContent());
                        A.mapFragment.chatItemArea.addView(ly[i]);
                    }
                }
            }else {
                LayoutInflater lf = A.getLayoutInflater().from(A);
                for (int i = 0; i < tempchatItems.size(); i++) {
                    ly[i]=new LinearLayout(A);
                    ly[i]= (LinearLayout) lf.inflate(R.layout.chat_item, null);
                    ((TextView)(ly[i].findViewById(R.id.chatMemberName))).setText(tempchatItems.get(i).getMemberId());
                    ((TextView)(ly[i].findViewById(R.id.memberChatContent))).setText(tempchatItems.get(i).getChatContent());
                    A.mapFragment.chatItemArea.addView(ly[i]);
                }
            }

            chatItems.addAll(tempchatItems);
        }
    }

    public String getLatestItemTime(List<ChatBean> lb){ //根据时间戳的聊天记录显示
        String time="2017-04-11 20:20:20";
        String tmp;
       int  n=lb.size();
            for (int i = 0; i < n; i++) {
                tmp = ((lb.get(i)).getChatItemDate());
                if (tmp.compareTo(time) > 0)
                    time = tmp;
            }
            return time;
    }

    List<Marker> markers = new ArrayList<>();
    List<GroupPosition> positions = new ArrayList<>();
    List<ChatBean> chatItems = new ArrayList<>();


    private InfoWindow mInfoWindow;
    //点击位置图标 显示成员名称
    public class MyOnMarkerClickListener implements BaiduMap.OnMarkerClickListener{
        public boolean onMarkerClick(final Marker marker) {
            Button button = new Button(A);
            button.setBackgroundResource(R.drawable.popup);
            InfoWindow.OnInfoWindowClickListener listener = null;
            int m=markers.size();
            for(int i=0;i<m;i++){
                if(marker==markers.get(i)){
                    button.setText(positions.get(i).getMemberId());
                    button.setBackgroundColor( 0xff0000 );
                    button.setWidth( 300 );

                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
//                            LatLng ll = marker.getPosition();
//                            LatLng llNew = new LatLng(ll.latitude + 0.005,
//                                    ll.longitude + 0.005);
//                            marker.setPosition(llNew);
                            baiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    baiduMap.showInfoWindow(mInfoWindow);
                }
            }
//            if (marker == mMarkerA || marker == mMarkerD) {
//                button.setText("更改位置");
//                button.setBackgroundColor( 0x0000f );
//                button.setWidth( 300 );
//
//                listener = new InfoWindow.OnInfoWindowClickListener() {
//                    public void onInfoWindowClick() {
//                        LatLng ll = marker.getPosition();
//                        LatLng llNew = new LatLng(ll.latitude + 0.005,
//                                ll.longitude + 0.005);
//                        marker.setPosition(llNew);
//                        baiduMap.hideInfoWindow();
//                    }
//                };
//                LatLng ll = marker.getPosition();
//                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
//                baiduMap.showInfoWindow(mInfoWindow);
//            } else if (marker == mMarkerB) {
//                button.setText("更改图标");
//                button.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        marker.setIcon(bd);
//                        baiduMap.hideInfoWindow();
//                    }
//                });
//                LatLng ll = marker.getPosition();
//                mInfoWindow = new InfoWindow(button, ll, -47);
//                baiduMap.showInfoWindow(mInfoWindow);
//            } else if (marker == mMarkerC) {
//                button.setText("删除");
//                button.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        marker.remove();
//                        baiduMap.hideInfoWindow();
//                    }
//                });
//                LatLng ll = marker.getPosition();
//                mInfoWindow = new InfoWindow(button, ll, -47);
//                baiduMap.showInfoWindow(mInfoWindow);
//            }
            return true;
        }
    }































    @Override
    public void onDestroy() {
        scheduledExecutorService.shutdown();
        super.onDestroy();
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
}
