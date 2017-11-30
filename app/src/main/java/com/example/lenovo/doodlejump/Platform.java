package com.example.lenovo.doodlejump;

import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Platform extends Sprite {
    protected int type;
    public Platform(int screenWidth, int screenHeight, int x, int y, Context context){
        interval = 16;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = x;
        this.y = y;
        additionVy = 0;
        g = 0;      a = 0;
        width = 185;
        height = 55;
    }

    public boolean refresh(){
        //如果platform掉出了屏幕外, 就返回false, 通知调用者进行处理
        //否则返回true, 表示不必进行处理
        double sumVy = vy + additionVy;
        y = y + (int)(sumVy * interval);
        if(y > screenHeight ) return false;

        x = x + (int)(vx * interval);

        return true;
    }

    public void impactCheck(Doodle doodle) {
        //检测doodle是否碰撞到platform
        //若发生碰撞则返回true, 否则返回false
        if(doodle.y + doodle.height < this.y + this.height
                && doodle.y + doodle.height > this.y
                && doodle.x + doodle.width > this.x
                && doodle.x < this.x + this.width )
            doodle.vy = -2.4;
    }

}

class normalPlat extends Platform {
    public normalPlat(int screenWidth, int screenHeight, int x, int y, Context context) {
        super(screenWidth, screenHeight, x, y, context);
        vx = 0;     vy = 0;
        type = PlatType.normal;
        if (!setBitmap(context, R.drawable.normalplat)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }
}

