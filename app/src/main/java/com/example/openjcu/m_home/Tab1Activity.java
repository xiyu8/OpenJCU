package com.example.openjcu.m_home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.openjcu.R;

public class Tab1Activity extends AppCompatActivity {

    WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_activity_lyt);
        init();
    }

    public void init() {
        wb = (WebView) findViewById(R.id.wb);
        wb.loadUrl("http://192.168.191.1/www/a.php");
        //本地文件用：wb.loadUrl("file:///android_asset/XX.html");
    }
}
