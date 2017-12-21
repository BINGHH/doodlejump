package com.example.lenovo.doodlejump;


import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Doodle extends Sprite{
    private boolean still;      //true则代表doodle需要停留在屏幕中部 false代表不需要
    private boolean gameOver;
    public Doodle(int screenWidth, int screenHeight, Context context){
        still = false;
        gameOver = false;
        //根据计算, 每次普通跳跃花费630ms的时间到达最高点, 跳跃高度为600像素.
        //因此重力加速度g = 0.00322 px/ms² 初始速度为-1.96 px/ms
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        width = 138;
        height = 135;
        x = (screenWidth - 138) / 2;
        y = screenHeight - 135;
        vx = 0;
        vy = -1.96;
        additionVy = 0;
        g = 0.00322;
        if (!setBitmap(context, R.drawable.doodle)) Log.e(TAG, "Unable to set doodle.bitmap.");
    }

    public void refresh() {
        //我们假定每个interval刷新一次
        x += (int) (vx * interval);
        //if (vx < 2.4 && vx > -2.4) vx += a * interval;              //横向速度有最大速度限制
        if (x < -width / 2) x = screenWidth - width / 2 - 1;        //从屏幕左边出去能从屏幕右边回来
        else if (x > screenWidth - width / 2) x = -width / 2 + 1;   //从屏幕右边出去能从屏幕左边回来

        if(!still) y = y + (int) (vy * interval);
        vy = vy + g * interval;
        if (y <= screenHeight / 2 && vy < 0) still = true;
        else still = false;

        if (y > screenHeight) {
            gameOver = true;
            //y = screenHeight - height;
            //vy = -1.9;
        }
    }

    public boolean isStill() {
        return still;
    }

    public void setVx(double roll) {
        vx = - 4.8 * Math.sin(roll / 180 * Math.PI);
        //Log.e(TAG, "vx = " + vx);
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
