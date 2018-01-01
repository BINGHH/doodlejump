package com.example.lenovo.doodlejump;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class Sprite {
    int interval = 16;         //最小时间间隔 单位: ms
    private Bitmap bitmap = null;
    private Bitmap secBitmap = null;
    int screenWidth, screenHeight;    //屏幕长宽
    int width, height;    //单位: px  该精灵的长宽.
    int x, y;                //单位: px
    double vx, vy;           //单位: px/ms
    double additionVy;       //除自己本身外的附加速度
    double g;             //重力加速度, 向下为正, 单位px/ms²
    //protected double a;             //横向加速度, 向右为正, 单位px/ms²

    Bitmap getBitmap() {
        return bitmap;
    }

    public int getWidth() { return width; }

    public int getHeight(){ return height; }

    boolean setBitmap(Context context, int src) {
        //src为指定的图像, 示例取值为R.drawable.ldoodle, 如果成功则返回true, 否则返回false.
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

    boolean setSecBitmap(Context context, int src) {
        //src为指定的图像, 示例取值为R.drawable.ldoodle, 如果成功则返回true, 否则返回false.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = 2;
        try {
            secBitmap = BitmapFactory.decodeResource(context.getResources(), src, options);
            secBitmap = Bitmap.createScaledBitmap(secBitmap, width, height, false);
        } catch (Exception e) {
            secBitmap = null;
            return false;
        }
        if(secBitmap == null) return false;
        else return true;
    }

    public void drawBitmap(Canvas canvas, Paint paint, int num) {
        // num == 0: draw bitmap; num == 1: draw secBitmap
        if(num == 0) {
            try {
                canvas.drawBitmap(bitmap, x, y, paint);
            } catch (Exception e) {
                Log.e(TAG, "sprite.drawBitmap() failed.");
            }
        }
        else if(num == 1) {
            try {
                canvas.drawBitmap(secBitmap, x, y, paint);
            } catch (Exception e) {
                Log.e(TAG, "sprite.drawBitmap() failed.");
            }
        }
    }
}

class Title extends Sprite {
    private int score;
    private int scoreX, scoreY;     //score的坐标, 决定score显示的位置
    Title(int screenWidth, int screenHeight, Context context) {
        score = 0;
        scoreX = 45;
        scoreY = 70;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        width = screenWidth;
        height = (int) (screenWidth / 1080 * 148);
        x = 0;      y = 0;
        vx = 0;     vy = 0;
        additionVy = 0;
        g = 0;
        //a = 0;
        if(!setBitmap(context, R.drawable.title)) Log.e(TAG, "Unable to set title.bitmap.");
    }

    void addScore(int deltaY) {
        score += deltaY / 2;
    }

    int getScore(){
        return score;
    }

    @Override
    public void drawBitmap(Canvas canvas, Paint paint, int num) {
        super.drawBitmap(canvas, paint, 0);
        try {
            canvas.drawText(Integer.toString(score), scoreX, 70, paint);
            //canvas.drawBitmap(bitmap, x, y, paint);
        } catch (Exception e) {
            Log.e(TAG, "sprite.drawText() failed.");
        }
    }

}

class Monster extends Sprite {
    private int localX;
    private int localY;
    private double localVx, localVy, maxLocalVx, maxLocalVy;
    Monster(int screenWidth, int screenHeight, int baseY, Context context) {
        localX = 0;
        localY = 0;
        maxLocalVx = 0.3;
        maxLocalVy = 0.3;
        localVx = maxLocalVx;
        localVy = maxLocalVy;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        vx = 0;
        vy = 0;
        additionVy = 0;
        g = 0;
        int rv = (int)(Math.random() * 1000);
        int drawable;
        if(rv > 700) {
            drawable = R.drawable.monster1;
            width = 187;
            height = 166;
        }
        else if(rv > 300) {
            drawable = R.drawable.monster2;
            width = 262;
            height = 158;
        }
        else {
            drawable = R.drawable.monster3;
            width = 157;
            height = 120;
        }
        this.x = (int) (Math.random() * (screenWidth - width - 50) + 50);
        this.y = baseY - height - 10;
        if(!setBitmap(context, drawable)) Log.e(TAG, "Unable to set Monster.bitmap");
    }

    boolean refresh() {
        //如果platform掉出了屏幕外, 就返回false, 通知调用者进行处理
        //否则返回true, 表示不必进行处理

        localY += (int) (localVy * interval);
        if(localY > 30) localVy = -maxLocalVy;
        else if(localY < -30) localVy = maxLocalVy;

        double sumVy = vy + localVy + additionVy;
        int tempY = y + (int) (sumVy * interval);
        if(tempY > screenHeight) return false;
        y = tempY;

        localX += (int) (localVx * interval);
        if(localX > 30) localVx = -maxLocalVx;
        else if(localX < -30) localVx = maxLocalVx;

        double sumVx = vx + localVx;
        int tempX = x + (int)(sumVx * interval);
        if(tempX >= screenWidth - this.width || tempX <= 0)
            vx = -vx;
        else x = tempX;
        return true;
    }

    void impactCheck(Doodle doodle, Context context) {
        //检测doodle是否碰撞到monster
        int footX1, footX2;
        if (doodle.direction == 0) {
            //朝左
            footX1 = doodle.x + 46;
            footX2 = doodle.x + doodle.width - 11;
        } else {
            footX1 = doodle.x + 11;
            footX2 = doodle.x + doodle.width - 46;
        }

        if (doodle.vy > 0) {
            //此时doodle正在下降 如果踩到monster, 就获得类似弹簧的加速效果
            //判断doodle有没有踩到monster上
            if (doodle.y + doodle.height < this.y + this.height
                    && doodle.y + doodle.height > this.y
                    && footX1 < this.x + this.width
                    && footX2 > this.x) {
                //注意: x方向上判断的是doodle的脚的左右坐标
                doodle.vy = -3;
                vy = 1.7;
            }
        } else if (doodle.vy <= 0) {
            //此时doodle正在上升 如果撞到monster, 游戏结束
            if (doodle.y < this.y + this.height
                    && doodle.y + doodle.height > this.y
                    && doodle.x < this.x + this.width
                    && doodle.x + doodle.width > this.x
                    && !doodle.isEquipRocket())
                doodle.yesGameOver();
        }
    }
}

class Rocket extends Sprite {
    /*  isEquipped == 0 && isTimeOut == 0: 还未被装备
        isEquipped == 1 && isTimeOut == 0: 已经装备上并且火箭还在工作
        isEquipped == 0 && isTimeOut == 1: 火箭推进时间已到, 开始脱落
     */
    private boolean isEquipped;
    private boolean isTimeOut;
    private int maxLastTime;
    private int lastTime;
    private int numPlat;
    Rocket(int screenWidth, int screenHeight, Platform plat, int numPlat, Context context) {
        isEquipped = false;
        isTimeOut = false;
        maxLastTime = 160;
        lastTime = 0;
        this.numPlat = numPlat;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        width = 82;     height = 124;
        x = plat.x + plat.width / 2 - width / 2;
        y = plat.y - height;
        vx = plat.vx;       vy = plat.vy;
        additionVy = plat.additionVy;
        g = plat.g;
        //Log.e(TAG, "A Rocket is creating. Plattype = " + plat.type);
        if(!setBitmap(context, R.drawable.rocket)) Log.e(TAG, "Unable to set Rocket.bitmap");
        if(!setSecBitmap(context, R.drawable.halfrocket)) Log.e(TAG, "Unable to set Rocket.secBitmap");
    }

    boolean refresh(Platform [] plat, Doodle doodle) {
        //如果rocket掉出了屏幕外, 就返回false, 通知调用者进行处理
        //否则返回true, 表示不必进行处理
        if(!isEquipped && !isTimeOut) {
            if(numPlat >= 0) {
                //说明依附的plat还未掉出屏幕外
                //Log.e(TAG, "Rocket refreshing: plattype = " + plat[numPlat].type + " num = " + numPlat);
                x = plat[numPlat].x + plat[numPlat].width / 2 - width / 2;
                y = plat[numPlat].y - height;
            }
            else {
                //依附的plat已经掉出屏幕外
                vy = 1;
                double sumVy = vy + additionVy;
                y = y + (int) (sumVy * interval);
                if(y > screenHeight)
                    return false;
            }
            return true;
        }
        else if(isEquipped && !isTimeOut) {
            /*  这里火箭的x, y坐标随便设什么都行.
                因为此时火箭正在飞行, 火箭的图片包含在doodle.bitmap中,
                此时Rocket.bitmap实际上并不显示.
            */
            lastTime--;
            if(lastTime <= 2) {
                lastTime = 0;
                isEquipped = false;
                isTimeOut = true;
                if(doodle.direction == 0) {
                    //朝左
                    x = doodle.x + 149;
                    y = doodle.y + 5;
                    vx = 1;
                    vy = 0;
                    g = 0.00322;
                }
                else {
                    //朝右
                    x = doodle.x + doodle.width - 149;
                    y = doodle.y + 5;
                    vx = -1;
                    vy = 0;
                    g = 0.00322;
                }
            }
            return true;
        }
        else if(!isEquipped) {
            x += (int) (vx * interval);
            if(x < -width / 2) return false;
            else if(x > screenWidth - width / 2) return  false;

            double sumVy = vy + additionVy;
            y = y + (int) (sumVy * interval);
            vy = vy + g * interval;
            if(y > screenHeight)
                return false;
        }
        else {
            Log.e(TAG, "Unexpected branch.");
            return false;
        }
        return  true;
    }

    void drawBitmap(Canvas canvas, Paint paint) {
        if(!isEquipped && !isTimeOut)
            drawBitmap(canvas, paint, 0);
        else if(!isEquipped)
            drawBitmap(canvas, paint, 1);
    }

    void impactCheck(Doodle doodle, Context context) {
        //检测doodle是否捡到rocket
        if(!isEquipped && !isTimeOut) {
            //只有当rocket还在plat上时才能被捡起
            if (doodle.y < this.y + this.height
                    && doodle.y + doodle.height > this.y
                    && doodle.x < this.x + this.width
                    && doodle.x + doodle.width > this.x) {
                this.isEquipped = true;
                this.isTimeOut = false;
                this.numPlat = -1;
                this.lastTime = maxLastTime;
                doodle.setRocketLastTime(maxLastTime);
                doodle.yesRocketOn(context);
            }
        }
    }

    int getNumPlat() {
        return numPlat;
    }

    void platInvalidate() {
        //表明附着的plat已经掉出屏幕外
        if(!isEquipped && !isTimeOut)
            numPlat = -1;
    }

}
