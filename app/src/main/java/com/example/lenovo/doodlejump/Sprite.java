package com.example.lenovo.doodlejump;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class Sprite {
    protected int interval;         //最小时间间隔 单位: ms
    protected Bitmap bitmap;
    protected int screenWidth, screenHeight;    //屏幕长宽
    protected int width, height;    //单位: px  该精灵的长宽.
    public int x, y;                //单位: px
    public double vx, vy;           //单位: px/ms
    protected double g;             //重力加速度, 向下为正, 单位px/ms²
    protected double a;             //横向加速度, 向右为正, 单位px/ms²

    public boolean setBitmap(Context context, int src) {
        //使用src与指定的长宽设置bitmap, 如果成功则返回true, 否则返回false.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = 2;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), src, options);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        } catch (Exception e) {
            bitmap = null;
            return false;
        }
        if(bitmap == null) return false;
        else return true;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }


}
