package com.example.lenovo.doodlejump;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/11/29.
 */

public class Platforms {
    private int size;   //platform数组长度
    private int num;    //platform中有效元素个数
    private Platform[] platform;
    private int screenWidth, screenHeight;
    private int maxPlatInterval;
    private int head, rear;
    private int lowestY;

    public Platforms(int screenWidth, int screenHeight, int size, Context context) {
        this.size = size;
        this.num = size;
        platform = new Platform[size];
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        maxPlatInterval = 500;     //每两个platform之间的间隔最多500px
        head = 0;   rear = num - 1;
        lowestY = 55;
        for(int i = 0; i < num; i++)
            platform[i] = new normalPlat(screenWidth, screenHeight, randomX(), randomY(), context);
    }

    private int randomX() {
        //return 45;
        return (int) (Math.random() * (screenWidth - 185));
    }

    private int randomY() {
        //lowestY += 50;
        //return lowestY;
        lowestY += (int) (Math.random() * maxPlatInterval);
        return screenHeight - lowestY;
    }

    public int getSize() {
        return size;
    }

    public int getNum() {
        return num;
    }

    public int getHead() {
        return head;
    }

    public Bitmap getBitmap(int i) {
        i = head + i;
        i = i % size;
        return platform[i].getBitmap();
    }

    public int getX(int i) {
        i = head + i;
        i = i % size;
        return platform[i].x;
    }

    public int getY(int i) {
        i = head + i;
        i = i % size;
        return platform[i].y;
    }

    public void refresh() {
        for(int i = 0, j = head; i < num; i++, j++) {
            if(j > size - 1) j = 0;
            platform[i].refresh();
        }
    }

}
