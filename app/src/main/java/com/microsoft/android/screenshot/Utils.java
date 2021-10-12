package com.microsoft.android.screenshot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Utils {
    private static final String TAG = "Utils";
    private static String STORE_DIR = "/storage/emulated/0/Pictures/Screenshots";

    public static void saveBitmap(Bitmap combinedBitmap) {
        // write bitmap to a file
        String filePath = STORE_DIR + "/Screenshot_" + System.currentTimeMillis() + ".png";
        Log.e(TAG, "Captured image filePath: " + filePath);
        File imageFile = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            combinedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap combineImageIntoOneFlexWidth(ArrayList<Bitmap> bitmaps) {
        Log.d(TAG, "combine images: " + bitmaps);
        int w = 0, h = 0;
        for (int i = 0; i < bitmaps.size(); i++) {
            w = bitmaps.get(i).getWidth();
            h += bitmaps.get(i).getHeight();
            Log.d(TAG, "i: " + i + ", w: " + bitmaps.get(i).getWidth() + ", h: " + bitmaps.get(i).getHeight());
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 0; i < bitmaps.size(); i++) {
            Log.e(TAG, "Combine: " + i + "/" + bitmaps.size() + 1);

            top = (i == 0 ? 0 : top + bitmaps.get(i).getHeight());
            //attributes 1:bitmap,2:width that starts drawing,3:height that starts drawing
            canvas.drawBitmap(bitmaps.get(i), 0f, top, null);
        }
        return temp;
    }
}
