package com.klaus.service.writememory;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.klaus.util.writememory.DBHelper;
import com.wilddog.client.Wilddog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadDataService extends Service {
    public UploadDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            boolean tagNet=jugeRegNet();
            if(tagNet==true){
                uploadData();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void uploadData(){

        List<Map> list = new ArrayList<Map>();

        DBHelper dbhelper = new DBHelper(this);
        dbhelper.CreatOrOpen("writememory");
        Cursor cur = dbhelper.selectInfo("select * from mything;");
        while (cur.moveToNext()) {

          //  Log.v("aa",String.valueOf(cur.getInt(0)));
           // Log.v("bb",cur.getString(1));
           // Log.v("cc",cur.getString(2));
           // Log.v("dd",cur.getString(3));
           // Log.v("ee" , String.valueOf(cur.getInt(4)));
           // Log.v("ff" ,String.valueOf(cur.getInt(5)));



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


    private Boolean jugeRegNet() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            return true;
        }
            return false;
    }
}
