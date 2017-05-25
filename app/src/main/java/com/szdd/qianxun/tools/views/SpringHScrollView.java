package com.szdd.qianxun.tools.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;

public class SpringHScrollView extends HorizontalScrollView {

	private static final int MAX_X_OVERSCROLL_DISTANCE = 200;
	private Context mContext;
	private int mMaxXOverscrollDistance;

	public SpringHScrollView(Context context) {
		super(context);
		mContext = context;
		initBounceDistance();
	}

	public SpringHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceDistance();
	}

	public SpringHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initBounceDistance();
	}

	private void initBounceDistance() {
		final DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		mMaxXOverscrollDistance = (int) (metrics.density * MAX_X_OVERSCROLL_DISTANCE);
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// 这块是关键性代码
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, mMaxXOverscrollDistance,
				maxOverScrollY, isTouchEvent);
	}

}