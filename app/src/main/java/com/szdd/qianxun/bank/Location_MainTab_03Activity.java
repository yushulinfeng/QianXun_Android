package com.szdd.qianxun.bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.szdd.qianxun.R;

import java.util.List;

public class Location_MainTab_03Activity extends Activity implements BDLocationListener {
	MapView mMapView = null;
	BaiduMap mBaidumap = null;
	String address;
	double latitude,longitude;
	
	public void onCreate(Bundle savedInstanceState) {
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		super.setContentView(R.layout.map_test);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaidumap = mMapView.getMap();
		// 显示用户位置
		showLocation();

		// 返回按钮
		showBackButton();
		
	}
//	public void showContextMenu() {
//	}

	
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	// /////////////////////////////////////定位
	public LocationClient mLocationClient = null;
	private boolean is_first = true;

	private void showLocation() {
		onLocationCreate();
		initLocation();
		mLocationClient.start();
	}

	private void onLocationCreate() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(this); // 注册监听函数
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系(要么是百度坐标"bd09ll"(更好)，或者是国测局02坐标"gcj02")
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// Receive Location
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());//结果类型
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());//纬度
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());//经度
		sb.append("\nradius : ");
		sb.append(location.getRadius());//精度范围
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());// 单位：公里每小时
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\nheight : ");
			sb.append(location.getAltitude());// 单位：米
			sb.append("\ndirection : ");
			sb.append(location.getDirection());// 单位度
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());//位置，中国……软件园
			sb.append("\ndescribe : ");
			sb.append("gps定位成功");
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			// 运营商信息
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
			sb.append("\ndescribe : ");
			sb.append("网络定位成功");
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			sb.append("\ndescribe : ");
			sb.append("离线定位成功，离线定位结果也是有效的");
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			sb.append("\ndescribe : ");
			sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			sb.append("\ndescribe : ");
			sb.append("网络不同导致定位失败，请检查网络是否通畅");
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			sb.append("\ndescribe : ");
			sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
		}
		sb.append("\nlocationdescribe : ");
		sb.append(location.getLocationDescribe());// 位置语义化信息，在……附近
		List<Poi> list = location.getPoiList();// POI数据
		if (list != null) {
			sb.append("\npoilist size = : ");
			sb.append(list.size());
			for (Poi p : list) {
				sb.append("\npoi= : ");
				sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
			}
		}

		// 在地图上显示位置
		if (is_first) {
			showInMap(location);
			is_first = false;
		}
	}

	private void showInMap(BDLocation location) {
		// //缩放级别
		// MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
		// .zoomBy(8);
		// mBaidumap.animateMapStatus(mapStatusUpdate);
//		mMapView.getController().setZoom(13);

		// 开启定位图层
		mBaidumap.setMyLocationEnabled(true);
		// 构造定位数据
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		// 设置定位数据
		mBaidumap.setMyLocationData(locData);
		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		// BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
		// .fromResource(R.drawable.ic_launcher);
		// MyLocationConfiguration config = new MyLocationConfiguration(
		// com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL,
		// true, mCurrentMarker);
		// mBaidumap.setMyLocationConfiguration();

		// 设置我的位置为地图的中心点
		mBaidumap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(17.0f).build()));
		LatLng latlng = new LatLng(location.getLatitude(),
				location.getLongitude());
		mBaidumap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));

		// // 构建Marker图标
		// BitmapDescriptor bitmap = BitmapDescriptorFactory
		// .fromResource(R.drawable.ic_launcher);
		// // 构建MarkerOption，用于在地图上添加Marker
		// OverlayOptions option = new MarkerOptions().position(latlng).icon(
		// bitmap);
		// // 在地图上添加Marker，并显示
		// mBaidumap.addOverlay(option);

		// // 当不需要定位图层时关闭定位图层
		// mBaidumap.setMyLocationEnabled(false);


		((TextView) findViewById(R.id.top_text)).setTextSize(12);
		((TextView) findViewById(R.id.top_text)).setText(location.getAddrStr());
		 address = location.getAddrStr();//地址
		 latitude =  location.getLatitude();//纬度
		 longitude = location.getLongitude();//经度
	}

	
	public void showBackButton() {
		ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
		top_back.setVisibility(ImageButton.VISIBLE);
		top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent =getIntent() ;//new Intent(Location_MainTab_03Activity.this,MainActivity.class);
				Bundle mBundle = new Bundle(); 
				mBundle.putString("Address", address); 
				mBundle.putDouble("Latitude", latitude);
				mBundle.putDouble("Longitude", longitude);
			    intent.putExtras(mBundle); 
			    setResult(2,intent);
				finish();
			}
			
		});
	}
	
}
