package com.szdd.qianxun.sell.orders.buy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.service.SellOrderDetailActivityBuy;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ServerURL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class BuyFinishAdapter extends BaseAdapter {
    private SharedPreferences sp;
    private Context context;
    private List<Map<String, Object>> listitems;
    private LayoutInflater mInflater;
    private AlertDialog myDialog;

    public BuyFinishAdapter(Context context,
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
            view = mInflater.inflate(R.layout.sell_allorder_buy_finish, null);
        holder.allorder_buy_finish_rhv_headview = (ImageView) view.findViewById(R.id.allorder_buy_finish_rhv_headview);
        holder.allorder_buy_finish_tv_nickname = (TextView) view.findViewById(R.id.allorder_buy_finish_tv_nickname);
        holder.allorder_buy_finish_iv_describe = (ImageView) view.findViewById(R.id.allorder_buy_finish_iv_describe);
        holder.allorder_buy_finish_tv_progectcontent = (TextView) view.findViewById(R.id.allorder_buy_finish_tv_progectcontent);
        holder.allorder_buy_finish_tv_pricecontent = (TextView) view.findViewById(R.id.allorder_buy_finish_tv_pricecontent);
        holder.allorder_buy_finish_tv_numbercontent = (TextView) view.findViewById(R.id.allorder_buy_finish_tv_numbercontent);
        holder.allorder_buy_finish_tv_timecontent = (TextView) view.findViewById(R.id.allorder_buy_finish_tv_timecontent);
        holder.allorder_buy_finish_tv_connectcontent = (TextView) view.findViewById(R.id.allorder_buy_finish_tv_connectcontent);
        holder.allorder_buy_finish_tv_chargecontent = (TextView) view.findViewById(R.id.allorder_buy_finish_tv_chargecontent);
        id = (int) listitems.get(position).get("id");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SellOrderDetailActivityBuy.class);
                intent.putExtra("ugsId", "" + id);
                context.startActivity(intent);
            }
        });
        getBitmapHead(holder.ip + (String) listitems.get(position).get("headview"), holder.allorder_buy_finish_rhv_headview);
        final long username = (Long) listitems.get(position).get("username");
        holder.allorder_buy_finish_rhv_headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgPublicTool.showAllInfo(context, username);
            }
        });
        holder.allorder_buy_finish_tv_nickname.setText((String) listitems.get(position).get("nickname"));
        getBitmap(holder.ip + (String) listitems.get(position).get("describe"), holder.allorder_buy_finish_iv_describe);
        holder.allorder_buy_finish_tv_progectcontent.setText((String) listitems.get(position).get("progectcontent"));
        holder.allorder_buy_finish_tv_pricecontent.setText((String) listitems.get(position).get("reward_money") + "元/" + (String) listitems.get(position).get("reward_unit"));
        holder.allorder_buy_finish_tv_numbercontent.setText("" + (Integer) listitems.get(position).get("numbercontent"));
        holder.allorder_buy_finish_tv_timecontent.setText((String) listitems.get(position).get("timecontent"));
        holder.allorder_buy_finish_tv_connectcontent.setText("" + (long) listitems.get(position).get("connectcontent"));
        holder.allorder_buy_finish_tv_chargecontent.setText("" + (Integer) listitems.get(position).get("charge") + "元");
        return view;
    }

    static final class ViewHolder {
        public TextView allorder_buy_finish_tv_nickname, allorder_buy_finish_tv_progectcontent,
                allorder_buy_finish_tv_pricecontent, allorder_buy_finish_tv_numbercontent, allorder_buy_finish_tv_timecontent, allorder_buy_finish_tv_connectcontent, allorder_buy_finish_tv_chargecontent;
        public ImageView allorder_buy_finish_iv_describe;
        public ImageView allorder_buy_finish_rhv_headview;
        public String ip = ServerURL.getIP();


    }

    private void getBitmap(final String url, final ImageView imageview) {
        StaticMethod.BITMAP(context, url, imageview);
    }

    private void getBitmapHead(final String url, final ImageView imageview) {
        StaticMethod.BITMAPHEAD(context, url, imageview);
    }
}
