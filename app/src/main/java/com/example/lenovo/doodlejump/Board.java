package com.example.lenovo.doodlejump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private Platforms platforms = null;

    public Board(Context context) {
        super(context);
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    private void InitBitmap() {
        screenWidth = getWidth();
        screenHeight = getHeight();
        doodle = new Doodle(screenWidth, screenHeight, getContext());
        platforms = new Platforms(screenWidth, screenHeight, 10, getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (doodle == null) InitBitmap();
        try {
            canvas.drawBitmap(doodle.getBitmap(), doodle.x, doodle.y, paint);
            doodle.refresh();
            for(int i = 0, n = platforms.getNum(); i < n; i++) {
                canvas.drawBitmap(platforms.getBitmap(i), platforms.getX(i), platforms.getY(i), paint);
                //canvas.drawBitmap(platforms.getBitmap(i), 25, 56, paint);
                platforms.refresh();
            }
        } catch (Exception e) {
            Log.e(TAG, "canvas.drawBitmap failed.");
        }
    }

}
