package com.microsoft.android.screenshot.imageio;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MergeBitmap {

    Bitmap mFirstBitmap;
    Bitmap mSecondBitmap;

    public MergeBitmap(Bitmap first, Bitmap second) {
        mFirstBitmap = first;
        mSecondBitmap = second;
    }

    public Bitmap merge() {
        Bitmap mergedBitmap = null;
        int width, height = 0;
        width = mFirstBitmap.getWidth();
        height = mSecondBitmap.getHeight();
        mergedBitmap = Bitmap.createBitmap(width, height*2, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mergedBitmap);

        canvas.drawBitmap(mFirstBitmap, 0f, 0f, null);
        canvas.drawBitmap(mSecondBitmap, 0f, height, null);

        return mergedBitmap;
    }

    //TODO: getBitmapFromAsset is temp function to fetch the image from the assets
    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //TODO: saveBitmap is temp function just to ensure that merging of bitmap is working correctly.
    public static void saveBitmap(String bitName, Bitmap mBitmap) {
        String path = Environment.getExternalStorageDirectory().toString()+ "/" + bitName + ".png";
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
