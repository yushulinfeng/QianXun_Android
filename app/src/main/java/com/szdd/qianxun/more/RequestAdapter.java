package com.szdd.qianxun.more;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.request.ApplyDetails;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;
import com.szdd.qianxun.tools.views.RoundHeadView;

import java.util.List;
import java.util.Map;

public class RequestAdapter extends BaseAdapter {
    private List<Map<String, Object>> strList;
    private Context mContext;
    private int my_layout_item;
    private int falg;

    public RequestAdapter(Context mContext, int my_layout_item, List<Map<String, Object>> strList, int falg) {
        this.strList = strList;
        this.mContext = mContext;
        this.my_layout_item = my_layout_item;
        this.falg = falg;

    }

    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public Object getItem(int position) {
        return strList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_listitem, null, false);
        ViewHolder h = new ViewHolder();
        final ViewHolder holder = new ViewHolder();
        holder.headIcon = (RoundHeadView) convertView
                .findViewById(R.id.roundHeadview_in_expandList);
        holder.tv_name = (TextView) convertView.findViewById(R.id.name_post_request);
        holder.img = new ImageView[3];
        holder.img[0] = (ImageView) convertView.findViewById(R.id.request_picture01);
        holder.img[1] = (ImageView) convertView.findViewById(R.id.request_picture02);
        holder.img[2] = (ImageView) convertView.findViewById(R.id.request_picture03);
        holder.tv_descripe = (TextView) convertView.findViewById(R.id.request_drscripe);
        holder.tv_reward = (TextView) convertView.findViewById(R.id.RewardValue);
        holder.tv_reward_self = (TextView) convertView.findViewById(R.id.RewardValue_self);
        holder.btn_left = (Button) convertView.findViewById(R.id.btn_left);
        holder.btn_right = (Button) convertView.findViewById(R.id.btn_right);
        ShowButton(falg, holder, position);

        try {
            StaticMethod.BITMAPHEAD(mContext, strList.get(position).get("post_headIcon").toString(), holder.headIcon);
            holder.tv_name.setText((String) strList.get(position).get("post_user_nickname"));
            holder.tv_descripe.setText((String) strList.get(position).get("descripe"));
            holder.tv_reward.setText((String) strList.get(position).get("reward"));
            holder.tv_reward_self.setText((String) strList.get(position).get("reward_self"));
        } catch (Exception e) {
        }
        convertView.setTag(holder);
        return convertView;
    }

    public void ShowButton(int falg, ViewHolder holder, final int pos) {
        if (falg == 0) {// 我的需求——报名中
            holder.btn_left.setText("报名详情");
            holder.btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(mContext, ApplyDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userRequestId", strList.get(pos).get("userRequestId").toString());
                    intent.putExtras(bundle);
                    ((FragmentActivity) mContext).startActivityForResult(intent, 1010);
                }
            });
            holder.btn_right.setText("撤回");
            holder.btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("温馨提示");
                    builder.setMessage("您确定要撤回这条需求吗？");
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    deleteRequest(mContext, ServerURL.DELETE_REQUEST_URL, strList.get(pos));
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else if (falg == 1) {// 我的需求——任务进行中
            holder.btn_left.setVisibility(View.GONE);
            holder.btn_right.setVisibility(View.GONE);
        } else if (falg == 2) {// 我的任务——已完成
            holder.btn_left.setVisibility(View.GONE);
            holder.btn_right.setText("删除");
            holder.btn_right.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("温馨提示");
                    builder.setMessage("您确定要删除这条需求吗？");
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    deleteRequest(mContext, ServerURL.DELETE_REQUEST_URL, strList.get(pos));
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    return;
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else if (falg == 3) {// Ta的需求——已报名
            holder.btn_left.setVisibility(View.GONE);
            holder.btn_right.setVisibility(View.GONE);
        } else if (falg == 4) {// Ta的需求——任务进行中
            holder.btn_left.setVisibility(View.GONE);
            holder.btn_right.setVisibility(View.GONE);
        } else if (falg == 5) {// Ta的需求——已完成
            holder.btn_left.setVisibility(View.GONE);
            holder.btn_right.setVisibility(View.GONE);
        }
    }

    public void deleteRequest(final Context cont, String url,
                              final Map<String, Object> Data) {
        StaticMethod.POST(cont, ServerURL.DELETE_REQUEST_URL,
                new ConnectListener() {
                    @Override
                    public ConnectDialog showDialog(ConnectDialog dialog) {
                        dialog.config(cont, "操作中", "正在撤回...", false);
                        return dialog;
                    }

                    @Override
                    public ConnectList setParam(ConnectList list) {
                        list.put("requestId", Data.get("userRequestId").toString());
                        return list;
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response == null || response.equals("")) showToast("数据异常");
                        else if (response.equals("1")) showToast("删除成功");
                        else if (response.equals("-1")) showToast("删除失败");
                        else if (response.equals("-2")) showToast("服务器出错");
                        else if (response.equals("-3")) showToast("任务不存在");
                        else if (response.equals("-4")) showToast("该需求已有人接单,不接受删除！");
                    }
                });
    }

    private void showToast(String text) {
        QianxunToast.showToast(mContext, text, QianxunToast.LENGTH_SHORT);
    }

    public List<Map<String, Object>> getList() {
        return strList;
    }

    class ViewHolder {
        public TextView tv_descripe, tv_reward, tv_reward_self, tv_name;
        public Button btn_left, btn_right;
        public RoundHeadView headIcon;
        public ImageView img[];
    }
}