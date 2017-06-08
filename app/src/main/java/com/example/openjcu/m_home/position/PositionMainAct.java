package com.example.openjcu.m_home.position;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.openjcu.R;

public class PositionMainAct extends AppCompatActivity {

    LinearLayout dinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_main);
        init();
    }
    public void init(){
        dinner = (LinearLayout) findViewById(R.id.dinner);

    }
    public void onClick(View v){
        switch (v.getId()) {
            default:  break;
        }
    }
}
