# screenshot

First time launch the app.

Second time onwards, please give this intent in the adb command:

adb shell am broadcast -a android.intent.action.show_screenshot_widget com.microsoft.android.screenshot

Then a window manager widget will come. Please do implement respective functionalities.
