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
import com.baidu.mapapi.search.route.TransitRouteLine;

public class TransitRouteOverlay extends OverlayManager {
	private TransitRouteLine c = null;

	public TransitRouteOverlay(BaiduMap paramBaiduMap) {
		super(paramBaiduMap);
	}

	public final List<OverlayOptions> getOverlayOptions() {
		if (this.c == null)
			return null;
		ArrayList localArrayList1 = new ArrayList();
		ArrayList localArrayList2;
		Iterator localIterator;
		TransitRouteLine.TransitStep localTransitStep;
		if ((this.c.getAllStep() != null) && (this.c.getAllStep().size() > 0)) {
			localArrayList2 = new ArrayList();
			localIterator = this.c.getAllStep().iterator();
			while (localIterator.hasNext()) {
				localTransitStep = (TransitRouteLine.TransitStep) localIterator
						.next();
				Bundle localBundle = new Bundle();
				localBundle.putInt("index",
						this.c.getAllStep().indexOf(localTransitStep));
				if (localTransitStep.getEntrance() != null)
					localArrayList1.add(new MarkerOptions()
							.position(
									localTransitStep.getEntrance()
											.getLocation()).anchor(0.5F, 0.5F)
							.zIndex(10).extraInfo(localBundle)
							.icon(a(localTransitStep)));
				if ((this.c.getAllStep().indexOf(localTransitStep) == this.c
						.getAllStep().size() - 1)
						&& (localTransitStep.getExit() != null))
					localArrayList1.add(new MarkerOptions()
							.position(localTransitStep.getExit().getLocation())
							.anchor(0.5F, 0.5F).zIndex(10)
							.icon(a(localTransitStep)));
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
			localArrayList2 = new ArrayList();
			localIterator = this.c.getAllStep().iterator();
			while (localIterator.hasNext()) {
				localTransitStep = (TransitRouteLine.TransitStep) localIterator
						.next();
				if (localTransitStep.getWayPoints() != null) {
					int i = 0;
					if (localTransitStep.getStepType() != TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING)
						i = getLineColor() != 0 ? getLineColor() : Color.argb(
								178, 0, 78, 255);
					else
						i = getLineColor() != 0 ? getLineColor() : Color.argb(
								178, 88, 208, 0);
					localArrayList1.add(new PolylineOptions()
							.points(localTransitStep.getWayPoints()).width(10)
							.color(i).zIndex(0));
				}
			}
		}
		return localArrayList1;
	}

	private BitmapDescriptor a(TransitRouteLine.TransitStep paramTransitStep) {
		switch (paramTransitStep.getStepType().ordinal()) {
		case 1:
			return BitmapDescriptorFactory
					.fromAssetWithDpi("Icon_bus_station.png");
		case 2:
			return BitmapDescriptorFactory
					.fromAssetWithDpi("Icon_subway_station.png");
		case 3:
			return BitmapDescriptorFactory
					.fromAssetWithDpi("Icon_walk_route.png");
		}
		return null;
	}

	public void setData(TransitRouteLine paramTransitRouteLine) {
		this.c = paramTransitRouteLine;
	}

	public BitmapDescriptor getStartMarker() {
		return null;
	}

	public BitmapDescriptor getTerminalMarker() {
		return null;
	}

	public int getLineColor() {
		return 0;
	}

	public boolean onRouteNodeClick(int paramInt) {
		if ((this.c.getAllStep() != null)
				&& (this.c.getAllStep().get(paramInt) != null))// 本来它是Toast
			Log.e("BaiduMap_onRouteNodeClick",
					((TransitRouteLine.TransitStep) this.c.getAllStep().get(
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
 * com.baidu.mapapi.overlayutil.TransitRouteOverlay JD-Core Version: 0.6.2
 */