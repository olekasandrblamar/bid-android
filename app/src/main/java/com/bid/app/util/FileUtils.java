package com.bid.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.bid.app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();
    private Context mContext;

    public FileUtils(Context mContext) {
        this.mContext = mContext;
    }

    public File saveIMAImage(Bitmap bitmap) {
        final String root = Environment.getExternalStorageDirectory().toString();
        File fileFolderName = new File(root + "/" + mContext.getString(R.string.app_name));
        if (!fileFolderName.exists()) {
            fileFolderName.mkdirs();
        } else {
            Log.e(TAG, "Folder already created successfully");
        }
        return saveImage(bitmap);
    }

    public Boolean isFileAvailable(final String fileName) {
        final String filePath = Environment.getExternalStorageDirectory().toString() + "/" + mContext.getString(R.string.app_name) + "/" + fileName + ".jpg";
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public File getFilePath(final String fileName) {
        final String filePath = Environment.getExternalStorageDirectory().toString() + "/" + mContext.getString(R.string.app_name) + "/" + fileName + ".jpg";
        return new File(filePath);
    }

    public File saveImage(Bitmap bitmap) {

        final String fullName = Utils.getMD5Hash(Utils.getCurrentDateTime().toString().replace(" ", "_")) +".jpg";
        File file = new File(mContext.getCacheDir(), fullName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bytes = bos.toByteArray();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (isFileAvailable(fullName)) {
            //final File filePath = new File(getFilePath(fullName));
            Logger.e(TAG, "filePath : " + getFilePath(fullName));
            return getFilePath(fullName);
        } else {
            Logger.e(TAG, "filePath : not available");
        }

        return file;

    }


}
