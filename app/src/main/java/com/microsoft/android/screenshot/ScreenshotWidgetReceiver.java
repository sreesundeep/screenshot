package com.microsoft.android.screenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.startActivity;

public class ScreenshotWidgetReceiver extends BroadcastReceiver {

    private static final String TAG = "ScreenshotWidgetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive intent action "+intent.getAction());
        showWidget(context);
    }


    public void showWidget(Context context) {
        Log.d(TAG, "showWidget");
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
                    // TODO: Take Left Screen Screenshot
                }
            });

            mOverlayView.findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Right Screen Screenshot
                }
            });

            mOverlayView.findViewById(R.id.dual).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Take Dual Screen Screenshot
                    takeScreenshot();
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
                    // TODO: Take Selective Screen Scroll Screenshot
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeScreenshot() {
        Log.d(TAG, "takeScreenshot");
        try {
            String mPath = "/storage/emulated/0/Pictures/Screenshots/Hello_1" + ".png";

            Bitmap bitmap = getBitmapFromView(Util.getView());

            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            // openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromView(View view) {
        Log.d(TAG, "getBitmapFromView");
        Bitmap bitmap = Bitmap.createBitmap(2754, 1892, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
