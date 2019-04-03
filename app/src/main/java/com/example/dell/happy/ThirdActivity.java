package com.example.dell.happy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener{
    TextView out;
    String str;
    int score;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        out = findViewById(R.id.score);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn = findViewById(R.id.btn);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                num=1;
                break;
            case R.id.btn2:
                num=2;
                break;
            case R.id.btn3:
                num=3;
                break;
            case R.id.btn:
                score=0;
                break;
        }
        score = score + num;
        out.setText(String.valueOf(score));
        num = 0;
    }
}

