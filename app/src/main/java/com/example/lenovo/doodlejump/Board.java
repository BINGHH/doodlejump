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
    private Doodle doodle = null;
    private Canvas DDCanvas;
    private Paint paint;

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
            src =  BitmapFactory.decodeResource(getResources(), R.drawable.doodle, options);
            doodle = new Doodle(getWidth(), getHeight(), src);
            //DDBitmap = Bitmap.createScaledBitmap(src, 138, 135, false);
            //TestBitmap = Bitmap.createScaledBitmap(src, 138, 135, false);
            //Log.e(TAG, "DD width = " + DDBitmap.getWidth() + " DD height = " + DDBitmap.getHeight());
            DDCanvas = new Canvas(doodle.getBitmap());
        } catch (Exception e) {
            Log.e(TAG, "something went wrong with InitBitmap.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (doodle == null) InitBitmap();
        try {
            canvas.drawBitmap(doodle.getBitmap(), doodle.getX(), doodle.getY(), paint);
            doodle.refresh();
            //canvas.drawBitmap(TestBitmap, 258, 345, paint);
        } catch (Exception e) {
            Log.e(TAG, "canvas.drawBitmap failed.");
        }
    }

}
