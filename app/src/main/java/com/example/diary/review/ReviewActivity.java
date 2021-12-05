package com.example.diary.review;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.diary.R;
import com.example.diary.write.MainActivity;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_choice);
        Button button2=(Button) findViewById(R.id.appoint);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search = new Intent(ReviewActivity.this, MainActivity.class);
                startActivity(intent_search);
            }
        });
        Button button3=(Button) findViewById(R.id.random);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search1 = new Intent(ReviewActivity.this, ReviewRanActivity.class);
                startActivity(intent_search1);
            }
        });
    }


}