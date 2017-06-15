package com.szdd.qianxun.tools.all;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.szdd.qianxun.advertise.ZoomImage;
import com.szdd.qianxun.tools.alipay.AlipayBuilder;
import com.szdd.qianxun.tools.alipay.AlipayEasy;
import com.szdd.qianxun.tools.alipay.AlipayListener;
import com.szdd.qianxun.tools.bitmap.BitmapEasy;
import com.szdd.qianxun.tools.bitmap.BitmapListener;
import com.szdd.qianxun.tools.bitmap.BitmapTool;
import com.szdd.qianxun.tools.bitmap.ImageLoader;
import com.szdd.qianxun.tools.connect.ConnectEasy;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.map.Location;
import com.szdd.qianxun.tools.map.LocationListener;
import com.szdd.qianxun.tools.map.LocationTransListener;
import com.szdd.qianxun.tools.thread.Run;
import com.szdd.qianxun.tools.thread.RunListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 静态方法集合类
 */
public class StaticMethod {
    /**
     * 从指定网址GET图片
     *
     * @param url      网址
     * @param listener 监听回调
     */
    public static void GET(String url, BitmapListener listener) {
        BitmapEasy.GET(url, listener);
    }

    /**
     * 向指定网址发起post请求
     *
     * @param context  context
     * @param url      网址
     * @param listener 监听回调
     */
    public static void POST(Context context, String url,
                            ConnectListener listener) {
        ConnectEasy.POST(context, url, listener);
    }

    /**
     * 轻量级多线程封装，比自己写Handler与Async省力
     *
     * @param listener 线程监听
     */
    public static void RUN(RunListener listener) {
        Run.RUN(listener);
    }


    /**
     * 获取网络图片，加载到ImageView，使用请求队列
     */
    public static void BITMAP(Context context, String url, ImageView view) {
        // BitmapEasy.BITMAP(context, url, view);
        UBITMAP(url, view);
    }

    /**
     * 获取网络图片，加载到ImageView，使用请求队列
     */
    public static void UBITMAP(String url, ImageView view) {
        com.nostra13.universalimageloader.core.
                ImageLoader.getInstance().displayImage(url, view, BitmapEasy.mOptions);
    }

    /**
     * 立即获取网络图片，加载到ImageView，头像专用
     */
    public static void UBITMAP(final String url, final BitmapListener listener) {
        com.nostra13.universalimageloader.core.
                ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            public void onLoadingStarted(String s, View view) {
            }

            public void onLoadingFailed(String s, View view, FailReason failReason) {
                listener.onResponse(null);
            }

            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                listener.onResponse(bitmap);
            }

