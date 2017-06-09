package com.szdd.qianxun.bank;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class BorderTextView extends TextView {

    public BorderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.GRAY);
        PathEffect effects = new DashPathEffect(new float[]{4, 4}, 1);
        paint.setPathEffect(effects);
        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
//1、横坐标0到this.getWidth()-1，纵坐标0到0
        canvas.drawLine(0, -3, 0, this.getHeight() - 1, paint);
//2、横坐标0到0，纵坐标0到this.getHeight()-1
        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
//3、横坐标this.getWidth()-1到this.getWidth()-1，纵坐标0到this.getHeight()-1
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);
//4、横坐标0到this.getWidth()-1，纵坐标this.getHeight()-1到this.getHeight()-1
//下面用图介绍边框的绘制
    }
}

