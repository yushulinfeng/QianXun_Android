package com.szdd.qianxun.request;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.RoundHeadView;

import java.util.List;
import java.util.Map;

public class AdapterForDetails extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> listitems;
    private LayoutInflater mInflater;
    private Activity activity;

    public AdapterForDetails(Activity act, Context context, List<Map<String, Object>> listitems) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listitems = listitems;
        this.activity = act;
    }

    @Override
    public int getCount() {
        return listitems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int pos = position;
        holder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.listitem_in_applyd_details, null);
        final View v = convertView;
        holder.applyName = (TextView) convertView.findViewById(R.id.applyer_name);
        holder.applyPicture = (RoundHeadView) convertView.findViewById(R.id.applyer_picture);
        holder.btnAgree = (Button) convertView.findViewById(R.id.agree);
        holder.btnAgree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("温馨提示");
                builder.setMessage("确认要有此用户来完成您的需求吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LetSbToDo(context, ServerURL.LET_SOMEONE_TO_DO, listitems.get(pos));
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        StaticMethod.BITMAPHEAD(context, listitems.get(pos).get("icon_url").toString(), holder.applyPicture);
        holder.applyName.setText((String) listitems.get(pos).get("applyname"));
        return convertView;
    }

    private void LetSbToDo(final Context con, final String url, final Map<String, Object> lists) {
        StaticMethod.POST(con, url, new ConnectListener() {
            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                dialog.config(con, "发送中...", "玩命加载中...", false);
                return dialog;
            }

            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("username", lists.get("applyid").toString());
                list.put("requestId", lists.get("requestId").toString());
                return list;
            }

            @Override

            public void onResponse(String response) {
                if (response == null) showToast("未知错误");
                else {
                    int res = Integer.parseInt(response);
                    switch (res) {
                        case 1:
                            showToast("操作成功");
                            activity.finish();
                            break;
                        case -2:
                            showToast("服务器出错");
                            break;
                        case -3:
                            showToast("已有人接单");
                            break;

                    }
                }
            }
        });
    }

    static final class ViewHolder {
        public RoundHeadView applyPicture;
        public TextView applyName;
        public Button btnAgree;
    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }
}
