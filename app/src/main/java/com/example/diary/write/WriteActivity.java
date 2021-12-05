package com.example.diary.write;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diary.tools.MyDatabaseHelper;
import com.example.diary.R;
import com.example.diary.tools.Option;
import com.example.diary.tools.Tools;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.aliasi.util.Files;

import java.util.Scanner;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;

import com.aliasi.lm.NGramProcessLM;
import com.example.diary.write.MainActivity;
import com.example.diary.write.WriteActivity;
import com.master.permissionhelper.PermissionHelper;

import java.io.IOException;

public class WriteActivity extends AppCompatActivity {

    private String TAG = "WriteActivity";
    private MyDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);

        //获取到intent传过来的数据
        Intent intent = getIntent();
        boolean isNew = intent.getBooleanExtra("isNew",false);

        //获取sqlite实例
        dbHelper = new MyDatabaseHelper(this, "Q.db", null, 2);

        //根据isNew的值来判断是否为新建日记
        if (isNew){
            Log.d(TAG, "isNew is true");

            //获取当前的日期和星期几
            Calendar calendar = Calendar.getInstance(); // get current instance of the calendar
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            //System.out.println(formatter.format(calendar.getTime()) + "-" + getWeek(week));


            //新建日记的日期为当前日期
            TextView textView_time = findViewById(R.id.time);
            textView_time.setText(formatter.format(calendar.getTime()) + " " + getWeek(week));

        }else {
            Log.d(TAG, "isNew is false");

            Integer diaryId = intent.getIntExtra("diaryId",0);
            Log.d(TAG, "diaryId is " + diaryId);
            //根据diaryId显示日记内容
            //编辑日记的日期为创建时候的日期
            EditText editText_title = findViewById(R.id.title);
            TextView textView_time = findViewById(R.id.time);
            EditText editText_content = findViewById(R.id.content);

            //此处根据diaryId从数据库中取出日记内容
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor=db.rawQuery("select * from Diarys where diaryId = ?",new String[]{diaryId.toString()});
            if(cursor.moveToFirst())//Move the cursor to the first row. This method will return false if the cursor is empty.
            {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String showTime = cursor.getString(cursor.getColumnIndex("showTime"));

                //设置显示
                editText_title.setText(title);
                editText_title.setSelection(title.length());
                textView_time.setText(showTime);
                editText_content.setText(content);
                editText_content.setSelection(content.length());  //设置光标显示在内容的最后（待实现）
            }

        }

    }

    //注意活动的生命周期问题，在write这个活动没有销毁前，上一个main活动已经开始创建
    //所以退出存数据的时候不能在destroy的时候存，这样的话上一个main活动加载不到新存的数据
    //应该在pause的时候存
    @Override
    protected void onPause() {
        super.onPause();

        //获取到intent传过来的数据
        Intent intent = getIntent();
        boolean isNew = intent.getBooleanExtra("isNew",false);
        int[] btn=intent.getIntArrayExtra("buttons");
        int pos=intent.getIntExtra("pos",0);
        int neg=intent.getIntExtra("neg",0);
        int neu=intent.getIntExtra("neu",0);

        //获取到当前内容
        EditText editText_title = findViewById(R.id.title);
        TextView textView_time = findViewById(R.id.time);
        EditText editText_content = findViewById(R.id.content);
        String title = editText_title.getText().toString();
        String time = textView_time.getText().toString();
        String content = editText_content.getText().toString();
        SharedPreferences pref = getSharedPreferences("author",MODE_PRIVATE);
        String author = pref.getString("name","佚名");
        //获取文本NLP结果

        int k = 0;
        int[][] p = new int[3][5];
        try {
            k=new Tool().run(content);//传入文本内容
            p= Option.setMood(btn);
        } catch (Throwable t) {
            System.out.println("Thrown: " + t);
            System.out.println(k);
            t.printStackTrace(System.out);
        }

//        System.out.println(p);
        int q= Tools.getMood(p,k);//最终被存入数据库的数据

        //退出当前活动的时候保存内容
        if (isNew){
            //如果是新日记则在数据库新建记录
            Log.d(TAG, "onDestroy: save");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            // 开始组装数据
            values.put("title", title);
            values.put("content", content);
            values.put("author", author);
            values.put("showTime", time);
            values.put("moodType",q);
            db.insert("Diarys", null, values); // 插入第一条数据

            //在这个活动没有完全退出前，上一个活动已经开始执行，导致initDiarys里面数据不刷新
            Log.d(TAG, "onStop: have saved");

        }else {
            Log.d(TAG, "onDestroy: update");
            //不是新日记则根据diaryId更新数据库里的记录
            Integer diaryId = intent.getIntExtra("diaryId",0);;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("content", content);
            values.put("author", author);
            values.put("moodType",q);
            db.update("Diarys", values, "diaryId = ?", new String[] { diaryId.toString() });

        }

    }






    //如果title和content有一个为空则不进行更新和保存，返回的时候弹出确认框让用户确认
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EditText editText_title = findViewById(R.id.title);
        EditText editText_content = findViewById(R.id.content);
        String title = editText_title.getText().toString();
        String content = editText_content.getText().toString();

        if (keyCode==KeyEvent.KEYCODE_BACK && (title.equals("") || content.equals(""))){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示：");
            builder.setMessage("您的日记还没写完呢？");

            //设置确定按钮
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //设置取消按钮
            builder.setPositiveButton("取消",null);
            //显示提示框
            builder.show();
        }

        return super.onKeyDown(keyCode, event);
    }

    //匹配周几
    public static String getWeek(int week) {
        //制作表：
        String[] arr = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return arr[week - 1];
    }

    public class Tool {

        File mPolarityDir;
        String[] mCategories=new String[2];
        DynamicLMClassifier<NGramProcessLM> mClassifier;

        public Tool() {
            System.out.println("\nBASIC POLARITY DEMO");
            mPolarityDir = new File("POLARITY_DIR/Chinese");
            System.out.println("\nData Directory=" + mPolarityDir);
            mCategories[0]="neg";
            mCategories[1]="pos";
            System.out.println(mCategories);

            int nGram = 8;
            mClassifier
                    = DynamicLMClassifier
                    .createNGramProcess(mCategories, nGram);
        }


        public int run(String review) throws ClassNotFoundException, IOException {//review指的文本内容
            int k;
            train();
            k=evaluate(review);
            return k;
        }

        boolean isTrainingFile(File file) {
            return file.getName().charAt(2) != '9';  // test on fold 9
        }

        public void train() throws IOException {
            int numTrainingCases = 2;
            int numTrainingChars = 0;
            System.out.println("\nTraining.");
            String category1 = mCategories[0];
            String category2 = mCategories[1];
            Classification classification1
                    = new Classification(category1);
            Classification classification2
                    = new Classification(category2);
            String neg_type=load1();
            String pos_type=load2();
            /*byte[] review = neg_type.getBytes("ISO-8859-1");
            String reviewStr = new String(review, "ISO-8859-1");
            byte[] review1 = pos_type.getBytes("ISO-8859-1");
            String reviewStr1 = new String(review1, "ISO-8859-1");*/
            numTrainingChars += neg_type.length();
            Classified<CharSequence> classified
                    = new Classified<CharSequence>(neg_type, classification1);
            System.out.println(numTrainingChars);
            mClassifier.handle(classified);
            numTrainingChars += pos_type.length();
            System.out.println(numTrainingChars);
            Classified<CharSequence> classified1
                    = new Classified<CharSequence>(pos_type, classification2);
            mClassifier.handle(classified1);
            System.out.println("  # Training Cases=" + numTrainingCases);
            System.out.println("  # Training Chars=" + numTrainingChars);
        }



        public int evaluate(String review) throws IOException {
            int negnum = 0;
            int posnum = 0;
            int k;
            /*byte[] review1 = review.getBytes("ISO-8859-1");
            String reviewStr = new String(review1, "ISO-8859-1");*/
            Classification classification = mClassifier.classify(review);
            String resultCategory = classification.bestCategory();
            System.out.println("该篇日记的心情是：" + resultCategory);
            if (classification.bestCategory().equals("neg")) {
                negnum = negnum + 1;
            } else{
                posnum = posnum + 1;
            }
            k=posnum;
            System.out.println(posnum);
            System.out.println("negnum:" + negnum + "\n" + "posnum:" + posnum);
            return k;
        }

        public String load1(){
            FileInputStream in1=null;
            BufferedReader reader1=null;
            StringBuilder content1 = new StringBuilder();
            try{
                in1=openFileInput("neg.txt");
                reader1=new BufferedReader(new InputStreamReader(in1));
                String line="";
                while((line=reader1.readLine())!=null){
                    content1.append(line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally{
                if(reader1!=null){
                    try{
                        reader1.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                return content1.toString();
            }}

        public String load2(){
            FileInputStream in1=null;
            BufferedReader reader1=null;
            StringBuilder content1 = new StringBuilder();
            try{
                in1=openFileInput("pos.txt");
                reader1=new BufferedReader(new InputStreamReader(in1));
                String line="";
                while((line=reader1.readLine())!=null){
                    content1.append(line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally{
                if(reader1!=null){
                    try{
                        reader1.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                return content1.toString();
            }}



        //结合按钮与NLP结果，得出当日心情类型
        public int getMood (int[][] options,int pos){
//参数options为按钮所得结果
            //参数pos与neg为NLP所得结果
            int[] r=new int[4];//四种心情结果
            int neg=0;
            if(pos==1){
                neg=0;
            }
            if(pos==0){
                neg=1;
            }
            r[0]=sum(options,0)+pos;//积极+pos=心情很好
            r[1]=sum(options,1)+pos;//中性+pos=心情不错
            r[2]=sum(options,1)+neg;//中性+neg=心情不太好
            r[3]=sum(options,2)+neg;//消极+neg=心情不好

            return max(r);//选出其中分数最高的一项作为当日心情结果
        }

        //返回int数组中最大的一项的角标
        private  int max(int[] r) {
            int max=0;
            int num=-1;
            int i;
            for(i=0;i<r.length;i++)
                if (r[i] > max) {
                    max = r[i];
                    num=i;
                }
            return num;
        }

        //返回二维数组某一行的加和
        private  int sum(int[][] option,int row) {
            int s=0;
            int i;
            for(i=0;i<option[0].length;i++)
                s+=option[row][i];
            return s;
        }

        //将getMood方法的返回结果转为能被存入moodfile的String类型
        public  String toMoodString(int[] options,int type){
//参数options表示用户输入的按钮选项结果
            //参数type表示getMood方法所返回的结果
            StringBuilder moodResult= new StringBuilder(new String(String.valueOf(type))+"\n");
            int i;
            for(i=0;i<15;i++)
                if(options[i]==1)
                    moodResult = new StringBuilder(moodResult + Option.moodWords[i]+"\n");
            return moodResult.toString();
        }




    }





    }