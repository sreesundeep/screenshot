package com.microsoft.android.screenshot;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    private ScreenshotWidgetReceiver myReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myReceiver = new ScreenshotWidgetReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getWindow().getDecorView().getRootView();
        Log.d("MainActivity", "view width "+view.getWidth() + " height "+view.getHeight());
        Util.setView(view);
        Util.showScreenshotWidget();
        this.registerReceiver(myReceiver, new IntentFilter("android.intent.action.show_screenshot_widget"));
        // finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

}
