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
    final ScreenCaptureService.IScreenshot screenshotReceiver = this;
    private Context mContext;
    private String mFilePath = "storage/emulated/0/Pictures/Screenshots";

    public ScreenshotWidgetReceiver(Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive intent action "+intent.getAction());
        // showScreenshotWidget(context);
    }

    public void showScreenshotWidget(Context context, int requestCode, int resultCode, Intent data) {
        mContext = context;
        context.startService(ScreenCaptureService.getStartIntent(context, resultCode, data));

        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
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
        windowManager.addView(mOverlayView, params);

        try {
            mOverlayView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    windowManager.removeView(mOverlayView);
                }
            });


            mOverlayView.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takeLeftScreenScreenshot();
                }
            });

            mOverlayView.findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takeRightScreenScreenshot();
                }
            });

            mOverlayView.findViewById(R.id.dual).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takeDualScreenScreenshot();
                }
            });

            mOverlayView.findViewById(R.id.complete_scroll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Complete Screen Scroll Screenshot
                    windowManager.removeView(mOverlayView);
                    ScreenCaptureService.getLatestImage(screenshotReceiver, new Rect(0, 0, 2816, 1832));
                    windowManager.addView(mOverlayView, params);
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
                    // TODO: Take Selective Screen Scroll Screenshot
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takeLeftScreenScreenshot() {
        Log.d(TAG, "takeLeftScreenScreenshot");
        ScreenCaptureService.getLatestImage(screenshotReceiver, new Rect(0, 0, 1344, 1832));
    }

    public void takeRightScreenScreenshot() {
        Log.d(TAG, "takeRightScreenScreenshot");
        ScreenCaptureService.getLatestImage(screenshotReceiver, new Rect(1410, 0, 2816, 1832));
    }

    public void takeDualScreenScreenshot() {
        Log.d(TAG, "takeDualScreenScreenshot");
        ScreenCaptureService.getLatestImage(screenshotReceiver, new Rect(0, 0, 2816, 1832));
    }

    public void takeCustomScreenshot(Rect rect) {
        Log.d(TAG, "takeCustomScreenshot");
        ScreenCaptureService.getLatestImage(screenshotReceiver, rect);
    }

    private void openScreenshot(File imageFile) {
        Log.d(TAG, "openScreenshot file path "+imageFile.getPath());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void sendScreenshot(File file) {
        Log.d(TAG, "sendScreenshot file path "+file.getPath());
        openScreenshot(file);
    }
}
