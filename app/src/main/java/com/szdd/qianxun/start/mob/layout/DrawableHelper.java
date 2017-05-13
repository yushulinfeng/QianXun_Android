package com.szdd.qianxun.start.mob.layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableHelper {
	/** 创建圆角背景 */
	public static Drawable createCornerBg(Context c) {
		StateListDrawable sd = new StateListDrawable();
		sd.addState(new int[] { android.R.attr.state_pressed },
				createCornerBgPressed(c));
		sd.addState(new int[] {}, createCornerBgNormal(c));
		return sd;
	}

	/** 一般状态下的背景 */
	public static Drawable createCornerBgNormal(Context c) {
		SizeHelper.prepare(c);

		// prepare
		int strokeWidth = SizeHelper.fromPxWidth(1);
		int roundRadius = SizeHelper.fromPxWidth(6);
		int strokeColor = Color.parseColor("#ffc9c9cb");
		int fillColor = Color.parseColor("#ffffffff");

		GradientDrawable gd = new GradientDrawable();
		gd.setColor(fillColor);
		gd.setCornerRadius(roundRadius);
		gd.setStroke(strokeWidth, strokeColor);

		return gd;
	}

	/** 被按下时的背景 */
	public static Drawable createCornerBgPressed(Context c) {
		SizeHelper.prepare(c);

		// prepare
		int strokeWidth = SizeHelper.fromPxWidth(1);
		int roundRadius = SizeHelper.fromPxWidth(6);
		int strokeColor = Color.parseColor("#ffc9c9cb");
		int fillColor = Color.parseColor("#afc9c9cb");

		GradientDrawable gd = new GradientDrawable();
		gd.setColor(fillColor);
		gd.setCornerRadius(roundRadius);
		gd.setStroke(strokeWidth, strokeColor);

		return gd;
	}
}
