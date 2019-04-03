package com.example.dell.happy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    TextView out;
    EditText inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        inp=findViewById(R.id.inp);
        out=findViewById(R.id.txtout);
        Button btn=findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i("second","onClick msg....");
        String str=inp.getText().toString();
        try{
            double strr=Double.valueOf(str);
            double result=strr*1.8+32;
            out.setText("转换结果为："+ String.format( "%.2f",result));
        }catch(Exception e){
            out.setText("请输入摄氏温度");
        }

    }
}

