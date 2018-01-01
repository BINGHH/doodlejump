package com.example.lenovo.doodlejump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2017/12/21.
 */

public class GOverActivity extends Activity {
    Button GOverBtnRtn;
    TextView TxtHScore, TxtScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        Intent it = getIntent();
        Bundle bun2 = it.getExtras();

        TxtScore = findViewById(R.id.TxtScore);
        TxtHScore = findViewById(R.id.TxtHScore);

        int score, hScore;
        String strScore, strHScore = "";
        String fileName = "DATA";
        String str = "*";


        score = bun2.getInt("SCORE");
        strScore = Integer.toString(score);
        TxtScore.setText(strScore);

        StringBuffer strBuf = readFile(fileName);
        if(strBuf != null)
            strHScore = getNum(strBuf);
        if(strHScore != null && strHScore.length() != 0) {
            // 则不是空文件
            TxtHScore.setText(strHScore);
            hScore = Integer.parseInt(strHScore);
            if(score > hScore) {
                try {
                    // 将新纪录写入文件
                    FileOutputStream fOut = openFileOutput(fileName, MODE_PRIVATE);
                    BufferedOutputStream bufOut = new BufferedOutputStream(fOut);
                    strScore = str + strScore + str;
                    bufOut.write(strScore.getBytes("UTF-8"));
                    bufOut.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            TxtHScore.setText(strScore);
            try {
                // 将新纪录写入文件
                FileOutputStream fOut = openFileOutput(fileName, MODE_PRIVATE);
                BufferedOutputStream bufOut = new BufferedOutputStream(fOut);
                strScore = str + strScore + str;
                bufOut.write(strScore.getBytes("UTF-8"));
                bufOut.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
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

    private StringBuffer readFile(String fileName) {
        // Given a filename, read the content in the file, then convert it into string and return it.
        // Any fail will result in a null return value.
        StringBuffer strBuf = new StringBuffer("");
        byte[] bufBytes = new byte[10];
        try {
            FileInputStream fIn = openFileInput(fileName);
            BufferedInputStream bufIn = new BufferedInputStream(fIn);

            while(true) {
                if(bufIn.read(bufBytes) == -1) break;
                else strBuf.append(new String(bufBytes));
            }
            fIn.close();
            bufIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strBuf;
    }

    private String getNum(StringBuffer strBuf) {
        // The file is saved like this: *123456*
        int startPos = 1;
        int endPos = strBuf.indexOf("*", startPos);
        if(endPos < 0)
            return null;
        char num[] = new char[endPos - startPos];
        strBuf.getChars(startPos, endPos, num, 0);
        return new String(num);
    }

}
