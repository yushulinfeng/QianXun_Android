package com.szdd.qianxun.service;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.szdd.qianxun.R;

import java.util.Calendar;

/**
 * Created by DELL on 2016/3/28.
 */
public class ChoseTimeDialog extends Dialog implements View.OnClickListener {

    private TextView[] slide_btn = new TextView[7];
    private TextView[] btn_time = new TextView[15];
    private int[] btn_time_id = {R.id.time_9, R.id.time_10, R.id.time_11,
            R.id.time_12, R.id.time_13, R.id.time_14, R.id.time_15, R.id.time_16,
            R.id.time_17, R.id.time_18, R.id.time_19, R.id.time_20,
            R.id.time_21, R.id.time_22, R.id.time_23};
    private int[] btn_slide_id = {R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7};
    public String result_date[] = new String[7];
    public String result = "";
    public int pos = 0;
    private TextView tv_date;
    private boolean[][] timeTable = new boolean[7][3];
    private String timeString = "0#0#1#0#0#0#1#0#1#1#0#1#0#0#1#0#1#1#1#1#0#";
    private int week_dir[] = new int[7];

    public ChoseTimeDialog(Context context, TextView textView, String str) {
        super(context);
        tv_date = textView;
        timeString = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_for_dialog);
        initView();
        setSlide_btnText();
        Calendar cc = Calendar.getInstance();
        int week = cc.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < 7; i++) {
            week_dir[i] = (week + i - 1) % 7;
        }
        timeTable = Cast1Dto2D(CastStringToArray(timeString));
        boolean[] flag = getBtnTimeStatus(timeTable[week_dir[0]]);

        setBtnTimeStyle(flag);
    }

    private void initView() {
        for (int i = 0; i < 7; i++) {
            slide_btn[i] = (TextView) this.findViewById(btn_slide_id[i]);
            slide_btn[i].setOnClickListener(this);
        }
        for (int i = 0; i < 15; i++) {
            btn_time[i] = (TextView) this.findViewById(btn_time_id[i]);
            btn_time[i].setOnClickListener(this);

        }
    }

    public boolean[] CastStringToArray(String str) {
        String[] flag = str.split("#");
        boolean[] bool = new boolean[flag.length];
        for (int i = 0; i < flag.length; i++) {
            if (flag[i].equals("1")) {
                bool[i] = true;
            } else {
                bool[i] = false;
            }
        }
        return bool;
    }

    public boolean[] getBtnTimeStatus(boolean[] bool) {
        boolean[] res = new boolean[15];
        res[0] = bool[0];
        res[1] = bool[0];
        res[2] = bool[0];
        res[3] = bool[0];
        res[4] = bool[1];
        res[5] = bool[1];
        res[6] = bool[1];
        res[7] = bool[1];
        res[8] = bool[1];
        res[9] = bool[2];
        res[10] = bool[2];
        res[11] = bool[2];
        res[12] = bool[2];
        res[13] = bool[2];
        res[14] = bool[2];
        return res;

    }

    public void setBtnTimeStyle(boolean[] flag) {
        for (int i = 0; i < 15; i++) {
            Log.i("88888888888888888", flag[i] + "");
            if (!flag[i]) {
                btn_time[i].setBackgroundColor(Color.parseColor("#ebebeb"));
                btn_time[i].setOnClickListener(null);
            }
        }
    }

    /**
     * 重置按钮的状态
     */
    public void BtnTimeReturn() {
        for (int i = 0; i < 15; i++) {

            btn_time[i].setBackgroundColor(Color.parseColor("#ffffff"));
            btn_time[i].setOnClickListener(this);

        }

    }

    public boolean[][] Cast1Dto2D(boolean[] bool) {
        boolean[][] res = new boolean[(int) (bool.length / 3)][(int) (bool.length / 7)];
        for (int i = 0; i < bool.length; i++) {
            int row = (int) i / 3;
            int col = i % 3;
            res[row][col] = bool[i];
        }
        return res;
    }

    public void setSlideBtnStyle(int position) {
        slide_btn[position].setBackgroundResource(R.drawable.shape_post_service_dialog_btn_selected);
        slide_btn[position].setTextColor(Color.parseColor("#99d71f1b"));
        for (int i = 0; i < 7; i++) {
            if (i == position) {
                continue;
            }
            slide_btn[i].setBackgroundResource(R.drawable.shape_post_service_dialog_btn_unselected);
            slide_btn[i].setTextColor(Color.parseColor("#D4D4D4"));
        }
    }

    /**
     * 设置滑动按钮的内容
     */
    public void setSlide_btnText() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < 7; i++) {
            String str = getDate(year, month, day) + "\n" + getWeekday(week + i);
            result_date[i] = year + "-" + getDate(year, month, day);
            slide_btn[i].setText(str);
            if (i == 0) {
                slide_btn[i].setTextColor(Color.parseColor("#99d71f1b"));
                slide_btn[i].setBackgroundResource(R.drawable.shape_post_service_dialog_btn_selected);
            }
            day++;
        }

    }

    public String getDate(int year, int month, int day) {
        String m = "", d = "";
        switch (month) {
            case 1:
                if (day > 31) {
                    d = day % 31 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }

                break;
            case 2:
                if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
                    if (day > 29) {
                        d = day % 29 + "";
                        m = (++month) + "";
                    } else {
                        d = day + "";
                        m = month + "";
                    }
                } else {
                    if (day > 28) {
                        d = day % 28 + "";
                        m = (++month) + "";
                    } else {
                        d = day + "";
                        m = month + "";
                    }
                }
                break;
            case 3:
                if (day > 31) {
                    d = day % 31 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 4:
                if (day > 30) {
                    d = day % 30 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 5:
                if (day > 31) {
                    d = day % 31 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 6:
                if (day > 30) {
                    d = day % 30 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 7:
                if (day > 31) {
                    d = day % 31 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 8:
                if (day > 31) {
                    d = day % 31 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 9:
                if (day > 30) {
                    d = day % 30 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 10:
                if (day > 31) {
                    d = day % 31 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 11:
                if (day > 30) {
                    d = day % 30 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;
            case 12:
                if (day > 31) {
                    d = day % 31 + "";
                    m = (++month) + "";
                } else {
                    d = day + "";
                    m = month + "";
                }
                break;

            default:
                break;

        }
        return m + "-" + d;
    }

    public String getWeekday(int i) {
        String str = "";
        switch (i % 7) {
            case 1:
                str = "星期日";
                break;
            case 2:
                str = "星期一";
                break;
            case 3:
                str = "星期二";
                break;
            case 4:
                str = "星期三";
                break;
            case 5:
                str = "星期四";
                break;
            case 6:
                str = "星期五";
                break;
            case 0:
                str = "星期六";
                break;
            default:
                break;
        }
        return str;
    }

    @Override
    public void onClick(View v) {
        boolean[] flag;
        switch (v.getId()) {
            //选择日期
            case R.id.btn_1:
                setSlideBtnStyle(0);
                BtnTimeReturn();
                flag = getBtnTimeStatus(timeTable[week_dir[0]]);
                setBtnTimeStyle(flag);
                pos = 0;
                break;
            case R.id.btn_2:
                setSlideBtnStyle(1);
                BtnTimeReturn();
                flag = getBtnTimeStatus(timeTable[week_dir[1]]);
                setBtnTimeStyle(flag);
                pos = 1;
                break;
            case R.id.btn_3:
                setSlideBtnStyle(2);
                BtnTimeReturn();
                flag = getBtnTimeStatus(timeTable[week_dir[2]]);
                setBtnTimeStyle(flag);
                pos = 2;
                break;
            case R.id.btn_4:
                setSlideBtnStyle(3);
                BtnTimeReturn();
                flag = getBtnTimeStatus(timeTable[week_dir[3]]);
                setBtnTimeStyle(flag);
                pos = 3;
                break;
            case R.id.btn_5:
                setSlideBtnStyle(4);
                BtnTimeReturn();
                flag = getBtnTimeStatus(timeTable[week_dir[4]]);
                setBtnTimeStyle(flag);
                pos = 4;
                break;
            case R.id.btn_6:
                setSlideBtnStyle(5);
                BtnTimeReturn();
                flag = getBtnTimeStatus(timeTable[week_dir[5]]);
                setBtnTimeStyle(flag);
                pos = 5;
                break;
            case R.id.btn_7:
                setSlideBtnStyle(6);
                BtnTimeReturn();
                flag = getBtnTimeStatus(timeTable[week_dir[6]]);
                setBtnTimeStyle(flag);
                pos = 6;
                break;
            //选择时间
            case R.id.time_9:
                result = result_date[pos] + "-" + btn_time[0].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_10:
                result = result_date[pos] + "-" + btn_time[1].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_11:
                result = result_date[pos] + "-" + btn_time[2].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_12:
                result = result_date[pos] + "-" + btn_time[3].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_13:
                result = result_date[pos] + "-" + btn_time[4].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_14:
                result = result_date[pos] + "-" + btn_time[5].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_15:
                result = result_date[pos] + "-" + btn_time[6].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_16:
                result = result_date[pos] + "-" + btn_time[7].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_17:
                result = result_date[pos] + "-" + btn_time[8].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_18:
                result = result_date[pos] + "-" + btn_time[9].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_19:
                result = result_date[pos] + "-" + btn_time[10].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_20:
                result = result_date[pos] + "-" + btn_time[11].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_21:
                result = result_date[pos] + "-" + btn_time[12].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_22:
                result = result_date[pos] + "-" + btn_time[13].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
            case R.id.time_23:
                result = result_date[pos] + "-" + btn_time[14].getText().toString();
                tv_date.setText(result);
                this.dismiss();
                break;
        }
    }

}
