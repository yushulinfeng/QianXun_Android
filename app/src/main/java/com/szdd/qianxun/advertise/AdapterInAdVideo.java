package com.szdd.qianxun.advertise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.all.StaticMethod;

import java.util.List;
import java.util.Map;


public class AdapterInAdVideo extends BaseAdapter {
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private List<Map<String, Object>> mapList;
    private LayoutInflater inflater;

    public AdapterInAdVideo(Context context, List<Map<String, Object>> mapList) {
        inflater = LayoutInflater.from(context);
        mySharedPreferences = context.getSharedPreferences("myvideogreat", Context.MODE_PRIVATE);
        editor = mySharedPreferences.edit();
        this.context = context;
        this.mapList = mapList;
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
        View view;
        if (convertView != null)
            view = convertView;
        else
            view = inflater.inflate(R.layout.ad_video_item, null);

        final VideoView vv = new VideoView(view);

        //标题
        vv.video_name.setText((String) mapList.get(i).get("video_name"));
        //uri
        vv.video_uri = (String) mapList.get(i).get("video_uri");
        //Id
        vv.id = (Long) mapList.get(i).get("id");

        if (mySharedPreferences.getBoolean(vv.id + "", false)) {
            vv.btn_great.setEnabled(false);
        } else {
            editor.putBoolean(vv.id + "", true);
            editor.commit();
        }

        String url = (String) mapList.get(i).get("video_picture");
        StaticMethod.UBITMAP(url, vv.btn_image);
        return view;
    }

    public static int getShape() {
        int[] shapes = {R.drawable.ad_corners_green, R.drawable.ad_corners_orange, R.drawable.ad_corners_purple};
        return shapes[(int) (Math.random() * 3)];
    }

    public static class VideoView {
        public TextView video_name;
        public ImageView btn_image;
        public Button btn_great;
        public String video_uri;
        public Long id;

        public VideoView(final View view) {
            video_name = (TextView) view.findViewById(R.id.ad_video_name);
            video_name.setBackgroundResource(getShape());
            btn_image = (ImageView) view.findViewById(R.id.ad_video_btn_image);
            btn_great = (Button) view.findViewById(R.id.ad_video_btn_great);
            btn_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(),
                            VideoPlayer.class);
                    intent.putExtra("video_uri", video_uri);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
