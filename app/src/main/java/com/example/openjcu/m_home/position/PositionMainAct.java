package com.example.openjcu.m_home.position;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

            toolbar = (Toolbar) findViewById(R.id.memo_commit_toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


    }







    Toolbar toolbar;

    //    toolbar = (Toolbar) findViewById(R.id.memo_commit_toolbar);
//    setSupportActionBar(toolbar);
//
//    ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

    //menu点击事件的处理
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.addmemo:
//                Intent in = new Intent(this, CommitMemoActivity.class); startActivity(in); break;
            case 16908332:  finish(); break;
            default: break;
        }
        return true;
    }
    //为toolbar 指定menu
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.playgroudtoolbar, menu);
        return true;
    }

    public void onClick(View v){
        switch (v.getId()) {
            default:  break;
        }
    }
}
