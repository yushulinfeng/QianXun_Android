package com.szdd.qianxun.service;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.szdd.qianxun.R;

/**
 * Created by DELL on 2016/4/27.
 */
public class DiaLogWhenBack extends Dialog implements View.OnClickListener {

    private Button btn_give_up, btn_continue;
    private Context context;
    private DialogWhenBackEvent dwbe;

    public DiaLogWhenBack(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_when_back);
        init();
    }

    public void init() {
        btn_give_up = (Button) this.findViewById(R.id.btn_give_up);
        btn_continue = (Button) this.findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        btn_give_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_give_up:
                if (dwbe != null) dwbe.negativeAction();
                break;
            case R.id.btn_continue:
                if (dwbe != null) dwbe.positiveAction();
                break;
            default:
                break;
        }
    }

    public void setWhenBack(DialogWhenBackEvent dwbe) {
        this.dwbe = dwbe;
    }

    public interface DialogWhenBackEvent {
        public void positiveAction();

        public void negativeAction();
    }
}
