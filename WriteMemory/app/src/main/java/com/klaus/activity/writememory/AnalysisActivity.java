package com.klaus.activity.writememory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.klaus.bean.writememory.ThingSum;
import com.klaus.util.writememory.Utils;
import com.klaus.util.writememory.DBHelper;

public class AnalysisActivity extends AppCompatActivity {

    private DataAdapter mAdapter;
    private ListView mListView;
    private DBHelper dbhelper;
    private View footerView;
    private RelativeLayout relativelayout;
    private List<ThingSum> list = new ArrayList<ThingSum>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        initDate();

        initView();

    }



    public void btonclik(View view){

        finish();

    }

    private void initDate() {

        dbhelper = new DBHelper(this);
        dbhelper.CreatOrOpen("writememory");


        Calendar cal=Calendar.getInstance();

        //int month=cal.MONTH;

        int month = cal.get(Calendar.MONTH)+1;



        System.out.println(month);

        for(int i=month;i>1;i--){

            int a=0,b=0,c=0;

            Calendar calen1=Calendar.getInstance();
            calen1.set(2016, i, 1, 0, 0, 0);
            Calendar calen2=Calendar.getInstance();
            calen2.set(2016, i-1, 1, 0, 0, 0);

            String str1=Utils.timeToLong(calen1.getTime())+"";
            String str2=Utils.timeToLong(calen2.getTime())+"";

            System.out.println(str1);
            System.out.println(str2);

            Cursor cur = dbhelper.selectInfo("select count(*) from mything where starttime>'"+str2+"' and endtime<'"+str1+"';");
            while (cur.moveToNext()) {

                a= cur.getInt(0);

                System.out.println("haha"+a);

            }

            Cursor cur1 = dbhelper.selectInfo("select count(*) from mything where starttime>'"+str2+"' and endtime<'"+str1+"' and pride=1;");
            while (cur1.moveToNext()) {

                b= cur1.getInt(0);

            }

            Cursor cur2 = dbhelper.selectInfo("select count(*) from mything where starttime>'"+str2+"' and endtime<'"+str1+"' and important=1;");
            while (cur2.moveToNext()) {

                c= cur2.getInt(0);

            }


            if(a!=0){

                ThingSum sum=new ThingSum();
                sum.year=2016;
                sum.month=i;
                sum.summer=a;
                sum.good=b;
                sum.important=c;

                list.add(sum);

            }


        }

        dbhelper.closeDB();

    }

    private void initView() {

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        mListView = (ListView) findViewById(R.id.list_sum);
        footerView = inflater.inflate(R.layout.footer, null);
        relativelayout=(RelativeLayout)footerView.findViewById(R.id.footerrl);
        mListView.addFooterView(footerView);
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                loadMore();

            }
        });

        mAdapter = new DataAdapter(this);
        mListView.setAdapter(mAdapter);

    }

    private void loadMore(){

        Toast.makeText(getApplicationContext(), "已经全部加载", Toast.LENGTH_SHORT).show();

    }

    private class DataAdapter extends BaseAdapter {
        @SuppressWarnings("unused")
        private Context ctx;
        private LayoutInflater inflater;
        private ViewCache cache;

        public DataAdapter(Context ctx) {
            this.ctx = ctx;
            inflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.sum_item, null);

                cache = new ViewCache();

                cache.allthingTime = (TextView) convertView.findViewById(R.id.allthingTime);
                cache.allthingSum = (TextView) convertView.findViewById(R.id.allthingSum);
                cache.allthingGood = (TextView) convertView.findViewById(R.id.allthingGood);
                cache.allthingImportant = (TextView) convertView.findViewById(R.id.allthingImportant);

                convertView.setTag(cache);

            } else {
                cache = new ViewCache();
                cache = (ViewCache) convertView.getTag();
            }

            cache.allthingTime.setText(list.get(position).year+"年"+list.get(position).month+"月");


            System.out.println(list.get(position).year+"年"+list.get(position).month+"月");

            cache.allthingSum.setText("共做"+list.get(position).summer+"件事情");
            cache.allthingGood.setText("其中"+list.get(position).good+"事情满意");
            cache.allthingImportant.setText("包含"+list.get(position).important+"件重大事情");

            return convertView;
        }

        private final class ViewCache {

            public TextView allthingTime;
            public TextView allthingSum;
            public TextView allthingGood;
            public TextView allthingImportant;
        }
    }






}
