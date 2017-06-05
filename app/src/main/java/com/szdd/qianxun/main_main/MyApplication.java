package com.szdd.qianxun.main_main;

import android.app.Application;
import android.graphics.Bitmap;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.im.CustomSampleHelper;
import com.szdd.qianxun.message.baichuan.im.LoginSampleHelper;
import com.szdd.qianxun.sell.discuss.DiscussTool;
import com.szdd.qianxun.tools.bitmap.BitmapEasy;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.CacheTool;
import com.szdd.qianxun.tools.font.FontTool;
import com.umeng.onlineconfig.OnlineConfigAgent;

import cn.smssdk.SMSSDK;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initBaiduMap();
        initBaiChuan();
        initUMParam();
        initUMDiscuss();
        initMobMessage();
        initImageLoader();
        initStringCache();

//        initDefaultFont();
    }

    private void initBaiChuan() {
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
        //初始化IM SDK
        if (SysUtil.isMainProcess(this)) {//只能在主进程进行初始化
            //  以下步骤调用顺序有严格要求，请按照示例的步骤的顺序调用！
            //［IM定制初始化］由于增加全局初始化，该配置需最先执行！
            CustomSampleHelper.initCustom();
            //SDK初始化
            LoginSampleHelper.getInstance().initSDK_Sample(this);
            //后期将使用Override的方式进行集中配置，请参照YWSDKGlobalConfigSample
            YWAPI.enableSDKLogOutput(true);
        }
    }

    private void initDefaultFont() {
        // 处理字体
        FontTool.dealFont(this);
        // //强制修改字体
//        /*
//         * 如果我们将系统的TextAppearance改为monospace，修改方法就是在系统样式中指定默认的typeface为monospace
//		 * 然后，通过反射，修改monospace字体，即可实现全局修改字体
//		 */
//        String value = ParamTool.getParam("font_type");
//        if (value.equals("")) {// 首次运行可能无法获取到在线参数，使用初始字体
//        } else if (value.equals("0")) {// 默认字体
//        } else if (value.equals("1")) {
//            FontsOverride.setDefaultFont(this, "MONOSPACE", "fangzheng.ttf");//已删除
//        }
    }

    /**
     * 初始化百度地图
     */
    private void initBaiduMap() {
        SDKInitializer.initialize(this);
    }

    /**
     * 初始化通知推送
     */
    private void initUMParam() {
        //测试模式(将禁用刷新间隔)
        if (!ServerURL.isTest())
            OnlineConfigAgent.getInstance().setDebugMode(true);
        OnlineConfigAgent.getInstance().updateOnlineConfig(this);
    }
    /**
     * 初始化通知推送
     */
    private void initUMDiscuss() {
        DiscussTool.getInstance().initSDK(this);
    }

    /**
     * 初始化短信验证
     */
    private void initMobMessage() {
        SMSSDK.initSDK(this, "ef7ec3e81c82", "88d498f659bd84de211fcbe7ebc11f89");
    }

    /**
     * 初始化图片加载
     */
    private void initImageLoader() {
        //ImageLoader.getInstance().init(
        //        ImageLoaderConfiguration.createDefault(this));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(200 * 1024 * 1024) // 200 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
        //统一使用
        BitmapEasy.mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_img_load_ing)   //加载过程中
                .showImageForEmptyUri(R.drawable.icon_img_load_fail) //uri为空时
                .showImageOnFail(R.drawable.icon_img_load_fail)      //加载失败时
                .cacheOnDisk(true)
                .cacheInMemory(true)                             //允许cache在内存和磁盘中
                .bitmapConfig(Bitmap.Config.RGB_565)             //图片压缩质量参数
                .build();
        BitmapEasy.mHeadOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.mascot_orange)   //加载过程中
                .showImageForEmptyUri(R.drawable.mascot_orange) //uri为空时
                .showImageOnFail(R.drawable.mascot_orange)      //加载失败时
                .cacheOnDisk(false)
                .cacheInMemory(true)                             //允许cache在内存和磁盘中
                .bitmapConfig(Bitmap.Config.RGB_565)             //图片压缩质量参数
                .build();
    }

    /**
     * 初始化字符串缓存机制
     */
    private void initStringCache() {
        CacheTool.initCache(this);
    }

}