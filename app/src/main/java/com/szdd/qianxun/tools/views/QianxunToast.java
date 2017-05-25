package com.szdd.qianxun.tools.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.szdd.qianxun.R;

/**
 * Created by linorz on 2016/4/28.
 */
public class QianxunToast extends Toast {
    TextView text;
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;

    public QianxunToast(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.qianxun_toast, null);
        text = (TextView) view.findViewById(R.id.qianxun_toast_text);
        setView(view);
        setGravity(Gravity.BOTTOM, 0, 200);
    }

    public static void showToast(Context context, String content, int duration) {
        QianxunToast toastCommom = new QianxunToast(context);
        toastCommom.text.setText(content);
        toastCommom.setDuration(duration);
        toastCommom.show();
    }

}
