package com.szdd.qianxun.sell.orders.sale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.szdd.qianxun.service.SellOrderDetailActivitySale;
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
public class SaleRefundAdapter extends BaseAdapter {
    private SharedPreferences sp;
    private Context context;
    private List<Map<String, Object>> listitems;
    private LayoutInflater mInflater;
    private AlertDialog myDialog;

    public SaleRefundAdapter(Context context, List<Map<String, Object>> listitems) {
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
            view = mInflater.inflate(R.layout.sell_allorder_sale_refund, null);
        holder.allorder_sale_refund_rhv_headview = (ImageView) view.findViewById(R.id.allorder_sale_refund_rhv_headview);
        holder.allorder_sale_refund_tv_nickname = (TextView) view.findViewById(R.id.allorder_sale_refund_tv_nickname);
        holder.allorder_sale_refund_iv_describe = (ImageView) view.findViewById(R.id.allorder_sale_refund_iv_describe);
        holder.allorder_sale_refund_tv_progectcontent = (TextView) view.findViewById(R.id.allorder_sale_refund_tv_progectcontent);
        holder.allorder_sale_refund_tv_pricecontent = (TextView) view.findViewById(R.id.allorder_sale_refund_tv_pricecontent);
        holder.allorder_sale_refund_tv_numbercontent = (TextView) view.findViewById(R.id.allorder_sale_refund_tv_numbercontent);
        holder.allorder_sale_refund_tv_timecontent = (TextView) view.findViewById(R.id.allorder_sale_refund_tv_timecontent);
        holder.allorder_sale_refund_tv_connectcontent = (TextView) view.findViewById(R.id.allorder_sale_refund_tv_connectcontent);
        holder.allorder_sale_refund_btn_agree = (Button) view.findViewById(R.id.allorder_sale_refund_btn_agree);
        holder.allorder_sale_refund_btn_disagree = (Button) view.findViewById(R.id.allorder_sale_refund_btn_disagree);
        holder.allorder_sale_refund_RL = (RelativeLayout) view.findViewById(R.id.allorder_sale_refund_RL);
        id = (int) listitems.get(position).get("id");
        holder.allorder_sale_refund_RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SellOrderDetailActivitySale.class);
                intent.putExtra("ugsId", "" + id);
                intent.putExtra("come", "refound");
                context.startActivity(intent);
            }
        });
        getBitmapHead(holder.ip + (String) listitems.get(position).get("headview"), holder.allorder_sale_refund_rhv_headview);
        final long username = (Long) listitems.get(position).get("username");
        holder.allorder_sale_refund_rhv_headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgPublicTool.showAllInfo(context, username);

            }
        });
        holder.allorder_sale_refund_tv_nickname.setText((String) listitems.get(position).get("nickname"));
        getBitmap(holder.ip + (String) listitems.get(position).get("describe"), holder.allorder_sale_refund_iv_describe);
        holder.allorder_sale_refund_tv_progectcontent.setText((String) listitems.get(position).get("progectcontent"));
        holder.allorder_sale_refund_tv_pricecontent.setText((String) listitems.get(position).get("reward_money") + "元/" + (String) listitems.get(position).get("reward_unit"));
        holder.allorder_sale_refund_tv_numbercontent.setText("" + (Integer) listitems.get(position).get("numbercontent"));
        holder.allorder_sale_refund_tv_timecontent.setText((String) listitems.get(position).get("timecontent"));
        holder.allorder_sale_refund_tv_connectcontent.setText("" + (long) listitems.get(position).get("connectcontent"));
        if((int) listitems.get(position).get("status")==-3){
            holder.allorder_sale_refund_btn_agree.setVisibility(Button.INVISIBLE);
            holder.allorder_sale_refund_btn_disagree.setVisibility(Button.INVISIBLE);
        }
        holder.allorder_sale_refund_btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View agrs0) {
                StaticMethod.POST(context, ServerURL.ALL_ORDER_CONFIRMREFUND_URL,
                        new ConnectListener() {
                            @Override
                            public ConnectDialog showDialog(ConnectDialog dialog) {
                                dialog.config(context, "正在连接", "请稍后……", true);
                                return dialog;
                            }
                            @Override
                            public ConnectList setParam(ConnectList list) {
                                list.put("ugsId", "" + id, false);
                                return list;
                            }
                            @Override
                            public void onResponse(String response) {
                                if (response == null || response.equals("")) {
                                    showToast("网络错误,请连接网络后重新加载");
                                    return;
                                } else {
                                    int statuss = Integer.parseInt(response);
                                    if (statuss == 1) {
                                        showToast("成功同意退款");
                                        holder.allorder_sale_refund_btn_agree.setEnabled(false);
                                    } else if (statuss == -1) {
                                        showToast("同意退款失败");
                                    } else if (statuss == -2) {
                                        showToast("服务器出错");
                                    } else if (statuss == -3) {showToast("服务器状态不正常");
                                    }
                                }
                            }
                        });
            }
        });
        holder.allorder_sale_refund_btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View agrs0) {
                StaticMethod.POST(context, ServerURL.ALL_ORDER_REFUSEREFUND_URL,
                        new ConnectListener() {
                            @Override
                            public ConnectDialog showDialog(ConnectDialog dialog) {
                                dialog.config(context, "正在连接", "请稍后……", true);
                                return dialog;
                            }
                            @Override
                            public ConnectList setParam(ConnectList list) {
                                list.put("ugsId", "" + id, false);
                                return list;
                            }
                            @Override
                            public void onResponse(String response) {
                                if (response == null || response.equals("")) {
                                    showToast("网络错误,请连接网络后重新加载");
                                    return;
                                } else {
                                    int statuss = Integer.parseInt(response);
                                    if (statuss == 1) {
                                        showToast("成功拒绝退款");
                                        holder.allorder_sale_refund_btn_disagree.setEnabled(false);
                                    } else if (statuss == -1) {
                                        showToast("拒绝退款失败");
                                    } else if (statuss == -2) {
                                        showToast("服务器出错");
                                    } else if (statuss == -3) {
                                        showToast("服务器状态不正常");
                                    }
                                }
                            }
                        });
            }
        });
        return view;
    }

    static final class ViewHolder {
        public TextView allorder_sale_refund_tv_nickname, allorder_sale_refund_tv_progectcontent,
                allorder_sale_refund_tv_pricecontent, allorder_sale_refund_tv_numbercontent, allorder_sale_refund_tv_timecontent, allorder_sale_refund_tv_connectcontent;
        public ImageView allorder_sale_refund_iv_describe;
        public ImageView allorder_sale_refund_rhv_headview;
        private Button allorder_sale_refund_btn_agree, allorder_sale_refund_btn_disagree;
        private RelativeLayout allorder_sale_refund_RL;
        public String ip = ServerURL.getIP();


    }

    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }

    private void getBitmap(final String url, final ImageView imageview) {
        StaticMethod.BITMAP(context, url, imageview);
    }

    private void getBitmapHead(final String url, final ImageView imageview) {
        StaticMethod.BITMAPHEAD(context, url, imageview);
    }
}
