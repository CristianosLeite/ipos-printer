package com.conecsa.iposprinter.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class BitmapHandler {
    public static Bitmap convertFromBase64(String base64) {
        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    protected static Bitmap getBitmapFromData(int[] pixels, int width, int height)
    {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap getLineBitmapFromData(int size, int width)
    {
        int[] pixels = BytesUtil.createLineData(size, width);
        return getBitmapFromData(pixels, width, size + 6);
    }
}
