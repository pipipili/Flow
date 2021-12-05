package com.example.diary.geren;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.R;
import com.example.diary.list.CardAdapter;
import com.example.diary.list.ListMainActivity;
import com.example.diary.list.MyDataControl;
import com.example.diary.list.Record;
import com.example.diary.list.MyDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class GerenAdvice extends AppCompatActivity {
    String TAG = "GerenAdvice";
    private MyDataControl mydb1;
    private List<Record> myList=new ArrayList<>();
    private ListView lView;
    private CardAdapter cardAdapter;
    private FloatingActionButton floatingActionButton;
    private MyDBHelper dbHelper;
    private Record record;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.geren_advice);

        Random ran=new Random();
        int num=ran.nextInt(30);
        Resources m = getResources();
        Resources s = getResources();
        String[] methods = m.getStringArray(R.array.psymethod);
        String[] supports = s.getStringArray(R.array.psysupport);
        String textMethod=methods[num];
        String textSupport=supports[num];

        final TextView met = (TextView) findViewById(R.id.randomDiary);
        TextView sup = (TextView) findViewById(R.id.juxing3);
        met.setText(textMethod);
        sup.setText(textSupport);
        mydb1 = MyDataControl.getAvailable(getApplicationContext());
        myList = mydb1.getRecords();
        Button button_addToList = findViewById(R.id.Button_addToList);
        button_addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将method建议加入todolist数据库中
                //时长统一设定为21天
                Record rec = new Record();
                rec.setDate(new Date());
                String title = met.getText().toString();
                int target=21;
                rec.setTitle(title);
                rec.setTarget(target);
                myList.add(rec);
                mydb1.addRecord(rec);
                //cardAdapter.notifyDataSetChanged();
            }
        });


        Button button_exit = findViewById(R.id.Button_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GerenAdvice.this.finish();
            }
        });
    }
}
