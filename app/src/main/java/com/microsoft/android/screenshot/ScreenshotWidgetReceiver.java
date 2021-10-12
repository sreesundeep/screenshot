package com.microsoft.android.screenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.microsoft.android.screenshot.mediaprojection.ScreenCaptureService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;

public class ScreenshotWidgetReceiver extends BroadcastReceiver implements ScreenCaptureService.IScreenshot {
    private static final String TAG = "ScreenshotWidgetReceiver";

    private Context mContext;
    private Bitmap mScreenShot;
    private final ArrayList<Bitmap> mScrollScreenShots = new ArrayList<>(10);
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive intent action " + intent.getAction());
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
        final ScreenCaptureService.IScreenshot screenshotReceiver = this;

        try {
            mOverlayView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mergeScrollScreenshots();
                    windowManager.removeView(mOverlayView);
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
                    try {
                        String keyCommand = "/system/bin/screencap -p /sdcard/Download/img.png";
                        Runtime runtime = Runtime.getRuntime();
                        Process proc = runtime.exec(keyCommand);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // TODO: Take Complete Screen Scroll Screenshot
                }
            });

            mOverlayView.findViewById(R.id.custom_scroll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Custom Screen Scroll Screenshot
                    scrollDown();
                    mHandler.postDelayed(() -> ScreenCaptureService.getLatestImage(new ScreenCaptureService.IScreenshot() {
                        @Override
                        public void sendScreenshot(File file) {
                        }

                        @Override
                        public void sendBitmap(Bitmap bitmap) {
                            Log.d(TAG, "bitmap received: " + mScrollScreenShots.size());
                            mScrollScreenShots.add(bitmap);
                        }
                    }), 200);
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

    private void mergeScrollScreenshots() {
        if (mScrollScreenShots.size() > 1) {
            // TODO: merge screenshots
            Bitmap combinedBitmap = Utils.combineImageIntoOneFlexWidth(mScrollScreenShots);
            Utils.saveBitmap(combinedBitmap);
        }
        mScrollScreenShots.clear();
    }

    private void scrollDown() {
        // TODO: using page down for now, check with roll once
        try {
            String keyCommand = "input keyevent 93" /* PAGE_DOWN */;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
        } catch (IOException e) {
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
        Log.d(TAG, "sendScreenshot file path " + file.getPath());
        openScreenshot(file);
    }

    @Override
    public void sendBitmap(Bitmap bitmap) {
        mScreenShot = bitmap;
        mScrollScreenShots.add(bitmap);
    }
}
