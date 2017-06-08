package com.example.openjcu.m_home.info_search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openjcu.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SuccessActivity extends AppCompatActivity implements View.OnClickListener {
    private Button getCourse;
    private TextView lesson;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    boolean isFirstIn = false;
    public String studentName;
    public String cookie;
    public String code;
    private Button getseat;
    public OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Intent intent=getIntent();
        studentName=intent.getStringExtra("xsxm");
        code=intent.getStringExtra("xh");
        cookie=intent.getStringExtra("setCookie");
        initView();
        if(studentName!=null) {
            getCourse();
        }
    }
    private void initView(){
        SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            Message message = handler.obtainMessage();
            message.what = 3;
            handler.sendMessage(message);
        }

        List<Curriculum>curricula= DataSupport.findAll(Curriculum.class);
        for (Curriculum curriculum1:curricula){
            String str1=curriculum1.getLessons();
            Log.i("AAAAAAAA",str1);
            int inti=curriculum1.getDay();
            int intj=curriculum1.getSection();
            lesson = (TextView) findViewById(lessons[inti][intj]);
            lesson.setText(str1);
            int n = 0 + (int) (Math.random() * 9);
            //lesson.setBackground(R.drawable.text_view_border);
            lesson.setBackgroundColor(Color.parseColor(color[n]));
        }

    }
    private int[][] lessons={
            {R.id.course10,R.id.course11,R.id.course12,R.id.course13,R.id.course14,R.id.course15,R.id.course16},
            {R.id.course20,R.id.course21,R.id.course22,R.id.course23,R.id.course24,R.id.course25,R.id.course26},
            {R.id.course30,R.id.course31,R.id.course32,R.id.course33,R.id.course34,R.id.course35,R.id.course36},
            {R.id.course40,R.id.course41,R.id.course42,R.id.course43,R.id.course44,R.id.course45,R.id.course46},
            {R.id.course50,R.id.course51,R.id.course52,R.id.course53,R.id.course54,R.id.course55,R.id.course56},
            {R.id.course60,R.id.course61,R.id.course62,R.id.course63,R.id.course64,R.id.course65,R.id.course66},
    };
    public String[] color={"#FF4500","#F5DEB3","#FFF8DC","#FFFFE0","#6B8E23","#20B2AA","#87CEFA","#FFB6C1","#f6e1a0"};
    public void onClick(View v) {
        switch (v.getId()) {
            default:break;
        }
    }
    public static void actionStart(Context context,String xsxm,String xh,String setCookie){
        Intent intent=new Intent(context,SuccessActivity.class);
        intent.putExtra("xsxm",xsxm);
        intent.putExtra("xh",xh);
        intent.putExtra("setCookie",setCookie);
        context.startActivity(intent);
    }
    private void getCourse() {
        String url="http://jwweb.scujcc.cn/xskbcx.aspx?";
        String xh="xh="+code;
        String xm="&xm="+studentName;
        String gnmkdm="&gnmkdm=N121602";
        String cc=url+xh+xm+gnmkdm;
        Log.i("ABSNDurl",cc);
//        String url2="http://jwweb.scujcc.cn/xskbcx.aspx?xh=130830145&xm=何广杰&gnmkdm=N121602";
        // String encode = URLEncoder.encode(content, “utf-8”);
        Log.i("info_get", "cookie不变:" + cookie);
//        FormBody body = new FormBody.Builder()
//                .add("__EVENTARGUMENT","")
//                .add("__EVENTTARGET","xqd")
//                .add("xnd","2016-2017")
//                .add("__VIEWSTATE", "dDwzOTI4ODU2MjU7dDw7bDxpPDE+Oz47bDx0PDtsPGk8MT47aTwyPjtpPDQ+O2k8Nz47aTw5PjtpPDExPjtpPDEzPjtpPDE1PjtpPDI0PjtpPDI2PjtpPDI4PjtpPDMwPjtpPDMyPjtpPDM0Pjs+O2w8dDxwPHA8bDxUZXh0Oz47bDwxMjAwNS0yMDA2Mjs+Pjs+Ozs+O3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDx4bjt4bjs+Pjs+O3Q8aTwyPjtAPDIwMTYtMjAxNzsyMDE1LTIwMTY7PjtAPDIwMTYtMjAxNzsyMDE1LTIwMTY7Pj47bDxpPDA+Oz4+Ozs+O3Q8dDw7O2w8aTwxPjs+Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpuWPt++8mjEzMDgzMDE0NTs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85aeT5ZCN77ya5L2V5bm/5p2wOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlrabpmaLvvJrnlLXlrZDkv6Hmga/lt6XnqIvns7s7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOS4k+S4mu+8mueUteWtkOS/oeaBr+W3peeoizs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w86KGM5pS/54+t77ya5L+h5oGv5bel56iLMjAxNC0wMeePrTs+Pjs+Ozs+O3Q8O2w8aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O2w8aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8MD47aTwwPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8UGFnZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYXRhS2V5czs+O2w8aTwxPjtpPDA+O2k8MD47bDw+Oz4+Oz47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47Pr0A1OFUGfQlGE6Qd4I2vBYT0fG0")
//                .add("xqd", "1")
//                .build();
        okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(cc)
                .addHeader("cookie",cookie)
                .addHeader("Referer","http://jwweb.scujcc.cn/xs_main.aspx?xh=130830145")
                .addHeader("Host","jwweb.scujcc.cn")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               String Html = response.body().string();
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj=Html;
                handler.sendMessage(message);
            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Document document = Jsoup.parse((String) msg.obj);
                    Element table = document.getElementById("Table1");
                    LitePal.getDatabase();
                    Elements tr = table.getElementsByTag("tr");
                    for (int i=2;i<=tr.size()-2;i+=2) {
                        Elements td = tr.get(i).getElementsByTag("td");
                        Log.i("tdSIZE", String.valueOf(i));
                        Elements td1 = td.select("td[align=\"Center\"]");
                        for (int j = 0; j < td1.size(); j++) {
                            String course = td1.get(j).text().trim();
                            if (course.length()>1) {
                                int dayofWeek = i / 2 - 1;//转换为lessons数组对应的行下标
                                int section = j;//转换为lessons数组对应的列下标
                                Curriculum curriculum=new Curriculum();
                                curriculum.setLessons(course);
                                curriculum.setDay(dayofWeek);
                                curriculum.setSection(section);
                                curriculum.save();
                            }
                            Message message = handler.obtainMessage();
                            message.what = 3;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case 2:
                    Toast.makeText(SuccessActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
                case 3:

                    break;
            }
        }
    };
}
