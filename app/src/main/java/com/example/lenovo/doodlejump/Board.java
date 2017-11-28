package com.example.lenovo.doodlejump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Board extends View{
    private int screenWidth, screenHeight;
    private Paint paint;
    private Doodle doodle = null;
    //private Platform platform [] = new Platform[25];

    public Board(Context context) {
        super(context);
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    private void InitBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = 2;
        Bitmap src;
        try {
            screenWidth = getWidth();
            screenHeight = getHeight();
            src =  BitmapFactory.decodeResource(getResources(), R.drawable.doodle, options);
            doodle = new Doodle(screenWidth, screenHeight, src);
        } catch (Exception e) {
            Log.e(TAG, "something went wrong when doodle is initialed.");
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (doodle == null) InitBitmap();
        try {
            canvas.drawBitmap(doodle.getBitmap(), doodle.x, doodle.y, paint);
            doodle.refresh();
        } catch (Exception e) {
            Log.e(TAG, "canvas.drawBitmap failed.");
        }
    }

}
