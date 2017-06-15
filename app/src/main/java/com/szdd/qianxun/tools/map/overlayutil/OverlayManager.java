package com.szdd.qianxun.tools.map.overlayutil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLngBounds;

public abstract class OverlayManager implements BaiduMap.OnMarkerClickListener {
	BaiduMap a = null;
	private List<OverlayOptions> c = null;
	List<Overlay> b = null;

	public OverlayManager(BaiduMap paramBaiduMap) {
		this.a = paramBaiduMap;
		if (this.c == null)
			this.c = new ArrayList();
		if (this.b == null)
			this.b = new ArrayList();
	}

	public abstract List<OverlayOptions> getOverlayOptions();

	public final void addToMap() {
		if (this.a == null)
			return;
		removeFromMap();
		List localList = getOverlayOptions();
		if (localList != null)
			this.c.addAll(getOverlayOptions());
		Iterator localIterator = this.c.iterator();
		while (localIterator.hasNext()) {
			OverlayOptions localOverlayOptions = (OverlayOptions) localIterator
					.next();
			this.b.add(this.a.addOverlay(localOverlayOptions));
		}
	}

	public final void removeFromMap() {
		if (this.a == null)
			return;
		Iterator localIterator = this.b.iterator();
		while (localIterator.hasNext()) {
			Overlay localOverlay = (Overlay) localIterator.next();
			localOverlay.remove();
		}
		this.c.clear();
		this.b.clear();
	}

	public void zoomToSpan() {
		if (this.a == null)
			return;
		if (this.b.size() > 0) {
			LatLngBounds.Builder localBuilder = new LatLngBounds.Builder();
			Iterator localIterator = this.b.iterator();
			while (localIterator.hasNext()) {
				Overlay localOverlay = (Overlay) localIterator.next();
				if ((localOverlay instanceof Marker))
					localBuilder.include(((Marker) localOverlay).getPosition());
			}
			this.a.setMapStatus(MapStatusUpdateFactory
					.newLatLngBounds(localBuilder.build()));
		}
	}
}
