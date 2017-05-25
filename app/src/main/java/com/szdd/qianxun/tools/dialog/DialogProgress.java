package com.szdd.qianxun.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;

public class DialogProgress extends Dialog {
	protected Context context;
	protected TextView tv_text;
	protected ImageView iv_img;
	protected String title = "", text = "";
	protected int img_id = 0;

	public DialogProgress(Context context) {
		super(context, R.style.MyDialogTheme);
		this.context = context;
		this.title = "";
	}

	public DialogProgress(Context context, String title) {
		super(context, R.style.MyDialogTheme);
		this.context = context;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (title != null && !title.equals(""))
			setTitle(title);
		else
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_progress);
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initViewSet();
	}

	private void initView() {
		tv_text = (TextView) findViewById(R.id.dialog_progress_text);
		iv_img = (ImageView) findViewById(R.id.dialog_progress_img);
		AnimationDrawable animationDrawable = (AnimationDrawable) iv_img
				.getBackground();
		animationDrawable.start();
	}

	protected void initViewSet() {
		if (!"".equals(text)) {
			tv_text.setText(text);
		}
		if (img_id != 0) {
			iv_img.setBackgroundResource(img_id);
			try {
				AnimationDrawable animationDrawable = (AnimationDrawable) iv_img
						.getBackground();
				animationDrawable.start();
			} catch (Exception e) {
			}
		}
	}

	public DialogProgress setTitleNew(String title) {
		this.title = title;
		return this;
	}

	public DialogProgress setText(String text) {
		this.text = text;
		return this;
	}

	public DialogProgress setImageRes(int id) {
		this.img_id = id;
		return this;
	}

}
