package com.ygl.androidpanimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AndroidPAnimationView extends View {

    private Paint mPaint;

    public AndroidPAnimationView(Context context) {
        super(context);
    }

    public AndroidPAnimationView(Context context,AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        mPaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w=getWidth()-getPaddingLeft()-getPaddingRight();
        int h=getHeight()-getPaddingTop()-getPaddingBottom();
        int r=Math.min(w,h)/2;
        canvas.drawCircle(w/2+getPaddingRight(),h/2+getPaddingTop(),r,mPaint);
    }
}
