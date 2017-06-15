package com.szdd.qianxun.message.fun.map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.szdd.qianxun.R;
import com.szdd.qianxun.message.baichuan.util.AndTools;
import com.szdd.qianxun.tools.map.Location;
import com.szdd.qianxun.tools.map.LocationListener;
import com.szdd.qianxun.tools.map.overlayutil.WalkingRouteOverlay;

//最后把toast与test_move删除就是正确的
public class MapShowDialog extends Dialog {
    private static final int HANDLER_INT = 2;
    private static final int WAIT_TIME = 20 * 1000;// 20s
    private boolean update_running = true;
    private Handler handler;
    private Context activity;
    private MapView mMapView = null;
    private BaiduMap mBaidumap = null;
    private double friend_x = 0, friend_y = 0;
    private WalkingRouteOverlay overlay = null;
    private RoutePlanSearch mSearch = null;
    private double test_move = 0.1;// 测试专用

    public MapShowDialog(Context activity) {
        super(activity, R.style.MyMapDialogTheme);
        this.activity = activity;
        setCancelable(false);
    }

    public MapShowDialog(Context activity, double friend_x, double friend_y) {
        super(activity, R.style.MyMapDialogTheme);
        this.activity = activity;
        this.friend_x = friend_x;
        this.friend_y = friend_y;
        setCancelable(false);
    }

    // 初始化handler
    private void initHandler() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == HANDLER_INT) {
                    showLocation();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void startUpdateThread() {
        new Thread() {
            public void run() {
                while (update_running) {
                    handler.sendEmptyMessage(HANDLER_INT);
                    try {
                        Thread.sleep(WAIT_TIME);
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 布局
        setContentView(R.layout.dialog_map_show);
        TextView tv_title = ((TextView) findViewById(R.id.top_text));
        tv_title.setText("点对点地图");
        showBackButton();
        // handler
        initHandler();
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

        // 实时刷新显示用户位置
        update_running = true;
        startUpdateThread();
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

    public void showBackButton() {
        ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
        top_back.setVisibility(ImageButton.VISIBLE);
        top_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelDialog();
            }
        });
    }

    private void cancelDialog() {// 所有的退出方式
        // ///////////////////////////
        // AndTools.showToast(activity, "CANCEL");

        MapShowDialog.this.dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        update_running = false;// 终止线程
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

    private void showLocation() {
        Location.getLocation(activity, new LocationListener() {
            public void locationRespose(String locationName, double x,
                                        double y, float limit) {
                // 在地图上显示位置
                if (friend_x != 0 && friend_y != 0) {
                    showInMap(x, y);
                } else {
                    AndTools.showToast(activity, "获取到对方位置失败");
                    showMyLocation(x, y, limit);
                }
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

    private void showInMap(double my_x, double my_y) {
        if (mSearch != null)
            mSearch.destroy();
        mSearch = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            public void onGetWalkingRouteResult(WalkingRouteResult result) {// 步行
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR
                        || result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 空值、有错、或有地点歧义
                    Toast.makeText(activity, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                // 添加到地图中
                WalkingRouteLine route = result.getRouteLines().get(0);
                if (overlay != null) {
                    overlay.removeFromMap();// 移除
                    overlay = new WalkingRouteOverlay(mBaidumap);
                    overlay.setData(route);
                    overlay.addToMap();
                } else {
                    overlay = new WalkingRouteOverlay(mBaidumap);
                    overlay.setData(route);
                    overlay.addToMap();
                    overlay.zoomToSpan();// 缩放
                }
            }

            public void onGetTransitRouteResult(TransitRouteResult result) {// 公交
            }

            public void onGetDrivingRouteResult(DrivingRouteResult result) {// 驾车
            }

            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            }
        };
        mSearch.setOnGetRoutePlanResultListener(listener);
        LatLng lat_start = new LatLng(my_y + test_move, my_x + test_move);
        LatLng lat_end = new LatLng(friend_x, friend_y);
        PlanNode stNode = PlanNode.withLocation(lat_start);
        PlanNode enNode = PlanNode.withLocation(lat_end);
        mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
                enNode));

//		Toast.makeText(activity, "路线测试", Toast.LENGTH_SHORT).show();
//		test_move = test_move - 0.01;// /////////////////////////
//		Log.e("BaiduLocationApi_move", "EEEE  " + test_move + "");
    }

}
