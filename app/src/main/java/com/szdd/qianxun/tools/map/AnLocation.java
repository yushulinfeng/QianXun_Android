package com.szdd.qianxun.tools.map;

public class AnLocation {
    private String locationName;
    private double x;
    private double y;
    private float limit;

    public AnLocation() {
    }

    public AnLocation(String locationName, double x, double y, float limit) {
        this.locationName = locationName;
        this.x = x;
        this.y = y;
        this.limit = limit;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }


}
