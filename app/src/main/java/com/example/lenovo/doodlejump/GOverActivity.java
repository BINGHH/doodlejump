package com.example.lenovo.doodlejump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by lenovo on 2017/12/21.
 */

public class GOverActivity extends Activity {
    Button GOverBtnRtn;
    TextView mtxt1,mtxt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);
        Intent it = getIntent();
        Bundle bun2 = it.getExtras();
        mtxt1 = findViewById(R.id.textView2);
        mtxt2 = findViewById(R.id.textView);
        int score = bun2.getInt("SCORE");
        mtxt1.setText(Integer.toString(score));
        byte bLoop;
        int hscore = 0;
        try {
            FileInputStream fileIn = openFileInput("HighScore");
            BufferedInputStream bufFileIn = new BufferedInputStream(fileIn);
            byte[] bufBytes = new byte[10];
            do{
                int c = bufFileIn.read(bufBytes);
                if(c == -1) break;
            }while (true);
            for (int i = 0; i < bufBytes.length; i++) {
                bLoop = bufBytes[i];
                hscore += (bLoop & 0xFF) << (8 * i);
            }
            bufFileIn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(score >= hscore){
            hscore = score;
        }
        mtxt2.setText(Integer.toString(13547));
        FileOutputStream fileout = null;
        BufferedOutputStream bufFileOut = null;
        try {
            fileout = openFileOutput("HighScore",MODE_PRIVATE);
            bufFileOut = new BufferedOutputStream(fileout);
            bufFileOut.write(Integer.toString(hscore).getBytes());
            bufFileOut.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        GOverBtnRtn = findViewById(R.id.GOverBtnRtn);
        GOverBtnRtn.setOnClickListener(GOverBtnRtnClick);
    }

    private Button.OnClickListener GOverBtnRtnClick = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {

            Intent it2 = new Intent();
            it2.setClass(GOverActivity.this, BeginActivity.class);
            startActivity(it2);
            finish();
        }
    };

}
