package com.example.yhadmin.viewsliding.view;

/*
 *  @项目名：  ViewSliding 
 *  @包名：    com.example.yhadmin.viewsliding.view
 *  @文件名:   StickyLayout
 *  @创建者:   YHAdmin
 *  @创建时间:  2018/5/9 14:17
 *  @描述：    TODO
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class StickyLayout
        extends LinearLayout
{
    public StickyLayout(Context context) {
        super(context);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
