package com.example.lenovo.doodlejump;

import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Platform extends Sprite {
    protected int type;
    protected int baseWidth, baseHeight;
    public Platform(int screenWidth, int screenHeight, int x, int y, Context context){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = x;
        this.y = y;
        additionVy = 0;
        g = 0;
        baseWidth = 194;
        baseHeight = 52;
    }

    public boolean refresh(){
        //如果platform掉出了屏幕外, 就返回false, 通知调用者进行处理
        //否则返回true, 表示不必进行处理
        double sumVy = vy + additionVy;
        y = y + (int)(sumVy * interval);
        if(y > screenHeight )  {
            if(type == PlatType.broken) {
                Log.e(TAG, "A broken platform droped out of the screen. " + "x = " + x + " y = " + y);
            }
            return false;
        }

        int tempX = x + (int)(vx * interval);
        if(tempX >= screenWidth - this.width || tempX <= 0)
            vx = -vx;
        else x = tempX;
        return true;
    }

    public void impactCheck(Doodle doodle, Context context) {
        //检测doodle是否碰撞到platform
        if(doodle.direction == 0) {
            //朝左

            //判断doodle有没有踩在板子上
            if (type != PlatType.broken
                    && doodle.y + doodle.height < this.y + this.height
                    && doodle.y + doodle.height > this.y + this.height - this.baseHeight
                    && doodle.x + doodle.width - 11 > this.x
                    && doodle.x + 46 < this.x + this.width)
                //注意: x方向上判断的是doodle的脚的左右坐标
                doodle.vy = -1.96;

            //判断doodle有没有踩到弹簧上
            if(type == PlatType.withspring
                    && doodle.vy > 0    /* 表明doodle正在下降 */
                    && doodle.y + doodle.height < this.y + 29   /* 29是弹簧高度 */
                    && doodle.y + doodle.height > this.y
                    && doodle.x + doodle.width - 11 > this.x + 48
                    && doodle.x + 46 < this.x + this.width - 91) {
                //doodle踩到弹簧了
                doodle.vy = -3.5;
                this.y = this.y - (132 - this.height);
                this.height = 132;
                if (!setBitmap(context, R.drawable.springupplat))
                    Log.e(TAG, "Unable to set Platform.bitmap.");
            }
        } //朝左
        else {
            //朝右

            //判断doodle有没有踩在板子上
            if (type != PlatType.broken
                    && doodle.y + doodle.height < this.y + this.height
                    && doodle.y + doodle.height > this.y + this.height - this.baseHeight
                    && doodle.x + doodle.width - 46> this.x
                    && doodle.x + 11 < this.x + this.width)
                //注意: x方向上判断的是doodle的脚的左右坐标
                doodle.vy = -1.96;

            //判断doodle有没有踩到弹簧上
            if(type == PlatType.withspring
                    && doodle.vy > 0    /* 表明doodle正在下降 */
                    && doodle.y + doodle.height < this.y + 29   /* 29是弹簧高度 */
                    && doodle.y + doodle.height > this.y
                    && doodle.x + doodle.width - 46 > this.x + 48
                    && doodle.x + 11 < this.x + this.width - 91) {
                //doodle踩到弹簧了
                doodle.vy = -3.5;
                this.y = this.y - (132 - this.height);
                this.height = 132;
                if (!setBitmap(context, R.drawable.springupplat))
                    Log.e(TAG, "Unable to set Platform.bitmap.");
            }
        }
    }
}

class normalPlat extends Platform {
    public normalPlat(int screenWidth, int screenHeight, int x, int y, Context context) {
        super(screenWidth, screenHeight, x, y, context);
        int rv = (int)(Math.random() * 1000);
        if(rv > 750) vx = 0.322;
        else if(rv > 500) vx = -0.322;
        else vx = 0;
        vy = 0;
        width = baseWidth;     height = baseHeight;
        type = PlatType.normal;
        int drawable = vx == 0 ? R.drawable.normalplat : R.drawable.blueplat;
        if (!setBitmap(context, drawable)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }
}

class brokenPlat extends Platform {
    public brokenPlat(int screenWidth, int screenHeight, int x, int y, Context context){
        super(screenWidth, screenHeight, x, y, context);
        int rv = (int)(Math.random() * 1000);
        if(rv > 750) vx = 0.322;
        else if(rv > 500) vx = -0.322;
        else vx = 0;
        vy = 0;
        width = baseWidth;    height = baseHeight;
        type = PlatType.broken;
        if (!setBitmap(context, R.drawable.brokenplat)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }
}

class springPlat extends Platform {
    public springPlat(int screenWidth, int screenHeight, int x, int y, Context context){
        super(screenWidth, screenHeight, x, y, context);
        int rv = (int)(Math.random() * 1000);
        if(rv > 750) vx = 0.322;
        else if(rv > 500) vx = -0.322;
        else vx = 0;
        vy = 0;
        width = baseWidth;    height = 81;
        type = PlatType.withspring;
        if (!setBitmap(context, R.drawable.springplat)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }
}


