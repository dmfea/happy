package com.example.dell.happy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Third2Activity extends AppCompatActivity {
    TextView score;
    TextView score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third2);
        score = findViewById(R.id.score);
        score2 = findViewById(R.id.score2);
    }
    public void btnAdd1(View btn){
        if(btn.getId()==R.id.btn1){
            showscore(1);
        }else {
            showscore2(1);
        }
    }
    public void btnAdd2(View btn){
        if(btn.getId()==R.id.btn2){
            showscore(2);
        }else {
            showscore2(2);
        }
    }
    public void btnAdd3(View btn){
        if(btn.getId()==R.id.btn3){
            showscore(3);
        }else {
            showscore2(3);
        }
    }
    public void btnreset(View btn){
        score.setText("0");
        score2.setText("0");
    }
    private void showscore(int inc){
        Log.i("show", "inc= "+inc);
        String oldscore = (String) score.getText();
        int newscore = Integer.parseInt(oldscore)+inc;
        score.setText(""+newscore);
    }
    private void showscore2(int inc){
        Log.i("show", "inc= "+inc);
        String oldscore = (String) score2.getText();
        int newscore = Integer.parseInt(oldscore)+inc;
        score2.setText(""+newscore);
    }
}
