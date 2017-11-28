package com.example.lenovo.doodlejump;

import android.graphics.Bitmap;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Platform extends Sprite {
    private boolean validate;

    public Platform(int screenWidth, int screenHeight, Bitmap src){
        validate = true;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        width = 185;
        height = 55;
        vx = 0;     vy = 0;
        x = 0;      y = 0;
        if (!setBitmap(src)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }

    public boolean refresh(){
        //如果platform掉出了屏幕外, 就返回true, 通知调用者进行处理
        //否则不必进行处理
        y = y + (int)(vy * interval);
        vy = vy + g * interval;
        if(y > screenHeight ) return true;
        else return false;
    }

    public boolean isValidate() {
        //true代表有效, false代表无效
        return validate;
    }

    public void recycle() {
        //暂时用不上了, 销毁以释放空间
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        validate = false;
    }

    public void reuse(int screenWidth, int screenHeight, Bitmap src) {
        //重用, 代码与构造函数一致
        validate = true;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        width = 185;
        height = 55;
        vx = 0;     vy = 0;
        x = 0;      y = 0;
        if (!setBitmap(src)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }


}
