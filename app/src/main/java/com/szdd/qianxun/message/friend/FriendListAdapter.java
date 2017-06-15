package com.szdd.qianxun.message.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ServerURL;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<AnFriendItem> array = null;
    private boolean is_collect = false;

    public FriendListAdapter(Context context, ArrayList<AnFriendItem> array, boolean is_collect) {
        this.context = context;
        this.array = array;
        this.is_collect = is_collect;
    }

    @Override
    public int getCount() {
        if (is_collect) {
            int count = 0;
            for (AnFriendItem item : array) {
                if (item.getState())
                    count++;
            }
            return count;
        }
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.msg_friend_list_item, null, false);
        ViewHolder holder = new ViewHolder(convertView);
        dealView(holder, array.get(position));
        return convertView;
    }

    private void dealView(ViewHolder holder, AnFriendItem item) {
        if (item.getIconPath() != null && !item.getIconPath().trim().equals(""))
            StaticMethod.BITMAPHEAD(context, ServerURL.getIP() + item.getIconPath(), holder.msgFriendListItemIcon);
        holder.msgFriendListItemId.setText(item.getUserName() + "");
        holder.msgFriendListItemName.setText((item.getMyTag() != null) ? item.getMyTag() : item.getNickName());
        holder.msgFriendListItemState.setText(item.getState() ? "已关注" : "");
    }

    static class ViewHolder {
        @Bind(R.id.msg_friend_list_item_icon)
        SelectableRoundedImageView msgFriendListItemIcon;
        @Bind(R.id.msg_friend_list_item_name)
        TextView msgFriendListItemName;
        @Bind(R.id.msg_friend_list_item_id)
        TextView msgFriendListItemId;
        @Bind(R.id.msg_friend_list_item_state)
        TextView msgFriendListItemState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
