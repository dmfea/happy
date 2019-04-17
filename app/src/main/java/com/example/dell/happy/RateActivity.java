package com.example.dell.happy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {
    EditText rmb;
    TextView show;
    private float dollarRate=0.1f;
    private float euroRate=0.2f;
    private float wonRate=0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.show);
    }

    public void onClick(View btn){
        //获取用户输入
        String str = rmb.getText().toString();
        float r = 0;
        float val = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else {
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
        }
        if(btn.getId()==R.id.btn_dollar){
            val = r*dollarRate;
        }else if(btn.getId()==R.id.btn_euro){
            val = r*euroRate;
        }else{
            val = r*wonRate;
        }
        show.setText(String.valueOf(String.format("%.2f",val)));
    }

    public void openOne(View btn){
        //打开其他页面的代码
        Log.i("open","openOne");
        Intent hello = new Intent(this,MainActivity.class);
        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("Tel:17396249909"));
        startActivity(hello);
        finish();
    }
    public void openOne2(View btn){
        openConfig();

    }

    private void openConfig() {
        Intent config = new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i("rate","givedollar"+dollarRate);
        Log.i("rate","giveeuro"+euroRate);
        Log.i("rate","givewon"+wonRate);
        //startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);

            Log.i("save","onActivityResult:dollarRate="+dollarRate);
            Log.i("save","onActivityResult:euroRate="+euroRate);
            Log.i("save","onActivityResult:wonRate="+wonRate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
