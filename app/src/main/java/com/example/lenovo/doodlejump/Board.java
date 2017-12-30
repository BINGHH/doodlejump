package com.example.lenovo.doodlejump;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2017/11/28.
 */

public class Board extends View{
    private boolean gameStart;
    private int count;
    private int screenWidth, screenHeight;
    private Paint paint;
    private Doodle doodle = null;
    private Platforms platforms = null;
    private Title title = null;
    public Board(Context context) { super(context ); }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameStart = false;
        count = 0;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
    }

    private void InitBitmap() {
        screenWidth = getWidth();
        screenHeight = getHeight();
        doodle = new Doodle(screenWidth, screenHeight, getContext());
        platforms = new Platforms(screenWidth, screenHeight, 10, getContext());
        title = new Title(screenWidth, screenHeight, getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!gameStart) {
            InitBitmap();
            gameStart = true;
        }
        count ++;
        platforms.drawBitmap(canvas, paint);        //画出所有的platform
        platforms.refresh(getContext(), title);     //更新所有platform的坐标并更新分数
        doodle.drawBitmap(canvas, paint);           //画出doodle
        doodle.refresh();                           //更新doodle坐标
        title.drawBitmap(canvas, paint, 0);            //画出标题栏以及标题栏左侧分数
        platforms.inform(doodle.isStill(), doodle.vy);  //告知platforms是否需要向下移动
        platforms.impactCheck(doodle, getContext());              //碰撞检测
        //canvas.drawText(Integer.toString(count), 500, 600, paint);
    }

    public void setDoodleVx(double roll){
        if(doodle != null) doodle.setVx(roll);
    }

    public boolean isGameOver() {
        if(doodle != null)
            return doodle.isGameOver();
        else return false;
    }

    public int getScore() {
        if(title != null)
            return title.getScore();
        else return 0;
    }

}
