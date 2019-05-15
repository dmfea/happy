package com.example.dell.happy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class RateCalcActivity extends AppCompatActivity {
    String TAG = "rateCalc";
    float rate = 0f;
    EditText inp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        String title = getIntent().getStringExtra("title");
        rate = getIntent().getFloatExtra("rate",0f);
        Log.i(TAG,"onCreat:title="+title);
        Log.i(TAG,"onCreat:rate="+rate);
        TextView tt = findViewById(R.id.title2);
        tt.setText(title);
        inp2 =  findViewById(R.id.inp2);
        inp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView show = RateCalcActivity.this.findViewById(R.id.show2);
                if (s.length()>0){
                    float val = Float.parseFloat(s.toString());
                    show.setText(val+"RMB==>"+(100/rate*val));
                }else {
                    show.setText("");
                }

            }
        });


    }
}
