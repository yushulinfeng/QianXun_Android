package com.szdd.qianxun.advertise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;

import java.util.List;
import java.util.Map;

/**
 * Created by LLX on 2015/10/27.
 */

public class AdapterInAdPicture extends RecyclerView.Adapter<AdapterInAdPicture.PictureView> {
    private Context context;
    private List<Map<String, Object>> mapList;
    private LayoutInflater inflater;
    private int lastPosition = -1;

    public AdapterInAdPicture(List<Map<String, Object>> list) {
        mapList = list;
    }

    public AdapterInAdPicture(Context context, List<Map<String, Object>> mapList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mapList = mapList;
    }

    public void setData(List<Map<String, Object>> datas) {
        mapList.clear();
        mapList = datas;
    }

    public void addDatas(List<Map<String, Object>> datas) {
        mapList.addAll(datas);
    }

    public void addDate(Map<String, Object> item) {
        mapList.add(item);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    @Override
    public PictureView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.ad_picture_item, viewGroup, false);
        return new PictureView(view);
    }

    @Override
    public void onBindViewHolder(final PictureView pv, int i) {
//        setAnimation(pv.frame, i);
        final String picture_url = (String) mapList.get(i).get("comment_picture");//图片地址
        final String user_icon_url = (String) mapList.get(i).get("user_icon");//头像
        pv.comment_id = (Long) mapList.get(i).get("comment_id");//交易Id或动态id
        pv.name.setText((String) mapList.get(i).get("comment_context"));//标题
        pv.user_id = (Long) mapList.get(i).get("user_id");//发单人id或发动态人id
        StaticMethod.UBITMAP(picture_url, pv.btn_image);
        StaticMethod.UBITMAPHEAD(user_icon_url, pv.head);
        //添加监听器
        pv.btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgPublicTool.showDynamicDetail(context, pv.comment_id);
            }
        });
    }

    public static int getShape() {
        int[] shapes = {R.drawable.ad_corners_green, R.drawable.ad_corners_orange, R.drawable.ad_corners_purple};

        return shapes[(int) (Math.random() * 3)];
    }


    @Override
    public void onViewDetachedFromWindow(PictureView pv) {
        super.onViewDetachedFromWindow(pv);
//        pv.frame.clearAnimation();
    }

    //动画加载
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class PictureView extends RecyclerView.ViewHolder {
        public ImageView btn_image;
        public ImageView head;
        public long comment_id;
        public TextView name;
        public long user_id;
        public FrameLayout frame;

        public PictureView(final View view) {
            super(view);
            frame = (FrameLayout) view.findViewById(R.id.ad_picture_frame);
            btn_image = (ImageView) view.findViewById(R.id.ad_picture_btn_image);//图片
            name = (TextView) view.findViewById(R.id.ad_picture_name);//图片名
            name.setBackgroundResource(getShape());
            head = (ImageView) view.findViewById(R.id.ad_picture_item_head);//头像
            //跳转个人主页
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MsgPublicTool.showHomePage(context, user_id);
                }
            });
        }
    }
}
