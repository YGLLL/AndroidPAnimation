package com.ygl.androidpanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AndroidPAnimationView extends View {

    public static final int MAX_CIRCLE_NUM=20;

    private Paint mPaint;
    private Random mRandom;
    private int mDrawW;
    private int mDrawH;
    private int mDrawX;
    private int mDrawY;
    private List<CircleBean> mData=new ArrayList<>();

    public AndroidPAnimationView(Context context) {
        super(context);
    }

    public AndroidPAnimationView(Context context,AttributeSet attrs) {
        super(context, attrs);
        mRandom=new Random(System.currentTimeMillis());
        mPaint=new Paint();
    }

    private boolean isFirst=true;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst){
            isFirst=false;
            initViewData();
        }
        for (CircleBean mDatum : mData) {
            mPaint.setColor(mDatum.getColor());
            canvas.drawCircle(mDrawX,mDrawY,mDatum.getRadius(),mPaint);
        }

    }

    private void initViewData() {
        mDrawW=getWidth()-getPaddingLeft()-getPaddingRight();
        mDrawH=getHeight()-getPaddingTop()-getPaddingBottom();
        mDrawX=mDrawW/2+getPaddingRight();
        mDrawY=mDrawH/2+getPaddingTop();
        getRandomData();
    }

    private void getRandomData() {
        mData.clear();
        int r=Math.max(mDrawW,mDrawH)/2;
        int circleNum=getRandomNum();
        for (int i = circleNum; i > 0; i--) {
            float theR=(((float)i)/((float)circleNum))*r;
            CircleBean bean=new CircleBean();
            bean.setColor(getRandomColor());
            bean.setRadius(theR);
            mData.add(bean);
        }
    }

    private int getRandomColor(){
        int color=0xff000000;
        byte[] bytes={0,0,0};

        mRandom.nextBytes(bytes);
        color |= ((bytes[0]&0xff)<<16);
        color |= ((bytes[1]&0xff)<<8);
        color |= (bytes[2]&0xff);
        return color;
    }

    private int getRandomNum(){
        return mRandom.nextInt(MAX_CIRCLE_NUM);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                smallAnimation(5);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
        }
        return super.onTouchEvent(event);
    }

    private void smallAnimation(int i){
        Iterator<CircleBean> iterator= mData.iterator();
        while (iterator.hasNext()){
            CircleBean bean=iterator.next();
            float newR=bean.getRadius()-i;
            if (newR>0){
                bean.setRadius(newR);
            }else {
                mData.remove(bean);
            }

        }
    }

    static class Test{
        public static void main(String[] s){
            byte i=53;
            int b=i<<12;
        }
    }
}
