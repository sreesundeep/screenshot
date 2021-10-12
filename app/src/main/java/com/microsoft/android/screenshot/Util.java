package com.microsoft.android.screenshot;

import android.util.Log;
import android.view.View;

public class Util {

    public static View screenView;

    public static void setView(View view) {
        Log.d("Util", "setView width "+view.getWidth() + " height "+view.getHeight());
        screenView = view;
    }

    public static View getView() {
        Log.d("Util", "getView width "+screenView.getWidth() + " height "+screenView.getHeight());
        return screenView;
    }

    public static void showScreenshotWidget() {
        ScreenshotWidgetReceiver screenshotWidgetReceiver = new ScreenshotWidgetReceiver();
        screenshotWidgetReceiver.showWidget(getView().getContext());
    }
}
