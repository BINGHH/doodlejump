package com.example.lenovo.doodlejump;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity implements SensorEventListener {
    private Board BView;
    private int interval = 16;      //timer调度timertask的时间间隔

    private SensorManager sManager;
    private Sensor mSensorOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorOrientation = sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_UI);

        BView = findViewById(R.id.BView);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        BView.invalidate();
                        if (BView.isGameOver()) {
                            cancel();
                            Intent IttGameOver = new Intent();
                            IttGameOver.setClass(MainActivity.this, GOverActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("SCORE", BView.getScore());
                            IttGameOver.putExtras(bundle);
                            startActivity(IttGameOver);
                            MainActivity.this.onDestroy();
                        }
                    }
                });
            }
        };


        timer.schedule(task, 0, interval);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*tv_value1.setText("方位角：" + (float) (Math.round(event.values[0] * 100)) / 100);
        tv_value2.setText("倾斜角：" + (float) (Math.round(event.values[1] * 100)) / 100);
        tv_value3.setText("滚动角：" + (float) (Math.round(event.values[2] * 100)) / 100);*/
        BView.setDoodleVx((float) (Math.round(event.values[2] * 100)) / 100);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }





}
