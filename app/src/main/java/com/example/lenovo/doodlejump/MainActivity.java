package com.example.lenovo.doodlejump;

import android.app.Activity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private Board BView;
    private int interval = 16;      //timer调度timertask的时间间隔
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BView = findViewById(R.id.BView);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        BView.invalidate();
                    }
                });
            }
        };
        timer.schedule(task, 0, interval);
    }

}
