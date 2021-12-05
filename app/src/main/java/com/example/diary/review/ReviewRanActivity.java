package com.example.diary.review;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.diary.R;
import com.example.diary.tools.MyDatabaseHelper;

public class ReviewRanActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_random);

        RadioButton button_goback=(RadioButton) findViewById(R.id.goback);
        button_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewRanActivity.this.finish();
            }
        });

        TextView diary= (TextView) findViewById(R.id.randomDiary);
        dbHelper = new MyDatabaseHelper(this, "Q.db", null, 2);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor_id = db.rawQuery("select * from Diarys " , null);

        //将所有diaryID都取出来存入数组area
        int[] area= new int[cursor_id.getCount()];
        int start=0;
        if(cursor_id.moveToFirst()){
            do{
                area[start]=cursor_id.getInt(cursor_id.getColumnIndex("diaryId"));
                start=start+1;
            }while(cursor_id.moveToNext());
        }

        //在area中随机得到一个值，输出此行内容到文本框
        int a=area[(int)(Math.random()*cursor_id.getCount())];
        System.out.println(a);
        Cursor cursor_content =db.rawQuery("select * from Diarys where diaryId = ?",new String[]{Integer.toString(a)});
        if(cursor_content.moveToFirst()) {
            String text=cursor_content.getString(cursor_content.getColumnIndex("content"));
            diary.setText(text);
        }

    }
}