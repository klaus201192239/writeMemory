package com.klaus.activity.writememory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.klaus.util.writememory.EmojiFilter;
import com.klaus.util.writememory.Utils;
import com.klaus.util.writememory.DBHelper;


import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class StartActivity extends AppCompatActivity {

    private EditText input;
    private String tmp = "";
    // 是否重置了EditText的内容
    private boolean resetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initView();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Toast.makeText(getApplicationContext(), "请按下确定或取消", Toast.LENGTH_SHORT).show();
        return false;

    }

    private void initView(){


        //禁止输入框输入Emoji表情符号
        input=(EditText)findViewById(R.id.sthing_content);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int arg2, int arg3) {
                if (!resetText) {
                    if ((arg3 == 2) && (!EmojiFilter.containsEmoji(s.toString().substring(start, start + 2)))) {
                        resetText = true;
                        input.setText(tmp);
                        input.invalidate();
                        if (input.getText().length() > 1){
                            Selection.setSelection(input.getText(), input.getText().length());
                        }
                        Toast.makeText(getApplicationContext(), "不支持表情输入",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (!resetText)
                    tmp = arg0.toString();

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    public void btonclick(View view){

        //按下确定按钮
        if(view.getId()==R.id.sthing_ok){

            //输入事件不允许为空
            String str=input.getText().toString();
            if(str.length()==0){
                Toast.makeText(this, "请输入内容～", Toast.LENGTH_SHORT).show();
                return ;
            }

            String sr=Utils.getLongTime()+"";
            //插入数据库，由于事件进行中，将结束时间置为0000
            DBHelper dbhelper = new DBHelper(this);
            dbhelper.CreatOrOpen("writememory");
            //(id INTEGER PRIMARY KEY,title text,starttime text,endtime text,pride int,important int);");
            dbhelper.excuteInfo("insert into mything values(null,'"+str+"','" +sr+ "','0000',0,0)");
            dbhelper.closeDB();

            //将记录状态设置为结束记录的状态
            SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.putInt("WriteDownState", 1);
            editor.commit();

            jump();

        }


        //取消开始事件
        if(view.getId()==R.id.sthing_no){

            jump();

        }

    }

    //界面跳转
    private void jump(){

        Intent intent=new Intent(StartActivity.this,MyActivity.class);
        startActivity(intent);
        finish();

    }
}
