package com.klaus.activity.writememory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klaus.util.writememory.DBHelper;
import com.klaus.util.writememory.Utils;
import com.klaus.bean.writememory.ThingObj;


public class ListActivity extends AppCompatActivity {


    private DataAdapter mAdapter;
    private ListView mListView;
    private DBHelper dbhelper;
    private int flag=0;
    private View footerView;
    private RelativeLayout relativelayout;
    private Button button;
    private List<ThingObj> list = new ArrayList<ThingObj>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initDate();

        initView();

    }




    private void initDate() {

        dbhelper = new DBHelper(this);
        dbhelper.CreatOrOpen("writememory");
        Cursor cur = dbhelper.selectInfo("select * from mything where endtime<>'0000' order by id desc limit 5;");
        while (cur.moveToNext()) {

            ThingObj obj = new ThingObj();
            obj.id = cur.getInt(0);
            obj.title = cur.getString(1);
            obj.startTime = cur.getString(2);
            obj.endTime = cur.getString(3);
            obj.pride = cur.getInt(4);
            obj.importanr = cur.getInt(5);

            list.add(obj);

			/*
			 * System.out.println("aa"+cur.getInt(0));
			 * System.out.println("bb"+cur.getString(1));
			 * System.out.println("cc"+cur.getString(2));
			 * System.out.println("dd"+cur.getString(3));
			 * System.out.println("ee"+cur.getInt(4));
			 * System.out.println("ff"+cur.getInt(5));
			 */

        }
        dbhelper.closeDB();

    }

    private void initView() {

        button=(Button)findViewById(R.id.list_import);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        mListView = (ListView) findViewById(R.id.list_list);
        footerView = inflater.inflate(R.layout.footer, null);
        relativelayout=(RelativeLayout)footerView.findViewById(R.id.footerrl);
        relativelayout.setBackgroundColor(Color.BLUE);
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


    public void btonclik(View view) {

        // 返回前一个界面
        if (view.getId() == R.id.back) {

            finish();

        }

        // 跳转到总结界面
        if (view.getId() == R.id.showSum) {

            Intent intent = new Intent(ListActivity.this,AnalysisActivity.class);
            startActivity(intent);

        }

        // 只显示重大事项， 需要 ，刷新ListView的值
        if (view.getId() == R.id.list_import) {

            list.clear();

            if(flag==1){

                dbhelper.CreatOrOpen("writememory");
                Cursor cur = dbhelper.selectInfo("select * from mything where endtime<>'0000' order by id desc limit 5;");
                while (cur.moveToNext()) {

                    ThingObj obj = new ThingObj();
                    obj.id = cur.getInt(0);
                    obj.title = cur.getString(1);
                    obj.startTime = cur.getString(2);
                    obj.endTime = cur.getString(3);
                    obj.pride = cur.getInt(4);
                    obj.importanr = cur.getInt(5);

                    list.add(obj);

                }
                dbhelper.closeDB();

                flag=0;

                button.setBackgroundResource(R.mipmap.commentni6);

            }
            else{
                dbhelper.CreatOrOpen("writememory");
                Cursor cur = dbhelper.selectInfo("select * from mything where endtime<>'0000' and important=1 order by id desc limit 5;");
                while (cur.moveToNext()) {

                    ThingObj obj = new ThingObj();
                    obj.id = cur.getInt(0);
                    obj.title = cur.getString(1);
                    obj.startTime = cur.getString(2);
                    obj.endTime = cur.getString(3);
                    obj.pride = cur.getInt(4);
                    obj.importanr = cur.getInt(5);

                    list.add(obj);

                }
                dbhelper.closeDB();

                flag=1;

                button.setBackgroundResource(R.mipmap.commentni9);
            }

            mAdapter.notifyDataSetChanged();

        }

    }

    private void loadMore(){

        int len = list.size() - 1;
        int cid = list.get(len).id;


        if(flag==0){
            dbhelper.CreatOrOpen("writememory");
            Cursor cur = dbhelper.selectInfo("select * from mything where id<'"+cid+"' order by id desc limit 5;");
            while (cur.moveToNext()) {

                ThingObj obj = new ThingObj();
                obj.id = cur.getInt(0);
                obj.title = cur.getString(1);
                obj.startTime = cur.getString(2);
                obj.endTime = cur.getString(3);
                obj.pride = cur.getInt(4);
                obj.importanr = cur.getInt(5);

                list.add(obj);

            }
            dbhelper.closeDB();
        }
        else{
            dbhelper.CreatOrOpen("writememory");
            Cursor cur = dbhelper.selectInfo("select * from mything where id<'"+cid+"' and important=1 order by id desc limit 5;");
            while (cur.moveToNext()) {

                ThingObj obj = new ThingObj();
                obj.id = cur.getInt(0);
                obj.title = cur.getString(1);
                obj.startTime = cur.getString(2);
                obj.endTime = cur.getString(3);
                obj.pride = cur.getInt(4);
                obj.importanr = cur.getInt(5);

                list.add(obj);

            }
            dbhelper.closeDB();
        }

        int lenNew = list.size() - 1;
        if(len==lenNew){
            Toast.makeText(getApplicationContext(), "已经全部加载了", Toast.LENGTH_SHORT).show();
        }
        else{
            mAdapter.notifyDataSetChanged();
        }


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

                convertView = inflater.inflate(R.layout.thing_item, null);

                cache = new ViewCache();

                cache.good_title = (TextView) convertView.findViewById(R.id.goodThing);
                cache.good_time = (TextView) convertView.findViewById(R.id.goodTime);
                cache.bad_title = (TextView) convertView.findViewById(R.id.badThing);
                cache.bad_time = (TextView) convertView.findViewById(R.id.badTime);
                cache.l_img = (ImageView) convertView.findViewById(R.id.listImage);

                convertView.setTag(cache);

            } else {
                cache = new ViewCache();
                cache = (ViewCache) convertView.getTag();
            }

            int mypride=list.get(position).pride;


            if(mypride==0){

                cache.good_title.setVisibility(View.GONE);
                cache.good_time .setVisibility(View.GONE);
                cache.bad_title.setVisibility(View.VISIBLE);
                cache.bad_time .setVisibility(View.VISIBLE);

                cache.bad_title.setText(list.get(position).title);
                SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
                cache.bad_time.setText(sdf.format(Utils.longToTime(Long.parseLong(list.get(position).startTime)))+"~"+sdf.format(Utils.longToTime(Long.parseLong(list.get(position).endTime))));
                cache.l_img.setImageResource(R.mipmap.commentni6);

            }
            else{
                cache.good_title.setVisibility(View.VISIBLE);
                cache.good_time .setVisibility(View.VISIBLE);
                cache.bad_title.setVisibility(View.GONE);
                cache.bad_time .setVisibility(View.GONE);

                cache.good_title.setText(list.get(position).title);
                SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
                cache.good_time.setText(sdf.format(Utils.longToTime(Long.parseLong(list.get(position).startTime)))+"~"+sdf.format(Utils.longToTime(Long.parseLong(list.get(position).endTime))));
                cache.l_img.setImageResource(R.mipmap.commentni9);
            }

            return convertView;
        }

        private final class ViewCache {

            public TextView good_title;
            public TextView good_time;
            public TextView bad_title;
            public TextView bad_time;
            public ImageView l_img;
        }
    }


}
