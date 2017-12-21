package com.example.lenovo.doodlejump;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by lenovo on 2017/12/21.
 */

public class GOverActivity extends Activity {
    Button GOverBtnRtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

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
