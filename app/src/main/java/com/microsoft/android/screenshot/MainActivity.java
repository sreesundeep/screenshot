package com.microsoft.android.screenshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.widget.Toast;

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
        // this.registerReceiver(myReceiver, new IntentFilter("android.intent.action.show_screenshot_widget"));
        startProjection();
        if(!checkAccessibilityPermission()){
            Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkAccessibilityPermission () {
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            // if not construct intent to request permission
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // request permission via start activity for result
            startActivity(intent);
            return false;
        } else {
            return true;
        }
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
        //  unregisterReceiver(myReceiver);
        // stopProjection();
    }

}
