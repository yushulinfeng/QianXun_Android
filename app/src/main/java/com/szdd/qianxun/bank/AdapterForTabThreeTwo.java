 package com.szdd.qianxun.bank;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ServerURL;

 public class AdapterForTabThreeTwo extends BaseAdapter {

	private SharedPreferences sp;
	private Context context;
	private List<Map<String, Object>> listitems;
	private LayoutInflater mInflater;
	 

	public AdapterForTabThreeTwo(Context context,
			List<Map<String, Object>> listitems) {
		mInflater = LayoutInflater.from(context);
		sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		this.context = context;
		this.listitems = listitems;
	}

	@Override
	public int getCount() {
		return listitems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final View v;
		holder = new ViewHolder();
		convertView = mInflater.inflate(R.layout.listitem_in_tab_03_02, null);
		v = convertView;
		holder.usernickname_apply = (TextView) convertView.findViewById(R.id.user_apply);
		holder.username = (Long) listitems.get(position).get("username");
		holder.id = (int)listitems.get(position).get("id");
		holder.otherheadview_apply = (ImageView) convertView.findViewById(R.id.otherheadview_apply);
		holder.otherheadview_apply.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				MsgPublicTool.showHomePage(context,(long)holder.id);
			}
		});
		holder.usernickname_apply.setText((String) listitems.get(position).get("nickName"));
		getBitmapHead(holder.ip+(String) listitems.get(position).get("headIcon"), holder.otherheadview_apply);
		return convertView;
	}

	static final class ViewHolder {
		public TextView usernickname_apply;
		public ImageView otherheadview_apply;
		public Long username;
		public int id;
		public String ip = ServerURL.getIP();
	}
	private void getBitmapHead(final String url, final ImageView imageview) {
		StaticMethod.BITMAPHEAD(context, url, imageview);
	}

	private void getBitmap(final String url, final ImageView imageview) {
		StaticMethod.BITMAP(context, url, imageview);
	}
}
