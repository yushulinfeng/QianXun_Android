package com.szdd.qianxun.tools.map;

public interface LocationTransListener {

    /**
     * 百度地图的位置转换，将坐标位置转换为地名
     *
     * @param locationName 地名：AddrStr
     */
    public void locationRespose(String locationName);
}
