package com.microsoft.android.screenshot;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class GestureAccessibilityService extends AccessibilityService {

    private static final String TAG = "GestureAccessibilityService";

    @Override
    public boolean onGesture (AccessibilityGestureEvent gestureEvent) {
        // super.onGesture(gestureEvent);
        Log.d(TAG, "onGesture 2:" + gestureEvent);
        if (GESTURE_2_FINGER_SWIPE_DOWN == gestureEvent.getGestureId() || GESTURE_2_FINGER_SWIPE_UP == gestureEvent.getGestureId()) {
        } else {
            return super.onGesture(gestureEvent);
        }
        return super.onGesture(gestureEvent);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        // Set the type of events that this service wants to listen to. Others
        // won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;

        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Log.d(TAG, "onAccessibilityEvent: app =" + event.getPackageName().toString());
    }

    @Override
    public void onInterrupt() {
        Log.d("TAG", "onInterrupt");
    }
}
