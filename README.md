# screenshot

First time launch the app. And exit the app.

Now onwards, please give the below intent in the adb command to launch the screenshot widget:

adb shell am broadcast -a android.intent.action.show_screenshot_widget com.microsoft.android.screenshot

On receiving the intent, a widget will be displayed on the screen as shown in the below image.

![Screenshot_20211004-184930](https://user-images.githubusercontent.com/13640495/136553573-984c5dfb-db06-4c8f-aa04-2f931b09d565.png)

Currently, on click of close (x) icon, widget will be closed.

We need to implement the below functions as part of FHL.

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


Then a window manager widget will come. Please do implement respective functionalities.
