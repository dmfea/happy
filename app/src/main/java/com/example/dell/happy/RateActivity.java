package com.example.dell.happy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable{
    EditText rmb;
    TextView show;
    Handler handler;
    private final String TAG = "RateActivity";
    private float dollarRate=0.1f;
    private float euroRate=0.2f;
    private float wonRate=0.3f;
    private String updateDate = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.show);

        //获取SP里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        /////sp只可以获取一个页面中的数据，上面的方法可以获得多个
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("update_date","");
        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        final String todayStr = sdf.format(today);

        Log.i(TAG,"onCreate: sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate: sp euroRate"+euroRate);
        Log.i(TAG,"onCreate: sp wonRate"+wonRate);
        Log.i(TAG,"onCreate: sp update_date"+updateDate);
        Log.i(TAG,"onCreate: sp todayStr"+todayStr);

        //判断时间
        if (!todayStr.equals(updateDate)){
            Log.i(TAG,"onCreat:需要更新");
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else {
            Log.i(TAG,"onCreat:不需要更新");
            Thread t = new Thread(this);
            t.start();
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==5){
                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate = bdl.getFloat("dollar-rate");
                    euroRate = bdl.getFloat("euro-rate");
                    wonRate = bdl.getFloat("won-rate");

                    Log.i(TAG,"handlerMessage:dollar="+dollarRate);
                    Log.i(TAG,"handlerMessage:euro="+euroRate);
                    Log.i(TAG,"handlerMessage:won="+wonRate);

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("update_date",todayStr);
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.apply();

                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };//这个封号表示这个方法是对类方法的重写·

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
        Log.i(TAG,"openOne");
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

        Log.i(TAG,"givedollar="+dollarRate);
        Log.i(TAG,"giveeuro="+euroRate);
        Log.i(TAG,"givewon="+wonRate);
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
        }else if (item.getItemId()==R.id.open_list){
            Intent list = new Intent(this,RateListActivity.class);
            startActivity(list);
            //测试数据库
            /*RateItem item1 = new RateItem("aa","11");
            RateManager manager = new RateManager(this);
            manager.add(item1);
            manager.add(new RateItem("bb","22"));
            Log.i(TAG,"onOptionsItemSelected：写入数据完毕");

            //查询所有数据
            List<RateItem> testList = manager.listAll();
            for (RateItem i:testList){
                Log.i(TAG,"onOptionsItemSelected:取出数据[id="+i.getId()+"]Name="+i.getCurName()+"Rate="+i.getCurRate());
            }*/
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

            //将新设置的汇率写到SP里，新设置是通过config实现的，而从网络中获取数据只是改变了当前的数据
            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据已经保存到sharedPreferences");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i(TAG,"RUN........................");
        for (int i=1;i<3;i++){
            Log.i(TAG,"run: i="+i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //用户保存获取的汇率
        Bundle bundle;

        //获取网络数据
        /*try {
            URL url = new URL("www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStreamToString(in);
            Log.i(TAG,"run:html="+html);
            Document doc = Jsoup.parse(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        bundle = getFromBOC();
        //bundle中保存所获取的汇率

        //获取msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        //msg.obj = "hello from run()";
        msg.obj = bundle;
        handler.sendMessage(msg);
        }

    private Bundle getFromBOC() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try{
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            //doc=Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
            /*for (Element table : tables) {
                Log.i(TAG,"run:table["+i+"]"+table);
                i++;
            }*/
            Element table1=tables.get(1);
            //Log.i(TAG,"run:table1="+table1);
            //获取td中的数据
            Elements tds = table1.getElementsByTag("td");
            for (int i=0;i<tds.size();i+=8){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG,"run:text="+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if ("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if ("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if ("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }
            /*for(Element td:tds){
                Log.i(TAG,"run:td="+td);
                Log.i(TAG,"run:text="+td.text());
                Log.i(TAG,"run:html="+td.html());
            }*/

        }catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }

    private Bundle getFromUsdCny() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try{
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
            /*for (Element table : tables) {
                Log.i(TAG,"run:table["+i+"]"+table);
                i++;
            }*/
            Element table1=tables.get(0);
            //Log.i(TAG,"run:table1="+table1);
            //获取td中的数据
            Elements tds = table1.getElementsByTag("td");
            for (int i=0;i<tds.size();i+=6){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG,"run:text="+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if ("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if ("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if ("韩元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }
            /*for(Element td:tds){
                Log.i(TAG,"run:td="+td);
                Log.i(TAG,"run:text="+td.text());
                Log.i(TAG,"run:html="+td.html());
            }*/

        }catch (IOException e){
            e.printStackTrace();
        }
        return bundle;
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
    final int bufferSize = 1024;
    final char[] buffer = new char[bufferSize];
    final StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(inputStream, "gb2312");
    for (; ; ) { int rsz = in.read(buffer, 0, buffer.length);
        if (rsz < 0)
        break;
        out.append(buffer, 0, rsz);
        }
        return out.toString();
        }
}
