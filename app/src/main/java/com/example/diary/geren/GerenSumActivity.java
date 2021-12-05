package com.example.diary.geren;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.R;
import com.example.diary.tools.MyDatabaseHelper;

public class GerenSumActivity extends AppCompatActivity {
    String TAG="GerenSumActivity";
    private Button advice;
    private MyDatabaseHelper dbHelper;
    private int sumPos,sumGood, sumCalm, sumBad;//达到不同心情等级的次数
    private int sumMood; //写的日记总数
    private int total;//对应记录的mood等级0,1,2,3的一个累加
    //由以上六个变量计算得出四种结果，对应四种不同的建议
    private int adviceType;//建议的类型：0，1，2，3。不开心程度逐级递升

    @SuppressLint("Recycle")
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.geren_summary);

        //标题
        //根据点击年、月、周显示
        final Intent intent = getIntent();
        String period = intent.getStringExtra("period");
        String text = "这一" + period;
        TextView title = (TextView) findViewById(R.id.reviewTitle);
        title.setText(text);

        //开始计算【总结】文本框的内容
        TextView sum = (TextView) findViewById(R.id.summary);
        dbHelper = new MyDatabaseHelper(this, "Q.db", null, 2);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor=null;
        //sql语言，选择当年、当月、当日所有记录的type属性列
        switch (period){
            case "年":
                cursor= db.rawQuery("select moodtype from Diarys " +
                                "        where createTime between datetime ('now','start of year','+1 second') " +
                                "        and datetime('now','start of year','+1 year','-1 second')",
                        null);
                break;
            case "个月":
                cursor= db.rawQuery("select moodtype from Diarys " +
                                "        where createTime between datetime('now','start of month','+1 second') " +
                                "        and datetime('now','start of month','+1 month','-1 second')",
                        null);
                break;
            case "星期":
                cursor= db.rawQuery("select moodtype from Diarys " +
                                "        where createTime between datetime('now','start of day','-6 day','weekday 1') " +
                                "        and datetime(datetime('now','start of day','weekday 0'),'start of day','+1 day')",
                        null);
                break;
            default:
                break;
        }
        sumMood=cursor.getCount();//计算结果有多少行

        if(cursor.moveToFirst()){
            do{
                //遍历，取出数据，计算sum和total
                int moodType=cursor.getInt(cursor.getColumnIndex("moodtype"));
                Log.d(TAG,"moodtype is "+moodType);
                total+=moodType;
                if (moodType==0)
                    sumPos++;
                else if(moodType==1)
                    sumGood++;
                else if(moodType==2)
                    sumCalm++;
                else if(moodType==3)
                    sumBad++;
            }while(cursor.moveToNext());
        }
        System.out.println(total);
        System.out.println(sumMood);
        System.out.println(getReturn(total,sumMood));
        sum.setText(getReturn(total,sumMood));//根据total和sumMood，得出总结

        advice = (Button) findViewById(R.id.advice);
        advice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent_advice=new Intent(GerenSumActivity.this,GerenAdvice.class);
                intent_advice.putExtra("adviceTpye",adviceType);
                startActivity(intent_advice);
            }
        });

    }

    private String getReturn(int mood,int diarynum){
        String analyze=null;
        String common="系统帮您记录了这段时间的大致心情：\n"+
                "心情很好---"+sumPos+"次\n"+"心情不错---"+ sumGood+"次\n"+
                "心情不太好---"+sumCalm+"次\n"+ "心情不好---"+sumBad+"次\n";
        if (mood >=0 && mood < 0.5 * diarynum) {
            analyze = "嗨，您最近的心情很不错哦~\n" + common +
                    "为了一直让您保有这种心流的体验\n" + "我们为您提供了一些持续感受幸福的途径\n" +
                    "快来体验吧！";
            adviceType=0;
        }
        else if (mood >= 0.5 * diarynum && mood < 1.5 * diarynum) {
            analyze = "嗨，您最近的心情还可以哦~\n" + common +
                    "为了让您提升生活的幸福感\n" + "我们为您提供了一些体验心流的途径\n" +
                    "快来体验吧！";
            adviceType=1;
        }

        else if (mood >= 1.5 * diarynum && mood < 2.5 * diarynum) {
            analyze = "嗨，您最近的心情一般般~\n" + common +
                    "为了让您保有对生活的热情\n" + "我们为您提供了一些通往幸福的途径\n" +
                    "快来体验吧！";
            adviceType=2;
        }

        else if (mood >= 2.5 * diarynum) {
            analyze = "嗨，您最近的心情不太好哦~\n" + common +
                    "为了让您提升对生活的热情\n" + "我们提供了一些打败坏心情的方法\n" +
                    "快来尝试吧！";
            adviceType=3;
        }
        return analyze;
    }
}