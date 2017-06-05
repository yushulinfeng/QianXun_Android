package com.szdd.qianxun.sell.discuss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DiscussAdapter extends BaseAdapter {
    private Context context;
    private String title, text;
    private List<AnDiscussReply> list;

    public DiscussAdapter(Context context, String title,
                          String text, List<AnDiscussReply> list) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (list == null) return null;
        if (position == 0) return null;
        return list.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (position == 0) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.sell_discuss_main_top, null, false);
            TopViewHolder holder = new TopViewHolder(view);
            title=title==null?"":title;
            text=text==null?"":text;
            holder.sellDiscussTopTitle.setText(title);
            holder.sellDiscussTopText.setText(text);
            return view;
        }
        view = LayoutInflater.from(context).inflate(
                R.layout.sell_discuss_main_item, null, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        AnDiscussReply reply = list.get(position - 1);
        holder.sellDiscussItemNick.setText(reply.getUserName());
        holder.sellDiscussItemText.setText(reply.getReplyText());
        holder.sellDiscussItemTime.setText(reply.getReplyTime());
        StaticMethod.BITMAPHEAD(context, reply.getUserHead(), holder.sellDiscussItemImg);
        return view;
    }

    static class TopViewHolder {
        @Bind(R.id.sell_discuss_top_title)
        TextView sellDiscussTopTitle;
        @Bind(R.id.sell_discuss_top_text)
        TextView sellDiscussTopText;

        TopViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ItemViewHolder {
        @Bind(R.id.sell_discuss_item_img)
        SelectableRoundedImageView sellDiscussItemImg;
        @Bind(R.id.sell_discuss_item_nick)
        TextView sellDiscussItemNick;
        @Bind(R.id.sell_discuss_item_text)
        TextView sellDiscussItemText;
        @Bind(R.id.sell_discuss_item_time)
        TextView sellDiscussItemTime;

        ItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
