package com.szdd.qianxun.tools.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

public class MenuScrollView extends HorizontalScrollView {

	private LinearLayout mWrapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;

	private int mScreenWidth;
	private int mMenuWidth;// 侧滑菜单的宽度,以dp为单位
	private int mMenuRightPadding;

	private boolean once;
	private boolean isOpen;

	private boolean shouldClose;// 用于点击菜单外部关闭菜单
	private int shouldCloseInt = 0;

	/**
	 * 未使用自定义属性时调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public MenuScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
		// 将dp转为px,在这里设置菜单的宽度，数字越大，菜单宽度越小
		mMenuRightPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources()
						.getDisplayMetrics());
		shouldClose = true;
		shouldCloseInt = 0;
	}

	/**
	 * 设置子View的宽和高 设置自己的宽和高
	 * 
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 保证这个方法只调用一次
		if (!once) {
			mWrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWrapper.getChildAt(0);
			mContent = (ViewGroup) mWrapper.getChildAt(1);
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 通过设置偏移量，将menu隐藏
	 * 
	 * @param changed
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// super语句可以将菜单隐藏到左侧
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	/**
	 * 滚动发生时调用
	 * 
	 * @param l
	 *            ; getScrollX()
	 * @param t
	 * @param oldl
	 * @param oldt
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth; // 1-0
		float rightScale = 0.8f + 0.2f * scale; // 主界面缩放
		float leftScale = 1.0f - scale * 0.3f; // 菜单缩放
		float leftAlpha = 0.6f + 0.4f * (1 - scale);// 透明度
		// 调用属性动画，设置getScrollX()
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);
		// 设置菜单缩放
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
		// 设置主界面缩放的中心点
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		// 偏移
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			// 点击有时会有颤抖，产生两三下move事件
			if (shouldClose) {
				shouldCloseInt++;
				if (shouldCloseInt > 3) {// 4下的误差
					shouldCloseInt = 0;
					shouldClose = false;// 很好
				}
			}
			// if (!isOpen) {// 优化一下列表上下滑动时的感觉，但会影响快速滑动
			// shouldCloseInt++;
			// if (shouldCloseInt < 3)//3下的误差
			// return true;
			// }
			break;// 此处只能break了。
		case MotionEvent.ACTION_UP:
			shouldCloseInt = 0;
			// 隐藏在左边的宽度
			if (shouldClose && ev.getX() > mMenuWidth) {
				// 点击区域外部，隐藏菜单
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
				shouldClose = false;
			} else if (getScrollX() >= mMenuWidth / 2) {
				// 隐藏在左边的宽度大于菜单宽度的二分之一时，保持隐藏
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
				shouldClose = false;
			} else {
				// 显示菜单
				this.smoothScrollTo(0, 0);
				isOpen = true;
				shouldClose = true;
			}
			// 这里只有返回true，方可响应所有触屏事件，但会屏蔽onClick。返回false时，只能响应Down事件。
			return true;
		}
		return super.onTouchEvent(ev);
	}

	// /** 中断touch事件，中断true，不中断false */
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isOpen && ev.getX() > mMenuWidth) // 点击区域外部，隐藏菜单
			return true;
		else
			return super.onInterceptTouchEvent(ev);
	}

	/** 打开菜单 */
	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
		shouldClose = true;
		shouldCloseInt = 0;
	}

	/** 关闭菜单 */
	public void closeMenu() {
		if (!isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
		shouldClose = false;
		shouldCloseInt = 0;
	}

	/** 强制关闭菜单 */
	public void forceCloseMenu() {
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
		shouldClose = false;
		shouldCloseInt = 0;
	}

	/** 切换菜单 */
	public void toggleMenu() {
		if (isOpen)
			closeMenu();
		else
			openMenu();
	}

	/** 获取菜单状态 */
	public boolean isOpen() {
		return isOpen;
	}

	/** 获取菜单宽度 */
	public int getMenuWidth() {
		return mMenuWidth;
	}

	/** 设定菜单滚动到的位置 */
	public void scrollTo(float x) {
		if (x > 0)
			this.smoothScrollTo(mMenuWidth - (int) x, 0);
		else
			this.smoothScrollTo(mMenuWidth, 0);
	}

	/** 判断菜单位置，确定是open还是close */
	public void checkLocation() {
		int scrollX = getScrollX();
		if (scrollX >= mMenuWidth / 2) {
			// 隐藏在左边的宽度大于菜单宽度的二分之一时，保持隐藏
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
			shouldClose = false;
			shouldCloseInt = 0;
		} else {
			// 显示菜单
			this.smoothScrollTo(0, 0);
			isOpen = true;
			shouldClose = true;
			shouldCloseInt = 0;
		}
	}

}
