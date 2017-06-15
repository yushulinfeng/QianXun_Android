package com.szdd.qianxun.tools.font;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.reflect.Field;

public final class FontsOverride {

    public static void resetFont(Context context, String staticTypefaceFieldName) {
        final Typeface regular = Typeface.create("monospace", 0);
        replaceFont(staticTypefaceFieldName, regular);
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改某个根组件的字体
     *
     * @param root
     * @param act
     * @param assetsFontPath
     */
    public static void changeFonts(ViewGroup root, Activity act,
                                   String assetsFontPath) {
        Typeface tf = Typeface.createFromAsset(act.getAssets(), assetsFontPath);
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, act, assetsFontPath);
            }
        }
    }

    /**
     * 修改某个组件的字体
     *
     * @param v
     * @param context
     * @param assetsFontPath
     */
    public static void changeFont(View v, Context context,
                                  String assetsFontPath) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), assetsFontPath);
        if (v instanceof TextView) {
            ((TextView) v).setTypeface(tf);
        } else if (v instanceof Button) {
            ((Button) v).setTypeface(tf);
        } else if (v instanceof EditText) {
            ((EditText) v).setTypeface(tf);
        }
    }

    /**
     * 修改整个Activity的字体
     *
     * @param act
     * @param assetsFontPath
     */
    public static void changeFonts(Activity act, String assetsFontPath) {
        changeFonts((ViewGroup) act.getWindow().getDecorView(), act,
                assetsFontPath);
    }

}
