package com.microsoft.android.screenshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.StrictMode;

import com.microsoft.android.screenshot.mediaprojection.ScreenCaptureService;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 100;

    private ScreenshotWidgetReceiver myReceiver;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // startService(ScreenCaptureService.getStartIntent(this, resultCode, data));
                myReceiver.showScreenshotWidget(this, requestCode, resultCode, data);
                finish();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        myReceiver = new ScreenshotWidgetReceiver();
        this.registerReceiver(myReceiver, new IntentFilter("android.intent.action.show_screenshot_widget"));
        startProjection();
    }

    private void startProjection() {
        MediaProjectionManager mProjectionManager =
            (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    private void stopProjection() {
        startService(ScreenCaptureService.getStopIntent(this));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        // stopProjection();
    }

}
