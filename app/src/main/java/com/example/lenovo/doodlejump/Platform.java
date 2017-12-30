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
    protected boolean valid;
    public Platform(int screenWidth, int screenHeight, int x, int y, Context context){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = x;
        this.y = y;
        additionVy = 0;
        g = 0;
        baseWidth = 194;
        baseHeight = 52;
        valid = true;
    }

    public boolean refresh(){
        //如果platform掉出了屏幕外, 就返回false, 通知调用者进行处理
        //否则返回true, 表示不必进行处理
        double sumVy = vy + additionVy;
        int tempY = y + (int)(sumVy * interval);
        if(tempY > screenHeight ) {
            if (type == PlatType.broken && height == 108)
                valid = false;
            return false;
        }
        y = tempY;

        int tempX = x + (int)(vx * interval);
        if(tempX >= screenWidth - this.width || tempX <= 0)
            vx = -vx;
        else x = tempX;
        return true;
    }

    public void impactCheck(Doodle doodle, Context context) {
        //检测doodle是否碰撞到platform
        int footX1, footX2;
        if(doodle.direction == 0) {
            //朝左
            footX1 = doodle.x + 46;
            footX2 = doodle.x + doodle.width - 11;
        }
        else {
            footX1 = doodle.x + 11;
            footX2 = doodle.x + doodle.width - 46;
        }

        //判断doodle有没有踩在板子上
        if (doodle.y + doodle.height < this.y + this.height
                && doodle.y + doodle.height > this.y + this.height - this.baseHeight
                && footX1 < this.x + this.width
                && footX2 > this.x) {
            //注意: x方向上判断的是doodle的脚的左右坐标
            if(type != PlatType.broken && valid) {
                doodle.vy = -1.96;
                if(type == PlatType.onetouch) valid = false;
            }
            else if(type == PlatType.broken) {
                width = 194;
                height = 108;
                vy = 1;
                if (!setBitmap(context, R.drawable.brokenplat2))
                    Log.e(TAG, "Unable to set Platform.bitmap.");
            }
        }

        //判断doodle有没有踩到弹簧上
        if(type == PlatType.withspring
                && doodle.vy > 0    /* 表明doodle正在下降 */
                && doodle.y + doodle.height < this.y + 29   /* 29是弹簧高度 */
                && doodle.y + doodle.height > this.y
                && footX1 < this.x + this.width - 91
                && footX2 > this.x + 48) {
            //doodle踩到弹簧了
            doodle.vy = -3.5;
            this.y = this.y - (132 - this.height);
            this.height = 132;
            if (!setBitmap(context, R.drawable.springupplat))
                Log.e(TAG, "Unable to set Platform.bitmap.");
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

class whitePlat extends Platform {
    public whitePlat(int screenWidth, int screenHeight, int x, int y, Context context) {
        super(screenWidth, screenHeight, x, y, context);
        vx = 0;
        vy = 0;
        width = baseWidth;
        height = baseHeight;
        type = PlatType.onetouch;
        if (!setBitmap(context, R.drawable.whiteplat)) Log.e(TAG, "Unable to set Platform.bitmap.");
    }
}


