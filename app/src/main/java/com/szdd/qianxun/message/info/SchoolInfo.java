package com.szdd.qianxun.message.info;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szdd.qianxun.R;
import com.szdd.qianxun.tools.file.SchoolJsonTool;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/5/17.
 */
public class SchoolInfo extends Activity {
    private ListView list_school;
    private EditText edit;
    private JSONArray array;
    private SchoolAdapter adapter;
    private String[] items;
    private TextView current_tv;
    private int current_yellow = 0;
    private LayoutInflater inflater;
    private ArrayList<String> schoolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_schoolmsg);
        initView();
        inflater = LayoutInflater.from(this);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String province_str = items[current_yellow].replace(" ", "");
                if (province_str.equals("全部")) province_str = null;
                adapter = new SchoolAdapter(getResult(province_str, s.toString()));
                list_school.setAdapter(adapter);
            }
        });
        list_school.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("school", schoolList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void initView() {
        list_school = (ListView) this.findViewById(R.id.list_school);
        ListView list_province = (ListView) this.findViewById(R.id.list_province);
        list_province.setAdapter(new ProvinceAdapter());
        array = SchoolJsonTool.getSchoolArray(SchoolInfo.this);
        adapter = new SchoolAdapter(getResult(null, null));
        list_school.setAdapter(adapter);
        edit = (EditText) this.findViewById(R.id.school_edit);
    }

    class ProvinceAdapter extends BaseAdapter {
        ProvinceAdapter() {
            items = new String[]{
                    "全 部",
                    "!A", "安 徽",
                    "!B", "北 京",
                    "!C", "重 庆",
                    "!F", "福 建",
                    "!G", "甘 肃", "广 东", "广 西", "贵 州",
                    "!H", "海 南", "河 北", "河 南", "黑龙江", "湖 北", "湖 南",
                    "!J", "吉 林", "江 西", "江 苏",
                    "!L", "辽 宁",
                    "!N", "内蒙古", "宁 夏",
                    "!Q", "青 海",
                    "!S", "山 东", "山 西", "陕 西", "上 海", "四 川",
                    "!T", "天 津",
                    "!X", "西 藏", "新 疆",
                    "!Y", "云 南",
                    "!Z", "浙 江"};
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final TextView t = new TextView(viewGroup.getContext());
            t.setPadding(20, 10, 10, 20);
            t.setGravity(Gravity.CENTER);
            if (items[i].startsWith("!")) {
                t.setTextColor(Color.BLACK);
                t.setTextSize(14);
                t.setText(items[i].substring(1));
            } else {
                t.setTextSize(18);
                t.setTextColor(0xff949494);
                t.setText(items[i]);
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_tv != null) current_tv.setBackgroundColor(Color.WHITE);
                        current_tv = t;
                        current_yellow = i;
                        t.setBackgroundColor(getResources().getColor(R.color.yellow));
                        String province = items[i];
                        province = province.replace(" ", "");
                        if (province.equals("全部")) province = null;
                        adapter = new SchoolAdapter(getResult(province, null));
                        list_school.setAdapter(adapter);
                    }
                });
            }
            if (current_yellow == i) {
                current_tv = t;
                t.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else t.setBackgroundColor(getResources().getColor(R.color.white));
            return t;
        }
    }

    class SchoolAdapter extends BaseAdapter {

        public SchoolAdapter(ArrayList<String> arrayList) {
            schoolList = arrayList;
        }

        @Override
        public int getCount() {
            return schoolList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.school_list_item, null);
            TextView tv_province = (TextView) convertView.findViewById(R.id.tv_province);
            tv_province.setText(schoolList.get(position));
            return convertView;
        }
    }

    private ArrayList<String> getResult(String provinceName, String schoolName) {
        ArrayList<String> result = new ArrayList<>();
        JSONObject ob;
        boolean b_p = provinceName != null && !provinceName.equals("");
        boolean b_s = schoolName != null && !schoolName.equals("");
        if (b_p && b_s) for (int i = 0; i < array.size(); i++) {
            ob = array.getJSONObject(i);
            if (ob.getString("place").contains(provinceName)
                    && ob.getString("name").contains(schoolName))
                result.add(ob.getString("name"));
        }
        else if (b_p) for (int i = 0; i < array.size(); i++) {
            ob = array.getJSONObject(i);
            if (ob.getString("place").contains(provinceName))
                result.add(ob.getString("name"));
        }
        else if (b_s) for (int i = 0; i < array.size(); i++) {
            ob = array.getJSONObject(i);
            if (ob.getString("name").contains(schoolName))
                result.add(ob.getString("name"));
        }
        else for (int i = 0; i < array.size(); i++)
                result.add(array.getJSONObject(i).getString("name"));
        return result;
    }
}
