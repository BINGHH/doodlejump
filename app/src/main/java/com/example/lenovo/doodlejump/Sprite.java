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
    private Bitmap bitmap;
    private Bitmap secBitmap;
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
