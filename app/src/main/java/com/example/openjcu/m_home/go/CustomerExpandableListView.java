package com.example.openjcu.m_home.go;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
/**
 * 处理scrollciew里嵌套ExpandableListView的滚动问题
 *
 */
public class CustomerExpandableListView extends ExpandableListView {

    public CustomerExpandableListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomerExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerExpandableListView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 只需要重写这个方法即可
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
