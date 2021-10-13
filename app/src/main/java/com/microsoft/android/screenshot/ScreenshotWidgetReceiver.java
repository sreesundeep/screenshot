package com.microsoft.android.screenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

import com.microsoft.android.screenshot.mediaprojection.ScreenCaptureService;

import java.io.File;

public class ScreenshotWidgetReceiver extends BroadcastReceiver implements ScreenCaptureService.IScreenshot {

    private static final String TAG = "ScreenshotWidgetReceiver";

    private Context mContext;
    WindowManager mWindowManager;
    private SnipView mSnipView;
    ScreenCaptureService.IScreenshot screenshotReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive intent action "+intent.getAction());
        // showScreenshotWidget(context);
    }

    public void showScreenshotWidget(Context context, int requestCode, int resultCode, Intent data) {
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        screenshotReceiver = this;
        mSnipView = new SnipView(context, screenshotReceiver);
        context.startForegroundService(ScreenCaptureService.getStartIntent(context, resultCode, data));

        View mOverlayView = LayoutInflater.from(context).inflate(R.layout.screenshot_widget_layout, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mWindowManager.addView(mOverlayView, params);

        try {
            mOverlayView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowManager.removeView(mOverlayView);
                }
            });


            mOverlayView.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Left Screen Screenshot
                    ScreenCaptureService.getLatestImage(screenshotReceiver, new Rect(0, 0, 1344, 1832));
                }
            });

            mOverlayView.findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Right Screen Screenshot
                    ScreenCaptureService.getLatestImage(screenshotReceiver, new Rect(1410, 0, 2816, 1832));
                }
            });

            mOverlayView.findViewById(R.id.dual).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Dual Screen Screenshot
                    ScreenCaptureService.getLatestImage(screenshotReceiver, new Rect(0, 0, 2816, 1832));
                }
            });

            mOverlayView.findViewById(R.id.complete_scroll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Complete Screen Scroll Screenshot
                }
            });

            mOverlayView.findViewById(R.id.custom_scroll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Custom Screen Scroll Screenshot
                }
            });

            mOverlayView.findViewById(R.id.selective_shot).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowManager.removeView(mOverlayView);
                    WindowManager.LayoutParams snipViewParams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        PixelFormat.TRANSLUCENT);
                    mWindowManager.addView(mSnipView, snipViewParams);
//                    windowManager.addView(mOverlayView, params);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        mContext.startActivity(intent);
    }

    @Override
    public void sendScreenshot(File file) {
        Log.d(TAG, "sendScreenshot file path "+file.getPath());
        openScreenshot(file);
        mWindowManager.removeView(mSnipView);
    }
}
