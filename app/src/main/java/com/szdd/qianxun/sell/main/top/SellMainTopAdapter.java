package com.szdd.qianxun.sell.main.top;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.szdd.qianxun.R;
import com.szdd.qianxun.sell.category.SellCategoryDetail;
import com.szdd.qianxun.tools.file.ServiceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SellMainTopAdapter extends PagerAdapter {

    private List<View> views = new ArrayList<>();

    public SellMainTopAdapter(Context context) {
//        views.add(getWhiteView(context));
        views.add(getGrideView(context, 0));
        views.add(getGrideView(context, 1));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    //两边的白色
    private View getWhiteView(final Context context) {
        View imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundColor(context.getResources().getColor(R.color.white));
        return imageView;
    }

    //中间的表格
    private View getGrideView(final Context context, final int page) {
        View view = LayoutInflater.from(context).inflate(R.layout.sell_main_topview_grid, null);

        GridView drawer_grid = (GridView) view.findViewById(R.id.activity_app_grid_allapp);
        ArrayList<HashMap<String, Object>> imagelist = new ArrayList<HashMap<String, Object>>();
        if (page == 0) {
            int count = 10;
            int[] image = {R.drawable.icon_top_01, R.drawable.icon_top_02,
                    R.drawable.icon_top_03, R.drawable.icon_top_04,
                    R.drawable.icon_top_05, R.drawable.icon_top_06,
                    R.drawable.icon_top_07, R.drawable.icon_top_08,
                    R.drawable.icon_top_09, R.drawable.icon_top_10};
            String[] text = new String[count];
            for (int i = 0; i < count; i++) {
                text[i] = ServiceType.getServiceType(i);
            }
            for (int i = 0; i < image.length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("image", image[i]);
                map.put("text", text[i]);
                imagelist.add(map);
            }
        } else if (page == 1) {
            int count = 5;
            int[] image = {R.drawable.icon_top_11, R.drawable.icon_top_12,
                    R.drawable.icon_top_13, R.drawable.icon_top_14,
                    R.drawable.icon_top_15};
            String[] text = new String[count];
            for (int i = 0; i < count; i++) {
                text[i] = ServiceType.getServiceType(i + 10);
            }
            for (int i = 0; i < count; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("image", image[i]);
                map.put("text", text[i]);
                imagelist.add(map);
            }
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(context,
                imagelist, R.layout.sell_main_top_item, new String[]{"image",
                "text"}, new int[]{R.id.app_icon, R.id.app_title});
        drawer_grid.setAdapter(simpleAdapter);

        drawer_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(context, SellCategoryDetail.class);
                intent.putExtra("index", position + page * 10);
                context.startActivity(intent);
            }
        });

        return view;
    }
}