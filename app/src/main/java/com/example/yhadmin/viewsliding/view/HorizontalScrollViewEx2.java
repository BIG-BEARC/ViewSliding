package com.example.yhadmin.viewsliding.view;

/*
 *  @项目名：  ViewSliding 
 *  @包名：    com.example.yhadmin.viewsliding.view
 *  @文件名:   HorizontalScrollViewEx
 *  @创建者:   YHAdmin
 *  @创建时间:  2018/5/3 15:22
 *  @描述：    TODO
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizontalScrollViewEx2
        extends ViewGroup
{
    private static final String TAG = "HorizontalScrollViewEx";
    private int mChildWidth;
    private int mChildSize;
    private int mChildIndex;

    //分别记录上次互动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;
    //分别记录上次互动的坐标
    private int mLastX          = 0;
    private int mLastY          = 0;

    private VelocityTracker mVelocityTracker;
    private Scroller        mScroller;


    public HorizontalScrollViewEx2(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int       measuredWidth  = 0;
        int       measuredHeight = 0;
        final int childCount     = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSpaceSize  = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode   = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode  = MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize, childView.getMeasuredHeight());
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measuredWidth, heightSpaceSize);
        } else {
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft  = 0;
        int childCount = getChildCount();//获取子元素
        mChildSize = childCount;

        for (int i = 0; i < childCount; i++) {//遍历子元素
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {//获取当前显示的子元素
                int childWidth = childView.getMeasuredWidth();//获取子元素的宽
                mChildWidth = childWidth;
                //重绘子元素
                childView.layout(childLeft,
                                 0,
                                 childLeft + childWidth,
                                 childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int x      = (int) ev.getX();
        int y      = (int) ev.getY();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mLastX = x;
            mLastY = y;
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
                return true;
            }
            return false;
        } else {
            return true;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                scrollBy(-deltaX, 0);
                break;
            }
            case MotionEvent.ACTION_UP: {
                //获取水平滑动的距离
                int scrollX = (int) getScrollX();

                mVelocityTracker.computeCurrentVelocity(1000);

                float xVelocity = mVelocityTracker.getXVelocity();

                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = xVelocity > 0
                                  ? mChildIndex - 1
                                  : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }

                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildSize - 1));
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx, 0);

                //释放占用的内存资源
                mVelocityTracker.clear();

                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        //释放 mVelocityTracker 资源
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
