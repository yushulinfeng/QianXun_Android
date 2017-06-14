package com.szdd.qianxun.advertise.customview;

/**
 * Created by linorz on 2016/1/26.
 */

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.szdd.qianxun.R;

public class CustomMediaController extends android.widget.MediaController {

    private Activity mActivity;

    private View mView;

    public CustomMediaController(Activity activity) {
        super(activity);
        mActivity = activity;
    }


    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        mView = LayoutInflater.from(getContext()).inflate(
                R.layout.video_button, null);
        //返回
        Button btn_back = (Button) mView.findViewById(R.id.video_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                mActivity.finish();
            }
        });
        try {
            SeekBar sb = (SeekBar) LayoutInflater.from(getContext()).inflate(
                    R.layout.video_seekbar, null);
            Field mRoot = android.widget.MediaController.class
                    .getDeclaredField("mRoot");
            mRoot.setAccessible(true);
            ViewGroup mRootVg = (ViewGroup) mRoot.get(this);
            ViewGroup vg = findSeekBarParent(mRootVg);
            int index = 1;
            for (int i = 0; i < vg.getChildCount(); i++) {
                if (vg.getChildAt(i) instanceof SeekBar) {
                    index = i;
                    break;
                }
            }
            vg.removeViewAt(index);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            vg.addView(sb, index, params);
            Field mProgress = android.widget.MediaController.class
                    .getDeclaredField("mProgress");
            mProgress.setAccessible(true);
            mProgress.set(this, sb);
            Field mSeekListener = android.widget.MediaController.class
                    .getDeclaredField("mSeekListener");
            mSeekListener.setAccessible(true);
            sb.setOnSeekBarChangeListener((OnSeekBarChangeListener) mSeekListener
                    .get(this));
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ViewGroup findSeekBarParent(ViewGroup vg) {
        ViewGroup viewGroup = null;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View view = vg.getChildAt(i);
            if (view instanceof SeekBar) {
                viewGroup = (ViewGroup) view.getParent();
                break;
            } else if (view instanceof ViewGroup) {
                viewGroup = findSeekBarParent((ViewGroup) view);
            } else {
                continue;
            }
        }
        return viewGroup;
    }

    @Override
    public void show(int timeout) {
        super.show(timeout);
        ((ViewGroup) mActivity.findViewById(android.R.id.content))
                .removeView(mView);
        ((ViewGroup) mActivity.findViewById(android.R.id.content))
                .addView(mView);
    }

    @Override
    public void hide() {
        super.hide();
        ((ViewGroup) mActivity.findViewById(android.R.id.content))
                .removeView(mView);
    }

}
