package com.ygl.androidpanimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AndroidPAnimationView extends View {

    public static final int MAX_CIRCLE_NUM=20;
    public static final int MIN_CIRCLE_NUM=10;
    public static final int MAX_COLOR_NUM=3;
    public static final int MIN_COLOR_NUM=2;

    private Paint mPaint;
    private Random mRandom;
    private int mDrawW;
    private int mDrawH;
    private int mDrawX;
    private int mDrawY;
    private List<CircleBean> mData=new ArrayList<>();
    private int[] mColors;

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
            startMyAnimation();
        }
        for (CircleBean mDatum : mData) {
            mPaint.setColor(mDatum.getColor());
            canvas.drawCircle(mDrawX,mDrawY,mDatum.getRadius(),mPaint);
        }

    }

    private boolean isInitViewData=false;
    private void initViewData() {
        isInitViewData=true;
        mDrawW=getWidth()-getPaddingLeft()-getPaddingRight();
        mDrawH=getHeight()-getPaddingTop()-getPaddingBottom();
        mDrawX=mDrawW/2+getPaddingRight();
        mDrawY=mDrawH/2+getPaddingTop();

        //初始化颜色
        mColors=new int[getRandomColorNum()];
        for (int i = 0; i < mColors.length; i++) {
            mColors[i]=getRandomColor();
        }
        getRandomData();
        isInitViewData=false;

    }

    private long mAnimationCount=0;
    private Timer mTimer=new Timer();
    private void startMyAnimation() {
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                if (!isInitViewData){
                    mAnimationCount++;
                    if (mAnimationCount<100){
                        smallAnimation(3);

                    }else {
                        bigAnimation(3);
                    }
                    invalidate();
                    if (mAnimationCount==200){
                        mAnimationCount=0;
                    }
                }
            }
        };

        mTimer.schedule(timerTask,200,30);
    }

    private double mCircleR=0;
    private float mCircleGap=0;//半径差
    private void getRandomData() {
        mData.clear();
        mCircleR=Math.sqrt(mDrawW*mDrawW+mDrawH*mDrawH)/2;
        int circleNum=getRandomNum();
        mCircleGap=(float) mCircleR/circleNum;

        //circleNum+1,多画一圈避免边角出现
        for (int i = circleNum+1; i > 0; i--) {
            CircleBean bean=new CircleBean();
            bean.setColor(getCircleColor(true));
            bean.setRadius(mCircleGap*i);
            mData.add(bean);
        }
    }



    private int getRandomNum(){
        int i=mRandom.nextInt(MAX_CIRCLE_NUM+1);
        while (i<MIN_CIRCLE_NUM){
            i=mRandom.nextInt(MAX_CIRCLE_NUM+1);
        }
        return i;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                initViewData();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void smallAnimation(int i){
        Iterator<CircleBean> iterator= mData.iterator();
        boolean isHasRemove=false;
        while (iterator.hasNext()){
            CircleBean bean=iterator.next();
            float newR=bean.getRadius()-i;
            if (newR>0){
                bean.setRadius(newR);
            }else {
                iterator.remove();
                isHasRemove=true;
            }

        }

        if (isHasRemove){
            float biggerR=mData.get(0).getRadius();
            CircleBean circleBean=new CircleBean();
            circleBean.setColor(getCircleColor(true));
            circleBean.setRadius(biggerR+mCircleGap);
            mData.add(0,circleBean);
        }

    }
    private void bigAnimation(int i){
        Iterator<CircleBean> iterator= mData.iterator();
        boolean isHasRemove=false;
        while (iterator.hasNext()){
            CircleBean bean=iterator.next();
            float newR=bean.getRadius()+i;
            if (newR<(mCircleR+mCircleGap)){
                bean.setRadius(newR);
            }else {
                iterator.remove();
                isHasRemove=true;
            }
        }
        if (isHasRemove){
            CircleBean circleBean=new CircleBean();
            circleBean.setColor(getCircleColor(false));
            circleBean.setRadius(1);
            mData.add(circleBean);
        }
    }
    private void specialBigAnimation(int i){
        Iterator<CircleBean> iterator= mData.iterator();
        boolean isHasRemove=false;
        while (iterator.hasNext()){
            CircleBean bean=iterator.next();
            float newR=bean.getRadius()+i;
            if (newR<(mCircleR+mCircleGap)){
                bean.setRadius(newR);
            }else {
                iterator.remove();
                isHasRemove=true;
            }
        }
        if (isHasRemove){
            CircleBean circleBean=new CircleBean();
            circleBean.setColor(getCircleColor(false));
            circleBean.setRadius(1);
            mData.add(circleBean);
        }
    }

    private int mColorNum=0;
    private int getCircleColor(boolean isToSmall) {
        if (isToSmall){
            mColorNum++;
        }else {
            mColorNum--;
        }

        if (mColorNum>(mColors.length-1)){
            mColorNum=0;
        }else if (mColorNum<0){
            mColorNum=mColors.length-1;
        }
        return mColors[mColorNum];
    }
    /**
     * 颜色数
     */
    private int getRandomColorNum(){
        int i=mRandom.nextInt(MAX_COLOR_NUM+1);
        while (i<MIN_COLOR_NUM){
            i=mRandom.nextInt(MAX_COLOR_NUM+1);
        }
        return i;
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

    static class Test{
        public static void main(String[] s){
            byte i=53;
            int b=i<<12;
        }
    }
}