            public void onLoadingCancelled(String s, View view) {
            }
        });
    }

    /**
     * 立即获取网络图片，加载到ImageView
     */
    public static void BITMAPNOW(Context context, String url, ImageView view) {
        BitmapEasy.BITMAPNOW(context, url, view);
    }

    /**
     * 立即获取网络图片，加载到ImageView，头像专用
     */
    public static void BITMAPHEAD(Context context, final String url, final ImageView img_view) {
        //BitmapEasy.BITMAPHEAD(context, url, img_view);
        UBITMAPHEAD(url, img_view);
    }

    /**
     * 立即获取网络图片，加载到ImageView，头像专用
     */
    public static void UBITMAPHEAD(final String url, final ImageView img_view) {
        com.nostra13.universalimageloader.core.
                ImageLoader.getInstance().displayImage(url, img_view, BitmapEasy.mHeadOptions);
    }

    /**
     * 立即获取网络图片，加载到ImageView，头像专用
     */
    public static void UBITMAPHEAD(final String url, final BitmapListener listener) {
        com.nostra13.universalimageloader.core.
                ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            public void onLoadingStarted(String s, View view) {
            }

            public void onLoadingFailed(String s, View view, FailReason failReason) {
                if (listener != null)
                    listener.onResponse(null);
            }

            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (listener != null)
                    listener.onResponse(bitmap);
            }

            public void onLoadingCancelled(String s, View view) {
            }
        });
    }

    /**
     * 获取网络图片，返回bitmap，使用请求队列
     */
    public static void BITMAP(Context context, String url, final BitmapListener listener) {
        //BitmapEasy.BITMAP(context, url, listener);
        UBITMAP(url, listener);
    }

    /**
     * 立即获取网络图片，返回bitmap
     */
    public static void BITMAPNOW(Context context, String url, final BitmapListener listener) {
        BitmapEasy.BITMAPNOW(context, url, listener);
    }


    /**
     * 立即获取网络PNG图片，使用请求队列
     */
    public static void PNG(Context context, String url, final BitmapListener listener) {
        BitmapEasy.PNG(context, url, listener);
    }

    /**
     * 立即获取网络JPG图片，使用请求队列
     */
    public static void JPG(Context context, String url, final BitmapListener listener) {
        BitmapEasy.JPG(context, url, listener);
    }

    /**
     * 立即获取网络PNG图片
     */
    public static void PNGNOW(Context context, String url, final BitmapListener listener) {
        BitmapEasy.PNGNOW(context, url, listener);
    }

    /**
     * 立即获取网络JPG图片
     */
    public static void JPGNOW(Context context, String url, final BitmapListener listener) {
        BitmapEasy.JPGNOW(context, url, listener);
    }

    /**
     * 加载本地图片
     *
     * @param local_path 本地图片路径
     * @param img_view   要显示的ImageView。需要指定宽高。
     */
    public static void LoadImage(String local_path, ImageView img_view) {
        ImageLoader.getInstance().loadImage(local_path, img_view);
    }

    /**
     * 获取当前位置
     *
     * @param applationContext 上下文
     * @param listener         回调监听
     */
    public static void Location(Context applationContext, LocationListener listener) {
        Location.getLocation(applationContext, listener);
    }

    /**
     * 将地理位置坐标转换为名称
     *
     * @param x        经度
     * @param y        纬度
     * @param listener 回调监听
     */
    public static void LocationTrans(double x, double y, LocationTransListener listener) {
        Location.transLocation(x, y, listener);
    }

    public static void downloadToPhone(Context context, Bitmap bitmap, String filepath) {
        ZoomImage.saveImageToGallery(context, bitmap, filepath);
    }

    /**
     * 统一压缩算法
     */
    public static boolean zip(String old_path, String new_path) {
        return BitmapTool.zip(old_path, new_path);
    }

    /**
     * 支付宝支付方法
     *
     * @param activity Activity
     * @param builder  文本构造器，便于封装
     * @param listener 结果监听
     */
    public static void PAY(Activity activity, AlipayBuilder builder, AlipayListener listener) {
        AlipayEasy.pay(activity, builder, listener);
    }

    /**
     * 等级转橙子数量
     */
    public static int changeLevel(int rank) {
        int n = 0;
        if (rank >= 16000) n = 5;
        else if (rank >= 8000) n = 4;
        else if (rank >= 4000) n = 3;
        else if (rank >= 2000) n = 2;
        else if (rank >= 1000) n = 1;
        return n;
    }

    /**
     * 统一压缩算法
     */
    public static Bitmap zip(String path) {
        return BitmapTool.zip(path);
    }

    /**
     * 统一压缩算法
     */
    public static Bitmap zip(Bitmap bitmap) {
        return BitmapTool.zip(bitmap);
    }

    /**
     * 格式化小数，保留2位。（小数点前多位）
     *
     * @param num
     * @return
     */
    public static String formatDouble(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        String dis_text = df.format(num);
        return dis_text;
    }

    /**
     * 格式化日期时间
     */
    public static String formatDateTime(String type, long time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(type, Locale.CHINA);
            String text = format.format(new Date(time));
            return text;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化日期时间
     */
    public static String formatDateTime(String type) {
        return formatDateTime(type, System.currentTimeMillis());
    }

    /**
     * 拨打电话，是跳转到拨号盘界面
     */
    public static boolean callTo(Context context, String phone) {
        try {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + phone));
            phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(phoneIntent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 发送短信，是跳转到发短信界面
     */
    public static boolean msgTo(Context context, String phone) {
        try {
            Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
                    Uri.parse("smsto:" + phone));
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
