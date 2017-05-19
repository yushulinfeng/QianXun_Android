package com.szdd.qianxun.tools.bitmap;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zengchan.lzc on 2014/12/29.
 */
public class ImageSaver {
    /**
     * 会对fileDir进行测试，若不存在则创建
     *
     * @param bitmap
     * @param fileDir
     * @param fileName
     * @return String 保存的路径
     */
    public static String savePicture(Bitmap bitmap, String fileDir, String fileName, Bitmap.CompressFormat compressFormat) {
        try {
            String retPath = fileDir + fileName;

            File dirFile = new File(fileDir);
            if (!dirFile.exists())
                dirFile.mkdirs();

            File myCaptureFile = new File(retPath);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                bitmap.compress(compressFormat, 100, bos);
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return retPath;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String saveJPEGBasedSDRoot(Bitmap bitmap, String fileRelativeDir, String fileName, Bitmap.CompressFormat compressFormat) {
        String fileDir = Environment.getExternalStorageDirectory().toString() + "/" + fileRelativeDir;
        return savePicture(bitmap, fileDir, fileName, compressFormat);
    }
}
