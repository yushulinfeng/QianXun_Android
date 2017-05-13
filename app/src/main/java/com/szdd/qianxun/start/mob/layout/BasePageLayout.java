package com.szdd.qianxun.start.mob.layout;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/** 页面布局基类 */
public abstract class BasePageLayout {
	LinearLayout layout = null;
	Context context = null;

	public BasePageLayout(Context c, boolean isSearch) {
		context = c;
		layout = new LinearLayout(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(0xffffffff);

		LinearLayout title = TitleLayout.create(context, isSearch);
		layout.addView(title);
		onCreateContent(layout);
	}

	/** 获取生成的布局 */
	public LinearLayout getLayout() {
		return layout;
	}

	protected abstract void onCreateContent(LinearLayout parent);
}
