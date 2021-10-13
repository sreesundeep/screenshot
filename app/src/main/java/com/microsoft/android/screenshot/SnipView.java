package com.microsoft.android.screenshot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.microsoft.android.screenshot.mediaprojection.ScreenCaptureService;

public class SnipView extends View {

    private Context mContext;
    private ScreenCaptureService.IScreenshot mScreenShotListener;
    private Paint mBorderPaint, mFullScreenPaint, mRectFillPaint;
    private float mBorderStrokeWidth = 3.0f;
    float left = 0, right = 0, top = 0, bottom = 0, mDownX = 0, mDownY = 0;

    public SnipView(Context context, ScreenCaptureService.IScreenshot screenshotListener) {
        super(context);
        mScreenShotListener = screenshotListener;
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mBorderPaint = new Paint();
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));


        mFullScreenPaint = new Paint();
        mFullScreenPaint.setColor(getResources().getColor(R.color.transparent_white));
        mFullScreenPaint.setStyle(Paint.Style.FILL);
        mFullScreenPaint.setAntiAlias(true);

        mRectFillPaint = new Paint();
        mRectFillPaint.setColor(Color.TRANSPARENT);
        mRectFillPaint.setStyle(Paint.Style.FILL);
        mRectFillPaint.setAntiAlias(true);
        mRectFillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), mFullScreenPaint);
        if (left == right && top == bottom) {
            return;
        }
        Log.d("ShubhamLog", getWidth() + " : " + getHeight());
        Log.d("ShubhamLog", left + " : " + right + " : " + top + " : " + bottom);
        canvas.drawRect(left, top, right, bottom, mBorderPaint);
        canvas.drawRect(left, top, right, bottom, mRectFillPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = x;
            mDownY = y;
            setBorderX(mDownX, mDownX);
            setBorderY(mDownY, mDownY);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (x < mDownX) {
                setBorderX(x, mDownX);
            } else {
                setBorderX(mDownX, x);
            }

            if (y < mDownY) {
                setBorderY(y, mDownY);
            } else {
                setBorderY(mDownY, y);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (left < right && top < bottom) {
                ScreenCaptureService.getLatestImage(mScreenShotListener, new Rect((int) left, (int) top, (int) right, (int) bottom));
            }
        }
        invalidate();
        return true;
    }

    private void setBorderX(float l, float r) {
        left = l;
        right = r;
    }

    private void setBorderY(float t, float b) {
        top = t;
        bottom = b;
    }
}
