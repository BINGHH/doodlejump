package com.example.lenovo.doodlejump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;

/**
 * Created by lenovo on 2017/12/21.
 */

public class BeginActivity extends Activity {
    Button BegBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin);
        BegBtnStart = findViewById(R.id.BegBtnStart);
        BegBtnStart.setOnClickListener(BegBtnStartClick);

        try {
            FileOutputStream fOut = openFileOutput("DATA", MODE_APPEND);
            fOut.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private Button.OnClickListener BegBtnStartClick = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent it = new Intent();
            try {
                it.setClass(BeginActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
