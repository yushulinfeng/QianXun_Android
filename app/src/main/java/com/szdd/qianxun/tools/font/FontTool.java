package com.szdd.qianxun.tools.font;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

public class FontTool {
    private static final String FONT_INDEX_SAVE_PATH = "all.font_index";

    public static void saveIndex(Context context, int index) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                FONT_INDEX_SAVE_PATH, Activity.MODE_PRIVATE).edit();
        prefs.putInt("font_index", index);
        prefs.commit();
    }

    public static int getIndex(Context context) {
        SharedPreferences mPref = context.getSharedPreferences(
                FONT_INDEX_SAVE_PATH, Activity.MODE_PRIVATE);
        int index = mPref.getInt("font_index", 0);
        return index;
    }

    public static void dealFont(Context context) {
        int font_index = getIndex(context);
        switch (font_index) {
            case 0:
                FontsOverride.resetFont(context, "MONOSPACE");
                break;
//            case 1:
//                FontsOverride.setDefaultFont(context, "MONOSPACE", "font_cute.ttf");
//                break;
        }
    }

    /**
     * 切换为方正纤细黑字体
     *
     * @param context Context
     * @param view    要切换字体的组件
     */
    public static void changeFontToXXH(Context context, View view) {
        FontsOverride.changeFont(view, context, "fangzheng.ttf");
    }

    /**
     * 为整个Activity切换为方正纤细黑字体
     *
     * @param activity 要切换字体的Activity
     */
    public static void changeAllFontToXXH(Activity activity) {
        FontsOverride.changeFonts(activity, "fangzheng.ttf");
    }

}
