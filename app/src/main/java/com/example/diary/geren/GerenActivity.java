package com.example.diary.geren;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.diary.R;

public class GerenActivity extends AppCompatActivity {
    String TAG= "GerenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geren);

        //从SharedPreferences中读取作者名字到editText中
        SharedPreferences pref = getSharedPreferences("author",MODE_PRIVATE);
        String author = pref.getString("name","佚名");

        final EditText editText_author = findViewById(R.id.edit_text3);
        editText_author.setEnabled(true);  //可编辑
        editText_author.setBackground(null);    //不显示下划线
        editText_author.setTextColor(Color.DKGRAY); //设置文本颜色
        editText_author.setText(author);
        editText_author.setSelection(editText_author.getText().toString().length());    //设置光标在最后

        Button button_save = findViewById(R.id.Button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新author信息sharedPreferences中
                String author = editText_author.getText().toString();
                Log.d(TAG, "onClick: " + author);
                //更新作者名字到sharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("author",MODE_PRIVATE).edit();
                editor.putString("name",author);
                editor.apply();
                editText_author.setEnabled(false);  //不可编辑
            }
        });

        Button button_edit = findViewById(R.id.Button_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String author = editText_author.getText().toString();
                editText_author.setEnabled(true);  //可编辑
                editText_author.setSelection(author.length());
            }
        });

        Button button9=(Button) findViewById(R.id.reviewOfYear);
        Button button10=(Button) findViewById(R.id.reviewOfMonth);
        Button button11=(Button) findViewById(R.id.reviewOfWeek);

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_g = new Intent(GerenActivity.this, GerenSumActivity.class);
                intent_g.putExtra("period","年");
                startActivity(intent_g);
            }
        });

        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_g1 = new Intent(GerenActivity.this, GerenSumActivity.class);
                intent_g1.putExtra("period","个月");
                startActivity(intent_g1);
            }
        });

        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_g2 = new Intent(GerenActivity.this, GerenSumActivity.class);
                intent_g2.putExtra("period","星期");
                startActivity(intent_g2);
            }
        });
    }
}