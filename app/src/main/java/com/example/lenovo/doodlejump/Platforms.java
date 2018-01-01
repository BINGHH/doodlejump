package com.example.lenovo.doodlejump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/11/29.
 */

public class Platforms {
    private int size;   //platform数组长度
    private int num;    //platform中有效元素个数
    private int score;
    private Platform[] platform;
    private Rocket rocket;
    private Monster monster;
    private int screenWidth, screenHeight;
    private int baseMaxInterval;    //相邻两个非broken platform之间的最大间隔 单位: px
    private int baseMaxBrotInterval; //broken platform与上一个非broken platform之前的最大间隔 单位: px
    private int head, rear;         //指向队头与队尾元素
    private int numWhitePlat;
    private int maxWhitePlat;

    Platforms(int screenWidth, int screenHeight, int size, Context context) {
        this.size = size;
        this.num = size;
        score = 0;
        platform = new Platform[size];
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        baseMaxInterval = 450;      //每两个非broken platform之间的间隔最多450px
        baseMaxBrotInterval = 200;   //一个broken platform与上一个非broken platform之间的间隔最多200px
        head = 0;   rear = 0;
        numWhitePlat = 0;
        maxWhitePlat = 10;
        for(int i = 0; i < num; i++) {
            platform[i] = new normalPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.normal, context), score, context);
            rear = i;
        }
        monster = null;
        rocket = null;
    }

    private int randomX() {
        return (int) (Math.random() * (screenWidth - 185));
    }

    private int randomY(int type, Context context) {
        int highestY;       //指的是最高platform的y坐标值(最高platform的y坐标值反而最小).
        int deltaY;
        int maxInterval, maxBroInterval;
        if(score < 3000) {
            maxInterval = 100;
            maxBroInterval = 0;
        }
        else if(score < 6000) {
            maxInterval = 200;
            maxBroInterval = 100;
        }
        else if(score < 9000) {
            maxInterval = 300;
            maxBroInterval = 100;
        }
        else if(score < 12000) {
            maxInterval = 350;
            maxBroInterval = 200;
        }
        else {
            maxInterval = baseMaxInterval;
            maxBroInterval = baseMaxBrotInterval;
        }
        //如果所有platform都还没有初始化, 则最高platform的y坐标从screenHeight - 55算起
        if(platform[head] == null) {
            highestY = screenHeight - 55;
            deltaY = (int) (Math.random() * maxInterval + 100);
        }
        else {
            highestY = platform[rear].y;
            if(type == PlatType.broken)
                deltaY = (int) ((Math.random() * maxBroInterval + 100));
            else if(platform[rear].type == PlatType.broken)
                deltaY = (int) ((Math.random() * (maxInterval - maxBroInterval - 100) + 100));
            else deltaY = (int) (Math.random() * maxInterval + 100);
        }
        if(deltaY >= 170 + 100) {
            //Log.e(TAG, "A Monster is about to be created.");
            int rv = (int) (Math.random() * 1000);
            if(rv < 500) {
                if(monster == null) {
                    monster = new Monster(screenWidth, screenHeight, highestY, context);
                    //Log.e(TAG, "A Monster has already been created.");
                }
            }
            else if(platform[rear].type == PlatType.normal && rocket == null) {
                rocket = new Rocket(screenWidth, screenHeight, platform[rear], rear, context);
                //Log.e(TAG, "A Rocket has already been created. Plattype = " + platform[rear].type);
            }
        }
        highestY -= deltaY;
        return highestY;
    }

    public int getSize() { return size; }

    public int getNum() { return num; }

    public Bitmap getBitmap(int i) {
        //返回第i个platform的bitmap 注意这里的第i个指的是从head开始起的第i个
        i = head + i;
        i = i % size;
        return platform[i].getBitmap();
    }

    public int getX(int i) {
        //返回第i个platform的X坐标 注意这里的第i个指的是从head开始起的第i个
        i = head + i;
        i = i % size;
        return platform[i].x;
    }

    public int getY(int i) {
        //返回第i个platform的Y坐标 注意这里的第i个指的是从head开始起的第i个
        i = head + i;
        i = i % size;
        return platform[i].y;
    }

    void drawBitmap(Canvas canvas, Paint paint) {
        for(int i = 0, j = head; i < num; i++, j = (j+1) % size)
            if(platform[j].valid) platform[j].drawBitmap(canvas, paint, 0);
        if(monster != null) monster.drawBitmap(canvas, paint, 0);
        if(rocket != null) {
            rocket.drawBitmap(canvas, paint);
            //Log.e(TAG, "Rocket x = " + rocket.x + "Rocket y = " + rocket.y);
        }
    }

    void refresh(Context context, Title title, Doodle doodle) {
        boolean flag = false;
        int deltaY = 0;
        int originHead = head;
        for(int i = 0, j = head; i < num; i++, j = (j+1) % size) {
            if(!flag) {
                deltaY = (int) (platform[j].additionVy * platform[j].interval);
                flag = true;
            }
            if (!platform[j].refresh()) {
                // 当有一个platform掉出屏幕时, 就删去它并在在队尾生成一个新的platform
                if(j == originHead) {
                    if(rocket != null && j == rocket.getNumPlat())
                        //rocket依附的plat已经掉出屏幕外
                        rocket.platInvalidate();
                    deleteHead();
                    newRear(context, title);
                }
            }
        }
        if(monster != null)
            if(!monster.refresh()) monster = null;
        if(rocket != null)
            if(!rocket.refresh(platform, doodle)) rocket = null;
        title.addScore(deltaY);
    }

    private void deleteHead() {
        if(num <= 0) Log.e(TAG, "wrong: try to delete element from a empty list.");
        platform[head] = null;
        head = (head + 1) % size;
        //lowest = (lowest + 1) % size;
        num--;
    }

    private void newRear(Context context, Title title) {
        if(num == size) {
            Log.e(TAG, "wrong: try to add element to a full list.");
            return;
        }
        score = title.getScore();
        int temp = (rear + 1) % size;
        int rv = (int)(Math.random() * 1000);
        if(platform[rear] == null)
            platform[temp] = new normalPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.normal, context), score, context);
        else {
            switch (platform[rear].type) {
                case PlatType.broken:
                    if(rv > 200) platform[temp] = new normalPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.normal, context), score, context);
                    else if(rv > 10) platform[temp] = new springPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.withspring, context), score, context);
                    else  {
                        platform[temp] = new whitePlat(screenWidth, screenHeight, randomX(), randomY(PlatType.onetouch, context), context);
                        numWhitePlat++;
                    }
                    break;
                case PlatType.onetouch:
                    if(numWhitePlat < maxWhitePlat) {
                        platform[temp] = new whitePlat(screenWidth, screenHeight, randomX(), randomY(PlatType.onetouch, context), context);
                        numWhitePlat++;
                    }
                    else {
                        platform[temp] = new normalPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.normal, context), score, context);
                        numWhitePlat = 0;
                    }
                    break;
                default:
                    if(rv > 300)
                        platform[temp] = new normalPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.normal, context), score, context);
                    else if(rv > 200)
                        platform[temp] = new brokenPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.broken, context), score, context);
                    else if(rv > 10)
                        platform[temp] = new springPlat(screenWidth, screenHeight, randomX(), randomY(PlatType.withspring,context), score, context);
                    else {
                        platform[temp] = new whitePlat(screenWidth, screenHeight, randomX(), randomY(PlatType.onetouch, context), context);
                        numWhitePlat++;
                    }
            }
        }
        rear = temp;
        num++;
    }

    public void inform(boolean still, double doodleVy) {
        //告诉platforms, doodle是否处于静止状态
        if(still) {
            //如果doodle处于静止状态, 则为所有的platform添上一个附加的速度
            for (int i = 0, j = head; i < num; i++, j = (j + 1) % size)
                platform[j].additionVy = -doodleVy;
            if(monster != null)
                monster.additionVy = -doodleVy;
            if(rocket != null)
                rocket.additionVy = -doodleVy;
        }
        else {
            for (int i = 0, j = head; i < num; i++, j = (j + 1) % size)
                platform[j].additionVy = 0;
            if(monster != null)
                monster.additionVy = 0;
            if(rocket != null)
                rocket.additionVy = 0;
        }
    }

    public void impactCheck(Doodle doodle, Context context) {
        if(doodle.vy > 0)
            //表示doodle正在下降
            for(int i = 0, j = head; i < num; i++, j = (j+1) % size)
                platform[j].impactCheck(doodle, context);
        if(monster != null) monster.impactCheck(doodle, context);
        if(rocket != null) rocket.impactCheck(doodle, context);
    }
}
