package com.microsoft.android.screenshot;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends Activity {

    private ScreenshotWidgetReceiver myReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new ScreenshotWidgetReceiver();
        this.registerReceiver(myReceiver, new IntentFilter("android.intent.action.show_screenshot_widget"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

}
