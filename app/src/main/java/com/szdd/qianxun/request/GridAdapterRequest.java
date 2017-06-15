package com.szdd.qianxun.request;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.service.ImageLoader;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/4/3.
 */
public class GridAdapterRequest extends BaseAdapter {
    private ArrayList<String> list_path = new ArrayList<String>();
    public static ArrayList<String> list_code = new ArrayList<String>();
    private Context context;
    private LayoutInflater inflater;
    private final int GET_PICTURE = 555;//获取图片

    public GridAdapterRequest(Context c, ArrayList<String> path) {
        this.list_path = path;
        this.context = c;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list_path.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        view = inflater.inflate(R.layout.layout_add_picture, null);
        ViewHolder holder = new ViewHolder();
        holder.iv_show = (ImageView) view.findViewById(R.id.id_item_image1);
        holder.iv_delete = (ImageView) view.findViewById(R.id.id_item_select1);

        if (list_path.get(position).equals("")) {
            holder.iv_delete.setVisibility(View.INVISIBLE);
            holder.iv_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostRequest.method_dialog.show();
                }
            });
        } else {
            ImageLoader.getInstance().loadImage(list_path.get(pos), holder.iv_show);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list_path.remove(pos);
                    list_path.remove("");
                    list_path.add("");
                    notifyDataSetChanged();
                }

            });
        }
        return view;
    }

    class ViewHolder {
        public ImageView iv_show, iv_delete;
    }
}