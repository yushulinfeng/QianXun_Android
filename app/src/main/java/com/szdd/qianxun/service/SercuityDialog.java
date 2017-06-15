package com.szdd.qianxun.service;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.szdd.qianxun.R;

/**
 * Created by DELL on 2016/4/24.
 */
public class SercuityDialog extends Dialog {
    private Context context;
    private Button btn_know;
    private View.OnClickListener listener;

    public SercuityDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sercuity_dialog);
        btn_know = (Button) this.findViewById(R.id.dialog_btn_know);
        if (listener != null)
            btn_know.setOnClickListener(listener);
        else
            btn_know.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

}
