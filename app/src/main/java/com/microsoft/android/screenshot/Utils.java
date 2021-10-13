package com.microsoft.android.screenshot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {
    private static final String TAG = "Utils";
    private static final String STORE_DIR = "/storage/emulated/0/Pictures/Screenshots";
    private static final int TOP_BAR_HEIGHT = 60;
    private static final int BOTTOM_BAR_HEIGHT = 60;

    public static File saveBitmap(Bitmap combinedBitmap) {
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
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return imageFile;
    }

    public static Bitmap combineImageIntoOneFlexWidthWithOffset(ArrayList<Bitmap> bitmaps, int offset) {
        Log.d(TAG, "combine images: " + bitmaps);
        int w = bitmaps.get(0).getWidth();
        int h = bitmaps.get(0).getHeight() + offset * (bitmaps.size() - 1);

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        canvas.drawBitmap(bitmaps.get(0), 0f, top, null);
        for (int i = 1; i < bitmaps.size(); i++) {
            Log.e(TAG, "Combine: " + i + "/" + bitmaps.size());
            top += offset;
            canvas.drawBitmap(bitmaps.get(i), 0f, top, null);
        }
        return temp;
    }

    public static Bitmap combineImageIntoOneFlexWidth(ArrayList<Bitmap> bitmaps) {
        Log.d(TAG, "combine images: " + bitmaps);
        int w = bitmaps.get(0).getWidth();
        int h = 0;
        for (int i = 0; i < bitmaps.size(); i++) {
            h += bitmaps.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        canvas.drawBitmap(bitmaps.get(0), 0f, top, null);
        for (int i = 1; i < bitmaps.size(); i++) {
            Log.e(TAG, "Combine: " + i + "/" + bitmaps.size());
            top += bitmaps.get(i-1).getHeight();
            canvas.drawBitmap(bitmaps.get(i), 0f, top, null);
        }
        return temp;
    }

    private static Bitmap getTopBar(Bitmap bitmap) {
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), TOP_BAR_HEIGHT);
    }

    private static Bitmap getBottomBar(Bitmap bitmap) {
        return Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() - BOTTOM_BAR_HEIGHT, bitmap.getWidth(), BOTTOM_BAR_HEIGHT);
    }

    public static Bitmap getMainContent(Bitmap bitmap) {
        return Bitmap.createBitmap(bitmap, 0, TOP_BAR_HEIGHT, bitmap.getWidth(), bitmap.getHeight() - TOP_BAR_HEIGHT - BOTTOM_BAR_HEIGHT);
    }
}
