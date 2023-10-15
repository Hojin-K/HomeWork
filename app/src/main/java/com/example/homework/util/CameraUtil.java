package com.example.homework.util;

import static android.os.Environment.DIRECTORY_PICTURES;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.example.homework.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class CameraUtil {
    private static CameraUtil instance;
    private MediaScanner mediaScanner;

    private CameraUtil() {

    }

    public static CameraUtil getInstance(){
        if(instance == null){
            instance = new CameraUtil();
        }
        return instance;
    }

    public void setImageToBitmap(String imageFilePath, ImageView imageView, MediaScanner mediaScanner) {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
        Log.i("i","imagePath =============================>>"+imageFilePath.toString());
        ExifInterface exif = null;
        this.mediaScanner = mediaScanner;

        try {
            exif = new ExifInterface(imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegress(exifOrientation);
        } else {
            exifDegree = 0;
        }

        String result = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        String filename = formatter.format(curDate);

        String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + "HOMEWORK" + File.separator;
        File file = new File(strFolderName);
        if (!file.exists())
            file.mkdirs();

        File f = new File(strFolderName + "/" + filename + ".png");
        result = f.getPath();

        App mApp = App.getInstance();
        mApp.setImageUri(result);
        Log.i("i","path =============================>>"+result.toString());

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = "Save Error fOut";
        }

        // 비트맵 사진 폴더 경로에 저장
        rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
            // 방금 저장된 사진을 갤러리 폴더 반영 및 최신화
            this.mediaScanner.mediaScanning(strFolderName + "/" + filename + ".png");
        } catch (IOException e) {
            e.printStackTrace();
            result = "File close Error";
        }


        imageView.setImageBitmap(rotate(bitmap, exifDegree));
        // 이미지 뷰에 비트맵을 set하여 이미지 표현
        //((this.view) findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap, exifDegree));


    }

    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}

