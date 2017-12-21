package com.example.lenovo.doodlejump;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class Sprite {
    protected int interval = 16;         //最小时间间隔 单位: ms
    protected Bitmap bitmap;
    protected int screenWidth, screenHeight;    //屏幕长宽
    protected int width, height;    //单位: px  该精灵的长宽.
    protected int x, y;                //单位: px
    protected double vx, vy;           //单位: px/ms
    protected double additionVy;       //除自己本身外的附加速度
    protected double g;             //重力加速度, 向下为正, 单位px/ms²
    //protected double a;             //横向加速度, 向右为正, 单位px/ms²

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getWidth() { return width; }

    public int getHeight(){ return height; }

    public boolean setBitmap(Context context, int src) {
        //src为指定的图像, 示例取值为R.drawable.doodle, 如果成功则返回true, 否则返回false.
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

    public void drawBitmap(Canvas canvas, Paint paint) {
        try {
            canvas.drawBitmap(bitmap, x, y, paint);
        } catch (Exception e) {
            Log.e(TAG, "sprite.drawBitmap() failed.");
        }
    }
}

class Title extends Sprite {
    private int score;
    private int scoreX, scoreY;     //score的坐标, 决定score显示的位置
    public Title(int screenWidth, int screenHeight, Context context) {
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

    public void addScore(int deltaY) {
        score += deltaY / 2;
    }

    public int getScore(){
        return score;
    }

    @Override
    public void drawBitmap(Canvas canvas, Paint paint) {
        super.drawBitmap(canvas, paint);
        try {
            canvas.drawText(Integer.toString(score), scoreX, 70, paint);
            //canvas.drawBitmap(bitmap, x, y, paint);
        } catch (Exception e) {
            Log.e(TAG, "sprite.drawText() failed.");
        }
    }

}
