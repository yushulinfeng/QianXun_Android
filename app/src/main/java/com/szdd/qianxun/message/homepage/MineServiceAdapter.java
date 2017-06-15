package com.szdd.qianxun.message.homepage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.tools.PxAndDip;
import com.szdd.qianxun.message.msg_tool.InfoTool;
import com.szdd.qianxun.message.msg_tool.MsgPublicTool;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.connect.ConnectDialog;
import com.szdd.qianxun.tools.connect.ConnectList;
import com.szdd.qianxun.tools.connect.ConnectListener;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.szdd.qianxun.tools.views.QianxunToast;

import java.util.List;
import java.util.Map;

/**
 * Created by linorz on 2016/3/26.
 */
public class MineServiceAdapter extends RecyclerView.Adapter<MineServiceAdapter.ServiceView> {
    private Context context;
    private List<Map<String, Object>> mapList;
    private LayoutInflater inflater;
    private int width;
    private RemoveItem removeItem;

    public MineServiceAdapter(List<Map<String, Object>> list) {
        mapList = list;
    }

    public MineServiceAdapter(Context context, List<Map<String, Object>> mapList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mapList = mapList;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        width -= PxAndDip.dipTopx(context, 80);
        width /= 3;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    @Override
    public ServiceView onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = inflater.inflate(R.layout.msg_mine_service_item, viewGroup, false);
        return new ServiceView(view);
    }

    @Override
    public void onBindViewHolder(final ServiceView sv, final int i) {
        String url = (String) mapList.get(i).get("url");//图片地址
        sv.style.setText((String) mapList.get(i).get("style"));//分类
        sv.content.setText((String) mapList.get(i).get("content"));//内容
        sv.price.setText((String) mapList.get(i).get("price"));//价格
        sv.unit.setText((String) mapList.get(i).get("unit"));//单价
        sv.sale.setText((String) mapList.get(i).get("sale"));//销量
        sv.id = (long) mapList.get(i).get("id");//服务id
        // 加载图片
        StaticMethod.UBITMAP(url, sv.image);
        sv.s_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(i, sv.id);
                return true;
            }
        });
    }

    public void setSquareImage(ImageView image) {
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) image.getLayoutParams();
        ll.height = width;
        ll.width = width;
        image.setLayoutParams(ll);
    }

    public class ServiceView extends RecyclerView.ViewHolder {
        long id;
        public ImageView image;
        public TextView style, content, price, unit, sale;
        public View s_view;

        public ServiceView(View view) {
            super(view);
            s_view = view;
            image = (ImageView) view.findViewById(R.id.mine_service_image);
            setSquareImage(image);
            style = (TextView) view.findViewById(R.id.mine_service_style);
            content = (TextView) view.findViewById(R.id.mine_service_content);
            price = (TextView) view.findViewById(R.id.mine_service_price);
            unit = (TextView) view.findViewById(R.id.mine_service_unit);
            sale = (TextView) view.findViewById(R.id.mine_service_sale);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MsgPublicTool.showServiceDetail(context, String.valueOf(id));
                }
            });
        }
    }

    private void showDialog(final int i, final long id) {
        Dialog dialog = new AlertDialog.Builder(context).setTitle("是否要删除？")
                .setItems(new String[]{"是", "否"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteService(i, id);
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void deleteService(final int i, final long id) {
        StaticMethod.POST(context, ServerURL.DELETE_SERVICE, new ConnectListener() {
            @Override
            public ConnectList setParam(ConnectList list) {
                list.put("userId", InfoTool.getUserID(context));
                list.put("serviceId", id);
                return list;
            }

            @Override
            public ConnectDialog showDialog(ConnectDialog dialog) {
                return null;
            }

            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "1":
                        if (removeItem != null) removeItem.removeNotify(i);
                        break;
                    case "-1":
                        showToast("失败");
                        break;
                    case "-2":
                        showToast("服务器出错");
                        break;
                    case "-3":
                        showToast("这不是您的服务");
                        break;
                    case "-4":
                        showToast("没有此服务");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void setRemoveItem(RemoveItem removeItem) {
        this.removeItem = removeItem;
    }

    public interface RemoveItem {
        public void removeNotify(int i);
    }


    private void showToast(String text) {
        QianxunToast.showToast(context, text, QianxunToast.LENGTH_SHORT);
    }
}
