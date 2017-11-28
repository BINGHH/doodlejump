package com.example.lenovo.doodlejump;

import android.graphics.Bitmap;


public class Sprite {
    protected int interval = 16;         //最小时间间隔 单位: ms
    protected Bitmap bitmap;
    protected int screenWidth, screenHeight;    //屏幕长宽
    protected int width, height;    //单位: px  该精灵的长宽.
    protected int x, y;             //单位: px
    protected double vx, vy;        //单位: px/ms
    protected double g;             //重力加速度, 单位px/ms²

    protected boolean setBitmap(Bitmap src) {
        //使用src与指定的长宽设置bitmap, 如果成功则返回true, 否则返回false.
        try {
            bitmap = Bitmap.createScaledBitmap(src, width, height, false);
        } catch (Exception e) {
            bitmap = null;
            return false;
        }
        if (bitmap == null) return false;
        else return true;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }


}
