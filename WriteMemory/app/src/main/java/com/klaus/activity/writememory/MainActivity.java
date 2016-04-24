package com.klaus.activity.writememory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.content.SharedPreferences;

import com.klaus.util.writememory.DBHelper;


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

        }

        stopThisActivity();

    }


    private void stopThisActivity(){

        new Thread(){
            public void run(){

                for(int i=0;i<1000000;i++){

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


}
