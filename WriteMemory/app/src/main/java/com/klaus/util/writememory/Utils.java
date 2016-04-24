package com.klaus.util.writememory;

import java.util.Date;
/**
 * Created by Administrator on 2016/4/23 0023.
 */
public class Utils {

    public static Date longToTime(long lon){

        return new Date(lon);

    }

    public static long timeToLong(Date date){

        return date.getTime();
    }

    public static long getLongTime(){

        return System.currentTimeMillis();

    }

    //public static Date getTime(){
    //	return null;
    //}
}

