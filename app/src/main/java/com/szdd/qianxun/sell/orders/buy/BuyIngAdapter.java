package com.szdd.qianxun.sell.orders.buy;

import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.sell.orders.SellAllOrderBuy;
import com.szdd.qianxun.service.SellOrderDetailActivityBuy;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class BuyIngAdapter extends BaseAdapter {
    private SharedPreferences sp;
    private Context context;
    private List<Map<String, Object>> listitems;
    private LayoutInflater mInflater;
    private AlertDialog myDialog;

    public BuyIngAdapter(Context context,
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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        final int pos = position;
        final int id;
        holder = new ViewHolder();
        View view = null;
        if (convertView != null)
            view = convertView;
        else
            view = mInflater.inflate(R.layout.sell_allorder_buy_comment, null);
        holder.allorder_buy_comment_rhv_headview = (ImageView) view.findViewById(R.id.allorder_buy_comment_rhv_headview);
        holder.allorder_buy_comment_tv_nickname = (TextView) view.findViewById(R.id.allorder_buy_comment_tv_nickname);
        holder.allorder_buy_comment_iv_describe = (ImageView) view.findViewById(R.id.allorder_buy_comment_iv_describe);
        holder.allorder_buy_comment_tv_progectcontent = (TextView) view.findViewById(R.id.allorder_buy_comment_tv_progectcontent);
        holder.allorder_buy_comment_tv_pricecontent = (TextView) view.findViewById(R.id.allorder_buy_comment_tv_pricecontent);
        holder.allorder_buy_comment_tv_numbercontent = (TextView) view.findViewById(R.id.allorder_buy_comment_tv_numbercontent);
        holder.allorder_buy_comment_tv_timecontent = (TextView) view.findViewById(R.id.allorder_buy_comment_tv_timecontent);
        holder.allorder_buy_comment_tv_connectcontent = (TextView) view.findViewById(R.id.allorder_buy_comment_tv_connectcontent);
        holder.allorder_buy_comment_btn_comment = (Button) view.findViewById(R.id.allorder_buy_comment_btn_comment);
        holder.allorder_buy_comment_btn_finish = (Button) view.findViewById(R.id.allorder_buy_comment_btn_finish);
        holder.allorder_buy_comment_RL = (RelativeLayout) view.findViewById(R.id.allorder_buy_comment_RL);
        id = (int) listitems.get(position).get("id");
        holder.allorder_buy_comment_RL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showToast("keyi");
                Intent intent = new Intent(context, SellOrderDetailActivityBuy.class);
                intent.putExtra("ugsId", "" + id);
                context.startActivity(intent);
            }
        });
        getBitmapHead(holder.ip + (String) listitems.get(position).get("headview"), holder.allorder_buy_comment_rhv_headview);
        final long username = (Long) listitems.get(position).get("username");
        holder.allorder_buy_comment_rhv_headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgPublicTool.showAllInfo(context, username);
            }
        });
        holder.allorder_buy_comment_tv_nickname.setText((String) listitems.get(position).get("nickname"));
        getBitmap(holder.ip + (String) listitems.get(position).get("describe"), holder.allorder_buy_comment_iv_describe);
        holder.allorder_buy_comment_tv_progectcontent.setText((String) listitems.get(position).get("progectcontent"));
        holder.allorder_buy_comment_tv_pricecontent.setText((String) listitems.get(position).get("reward_money") + "元/" + (String) listitems.get(position).get("reward_unit"));
        holder.allorder_buy_comment_tv_numbercontent.setText("" + (Integer) listitems.get(position).get("numbercontent"));
        holder.allorder_buy_comment_tv_timecontent.setText((String) listitems.get(position).get("timecontent"));
        holder.allorder_buy_comment_tv_connectcontent.setText("" + (long) listitems.get(position).get("connectcontent"));

        if ((int) listitems.get(position).get("status") == -3) {
            holder.allorder_buy_comment_btn_comment.setVisibility(Button.INVISIBLE);
            holder.allorder_buy_comment_btn_finish.setVisibility(Button.INVISIBLE);
        }
        Log.e("getStatus_rufund()",SellAllOrderBuy.getStatus_rufund()+"################");
        if (SellAllOrderBuy.getStatus_rufund() == 1) {
            holder.allorder_buy_comment_btn_comment.setEnabled(false);
        }
        holder.allorder_buy_comment_btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyRefund.class);
                intent.putExtra("ugsId", id);
                context.startActivity(intent);
            }
        });
        holder.allorder_buy_comment_btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticMethod.POST(context, ServerURL.ALL_ORDER_FINISHORDER_URL,
                        new ConnectListener() {
                            @Override
                            public ConnectDialog showDialog(ConnectDialog dialog) {
                                return null;
                            }

                            @Override
                            public ConnectList setParam(ConnectList list) {
                                list.put("ugsId", "" + id, false);
                                return list;
                            }

                            @Override
                            public void onResponse(String response) {
                                if (response == null || response.equals("")) {
                                    // 网络错误
                                    showToast("网络错误,请连接网络后重新加载");
                                    return;
                                } else {
                                    int statuss = Integer.parseInt(response);
                                    if (statuss == 1) {
                                        showToast("成功完成订单");
                                        holder.allorder_buy_comment_btn_finish.setEnabled(false);
                                    } else if (statuss == -1) {
                                        showToast("完成订单失败");
                                    } else if (statuss == -2) {
                                        showToast("服务器出错");
                                    } else if (statuss == -3) {
                                        showToast("服务器状态异常");
                                    }
                                }

                            }
                        });
            }
        });
        return view;
    }

    static final class ViewHolder {
        public TextView allorder_buy_comment_tv_nickname, allorder_buy_comment_tv_progectcontent,
                allorder_buy_comment_tv_pricecontent, allorder_buy_comment_tv_numbercontent, allorder_buy_comment_tv_timecontent, allorder_buy_comment_tv_connectcontent;
        public ImageView allorder_buy_comment_iv_describe;
        public ImageView allorder_buy_comment_rhv_headview;
        public Button allorder_buy_comment_btn_comment, allorder_buy_comment_btn_finish;
        private RelativeLayout allorder_buy_comment_RL;
        public String ip = ServerURL.getIP();


    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }

    private void getBitmapHead(final String url, final ImageView imageview) {
        StaticMethod.BITMAPHEAD(context, url, imageview);
    }

    private void getBitmap(final String url, final ImageView imageview) {
        StaticMethod.BITMAP(context, url, imageview);
    }
}
