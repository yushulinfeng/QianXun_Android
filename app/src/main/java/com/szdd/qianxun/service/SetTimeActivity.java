package com.szdd.qianxun.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import com.szdd.qianxun.R;


public class SetTimeActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private Button btn_sure;
    private Switch[] switches = new Switch[21];
    private int switch_id[] = {R.id.set_time_Mondy_morning, R.id.set_time_Mondy_afternoon, R.id.set_time_Mondy_evening,
            R.id.set_time_Tuesday_morning, R.id.set_time_Tuesday_afternoon, R.id.set_time_Tuesday_evening,
            R.id.set_time_Wednesday_morning, R.id.set_time_Wednesday_afternoon, R.id.set_time_Wednesday_evening,
            R.id.set_time_Thursday_morning, R.id.set_time_Thursday_afternoon, R.id.set_time_Thursday_evening,
            R.id.set_time_Firday_morning, R.id.set_time_Firday_afternoon, R.id.set_time_Firday_evening,
            R.id.set_time_Satuday_morning, R.id.set_time_Satuday_afternoon, R.id.set_time_Satuday_evening,
            R.id.set_time_Sunday_morning, R.id.set_time_Sunday_afternoon, R.id.set_time_Sunday_evening,
    };
    private boolean result_time[] = new boolean[21];
    private boolean[][] time_table = new boolean[3][7];

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_service_set_time);
        initView();

    }

    public void initView() {
        for (int i = 0; i < 21; i++) {
            switches[i] = (Switch) this.findViewById(switch_id[i]);
        }
        iv_back = (ImageView) this.findViewById(R.id.set_time_iv_back);
        btn_sure = (Button) this.findViewById(R.id.set_time_btn_sure);
        iv_back.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    /**
     * 根据Switch状态获取boolean数组
     *
     * @param value
     * @return
     */
    public boolean[] getResult_time(Switch[] value) {
        boolean temp[] = new boolean[value.length];
        for (int i = 0; i < value.length; i++) {
            temp[i] = value[i].isChecked();
        }
        return temp;
    }

    public String getTime(boolean[] bool) {
        String str = "";
        for (int i = 0; i < bool.length; i++) {
            if (bool[i]) {
                str = str + 1 + "#";
            } else {
                str = str + 0 + "#";
            }
        }
        return str;
    }

    public String getweek(boolean[] bool) {
        String str = "";
        for (int i = 0; i < bool.length - 2; i = i + 3) {
            if (bool[i] || bool[i + 1] || bool[i + 2]) {
                str = str + 1 + "#";
            } else {
                str = str + 0 + "#";
            }
        }
        return str;
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.set_time_iv_back:
                intent = new Intent();
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.set_time_btn_sure:
                String time = getTime(getResult_time(switches));
                String week = getweek(getResult_time(switches));
                intent = new Intent();
                intent.putExtra("timestatus", time);
                intent.putExtra("weekstatus", week);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED);
        finish();
    }
}
