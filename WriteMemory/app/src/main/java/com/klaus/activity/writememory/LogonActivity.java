package com.klaus.activity.writememory;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.klaus.bean.writememory.User;
import com.klaus.util.writememory.DBHelper;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Handler;
import android.os.Message;

public class LogonActivity extends AppCompatActivity {

    private List<User> userList=new ArrayList<User>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        initUsers();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            return false;

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


    public void btonclik(View view){

        int vId=view.getId();

        if(vId==R.id.logonback){

            finish();

            return ;

        }

        if(vId==R.id.logonsubmit){

            EditText user=(EditText)findViewById(R.id.logonusername);
            EditText pwd=(EditText)findViewById(R.id.logonpwd);

            String userString=user.getText().toString().trim();
            String pwdString=pwd.getText().toString().trim();

            if(userString==null||userString.length()==0||pwdString==null||pwdString.length()==0){

                Toast.makeText(getApplicationContext(), "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                return ;

            }

            int tag=verifyIndentity(userString,pwdString);

            if(tag==1){

                SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
                SharedPreferences.Editor editor = setting.edit();
                editor.putString("UserName", userString);
                editor.commit();

                updateLocalDB(userString);

               // finish();

                return ;

            }


            if(tag==0){

                Toast.makeText(getApplicationContext(), "您输入的用户名密码有误", Toast.LENGTH_SHORT).show();

                return ;

            }

            if(tag==-1){

                SharedPreferences setting = getSharedPreferences("writememory", MODE_PRIVATE);
                SharedPreferences.Editor editor = setting.edit();
                editor.putString("UserName", userString);
                editor.commit();

                Wilddog.setAndroidContext(this);

                Wilddog ref = new Wilddog("https://wild-snake-96493.wilddogio.com/android/writememoryuser");

                Map<String,Object> map=new HashMap<String,Object>();
                Map<String,String> map1=new HashMap<String, String>();
                map1.put("username",userString);
                map1.put("password",pwdString);
                map.put(userList.size() + "", map1);

                ref.updateChildren(map);

                Toast.makeText(getApplicationContext(), "恭喜您注册成功，数据同步即刻开启", Toast.LENGTH_SHORT).show();

                finish();

                return ;


            }

        }

    }

    private int verifyIndentity(String username,String password){

        for(int i=0;i<userList.size();i++){

            User user=userList.get(i);
            if (user.userName.equals(username)){

                if(user.passWord.equals(password)){
                    return 1;
                }
                else{
                    return 0;
                }

            }

        }

        return -1;
    }


    private String initUsers(){

        Wilddog.setAndroidContext(this);

        Wilddog ref = new Wilddog("https://wild-snake-96493.wilddogio.com/android/writememoryuser");

        ref.addValueEventListener(new ValueEventListener(){
            public void onDataChange(DataSnapshot snapshot) {

               long intemT= snapshot.getChildrenCount();

                for(int i=0;i<intemT;i++){

                    User user=new User();

                    user.userName=snapshot.child(i+"").child("username").getValue().toString();
                    user.passWord=snapshot.child(i+"").child("password").getValue().toString();

                    userList.add(user);

                }

            }

            public void onCancelled(WilddogError error) {
                if(error != null){
                 //   System.out.println(error.getCode());
                }
            }
        });

        return "";
    }


    private void updateLocalDB(String userName){

        progressDialog = ProgressDialog.show(LogonActivity.this, "", "正在同步数据，请稍后～！");

        Wilddog.setAndroidContext(this);

        Wilddog ref = new Wilddog("https://wild-snake-96493.wilddogio.com/android/writememory/"+userName);

        ref.addValueEventListener(new ValueEventListener(){
            public void onDataChange(DataSnapshot snapshot) {

                long intemT= snapshot.getChildrenCount();

                DBHelper dbhelper = new DBHelper(LogonActivity.this);
                dbhelper.CreatOrOpen("writememory");

                dbhelper.excuteInfo("delete from mything;");

                for(int i=0;i<intemT;i++){

                    int idd= Integer.parseInt(snapshot.child(i+"").child("id").getValue().toString());
                    String title=snapshot.child(i+"").child("title").getValue().toString();
                    String starttime=snapshot.child(i+"").child("starttime").getValue().toString();
                    String endtime=snapshot.child(i+"").child("endtime").getValue().toString();
                    int pride= Integer.parseInt(snapshot.child(i + "").child("pride").getValue().toString());
                    int important= Integer.parseInt(snapshot.child(i+"").child("important").getValue().toString());

                    dbhelper.excuteInfo("insert into mything values('"+idd+"','"+title+"','" +starttime+ "','"+endtime+"','"+pride+"','"+important+"')");

                }

                dbhelper.closeDB();

                Message msg_listData = new Message();
                msg_listData.what =0;
                handler.sendMessage(msg_listData);

            }

            public void onCancelled(WilddogError error) {
                if(error != null){
                    //   System.out.println(error.getCode());
                }
            }
        });

    }


    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    progressDialog.dismiss(); //关闭进度条
                    Toast.makeText(getApplicationContext(), "数据同步完毕～", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

}
