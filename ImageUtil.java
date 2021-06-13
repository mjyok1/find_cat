package com.example.find_cat_info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {


    private static final String TAG = ImageUtil.class.getName();

    public static final int GALLERY_IMG_CODE = 10;
    public static final int CROP_IMG_CODE = 10;

    public static final String TEMP_IMG_FILE_NAME = "TEMP.png";
    public static final String BODY_IMG_FILE_NAME = "body_image.png";
    public static final String CLOTHES_IMG_FILE_NAME = "clothes_image.png";

    public static Bitmap getContentUriImage(Context context, Uri contentUri) {
        ContentResolver contentResolver = context.getContentResolver();

        try {
            InputStream inputStream = contentResolver.openInputStream(contentUri);
            ExifInterface exifInterface = new ExifInterface(inputStream);
            int rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0) {
                matrix.preRotate(rotationInDegrees);
            }
            inputStream.close();
            inputStream = contentResolver.openInputStream(contentUri);
            Bitmap sourceBitmap = BitmapFactory.decodeStream(inputStream);

            return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void selectImage(Activity activity) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        activity.startActivityForResult(getIntent, GALLERY_IMG_CODE);
    }

    public static String saveClothesImage(Context context, byte[] byteArray) {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(date) + ".png";
//        Log.d(TAG, "saveClothesImage: fileName = " + fileName);

        saveByteArray(context, byteArray, fileName);
        return fileName;
    }

    public static String getImageFileName() {
        Date date = new Date(System.currentTimeMillis());
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(date) + ".png";
        return fileName;
    }

    public static boolean saveByteArray(Context context, byte[] byteArray, String filename) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(byteArray);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    public static Uri saveCacheBitamp(@NonNull final Context context, @NonNull final Bitmap bitmap,
                                      @NonNull final Bitmap.CompressFormat format,
                                      @NonNull final String mimeType,
                                      @NonNull final String displayName) throws IOException {

        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

        final ContentResolver resolver = context.getContentResolver();
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, values);

            if (uri == null)
                throw new IOException("Failed to create new MediaStore record.");

            try (final OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null)
                    throw new IOException("Failed to open output stream.");

                if (!bitmap.compress(format, 95, stream))
                    throw new IOException("Failed to save bitmap.");
            }

            return uri;
        } catch (IOException e) {

            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        }
    }


    public static String saveCacheBitamp(Context context, Bitmap bitmap, String name) {
        File storage = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        String fileName = name + ".png";
        File imgFile = new File(storage, fileName);
        try {
            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out); //썸네일로 사용하므로 퀄리티를 낮게설정
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("saveBitmapToJpg", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("saveBitmapToJpg", "IOException : " + e.getMessage());
        }

        Log.d("imgPath", context.getCacheDir() + "/" + fileName);
        return context.getCacheDir() + "/" + fileName;
    }

    public static String saveBitmap(Context context, Bitmap bitmap) {
        String fileName = getImageFileName();
        saveByteArray(context, bitmapToByteArray(bitmap), fileName);
        return fileName;
    }


    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getDataImage(Context context, String name) {
        File file = new File(context.getFilesDir(), name);
        return BitmapFactory.decodeFile(file.getPath());
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

}
