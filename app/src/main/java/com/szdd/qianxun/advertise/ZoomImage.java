package com.szdd.qianxun.advertise;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.szdd.qianxun.R;
import com.szdd.qianxun.advertise.customview.CustomSurfaceView;
import com.szdd.qianxun.tools.all.StaticMethod;
import com.szdd.qianxun.tools.bitmap.BitmapListener;
import com.szdd.qianxun.tools.file.FileTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by linorz on 2016/4/3.
 */
public class ZoomImage extends Activity {
    private String filepath = "download";
    CustomSurfaceView sv;
    Dialog method_dialog;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoomimage);
        initDialog();
        sv = (CustomSurfaceView) findViewById(R.id.zoom_image);
        findViewById(R.id.zoom_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Bitmap设置
        Intent intent = getIntent();
        bmp = intent.getParcelableExtra("bitmap");
        if (bmp == null) {
            String url = intent.getStringExtra("url");
            if (url != null)
                StaticMethod.BITMAP(this, url, new BitmapListener() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null)
                            sv.setBitmap(bitmap);
                        else
                            sv.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mascot_orange2));
                    }
                });
            else
                sv.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mascot_orange2));
        } else
            sv.setBitmap(bmp);

        //设置长按事件
        sv.setLongTouch(new CustomSurfaceView.LongTouchEvent() {
            @Override
            public void longTouch() {
                method_dialog.show();
            }
        });
        filepath = new File(FileTool.getBaseSDCardPath(), filepath).getAbsolutePath();
    }

    private void initDialog() {
        method_dialog = new AlertDialog.Builder(this)
                .setItems(new String[]{"保存", "取消"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                saveImageToGallery(ZoomImage.this, bmp, filepath);
                                break;
                            case 1:
                                method_dialog.dismiss();
                                break;
                        }
                    }
                }).create();
        method_dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = method_dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp, String filepath) {
        // 首先保存图片
        File appDir = new File(filepath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
    }
}