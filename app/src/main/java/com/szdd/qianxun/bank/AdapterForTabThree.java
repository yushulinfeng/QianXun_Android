package com.szdd.qianxun.bank;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.views.SquareRelativeLayout;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ServerURL;


import java.util.List;
import java.util.Map;

public class AdapterForTabThree extends BaseAdapter {

    private SharedPreferences sp;
    private Context context;
    private List<Map<String, Object>> listitems;
    private LayoutInflater mInflater;
    private AlertDialog myDialog;


    private String gender;
    private String headIcon;
    private int prove;
    private String[] request_picture = new String[3];
    private String distance;
    private String[] distancearray = null;
    private String reward_money, reward_object;


    public AdapterForTabThree(Context context,
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

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final View view = convertView;
        final int pos = position;
        final int id;
        holder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.listitem_in_tab_03, null);
        holder.layout_imggggg = (LinearLayout) convertView.findViewById(R.id.layout_imggggg);
//        holder.layout[0] = (SquareRelativeLayout) convertView.findViewById(R.id.layout_Square1);
//        holder.layout[1] = (SquareRelativeLayout) convertView.findViewById(R.id.layout_Square2);
//        holder.layout[2] = (SquareRelativeLayout) convertView.findViewById(R.id.layout_Square3);
        holder.otheruserheadview = (ImageView) convertView.findViewById(R.id.otheruserheadview);
        holder.details = (FrameLayout) convertView.findViewById(R.id.details);
        holder.user = (TextView) convertView.findViewById(R.id.users);
        holder.sex = (ImageView) convertView.findViewById(R.id.sex);
        holder.identity = (ImageView) convertView.findViewById(R.id.identity);
        holder.describe = (TextView) convertView.findViewById(R.id.describe);
        holder.describe_image[0] = (ImageView) convertView.findViewById(R.id.describe_image01);
        holder.describe_image[1] = (ImageView) convertView.findViewById(R.id.describe_image02);
        holder.describe_image[2] = (ImageView) convertView.findViewById(R.id.describe_image03);
        holder.reward_money = (TextView) convertView.findViewById(R.id.reward_money);
        holder.reward_object = (TextView) convertView.findViewById(R.id.reward_object);
        holder.reward_jia = (TextView) convertView.findViewById(R.id.reward_jia);
        holder.distance = (TextView) convertView.findViewById(R.id.distance);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.label = (TextView) convertView.findViewById(R.id.label);
        holder.beforelabel = (TextView) convertView.findViewById(R.id.beforelabel);

        id = (Integer) listitems.get(position).get("id");
        headIcon = (String) listitems.get(position).get("headIcon");
        getBitmapHead(holder.ip + headIcon, holder.otheruserheadview);
        holder.user.setText((String) listitems.get(position).get("nickName"));
        gender = (String) listitems.get(position).get("gender");
        if (gender.equals("男")) {
            holder.sex.setImageResource(R.drawable.boy);
        } else if (gender.equals("女")) {
            holder.sex.setImageResource(R.drawable.girl);
        } else {
            holder.sex.setVisibility(ImageView.INVISIBLE);
        }
        prove = (Integer) listitems.get(position).get("verifyStatus");
        if (prove == 2) {
            holder.identity.setImageResource(R.drawable.identity_student);
        } else if (prove == 1) {
        } else {
            holder.identity.setVisibility(ImageView.INVISIBLE);
        }

        holder.describe.setText((String) listitems.get(position).get("request_content"));

        request_picture[0] = (String) listitems.get(position).get("request_picture");
        request_picture[1] = (String) listitems.get(position).get("request_picture2");
        request_picture[2] = (String) listitems.get(position).get("request_picture3");
        for (int i = 0; i < 3 && request_picture[i] != null; i++) {
            if (request_picture[i].equals("")) {
                if(i==0){
                    holder.layout_imggggg.setVisibility(LinearLayout.GONE);
                }
                break;
            }
            holder.describe_image[i].setVisibility(View.VISIBLE);
            StaticMethod.BITMAP(context, ServerURL.getIP() + request_picture[i], holder.describe_image[i]);
        }
        reward_money = (String) listitems.get(position).get("reward_money");
        reward_object = (String) listitems.get(position).get("reward_thing");
        if ((reward_money == null || reward_money.equals("")) && (reward_object == null || reward_object.equals(""))) {
            holder.reward_money.setVisibility(TextView.INVISIBLE);
            holder.reward_jia.setVisibility(TextView.INVISIBLE);
            holder.reward_object.setVisibility(TextView.INVISIBLE);

        } else if ((reward_object == null || reward_object.equals("")) && (reward_money != null && !reward_money.equals(""))) {
            holder.reward_money.setVisibility(TextView.INVISIBLE);
            holder.reward_jia.setVisibility(TextView.INVISIBLE);
            holder.reward_money.setText("...");
            if (reward_money.length() >= 8) {
                holder.reward_object.setText("￥" + reward_money.substring(0, 8));
            } else {
                holder.reward_object.setText("￥" + reward_money);
            }
        } else if ((reward_object != null && !reward_object.equals("")) && (reward_money == null || reward_money.equals(""))) {
            holder.reward_money.setVisibility(TextView.INVISIBLE);
            holder.reward_jia.setVisibility(TextView.INVISIBLE);
            holder.reward_money.setText("...");
            if (reward_object.length() >= 8) {
                holder.reward_object.setText("￥" + reward_object.substring(0, 8));
            } else {
                holder.reward_object.setText("￥" + reward_object);
            }
        } else if ((reward_object != null && !reward_object.equals("")) && (reward_money != null && !reward_money.equals(""))) {
            if (reward_money.length() >= 3) {
                holder.reward_money.setText("￥" + reward_money.substring(0, 3) + "...");
            } else {
                holder.reward_money.setText("￥" + reward_money);
            }
            if (reward_object.length() >= 6) {
                holder.reward_object.setText("￥" + reward_object.substring(0, 6) + "...");
            } else {
                holder.reward_object.setText("￥" + reward_object);
            }

        }
        distance = (String) listitems.get(position).get("distance");
        if (distance != "") {
            distancearray = distance.split("\\.");
            holder.distance.setText(distancearray[0] + "m");
        } else {
            holder.distance.setText(distance);
        }

        holder.time.setText((String) listitems.get(position).get("request_postTime"));
        String request_key = (String) listitems.get(position).get("request_key");
        if (request_key == null || request_key.equals("")) {
            holder.label.setVisibility(TextView.INVISIBLE);
            holder.beforelabel.setVisibility(TextView.INVISIBLE);
        } else {
            holder.label.setText(request_key);
        }
        return convertView;
    }

    static final class ViewHolder {
        public TextView user, integration, describe, reward_money, reward_object, reward_jia, distance, time, label, beforelabel;
        public ImageView sex, identity;
        private ImageView[] describe_image = new ImageView[3];
        public ImageView otheruserheadview;
        public FrameLayout details;
        public String ip = ServerURL.getIP();
       // public SquareRelativeLayout layout[] = new SquareRelativeLayout[3];
        public LinearLayout layout_imggggg;
    }

    private void getBitmapHead(final String url, final ImageView imageview) {
        StaticMethod.BITMAPHEAD(context, url, imageview);
    }
}