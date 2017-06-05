package com.szdd.qianxun.sell.main.bottom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joooonho.SelectableRoundedImageView;
import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.file.ServiceType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SellMainAdapterBase extends BaseAdapter {
    private ArrayList<AnSellMainItem> item_list = null;
    private Context context;

    public SellMainAdapterBase(Context context, String response) {
        this.context = context;
        this.item_list = new ArrayList<>();
        if (response != null)
            dealResponse(response);
    }

    public SellMainAdapterBase(Context context, ArrayList<AnSellMainItem> item_list) {
        this.context = context;
        this.item_list = item_list;
    }

    public ArrayList<AnSellMainItem> getList() {
        return item_list;
    }

    public void dealResponse(String response) {
        //解析数据
        if (response == null || response.equals("")) {
            return;
        }
        try {
            JSONArray array = new JSONObject(response).getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                AnSellMainItem item = null;
                try {
                    item = new Gson().fromJson(obj.toString(), AnSellMainItem.class);
                } catch (Exception e) {
                    item = null;
                }
                if (item != null)
                    item_list.add(item);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getCount() {
        if (item_list == null) return 0;
        return item_list.size();
    }

    @Override
    public Object getItem(int position) {
        if (item_list == null || position < 0 ||
                position > item_list.size()) return null;
        return item_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.sell_main_list_item, null, false);
        ViewHolder holder = new ViewHolder(convertView);
        dealView(holder, item_list.get(position));
        return convertView;
    }

    private void dealView(ViewHolder holder, AnSellMainItem item) {
        StaticMethod.BITMAPHEAD(context, ServerURL.getIP() + item.getUser().getHeadIcon(), holder.sellMainItemRvHead);
        holder.sellMainItemIvSex.setImageResource(item.getUser().getGender().equals("女") ? R.drawable.girl : R.drawable.boy);
        holder.sellMainItemTvName.setText(item.getUser().getNickName());
        holder.sellMainItemTvTitle.setText("技能 " + item.getName());
        if (item.getUser().getVerifyStatus() <= 0) {
            holder.sellMainItemIvId.setVisibility(View.GONE);
            holder.sellMainItemIvStu.setVisibility(View.GONE);
        } else if (item.getUser().getVerifyStatus() == 1) {
            holder.sellMainItemIvStu.setVisibility(View.GONE);
        }
        if (!(item.getReward_money()+"").equals(""))
            holder.sellMainItemTvPrice.setText(item.getReward_money() + "元");
        holder.sellMainItemTvMethod.setText(ServiceType.getServiceWay(item.getServiceType()));
        if (item.getImages().size() == 0)
            holder.sellMainItemLayoutImg.setVisibility(View.GONE);
        if (item.getImages().size() > 0)
            StaticMethod.BITMAP(context, ServerURL.getIP() + item.getImages().get(0).getLink(), holder.sellMainItemIvImg1);
//        else
//            holder.sellMainItemIvImg1.setVisibility(View.GONE);
        if (item.getImages().size() > 1)
            StaticMethod.BITMAP(context, ServerURL.getIP() + item.getImages().get(1).getLink(), holder.sellMainItemIvImg2);
        else
            holder.sellMainItemIvImg2.setVisibility(View.GONE);
        if (item.getImages().size() > 2)
            StaticMethod.BITMAP(context, ServerURL.getIP() + item.getImages().get(2).getLink(), holder.sellMainItemIvImg3);
        else
            holder.sellMainItemIvImg3.setVisibility(View.GONE);
        holder.sellMainItemTvText.setText(item.getDetail());
        holder.sellMainItemTvLove.setText(item.getGreat() + "");//点赞
        holder.sellMainItemTvSee.setText(item.getFavoriteNumber() + "");//订单完成
        if (item.getReward_thing().equals("")) {
            holder.sellMainItemTvIcanshow.setVisibility(View.GONE);
            holder.sellMainItemTvIcantext.setVisibility(View.GONE);
        } else {
            holder.sellMainItemTvIcantext.setText(item.getReward_thing());
        }
    }

    static class ViewHolder {
        @Bind(R.id.sell_main_item_rv_head)
        SelectableRoundedImageView sellMainItemRvHead;
        @Bind(R.id.sell_main_item_iv_sex)
        SelectableRoundedImageView sellMainItemIvSex;
        @Bind(R.id.sell_main_item_tv_title)
        TextView sellMainItemTvTitle;
        @Bind(R.id.sell_main_item_tv_name)
        TextView sellMainItemTvName;
        @Bind(R.id.sell_main_item_iv_id)
        ImageView sellMainItemIvId;
        @Bind(R.id.sell_main_item_iv_stu)
        ImageView sellMainItemIvStu;
        @Bind(R.id.sell_main_item_tv_price)
        TextView sellMainItemTvPrice;
        @Bind(R.id.sell_main_item_tv_method)
        TextView sellMainItemTvMethod;
        @Bind(R.id.sell_main_item_iv_img1)
        ImageView sellMainItemIvImg1;
        @Bind(R.id.sell_main_item_layout_img)
        LinearLayout sellMainItemLayoutImg;
        @Bind(R.id.sell_main_item_iv_img2)
        ImageView sellMainItemIvImg2;
        @Bind(R.id.sell_main_item_iv_img3)
        ImageView sellMainItemIvImg3;
        @Bind(R.id.sell_main_item_tv_text)
        TextView sellMainItemTvText;
        @Bind(R.id.sell_main_item_iv_love)
        ImageView sellMainItemIvLove;
        @Bind(R.id.sell_main_item_tv_love)
        TextView sellMainItemTvLove;
        @Bind(R.id.sell_main_item_iv_see)
        ImageView sellMainItemIvSee;
        @Bind(R.id.sell_main_item_tv_see)
        TextView sellMainItemTvSee;
        @Bind(R.id.sell_main_item_iv_set)
        ImageView sellMainItemIvSet;
        @Bind(R.id.sell_main_item_tv_icanshow)
        TextView sellMainItemTvIcanshow;
        @Bind(R.id.sell_main_item_tv_icantext)
        TextView sellMainItemTvIcantext;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
