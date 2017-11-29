package com.example.lenovo.doodlejump;

import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Platform extends Sprite {
    protected boolean validate;
    protected int type;
    public Platform(int screenWidth, int screenHeight, int x, int y, Context context){
        interval = 16;
        validate = true;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = x;
        this.y = y;
        g = 0;      a = 0;
        width = 185;
        height = 55;
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

    public void reuse(int screenWidth, int screenHeight, int x, int y, Context context) {
        interval = 16;
        validate = true;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = x;
        this.y = y;
        g = 0;      a = 0;
        width = 185;
        height = 55;
    }
}

class normalPlat extends Platform {
    public normalPlat(int screenWidth, int screenHeight, int x, int y, Context context) {
        super(screenWidth, screenHeight, x, y, context);
        vx = 0;     vy = 0;
        type = PlatType.normal;
        if (!setBitmap(context, R.drawable.normalplat)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }

    @Override
    public void reuse(int screenWidth, int screenHeight, int x, int y, Context context) {
        super.reuse(screenWidth, screenHeight, x, y, context);
        vx = 0;     vy = 0;
        type = PlatType.normal;
        if (!setBitmap(context, R.drawable.normalplat)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }




}

