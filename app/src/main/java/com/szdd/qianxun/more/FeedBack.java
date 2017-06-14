package com.szdd.qianxun.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.top.TActivity;
import com.umeng.fb.ConversationActivity;

public class FeedBack extends ConversationActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		TActivity.addActionBar(this,R.color.main_yellow);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitle("意见反馈");
		showBackButton();
	}

	private void setTitle(String title) {
		((TextView) findViewById(R.id.top_text)).setText(title);
	}

	private void showBackButton() {
		ImageButton top_back = (ImageButton) findViewById(R.id.top_back);
		top_back.setVisibility(ImageButton.VISIBLE);
		top_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

}
