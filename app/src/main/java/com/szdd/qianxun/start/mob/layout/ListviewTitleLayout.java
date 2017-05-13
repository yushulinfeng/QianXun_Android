package com.szdd.qianxun.start.mob.layout;

import com.mob.tools.utils.R;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** ListView的标题布局 */
public class ListviewTitleLayout {

	static RelativeLayout create(Context context) {
		SizeHelper.prepare(context);

		RelativeLayout root = new RelativeLayout(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(params);

		TextView title = new TextView(context);
		title.setId(Res.id.tv_title);
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				SizeHelper.fromPxWidth(40));
		titleParams.topMargin = SizeHelper.fromPxWidth(-20);
		title.setLayoutParams(titleParams);
		title.setPadding(SizeHelper.fromPxWidth(20), 0, 0, 0);
		title.setLineSpacing(SizeHelper.fromPxWidth(8), 1);
		int resid = R.getStringRes(context, "smssdk_regist");
		title.setText(resid);
		title.setTextColor(0xff999999);
		title.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				SizeHelper.fromPxWidth(26));
		title.setGravity(Gravity.CENTER_VERTICAL);
		title.setBackgroundColor(0xffeae8ee);
		root.addView(title);

		return root;
	}
}
