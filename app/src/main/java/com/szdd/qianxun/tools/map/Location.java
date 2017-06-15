package com.szdd.qianxun.tools.map;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class Location implements BDLocationListener {
    private LocationClient mLocationClient = null;
    private LocationListener listener;
    private Context context;
    private String location_name;
    private double x, y;
    private float limit;
    private boolean is_first = true;// 需要有这个
    private boolean force_update = false;

    private Location(Context applationContext, LocationListener listener, boolean force_update) {
        this.context = applationContext;
        this.listener = listener;
        this.force_update = force_update;
    }

    private void initLocation() {
        if (!force_update) {
            AnLocation old_location = LocationCache.getLocation(context);
            location_name = old_location.getLocationName();
            if (location_name != null && !location_name.equals("")) {
                listener.locationRespose(location_name,
                        old_location.getX(), old_location.getY(), old_location.getLimit());
                return;
            }
        }
        // SDKInitializer.initialize(context);// 请在界面之前调用一次
        mLocationClient = new LocationClient(context); // 声明LocationClient类
        mLocationClient.registerLocationListener(this); // 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系(要么是百度坐标，或者是国测局02坐标)
        option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (is_first) {
            is_first = false;
        } else {
            return;
        }
        if (location.getLocType() == BDLocation.TypeGpsLocation
                || location.getLocType() == BDLocation.TypeNetWorkLocation
                || location.getLocType() == BDLocation.TypeOffLineLocation) {// 定位成功
            location_name = location.getAddrStr();
            x = location.getLongitude();
            y = location.getLatitude();
            limit = location.getRadius();
        } else {
            location_name = "";
            x = 0;
            y = 0;
            limit = 0;
        }
        AnLocation old_location = new AnLocation(location_name, x, y, limit);
        LocationCache.saveLocation(context, old_location);
        if (listener != null)
            listener.locationRespose(location_name, x, y, limit);
    }

    // ///////////////////////基于回调的方法///////////////////////

    /**
     * 获取位置信息
     *
     * @param applationContext 全局的context
     * @param listener         监听回调
     */
    public static void getLocation(Context applationContext,
                                   LocationListener listener) {
        new Location(applationContext, listener, false).initLocation();
    }

    /**
     * 获取位置信息
     *
     * @param applationContext 全局的context
     * @param listener         监听回调
     * @param force_update     是否强制刷新
     */
    public static void getLocation(Context applationContext,
                                   LocationListener listener, boolean force_update) {
        new Location(applationContext, listener, force_update).initLocation();
    }

    /**
     * * 百度地图的位置转换，将坐标位置转换为地名
     *
     * @param x        要转换的x
     * @param y        要转换的y
     * @param listener 监听回调
     */
    public static void transLocation(double x, double y,
                                     final LocationTransListener listener) {
        GeoCoder mSearch = GeoCoder.newInstance();
        OnGetGeoCoderResultListener lis = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    listener.locationRespose("");
                } else {
                    //获取地理编码结果
                    listener.locationRespose(result.getAddress());
                }
            }

            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    listener.locationRespose("");
                } else {
                    //获取反向地理编码结果
                    listener.locationRespose(result.getAddress());
                }
            }
        };
        mSearch.setOnGetGeoCodeResultListener(lis);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(y, x)));
//        mSearch.destroy();
    }


}