package com.example.visitorpass;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class AppHelper {
    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        Log.e("Image_post ", "Image_post" + byteArrayOutputStream.toByteArray());
        return byteArrayOutputStream.toByteArray();
    }
}
