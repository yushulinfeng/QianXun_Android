package com.szdd.qianxun.tools.bitmap;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

/**
 * Created by zengchan.lzc on 2014/12/29.
 */
public abstract class BitmapUtils {

    /**
     * 将bitmap对象转成bytes对象
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 将byte数组转成bitmap对象.
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bimap(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     *
     * @param bitmap
     * @param context
     * @param isGif 是否是GIF文件，如果是，则存储为gif文件格式
     * @return
     */
    public static String saveBitmapAndInsertToContent(Bitmap bitmap, Context context, boolean isGif) {
        return saveBitmap(bitmap, context, Bitmap.CompressFormat.JPEG, true, isGif);
    }

    public static File generateImageSaveFile(boolean isGif) {
        String newFileDir = Environment.getExternalStorageDirectory().toString() + "/laiwang/savedPic/";
        String fileName = UUID.randomUUID().toString() + (isGif ? ".gif" : ".jpg");
        String retPath = newFileDir + fileName;
        File dirFile = new File(newFileDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File localFile = new File(retPath);
        return localFile;
    }

    public static String saveBitmap(Bitmap bitmap, Context context, Bitmap.CompressFormat compressFormat, boolean isInsertToContent) {
        return saveBitmap(bitmap, context, compressFormat, isInsertToContent, false);
    }

    public static String saveBitmap(Bitmap bitmap, Context context, Bitmap.CompressFormat compressFormat, boolean isInsertToContent, boolean isGif) {
        if (bitmap != null) {
            String newFileDir = "dcim/Camera/";
            String fileName = UUID.randomUUID().toString() +  (isGif ? ".gif" : ".jpg");
            String retUrl = ImageSaver.saveJPEGBasedSDRoot(bitmap, newFileDir, fileName, compressFormat);
            if (isInsertToContent) {

                String file = Environment.getExternalStorageDirectory().toString() + "/" + newFileDir + fileName;
                insertIntoContent(context, file, "image/jpeg");
            }
            return retUrl;
        } else {
            return null;
        }
    }

    public static String insertIntoCamera(Bitmap bitmap, Context context, String fileName){

        return MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, "From Laiwang");
    }

    public static boolean insertIntoContent(Context context, String fileName, String mimeType){
        try{
            if(TextUtils.isEmpty(mimeType)){
                mimeType = "image/jpeg";
            }
            ContentValues localContentValues = new ContentValues();
            localContentValues.put("_data", fileName);
            localContentValues.put("description", "save image ---");
            localContentValues.put("mime_type", mimeType);
            localContentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

            ContentResolver localContentResolver = context.getContentResolver();
            Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            localContentResolver.insert(localUri, localContentValues);
            return true;
        }catch(Throwable tr){

        }
        return false;
    }

    public static final String DIR_CHAT_BACKGROUND = "laiwang/chatbg/";
    /**
     * 保存聊天设置的背景信息
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveChatListBackground(Context context, Bitmap bitmap){
        if(bitmap == null){
            return null;
        }
        String fileName = "bg"+System.currentTimeMillis()+".jpg";
        if(ImageSaver.saveJPEGBasedSDRoot(bitmap, DIR_CHAT_BACKGROUND, fileName, Bitmap.CompressFormat.JPEG) != null){
            return fileName;
        }
        return null;
    }

    /**
     * 删除指定的聊天背景
     * @param context
     * @param fileName
     */
    public static void deleteChatListBackgroundFile(Context context, String fileName){
        String fileDir = Environment.getExternalStorageDirectory().toString() + "/" + DIR_CHAT_BACKGROUND + fileName;
        File dirFile = new File(fileDir);
        if(dirFile.isFile()){
            dirFile.delete();
        }
    }
    /**
     * 获取聊天背景的内容
     * @param context
     * @param fileName
     * @return
     */
    public static Uri getChatListBackgroundFileUri(Context context, String fileName){
        String fileDir = Environment.getExternalStorageDirectory().toString() + "/" + DIR_CHAT_BACKGROUND + fileName;
        File dirFile = new File(fileDir);
        if(dirFile.isFile()){
            return Uri.fromFile(dirFile);
        }
        return null;
    }

    /**
     * 不等比缩放bitmap
     *
     * @param bmp
     * @param width
     * @param height
     * @return
     */
    public static Bitmap scale(Bitmap bmp, int width, int height) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);
        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
    }

    /**
     * 等比高度缩放
     *
     * @param bmp
     * @param height
     * @return
     */
    public static Bitmap scaleAccordingToHeight(Bitmap bmp, int height) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        float f = (height + 0.00f) / bmpHeght;
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
    }

    /**
     * 等比宽度缩放
     *
     * @param bmp
     * @param width
     * @return
     */
    public static Bitmap scaleAccordingToWidth(Bitmap bmp, int width) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        float f = (width + 0.00f) / bmpWidth;
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
    }
}
