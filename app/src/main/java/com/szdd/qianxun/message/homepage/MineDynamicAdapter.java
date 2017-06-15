package com.szdd.qianxun.message.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.tools.PxAndDip;
import com.szdd.qianxun.tools.all.StaticMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by linorz on 2016/3/26.
 */
public class MineDynamicAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> mapList;
    private LayoutInflater inflater;
    private int width;
    private int height;
    private int squre_width;
    private int half_width;

    public MineDynamicAdapter(Context context, List<Map<String, Object>> mapList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mapList = mapList;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        half_width = (width - PxAndDip.dipTopx(context, 80)) / 2;
        squre_width = (width - PxAndDip.dipTopx(context, 90)) / 3;
        width = squre_width * 3 + PxAndDip.dipTopx(context, 20);
        height = width * 9 / 16;
    }

    @Override
    public int getCount() {
        return mapList.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        if (i == 0) {
            view = inflater.inflate(R.layout.msg_mine_personal_detail, null);
            DetailView dv = new DetailView(view);
            dv.id = (long) mapList.get(i).get("id");
            dv.location.setText((String) mapList.get(i).get("location"));
            dv.sex.setText((String) mapList.get(i).get("sex"));
            dv.age.setText((String) mapList.get(i).get("age"));
            dv.star.setText((String) mapList.get(i).get("star"));
            dv.introduce.setText((String) mapList.get(i).get("introduce"));
        } else {
            view = inflater.inflate(R.layout.msg_mine_personal_item, null);
            DynamicView dv = new DynamicView(view);
            dv.id = (long) mapList.get(i).get("id");
            dv.text.setText((String) mapList.get(i).get("text"));
            dv.time1.setText((String) mapList.get(i).get("time1"));
            dv.time2.setText((String) mapList.get(i).get("time2"));
            String url1 = (String) mapList.get(i).get("image1");
            String url2 = (String) mapList.get(i).get("image2");
            String url3 = (String) mapList.get(i).get("image3");
            if (url1.isEmpty()) {
                setImage(dv.image1, 0, width);
                dv.image2.setVisibility(View.GONE);
                dv.image3.setVisibility(View.GONE);
            } else if (url2.isEmpty()) {
                setImage(dv.image1, height, width);
                dv.image2.setVisibility(View.GONE);
                dv.image3.setVisibility(View.GONE);
                StaticMethod.BITMAP(context, url1, dv.image1);
            } else if (url3.isEmpty()) {
                setImage(dv.image1, half_width, half_width);
                setImage(dv.image2, half_width, half_width);
                dv.image3.setVisibility(View.GONE);
                StaticMethod.BITMAP(context, url1, dv.image1);
                StaticMethod.BITMAP(context, url2, dv.image2);
            } else {
                setImage(dv.image1, squre_width, squre_width);
                setImage(dv.image2, squre_width, squre_width);
                setImage(dv.image3, squre_width, squre_width);
                StaticMethod.BITMAP(context, url1, dv.image1);
                StaticMethod.BITMAP(context, url2, dv.image2);
                StaticMethod.BITMAP(context, url3, dv.image3);
            }
        }
        return view;
    }

    public void setImage(ImageView image, int h, int w) {
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) image.getLayoutParams();
        ll.height = h;
        ll.width = w;
        image.setLayoutParams(ll);
    }

    public class DetailView {
        public TextView location, sex, age, star, introduce;
        public long id;

        public DetailView(final View view) {
            location = (TextView) view.findViewById(R.id.mine_personal_detail_location);
            sex = (TextView) view.findViewById(R.id.mine_personal_detail_sex);
            age = (TextView) view.findViewById(R.id.mine_personal_detail_age);
            star = (TextView) view.findViewById(R.id.mine_personal_detail_star);
            introduce = (TextView) view.findViewById(R.id.mine_personal_detail_introduce);
        }
    }

    public class DynamicView {
        public long id;
        public ViewGroup layout;
        public ImageView image1, image2, image3;
        public TextView text, time1, time2;

        public DynamicView(final View view) {
            image1 = (ImageView) view.findViewById(R.id.mine_dynamic_iv_1);
            image2 = (ImageView) view.findViewById(R.id.mine_dynamic_iv_2);
            image3 = (ImageView) view.findViewById(R.id.mine_dynamic_iv_3);
            text = (TextView) view.findViewById(R.id.mine_dynamic_tv);
            time1 = (TextView) view.findViewById(R.id.mine_dynamic_time_1);
            time2 = (TextView) view.findViewById(R.id.mine_dynamic_time_2);
            layout = (ViewGroup) view.findViewById(R.id.mine_dynamic_layout);
        }
    }

}
