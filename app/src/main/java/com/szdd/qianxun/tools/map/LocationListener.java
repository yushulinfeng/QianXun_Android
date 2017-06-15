package com.szdd.qianxun.tools.map;

public interface LocationListener {

	/**
	 * 百度地图的返回（说明：百度地图的调用，很多地方是先纬度再经度，要看好单次拼写）
	 * 
	 * @param locationName
	 *            地名：AddrStr
	 * @param x
	 *            经度：Longitude
	 * @param y
	 *            纬度：Latitude
	 * @param limit
	 *            精确度：Radius
	 */
	public void locationRespose(String locationName, double x, double y,
			float limit);

}
