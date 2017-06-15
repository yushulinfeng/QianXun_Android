package com.szdd.qianxun.tools.map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class LocationCache {
    private static final String CHCHE_SAVE_PATH = "location_cache";

    public static void saveLocation(Context context, AnLocation location) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                CHCHE_SAVE_PATH, Activity.MODE_PRIVATE).edit();
        prefs.putString("name", location.getLocationName());
        prefs.putString("x", location.getX() + "");
        prefs.putString("y", location.getY() + "");
        prefs.putFloat("limit", location.getLimit());
        prefs.commit();
    }

    public static AnLocation getLocation(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                CHCHE_SAVE_PATH, Activity.MODE_PRIVATE);
        String name = pref.getString("name", "");
        String x_temp = pref.getString("x", "0");
        String y_temp = pref.getString("y", "0");
        double x = 0, y = 0;
        try {
            x = Double.parseDouble(x_temp);
            y = Double.parseDouble(y_temp);
        } catch (Exception e) {
        }
        float limit = pref.getFloat("limit", 0);
        AnLocation location = new AnLocation(name, x, y, limit);
        return location;
    }
}
