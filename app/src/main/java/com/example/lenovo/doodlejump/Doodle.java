package com.example.lenovo.doodlejump;


import android.graphics.Bitmap;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Doodle extends Sprite{
    private int hehe;
    public Doodle(int screenWidth, int screenHeight, Bitmap src){
        interval = 16;
        hehe = 0;
        //根据计算, 每次普通跳跃花费500ms的时间到达最高点, 跳跃高度为600像素.
        //因此重力加速度g = 0.0048 px/ms² 初始速度为-2.4 px/ms
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        width = 138;
        height = 135;
        x = (screenWidth - 138) / 2;
        y = screenHeight - 135;
        vx = 0;
        vy = -2.4;
        g = 0.0048;
        if (!setBitmap(src)) Log.e(TAG, "Unable to set doodle.bitmap.");
    }

    public void refresh(){
        //我们假定20ms刷新一次
        y = y + (int)(vy * interval);
        vy = vy + g * interval;
        //Log.e(TAG, "y = " + y + "vy = " + vy + "screenH - height = " + (screenHeight - height));
        if(y > screenHeight - height) {
            y = screenHeight - height;
            vy = -2.4;
        }
    }

}
