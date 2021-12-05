package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.diary.bin.BinActivity;
import com.example.diary.geren.GerenActivity;
import com.example.diary.list.ListMainActivity;
import com.example.diary.review.ReviewActivity;
import com.example.diary.R;

import java.util.Random;

public class KaipingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaiping);

        Resources res = getResources();
        String[] kapingcard = res.getStringArray(R.array.psycard);
        Random random = new Random();
        int a=random.nextInt(30 - 0) + 0;
        String text=kapingcard[a];
        TextView card=(TextView) findViewById(R.id.kaipingcard);
        card.setText(text);

        RadioButton button_bin=(RadioButton) findViewById(R.id.bin);
        button_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_b = new Intent(KaipingActivity.this, BinActivity.class);
                startActivity(intent_b);
            }
        });

        RadioButton button_book=(RadioButton) findViewById(R.id.book);
        button_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_m = new Intent(KaipingActivity.this, ReviewActivity.class);
                startActivity(intent_m);
            }
        });

        RadioButton button_list=(RadioButton) findViewById(R.id.list);
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_l = new Intent(KaipingActivity.this, ListMainActivity.class);
                startActivity(intent_l);
            }
        });

        RadioButton button_geren=(RadioButton) findViewById(R.id.geren);
        button_geren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_g = new Intent(KaipingActivity.this, GerenActivity.class);
                startActivity(intent_g);
            }
        });
    }


}