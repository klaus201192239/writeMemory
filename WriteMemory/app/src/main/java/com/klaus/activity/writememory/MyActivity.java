package com.klaus.activity.writememory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends AppCompatActivity {

    private static Boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initView();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Toast.makeText(getApplicationContext(), "1111", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Toast.makeText(getApplicationContext(), "2222222", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;

    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "双击退出程序~", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }


    public void btonclik(View view){

        //跳转到所有事件列表
        if(view.getId()==R.id.showMemory){

            Intent intent=new Intent(MyActivity.this,ListActivity.class);
            startActivity(intent);
            //finish();

            return ;
        }


        if(view.getId()==R.id.showlogon){


            SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
            String user=setting.getString("UserName", null);

            if(user==null) {

                Intent intent = new Intent(MyActivity.this, LogonActivity.class);
                startActivity(intent);

            }else{

                Toast.makeText(getApplicationContext(), "您已经绑定了账号～", Toast.LENGTH_SHORT).show();
            }

            return ;
        }

        if(view.getId()==R.id.mybutton){

            SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
            int WriteDownState=setting.getInt("WriteDownState", -1);

            //按下开始按钮
            if(WriteDownState==0){

                Intent intent=new Intent(MyActivity.this,StartActivity.class);
                startActivity(intent);
                finish();

            }
            //按下结束按钮
            else{

                Intent intent=new Intent(MyActivity.this,EndActivity.class);
                startActivity(intent);
                finish();

            }

            return ;
        }

    }

    private void initView(){

        //获取事件处于何种状态，开始还是结束
        SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
        int WriteDownState=setting.getInt("WriteDownState", -1);

        //结束按钮
        if(WriteDownState==1){

            //换背景图片
     //       RelativeLayout RL=(RelativeLayout)findViewById(R.id.RLlayout2);
      //      RL.setBackgroundResource(R.mipmap.guide2);


            //换文字信息换按钮图片
          //  TextView textview=(TextView)findViewById(R.id.mycontent);
           // textview.setText("每一次回忆");

            //换按钮图片
            ImageView button=(ImageView)findViewById(R.id.mainimg);
            button.setImageResource(R.mipmap.mainimg);

           // button.setBackgroundResource(R.mipmap.commentni9);

        }

    }



}
