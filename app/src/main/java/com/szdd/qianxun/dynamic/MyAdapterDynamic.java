package com.szdd.qianxun.dynamic;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szdd.qianxun.R;
import com.szdd.qianxun.service.CommonAdapter;
import com.szdd.qianxun.service.ViewHolder;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterDynamic extends CommonAdapter<String> {
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static ArrayList<String> mSelectedImage = new ArrayList<String>();//存储选中图片的路径
    public static int count = 0;
    public Context context;
    private TextView tv_count;
    /**
     * 文件夹路径
     */
    private String mDirPath;

    public MyAdapterDynamic(Context context, List<String> mDatas, int itemLayoutId,
                            String dirPath, TextView tv, ArrayList<String> list) {
        super(context, mDatas, itemLayoutId);
        mSelectedImage = list;
        count = mSelectedImage.size() - 1;
        this.context = context;
        this.mDirPath = dirPath;
        tv_count = tv;
    }

    @Override
    public void convert(final ViewHolder helper, final String item) {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select,
                R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);
        tv_count.setText(count + "/3");
        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    count--;
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.picture_unselected);
                    mImageView.setColorFilter(null);
                    tv_count.setText(count + "/3");
                } else { // 未选择该图片
                    if (count >= 3) {
                        QianxunToast.showToast(context, "最多可以选择3张图片", Toast.LENGTH_SHORT);
                    } else {
                        count++;
                        mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.drawable.pictures_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                        tv_count.setText(count + "/3");
                    }

                }
            }
        });
        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }
}
