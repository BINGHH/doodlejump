package com.example.lenovo.doodlejump;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Doodle extends Sprite{
    private boolean still;              //true则代表doodle需要停留在屏幕中部 false代表不需要
    private boolean gameOver;
    private boolean isEquipRocket;
    private int rocketLastTime;
    private boolean isWearingCloak;
    private int maxCloakLastTime;
    private int cloakLastTime;
    protected int direction;             //doodle的朝向. 1: 朝右; 0: 朝左
    Doodle(int screenWidth, int screenHeight, Context context){
        still = false;
        gameOver = false;
        isEquipRocket = false;
        rocketLastTime = 0;
        isWearingCloak = false;
        maxCloakLastTime = 240;
        cloakLastTime = 0;
        //根据计算, 每次普通跳跃花费630ms的时间到达最高点, 跳跃高度为600像素.
        //因此重力加速度g = 0.00322 px/ms² 初始速度为-1.96 px/ms
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        width = 138;
        height = 135;
        x = (screenWidth - 138) / 2;
        y = screenHeight - 135;
        direction = 0;      // 初始时doodle朝左
        vx = 0;
        vy = -1.96;
        additionVy = 0;
        g = 0.00322;
        if (!setBitmap(context, R.drawable.ldoodle)) Log.e(TAG, "Unable to set doodle.bitmap.");
        if (!setSecBitmap(context, R.drawable.rdoodle)) Log.e(TAG, "Unable to set doodle.secBitmap.");
    }

    public void refresh(Context context) {
        //我们假定每个interval刷新一次
        if(isEquipRocket) {
            isWearingCloak = false;
            cloakLastTime = 0;
            vy = -6;
            g = 0;
            rocketLastTime--;
            if(rocketLastTime <= 0) {
                isEquipRocket = false;
                g = 0.00322;
                vy = -1.96;
                width = 138;
                height = 135;
                if (!setBitmap(context, R.drawable.ldoodle)) Log.e(TAG, "Unable to set doodle.bitmap.");
                if (!setSecBitmap(context, R.drawable.rdoodle)) Log.e(TAG, "Unable to set doodle.secBitmap.");
            }
        }

        if(isWearingCloak) {
            cloakLastTime--;
            if(cloakLastTime <= 0) {
                isWearingCloak = false;
                if (!setBitmap(context, R.drawable.ldoodle)) Log.e(TAG, "Unable to set doodle.bitmap.");
                if (!setSecBitmap(context, R.drawable.rdoodle)) Log.e(TAG, "Unable to set doodle.secBitmap.");
            }
        }

        x += (int) (vx * interval);
        if (x < -width / 2) x = screenWidth - width / 2 - 1;        //从屏幕左边出去能从屏幕右边回来
        else if (x > screenWidth - width / 2) x = -width / 2 + 1;   //从屏幕右边出去能从屏幕左边回来

        if(!still) y = y + (int) (vy * interval);
        vy = vy + g * interval;
        if (y <= screenHeight / 2 && vy < 0) still = true;
        else still = false;

        if (y > screenHeight)
            gameOver = true;
    }

    public boolean isStill() {
        return still;
    }

    public void setVx(double roll) {
        vx = - 4.8 * Math.sin(roll / 180 * Math.PI);
        if(direction == 0 && vx > 0) {
            //从朝左变成朝右
            direction = 1;
            x = x + 46;
        }
        else if(direction == 1 && vx < 0) {
            //从朝右变成朝左
            direction = 0;
            x = x - 46;
        }
        //Log.e(TAG, "vx = " + vx);
    }

    public void setRocketLastTime(int lastTime) {
        rocketLastTime = lastTime;
    }

    public void yesRocketOn(Context context) {
        //让doodle装备上火箭助推器
        isEquipRocket = true;
        width = 222;
        height = 212;
        if (!setBitmap(context, R.drawable.lwithrocket)) Log.e(TAG, "Unable to set doodle.bitmap.");
        if (!setSecBitmap(context, R.drawable.rwithrocket)) Log.e(TAG, "Unable to set doodle.secBitmap.");
    }

    public boolean isEquipRocket() {
        return isEquipRocket;
    }

    public void wearCloak(Context context){
        if(!isWearingCloak) {
            if (!setBitmap(context, R.drawable.ltpdoodle)) Log.e(TAG, "Unable to set doodle.bitmap.");
            if (!setSecBitmap(context, R.drawable.rtpdoodle)) Log.e(TAG, "Unable to set doodle.secBitmap.");
        }
        isWearingCloak = true;
        cloakLastTime = maxCloakLastTime;
    }

    public boolean isWearingCloak() {
        return isWearingCloak;
    }

    public void yesGameOver() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }


    public void drawBitmap(Canvas canvas, Paint paint) {
        if(direction == 0) drawBitmap(canvas, paint, 0);
        else drawBitmap(canvas, paint, 1);
    }
}
