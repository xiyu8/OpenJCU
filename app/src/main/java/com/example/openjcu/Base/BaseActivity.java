package com.example.openjcu.Base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.openjcu.R;

public class BaseActivity extends AppCompatActivity {

    public String app_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        app_url = getResources().getString(R.string.app_url);
    }
}
