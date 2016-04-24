package com.klaus.util.writememory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
    public Context context;
    public SQLiteDatabase my_DataBase;
    public DBHelper(Context con){
        this.context=con;
    }
    public SQLiteDatabase CreatOrOpen(String dbName){
        my_DataBase=this.context.openOrCreateDatabase(dbName+".db",0, null);
        return my_DataBase;
    }
    public boolean closeDB(){
        try{
            my_DataBase.close();
        }catch(Exception e){
            return false;
        }
        return true;
    }
    public boolean findTable(String tableName){
        try{
            String sqlstr="SELECT count(*) FROM sqlite_master WHERE type='table' AND name='"+tableName+"';";
            Cursor cursor = my_DataBase.rawQuery(sqlstr, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    return true;
                }
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }
    public boolean excuteInfo(String sqlstr){
        try{
            my_DataBase.execSQL(sqlstr);
        }catch(Exception e){
            return false;
        }
        return true;
    }
    public Cursor selectInfo(String sqlstr){
        return my_DataBase.rawQuery(sqlstr, null);
    }


}


