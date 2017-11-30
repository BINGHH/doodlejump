package com.example.lenovo.doodlejump;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Board extends View{
    private boolean gameStart;
    private int screenWidth, screenHeight;
    private Paint paint;
    private Doodle doodle = null;
    private Platforms platforms = null;

    public Board(Context context) { super(context ); }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameStart = false;
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
        if (!gameStart) {
            InitBitmap();
            gameStart = true;
        }
        platforms.drawBitmap(canvas, paint);
        platforms.refresh(getContext());
        doodle.drawBitmap(canvas, paint);
        doodle.refresh();
        platforms.inform(doodle.isStill(), doodle.vy);
        platforms.impactCheck(doodle);
    }
}
