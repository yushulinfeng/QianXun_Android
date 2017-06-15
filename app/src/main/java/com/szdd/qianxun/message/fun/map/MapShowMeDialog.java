package com.szdd.qianxun.message.fun.map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.szdd.qianxun.R;

//显示用户位置
public class MapShowMeDialog extends Dialog {
    private Context activity;
    private MapView mMapView = null;
    private BaiduMap mBaidumap = null;
    private double friend_x = 0, friend_y = 0;
    private String location_name = null;

    public MapShowMeDialog(Context activity) {
        super(activity, R.style.MyMapDialogTheme);
        this.activity = activity;
        setCancelable(false);
    }

    public MapShowMeDialog(Context activity, double friend_x, double friend_y, String location_name) {
        super(activity, R.style.MyMapDialogTheme);
        this.activity = activity;
        this.friend_x = friend_x;
        this.friend_y = friend_y;
        this.location_name = location_name;//null为默认标题
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 布局
        setContentView(R.layout.dialog_map_show);
        TextView tv_title = ((TextView) findViewById(R.id.top_text));
        tv_title.setText("查看地图");
        if (location_name != null)
            tv_title.setText(location_name);
        showBackButton();
        // 获取地图控件引用（此处添加到xml中会报错）
        mMapView = new MapView(activity);
        mMapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ((LinearLayout) findViewById(R.id.dia_map_show_layout))
                .addView(mMapView);
        mBaidumap = mMapView.getMap();
        // 显示指南针控件(此处必须先false再true)
        UiSettings mUiSettings = mBaidumap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setCompassEnabled(true);
        // 显示位置
        showMyLocation(friend_x, friend_y, 100.0f);
    }

    @Override
    public void show() {
        super.show();
        // 对话框全屏代码（写在onCreate中不成功）
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }

    private void showBackButton() {
        ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
        top_back.setVisibility(ImageButton.VISIBLE);
        top_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelDialog();
            }
        });
    }

    private void showMyLocation(double my_x, double my_y, float limit) {
        // 显示定位
        mBaidumap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder().accuracy(limit)
                .direction(100).latitude(my_y).longitude(my_x).build();
        mBaidumap.setMyLocationData(locData);
        // 设置为中心点
        mBaidumap.setMapStatus(MapStatusUpdateFactory
                .newMapStatus(new MapStatus.Builder().zoom(17.0f).build()));
        LatLng latlng = new LatLng(my_y, my_x);
        mBaidumap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
    }

    private void cancelDialog() {// 所有的退出方式
        MapShowMeDialog.this.dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mMapView.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
