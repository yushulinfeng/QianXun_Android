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
 * Created by linorz on 2016/4/1.
 */
public class MineRequestAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> mapList;
    private LayoutInflater inflater;
    private int width;
    private int height;
    private int squre_width;
    private int half_width;

    public MineRequestAdapter(Context context, List<Map<String, Object>> mapList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mapList = mapList;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        squre_width = (width - PxAndDip.dipTopx(context, 40)) / 3;
        half_width = (width - PxAndDip.dipTopx(context, 30)) / 2;
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
        view = inflater.inflate(R.layout.msg_mine_request_item, null);
        RequestView rv = new RequestView(view);
        String url1 = (String) mapList.get(i).get("image1");
        String url2 = (String) mapList.get(i).get("image2");
        String url3 = (String) mapList.get(i).get("image3");
        rv.id = (long) mapList.get(i).get("id");
        rv.label.setText((String) mapList.get(i).get("label"));
        rv.text.setText((String) mapList.get(i).get("text"));
        rv.distance.setText((String) mapList.get(i).get("distance"));
        rv.time.setText((String) mapList.get(i).get("time"));

        String money = (String) mapList.get(i).get("money");
        String thing = (String) mapList.get(i).get("thing");
        if (money.equals("") || thing.equals(""))
            rv.add.setVisibility(View.GONE);
        rv.money.setText(money + "元");
        rv.thing.setText(thing);

        if (url1.isEmpty()) {
            rv.image1.setVisibility(View.GONE);
            rv.image2.setVisibility(View.GONE);
            rv.image3.setVisibility(View.GONE);
        } else if (url2.isEmpty()) {
            setImage(rv.image1, height, width);
            rv.image2.setVisibility(View.GONE);
            rv.image3.setVisibility(View.GONE);
            StaticMethod.UBITMAP(url1, rv.image1);
        } else if (url3.isEmpty()) {
            setImage(rv.image1, half_width, half_width);
            setImage(rv.image2, half_width, half_width);
            rv.image3.setVisibility(View.GONE);
            StaticMethod.UBITMAP(url1, rv.image1);
            StaticMethod.UBITMAP(url2, rv.image2);
        } else {
            setImage(rv.image1, squre_width, squre_width);
            setImage(rv.image2, squre_width, squre_width);
            setImage(rv.image3, squre_width, squre_width);
            StaticMethod.UBITMAP(url1, rv.image1);
            StaticMethod.UBITMAP(url2, rv.image2);
            StaticMethod.UBITMAP(url3, rv.image3);
        }
        return view;
    }

    public class RequestView {
        long id;
        TextView label, text, distance, time, money, thing, add;
        ImageView image1, image2, image3;

        public RequestView(final View view) {
            label = (TextView) view.findViewById(R.id.mine_request_item_label);
            text = (TextView) view.findViewById(R.id.mine_request_item_text);
            distance = (TextView) view.findViewById(R.id.mine_request_item_distance);
            time = (TextView) view.findViewById(R.id.mine_request_item_time);
            money = (TextView) view.findViewById(R.id.mine_request_item_money);
            thing = (TextView) view.findViewById(R.id.mine_request_item_thing);
            image1 = (ImageView) view.findViewById(R.id.mine_request_item_iv1);
            image2 = (ImageView) view.findViewById(R.id.mine_request_item_iv2);
            image3 = (ImageView) view.findViewById(R.id.mine_request_item_iv3);
            add = (TextView) view.findViewById(R.id.mine_request_item_add);
        }
    }

    public void setImage(ImageView image, int h, int w) {
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) image.getLayoutParams();
        ll.height = h;
        ll.width = w;
        image.setLayoutParams(ll);
    }
}
