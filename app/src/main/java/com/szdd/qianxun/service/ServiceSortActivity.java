package com.szdd.qianxun.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.szdd.qianxun.R;

/**
 * Created by DELL on 2016/3/27.
 */
public class ServiceSortActivity extends Activity implements View.OnClickListener {
    private Button[] btn_sort;
    private int btn_id[] = {
            R.id.post_service_sort_btn_0, R.id.post_service_sort_btn_1, R.id.post_service_sort_btn_2, R.id.post_service_sort_btn_3
            , R.id.post_service_sort_btn_4, R.id.post_service_sort_btn_5, R.id.post_service_sort_btn_6, R.id.post_service_sort_btn_7
            , R.id.post_service_sort_btn_8, R.id.post_service_sort_btn_9, R.id.post_service_sort_btn_10, R.id.post_service_sort_btn_11
            , R.id.post_service_sort_btn_12, R.id.post_service_sort_btn_13, R.id.post_service_sort_btn_14};
    private String sortValue = "";

    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_service_sort);
        init();
    }

    private void init() {
        iv_back = (ImageView) this.findViewById(R.id.service_sort_iv_back);
        btn_sort = new Button[15];
        for (int i = 0; i < 15; i++) {
            btn_sort[i] = (Button) this.findViewById(btn_id[i]);
        }
        for (int i = 0; i < 15; i++) {
            btn_sort[i].setOnClickListener(this);
        }
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.service_sort_iv_back://返回
                intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;

            case R.id.post_service_sort_btn_0:
                sortValue = btn_sort[0].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 0 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_1:
                sortValue = btn_sort[1].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 1 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_2:
                sortValue = btn_sort[2].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 2 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_3:
                sortValue = btn_sort[3].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 3 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_4:
                sortValue = btn_sort[4].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 4 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_5:
                sortValue = btn_sort[5].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 5 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_6:
                sortValue = btn_sort[6].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 6 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_7:
                sortValue = btn_sort[7].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 7 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_8:
                sortValue = btn_sort[8].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 8 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_9:
                sortValue = btn_sort[9].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 9 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_10:
                sortValue = btn_sort[10].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 10 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_11:
                sortValue = btn_sort[11].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 11 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_12:
                sortValue = btn_sort[12].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 12 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_13:
                sortValue = btn_sort[13].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 13 + "");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.post_service_sort_btn_14:
                sortValue = btn_sort[14].getText().toString();
                intent = new Intent();
                intent.putExtra("service_sort", sortValue);
                intent.putExtra("service_type", 14 + "");
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
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
