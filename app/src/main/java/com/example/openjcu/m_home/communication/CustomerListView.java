package com.example.openjcu.m_home.communication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 和下拉刷新配合的listview
 */
public class CustomerListView extends ListView {
    public CustomerListView(Context context) {
       super(context);
    }

          public CustomerListView(Context context, AttributeSet attrs) {
              super(context, attrs);
         }
    public CustomerListView(Context context, AttributeSet attrs,
      int defStyle) {
              super(context, attrs, defStyle);
           }

         @Override
    /**
   * 重写该方法，达到使ListView适应ScrollView的效果
    */
         protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
           int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                   MeasureSpec.AT_MOST);
               super.onMeasure(widthMeasureSpec, expandSpec);
         }


}