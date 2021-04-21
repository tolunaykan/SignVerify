package com.tolunaykandirmaz.signverify;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    private static final String DIRECTORY_NAME = "SignVerify";

    public static void saveImage(Context context, Bitmap bitmap, String fileName) throws Exception{
        ContextWrapper contextWrapper = new ContextWrapper(context);

        File directory = contextWrapper.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE);
        if (!directory.exists()) {
            boolean isSucceed = directory.mkdir();
            if (!isSucceed) throw new Exception(DIRECTORY_NAME + " klasörü oluşturulamadı");
        }
        File path = new File(directory, fileName);

        FileOutputStream fileOutputStream = new FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();

    }

    public static Bitmap getImage(Context context, String fileName) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File directory = contextWrapper.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE);
        if (!directory.exists()) {
            return null;
        }
        final File file = new File(directory, fileName);

        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static File convertBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File file = new File(contextWrapper.getCacheDir(), fileName);
        try {
            boolean isFileCreated = file.createNewFile();
            if (isFileCreated) throw new IOException();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteData = byteArrayOutputStream.toByteArray();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteData);
            fileOutputStream.flush();
            fileOutputStream.close();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MYTAG", "bitmap file'a çevrilemedi");
        }

        return null;
    }

/*    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }*/
}
