package com.example.diary.bin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diary.tools.MyDatabaseHelper;
import com.example.diary.R;
import com.example.diary.write.diary;

import java.util.ArrayList;
import java.util.List;

public class BinActivity extends AppCompatActivity {

    private String TAG = "BinActivity";
    private List<diary> BinList = new ArrayList<>();
    private Boolean footOnly = false;   //用来判断是否为listView添加过foot，我们只在listView中foot添加一次TextView
    private MyDatabaseHelper dbHelper1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bin_main);

        //应用程序启动时读取SharedPreferences中的作者信息，若不存在作者信息，则设置一个默认的作者姓名
        SharedPreferences pref = getSharedPreferences("author",MODE_PRIVATE);
        String author = pref.getString("name","不存在作者信息");
        Log.d(TAG, "onCreate: " + author);
        if (author.equals("不存在作者信息")){
            //设置作者默认名字到sharedPreferences，默认名字为佚名
            SharedPreferences.Editor editor = getSharedPreferences("author",MODE_PRIVATE).edit();
            editor.putString("name","佚名");
            editor.apply();
        }

    }

    //这是使用onResume()来加载listView，有部分遮挡，所以用resume，返回这个活动的时候是从resume的时候开始执行，
    //因为要让编写日记界面返回主界面能刷新
    @Override
    protected void onStart() {
        super.onStart();
        //        //使用ArrayAdapter适配器，泛型适用String，android.R.layout.simple_list_item_1作为ListView的子项布局。
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this,android.R.layout.simple_list_item_1,data);
//        ListView listView = (ListView) findViewById(R.id.list_view);
//        listView.setAdapter(adapter);

        BinList.clear();  //清空list，否则每次返回到这个活动list内容会重新堆一次
        initBin();   //初始化日记数据
        BinAdapter adapter1 = new BinAdapter(BinActivity.this,R.layout.bin_card, BinList);
        ListView listView1 = (ListView) findViewById(R.id.list_bin);
        listView1.setAdapter(adapter1);

        //如果footOnly为ture则不在添加foot
        if (!footOnly){
            //在listView底部添加TextView
            TextView bootomText = new TextView(this);
            bootomText.setText(R.string.tip);
            bootomText.setGravity(Gravity.CENTER_HORIZONTAL);
            bootomText.setPadding(0,80,0,120);
            listView1.addFooterView(bootomText);
            footOnly = true;
        }

        //为每个item设置监听点击事件，获取到点击item的position
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                diary diary = BinList.get(position);
                Log.d(TAG, "onItemClick: " + position);
                //Toast.makeText(MainActivity.this, diary.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent_edit = new Intent(BinActivity.this, BinActivity1.class);
                intent_edit.putExtra("isNew",false);    //isNew为false代表这是要查看和编辑文章，会根据diaryId从数据库中取出内容来显示
                intent_edit.putExtra("diaryId",diary.getDiaryId());
                startActivity(intent_edit);
            }
        });

        //为item设置长按监听事件，弹出对话框"是否删除"
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemLongClick: click");
                showConfirm(position);
                return true;
            }
        });


    }

    //此方法的作用是创建一个选项菜单，我们要重写这个方法


    private void initBin() {
        //获取sqlite实例
        dbHelper1 = new MyDatabaseHelper(this, "Q.db", null, 2);
        SQLiteDatabase db = dbHelper1.getReadableDatabase();
        // 查询Book表中所有的数据
        Cursor cursor = db.query("Bins", null, null, null, null, null, "createTime desc");
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                int diaryId = cursor.getInt(cursor.getColumnIndex("diaryId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String showTime = cursor.getString(cursor.getColumnIndex("showTime"));

//                Log.d("MainActivity", "diaryId is " + diaryId);
//                Log.d("MainActivity", "title is " + title);
//                Log.d("MainActivity", "content is " + content);
//                Log.d("MainActivity", "author is " + author);
//                Log.d("MainActivity", "showTime is " + showTime);

                int[] moodsOptions={0};
                diary diary = new diary(diaryId,showTime,title,moodsOptions);
                BinList.add(diary);

            } while (cursor.moveToNext());
        }
        cursor.close();




        Log.d(TAG, "initDiarys have refreshed");

//        for (int i = 0;i < 1;i++){
//            diary diary1 = new diary(1,"2020-10-24 周六","title1");
//            diaryList.add(diary1);
//
//            diary diary2 = new diary(2,"2020-10-24 周六","title2");
//            diaryList.add(diary2);
//
//            diary diary3 = new diary(3,"2020-10-24 周六","title3");
//            diaryList.add(diary3);
//
//            diary diary4 = new diary(4,"2020-10-24 周六","title4");
//            diaryList.add(diary4);
//
//            diary diary5 = new diary(5,"2020-10-24 周六","title5");
//            diaryList.add(diary5);
//        }
    }

    private void showConfirm(final int position) {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);

        //确认
        confirm.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                diary diary = BinList.get(position);
                Integer diaryId = diary.getDiaryId();
                //从数据库中删除
                SQLiteDatabase db = dbHelper1.getWritableDatabase();
                db.delete("Bins", "diaryId=?", new String[] {diaryId.toString()});
                onStart();  //因为AlertDialog不属于活动，所以不影响mainActivity的生命周期，故需要在这里再执行一下start
            }
        });

        //取消
        confirm.setNegativeButton("取消",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //不进行操作
            }
        });

        confirm.setMessage("你确认要删除日记吗？");
        confirm.setTitle("提示");
        confirm.show();

    }

}