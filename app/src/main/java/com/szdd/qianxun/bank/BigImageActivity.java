package com.szdd.qianxun.bank;

import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class BigImageActivity extends Activity{
	private String mDatas ;
	private int mLocationX,mLocationY ,mWidth,mHeight ;
	private SmoothImageView imageView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		imageView = new SmoothImageView(this);
		mDatas =  getIntent().getStringExtra("images"); 
		mLocationX = getIntent().getIntExtra("locationX", 0);  
		mLocationY = getIntent().getIntExtra("locationY", 0);  
		mWidth = getIntent().getIntExtra("width", 0);  
		mHeight = getIntent().getIntExtra("height", 0);
		((SmoothImageView) imageView).setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);  
		((SmoothImageView) imageView).transformIn();  
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));  
		imageView.setScaleType(ScaleType.FIT_CENTER); 
		getBitmap(mDatas,imageView);
		setContentView(imageView);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	private void getBitmap(final String url, final ImageView imageview) {
		StaticMethod.BITMAP(BigImageActivity.this, url, new BitmapListener() {
			@Override
			public void onResponse(Bitmap bitmap) {
				if (bitmap != null) {
					try {
						imageview.setImageBitmap(bitmap);
					} catch (Exception e) {
					}
				}
			}
		});
	}
}
