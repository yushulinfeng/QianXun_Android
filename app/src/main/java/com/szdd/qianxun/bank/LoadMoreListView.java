package com.szdd.qianxun.bank;

import org.json.JSONException;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.szdd.qianxun.R;

public class LoadMoreListView extends ListView implements OnScrollListener {

	private View footer;

	private int totalItem;
	private int lastItem;

	private boolean isLoading;

	private OnLoadMore onLoadMore;

	private LayoutInflater inflater;

	public LoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	
	//用于添加头部的viewpage时使用
	private float xDistance, yDistance, xLast, yLast;   
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        switch (ev.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                xDistance = yDistance = 0f;  
                xLast = ev.getX();  
                yLast = ev.getY();  
                break;  
            case MotionEvent.ACTION_MOVE:  
                final float curX = ev.getX();  
                final float curY = ev.getY();  
                xDistance += Math.abs(curX - xLast);  
                yDistance += Math.abs(curY - yLast);  
                xLast = curX;  
                yLast = curY;  
                if (xDistance > yDistance) {  
                    return false;  
                }  
        }  
        return super.onInterceptTouchEvent(ev);  
    } 
    
    
    
    
    
	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.load_more_footer, null, false);
		footer.setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (this.totalItem == lastItem && scrollState == SCROLL_STATE_IDLE) {
			Log.v("isLoading", "yes");
			if (!isLoading) {
				isLoading = true;
				footer.setVisibility(View.VISIBLE);
				try {
					onLoadMore.loadMore();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.lastItem = firstVisibleItem + visibleItemCount;
		this.totalItem = totalItemCount;
	}

	public void setLoadMoreListen(OnLoadMore onLoadMore) {
		this.onLoadMore = onLoadMore;
	}

	public void onLoadComplete() {
		footer.setVisibility(View.GONE);
		isLoading = false;

	}

	public interface OnLoadMore {
		public void loadMore() throws JSONException;
	}

}
