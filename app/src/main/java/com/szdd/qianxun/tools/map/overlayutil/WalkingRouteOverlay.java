package com.szdd.qianxun.tools.map.overlayutil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.WalkingRouteLine;

public class WalkingRouteOverlay extends OverlayManager {
	private WalkingRouteLine c = null;

	public WalkingRouteOverlay(BaiduMap paramBaiduMap) {
		super(paramBaiduMap);
	}

	public void setData(WalkingRouteLine paramWalkingRouteLine) {
		this.c = paramWalkingRouteLine;
	}

	public final List<OverlayOptions> getOverlayOptions() {
		if (this.c == null)
			return null;
		ArrayList localArrayList1 = new ArrayList();
		Object localObject1;
		Object localObject2;
		Object localObject3;
		if ((this.c.getAllStep() != null) && (this.c.getAllStep().size() > 0)) {
			localObject1 = this.c.getAllStep().iterator();
			while (((Iterator) localObject1).hasNext()) {
				localObject2 = (WalkingRouteLine.WalkingStep) ((Iterator) localObject1)
						.next();
				localObject3 = new Bundle();
				((Bundle) localObject3).putInt("index", this.c.getAllStep()
						.indexOf(localObject2));
				if (((WalkingRouteLine.WalkingStep) localObject2).getEntrance() != null)
					localArrayList1
							.add(new MarkerOptions()
									.position(
											((WalkingRouteLine.WalkingStep) localObject2)
													.getEntrance()
													.getLocation())
									.rotate(360 - ((WalkingRouteLine.WalkingStep) localObject2)
											.getDirection())
									.zIndex(10)
									.anchor(0.5F, 0.5F)
									.extraInfo((Bundle) localObject3)
									.icon(BitmapDescriptorFactory
											.fromAssetWithDpi("Icon_line_node.png")));
				if ((this.c.getAllStep().indexOf(localObject2) == this.c
						.getAllStep().size() - 1)
						&& (((WalkingRouteLine.WalkingStep) localObject2)
								.getExit() != null))
					localArrayList1
							.add(new MarkerOptions()
									.position(
											((WalkingRouteLine.WalkingStep) localObject2)
													.getExit().getLocation())
									.anchor(0.5F, 0.5F)
									.zIndex(10)
									.icon(BitmapDescriptorFactory
											.fromAssetWithDpi("Icon_line_node.png")));
			}
		}
		if (this.c.getStarting() != null)
			localArrayList1.add(new MarkerOptions()
					.position(this.c.getStarting().getLocation())
					.icon(getStartMarker() != null ? getStartMarker()
							: BitmapDescriptorFactory
									.fromAssetWithDpi("Icon_start.png"))
					.zIndex(10));
		if (this.c.getTerminal() != null)
			localArrayList1.add(new MarkerOptions()
					.position(this.c.getTerminal().getLocation())
					.icon(getTerminalMarker() != null ? getTerminalMarker()
							: BitmapDescriptorFactory
									.fromAssetWithDpi("Icon_end.png"))
					.zIndex(10));
		if ((this.c.getAllStep() != null) && (this.c.getAllStep().size() > 0)) {
			localObject1 = null;
			localObject2 = this.c.getAllStep().iterator();
			while (((Iterator) localObject2).hasNext()) {
				localObject3 = (WalkingRouteLine.WalkingStep) ((Iterator) localObject2)
						.next();
				List localList = ((WalkingRouteLine.WalkingStep) localObject3)
						.getWayPoints();
				if (localList != null) {
					ArrayList localArrayList2 = new ArrayList();
					if (localObject1 != null)
						localArrayList2.add(localObject1);
					localArrayList2.addAll(localList);
					localArrayList1.add(new PolylineOptions()
							.points(localArrayList2)
							.width(10)
							.color(getLineColor() != 0 ? getLineColor() : Color
									.argb(178, 0, 78, 255)).zIndex(0));
					localObject1 = (LatLng) localList.get(localList.size() - 1);
				}
			}
		}
		return localArrayList1;
	}

	public BitmapDescriptor getStartMarker() {
		return null;
	}

	public int getLineColor() {
		return 0;
	}

	public BitmapDescriptor getTerminalMarker() {
		return null;
	}

	public boolean onRouteNodeClick(int paramInt) {
		if ((this.c.getAllStep() != null)
				&& (this.c.getAllStep().get(paramInt) != null))// 本来它是Toast
			Log.e("BaiduMap_onRouteNodeClick",
					((WalkingRouteLine.WalkingStep) this.c.getAllStep().get(
							paramInt)).getInstructions());
		return false;
	}

	public final boolean onMarkerClick(Marker paramMarker) {
		Iterator localIterator = this.b.iterator();
		while (localIterator.hasNext()) {
			Overlay localOverlay = (Overlay) localIterator.next();
			if (((localOverlay instanceof Marker))
					&& (localOverlay.equals(paramMarker))
					&& (paramMarker.getExtraInfo() != null))
				onRouteNodeClick(paramMarker.getExtraInfo().getInt("index"));
		}
		return true;
	}
}

/*
 * Location: D:\理务关一号\下载\百度地图SDK\BaiduMap_AndroidSDK_v3.4
 * .0_All\BaiduMap_AndroidSDK_v3.4.0_Lib\baidumapapi_v3_4_0.jar Qualified Name:
 * com.baidu.mapapi.overlayutil.WalkingRouteOverlay JD-Core Version: 0.6.2
 */