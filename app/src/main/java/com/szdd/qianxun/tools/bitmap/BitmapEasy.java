package com.szdd.qianxun.tools.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.szdd.qianxun.R;
import com.szdd.qianxun.main_main.QianxunApplication;

import java.io.IOException;

public class BitmapEasy extends AsyncTask<Void, Void, Bitmap> {
    private String url;
    private BitmapListener listener;

    public BitmapEasy(String url, BitmapListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return BitmapTool.getBitmapFromURL(url);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (listener != null)
            listener.onResponse(result);
    }

    // ///////////////////////基于回调的方法///////////////////////

    /**
     * 从指定网址GET图片
     *
     * @param url      网址
     * @param listener 监听回调
     */
    public static void GET(String url, BitmapListener listener) {
//        BitmapEasy connect = new BitmapEasy(url, listener);
//        connect.execute();
        BITMAP(QianxunApplication.getInstance(), url, listener);
    }

    //////////////////////////////////////////////////////////

    private static ImageLoader.ImageCache cache = new ImageCacheUtil();
    private static RequestQueue queue = null;
    private static ImageLoader loader = null;

    /**
     * 获取网络图片，加载到ImageView，使用请求队列
     */
    public static void BITMAP(Context context, String url, ImageView view) {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
        if (loader == null)
            loader = new ImageLoader(queue, cache);
        url = dealUrl(url);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view,
                R.drawable.icon_img_load_ing, R.drawable.icon_img_load_fail);
        loader.get(url, listener);
    }

    /**
     * 获取网络图片，返回bitmap，使用请求队列
     */
    public static void BITMAP(Context context, String url, final BitmapListener listener) {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
        if (loader == null)
            loader = new ImageLoader(queue, cache);
        url = dealUrl(url);
        loader.get(url,
                new ImageLoader.ImageListener() {
                    public void onErrorResponse(VolleyError error) {
                        listener.onResponse(null);
                    }

                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        listener.onResponse(response.getBitmap());//失败也是null
                    }
                });
    }

    /**
     * 立即获取网络图片，返回bitmap
     */
    public static void BITMAPNOW(Context context, String url, final BitmapListener listener) {
        url = dealUrl(url);
        new ImageLoader(Volley.newRequestQueue(context), cache).get(url,
                new ImageLoader.ImageListener() {
                    public void onErrorResponse(VolleyError error) {
                        listener.onResponse(null);
                    }

                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        listener.onResponse(response.getBitmap());//失败也是null
                    }
                });
    }

    /**
     * 立即获取网络图片，加载到ImageView
     */
    public static void BITMAPNOW(Context context, String url, ImageView view) {
        url = dealUrl(url);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view,
                R.drawable.icon_img_load_ing, R.drawable.icon_img_load_fail);
        new ImageLoader(Volley.newRequestQueue(context), cache).get(url, listener);
    }

    /**
     * 立即获取网络图片，加载到ImageView，头像专用
     */
    public static void BITMAPHEAD(final Context context, final String url, final ImageView img_view) {
        final String new_url = dealUrl(url);
        img_view.setImageResource(R.drawable.mascot_orange);
        PNG(context, new_url, new BitmapListener() {//确保头像刷新
            public void onResponse(Bitmap bitmap) {
                if (bitmap == null) {
                    ImageLoader.ImageListener listener = ImageLoader.getImageListener(img_view,
                            R.drawable.mascot_orange, R.drawable.mascot_orange);//之后修改
                    new ImageLoader(Volley.newRequestQueue(context), cache).get(new_url, listener);
                } else {
                    img_view.setImageBitmap(bitmap);
                }
            }
        });
    }

    /**
     * 立即获取网络PNG图片，使用请求队列
     */
    public static void PNG(Context context, String url, final BitmapListener listener) {
        BITMAPNOW(context, url, listener, true, false);
    }

    /**
     * 立即获取网络JPG图片，使用请求队列
     */
    public static void JPG(Context context, String url, final BitmapListener listener) {
        BITMAPNOW(context, url, listener, false, false);
    }

    /**
     * 立即获取网络PNG图片
     */
    public static void PNGNOW(Context context, String url, final BitmapListener listener) {
        BITMAPNOW(context, url, listener, true, true);
    }

    /**
     * 立即获取网络JPG图片
     */
    public static void JPGNOW(Context context, String url, final BitmapListener listener) {
        BITMAPNOW(context, url, listener, false, true);
    }

    /**
     * 立即获取图片，通过BitmapListener返回图片
     */
    private static void BITMAPNOW(Context context, String url, final BitmapListener listener,
                                  boolean is_png, boolean need_new_thread) {
        url = dealUrl(url);
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    public void onResponse(Bitmap response) {
                        listener.onResponse(response);
                    }
                }, 0, 0, is_png ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        listener.onResponse(null);
                    }
                });
        if (need_new_thread) {
            RequestQueue queue_temp = Volley.newRequestQueue(context);
            queue_temp.add(imageRequest);
            queue_temp.start();
        } else {
            if (queue == null)
                queue = Volley.newRequestQueue(context);
            queue.add(imageRequest);
        }
    }

    /**
     * 清除图片的全部磁盘缓存。
     */
    public static void clearBitmapCache() {
        try {
            DiskLruCache.deleteContents(ImageCacheUtil.getDiskCacheDir(
                    QianxunApplication.getInstance(), ImageCacheUtil.mCachePath));
        } catch (IOException e) {
        }
    }

    /**
     * 处理地址防止报错
     */
    private static String dealUrl(String url) {
        if (url == null)
            url = "";
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }
}

