package com.klaus.activity.writememory;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.content.SharedPreferences;
import com.klaus.util.writememory.DBHelper;
import com.wilddog.client.Wilddog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.klaus.service.writememory.UploadDataService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
        int RegisterFirst=setting.getInt("RegisterFirst", -1);

        //首次使用
        if(RegisterFirst==-1){

            doRegisterFirst();

        }else{
            long uploadTimeLastTime=setting.getLong("UploadTime", 0);

            long nowTime= System.currentTimeMillis();

            long chaTime=1000*60*60*24*3;

            if(nowTime-uploadTimeLastTime>=chaTime){

                Intent intent=new Intent(MainActivity.this,UploadDataService.class);
                startService(intent);

            }
        }

        stopThisActivity();

    }


    private void stopThisActivity(){

        new Thread(){
            public void run(){

                for(int i=0;i<10000000;i++){

                }

                Message msg_listData = new Message();
                msg_listData.what=0;
                handler.sendMessage(msg_listData);

            }
        }.start();

    }

    //首次使用时，必须的系统准备工作
    private void doRegisterFirst(){

        //将使用者标识为非首次使用者,并且，将记录状态设置为开始记录的状态
        SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt("RegisterFirst", 1);
        editor.putInt("WriteDownState", 0);
        editor.putLong("UploadTime", 0);
        editor.commit();

        //创建数据库
        DBHelper dbhelper = new DBHelper(this);
        dbhelper.CreatOrOpen("writememory");
        dbhelper.excuteInfo("create table mything(id INTEGER PRIMARY KEY,title text,starttime text,endtime text,pride int,important int);");
        dbhelper.closeDB();

    }

    public void pagejump(){
        //跳转至功能界面
        Intent intent=new Intent(MainActivity.this,MyActivity.class);
        startActivity(intent);
        finish();
        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    private Handler handler= new Handler(new Handler.Callback() {

        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    pagejump();
                    break;
            }
            return false;
        }
    });

    private void uploadData(){

        List<Map> list = new ArrayList<Map>();

        DBHelper dbhelper = new DBHelper(this);
        dbhelper.CreatOrOpen("writememory");
        Cursor cur = dbhelper.selectInfo("select * from mything;");
        while (cur.moveToNext()) {

          //  System.out.println("aa"+cur.getInt(0));
          //  System.out.println("bb"+cur.getString(1));
          //  System.out.println("cc"+cur.getString(2));
          //  System.out.println("dd"+cur.getString(3));
         //   System.out.println("ee" + cur.getInt(4));
          //  System.out.println("ff" + cur.getInt(5));

            Map<String,String> map=new HashMap<String,String>();
            map.put("id", cur.getInt(0)+"");
            map.put("title", cur.getString(1));
            map.put("starttime", cur.getString(2));
            map.put("endtime",cur.getString(3));
            map.put("pride", cur.getInt(4) + "");
            map.put("important", cur.getInt(5)+"");

            list.add(map);

        }
        dbhelper.closeDB();


        Map<String,List> mapSum=new HashMap<String,List>();
        mapSum.put("memorysum",list);


        Wilddog.setAndroidContext(this);

        Wilddog ref = new Wilddog("https://wild-snake-96493.wilddogio.com/android/writememory");

        ref.setValue(mapSum);

        long nowTime= System.currentTimeMillis();

        SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putLong("UploadTime", nowTime);
        editor.commit();

    }




}
