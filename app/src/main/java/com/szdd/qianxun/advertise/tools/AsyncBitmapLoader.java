package com.szdd.qianxun.advertise.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapListener;
import com.szdd.qianxun.tools.bitmap.BitmapTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by LLX on 2015/11/11.
 */
public class AsyncBitmapLoader {
    public static final String file_path = "/mnt/sdcard/GaoNeng/advertise/";

    /**
     * 内存图片软引用缓冲
     */
    private HashMap<String, SoftReference<Bitmap>> imageCache = null;

    public AsyncBitmapLoader() {
        imageCache = new HashMap<String, SoftReference<Bitmap>>();
    }

    public Bitmap loadBitmap(final ImageView imageView, final String imageURL,
                             final ImageCallBack imageCallBack) {
        // 在内存缓存中，则返回Bitmap对象
        if (imageCache.containsKey(imageURL)) {
            SoftReference<Bitmap> reference = imageCache.get(imageURL);
            Bitmap bitmap = reference.get();
            if (bitmap != null) {
                return bitmap;
            }
        } else {
            // 加上一个对本地缓存的查找

            String bitmapName = imageURL
                    .substring(imageURL.lastIndexOf("/") + 1);
            File cacheDir = new File(file_path);
            File[] cacheFiles = cacheDir.listFiles();
            int i = 0;
            if (null != cacheFiles) {
                for (; i < cacheFiles.length; i++) {
                    if (bitmapName.equals(cacheFiles[i].getName())) {
                        break;
                    }
                }
                if (i < cacheFiles.length) {
                    return BitmapFactory.decodeFile(file_path + bitmapName);
                }
            }
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
            }
        };
        // 如果不在内存缓存中，也不在本地，则开启线程下载图片
        new Thread() {
            /*
             * (non-Javadoc)
             *
             * @seejava.lang.Thread#run()
             */
            @Override
            public void run() {
                InputStream bitmapIs = HttpUtils.getStreamFromURL(imageURL);
                Bitmap bitmap = BitmapFactory.decodeStream(bitmapIs);
                imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
                Message msg = handler.obtainMessage(0, bitmap);
                handler.sendMessage(msg);
                File dir = new File(file_path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File bitmapFile = new File(file_path
                        + imageURL.substring(imageURL.lastIndexOf("/") + 1));
                if (!bitmapFile.exists()) {
                    try {
                        bitmapFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(bitmapFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return null;
    }

    public Bitmap loadBitmap(final String imageURL,
                             final BitmapListener listener) {
        // 在内存缓存中，则返回Bitmap对象
        if (imageCache.containsKey(imageURL)) {
            SoftReference<Bitmap> reference = imageCache.get(imageURL);
            Bitmap bitmap = reference.get();
            if (bitmap != null) {
                return bitmap;
            }
        } else {
            // 加上一个对本地缓存的查找
            String bitmapName = imageURL
                    .substring(imageURL.lastIndexOf("/") + 1);
            File cacheDir = new File("/mnt/sdcard/Qianxun/");
            File[] cacheFiles = cacheDir.listFiles();
            int i = 0;
            if (null != cacheFiles) {
                for (; i < cacheFiles.length; i++) {
                    if (bitmapName.equals(cacheFiles[i].getName())) {
                        break;
                    }
                }
                if (i < cacheFiles.length) {
                    return BitmapFactory.decodeFile("/mnt/sdcard/Qianxun/"
                            + bitmapName);
                }
            }
        }
        StaticMethod.GET(imageURL, new BitmapListener() {
            public void onResponse(Bitmap bitmap) {
                imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
                File dir = new File("/mnt/sdcard/Qianxun/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File bitmapFile = new File("/mnt/sdcard/Qianxun/"
                        + imageURL.substring(imageURL.lastIndexOf("/") + 1));
                if (!bitmapFile.exists()) {
                    try {
                        bitmapFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                BitmapTool.writeToFile(bitmap, "jpg",
                        bitmapFile.getAbsolutePath());
                listener.onResponse(bitmap);
            }
        });
        return null;
    }

    public Bitmap loadBitmap(final String filepath, final String imageURL,
                             final BitmapListener listener) {
        // 在内存缓存中，则返回Bitmap对象
        if (imageCache.containsKey(imageURL)) {
            SoftReference<Bitmap> reference = imageCache.get(imageURL);
            Bitmap bitmap = reference.get();
            if (bitmap != null) {
                return bitmap;
            }
        } else {
            // 加上一个对本地缓存的查找
            String bitmapName = imageURL
                    .substring(imageURL.lastIndexOf("/") + 1);
            File cacheDir = new File(filepath);
            File[] cacheFiles = cacheDir.listFiles();
            int i = 0;
            if (null != cacheFiles) {
                for (; i < cacheFiles.length; i++) {
                    if (bitmapName.equals(cacheFiles[i].getName())) {
                        break;
                    }
                }
                if (i < cacheFiles.length) {
                    return BitmapFactory.decodeFile(filepath + bitmapName);
                }
            }
        }
        StaticMethod.GET(imageURL, new BitmapListener() {
            public void onResponse(Bitmap bitmap) {
                imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
                File dir = new File(filepath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File bitmapFile = new File(filepath + imageURL.substring(imageURL.lastIndexOf("/") + 1));
                if (!bitmapFile.exists()) {
                    try {
                        bitmapFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                BitmapTool.writeToFile(bitmap, "png",
                        bitmapFile.getAbsolutePath());
                listener.onResponse(bitmap);
            }
        });
        return null;
    }

    public interface ImageCallBack {
        public void imageLoad(ImageView imageView, Bitmap bitmap);
    }
}
