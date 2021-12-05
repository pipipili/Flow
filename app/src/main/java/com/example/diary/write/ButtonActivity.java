package com.example.diary.write;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.*;

import com.example.diary.R;

public class ButtonActivity extends AppCompatActivity {
    public int[] options = new int[15];
    int count=0;
    int pos=0,neg=0,neu=0;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton10;
    private Button mButton11;
    private Button mButton12;
    private Button mButton13;
    private Button mButton14;
    private Button mButton15;
    private Button mButton_f;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.write_button);

        mButton1 = (Button) findViewById(R.id.button_1);
        mButton2 = (Button) findViewById(R.id.button_2);
        mButton3 = (Button) findViewById(R.id.button_3);
        mButton4 = (Button) findViewById(R.id.button_4);
        mButton5 = (Button) findViewById(R.id.button_5);
        mButton6 = (Button) findViewById(R.id.button_6);
        mButton7 = (Button) findViewById(R.id.button_7);
        mButton8 = (Button) findViewById(R.id.button_8);
        mButton9 = (Button) findViewById(R.id.button_9);
        mButton10 = (Button) findViewById(R.id.button_10);
        mButton11 = (Button) findViewById(R.id.button_11);
        mButton12 = (Button) findViewById(R.id.button_12);
        mButton13 = (Button) findViewById(R.id.button_13);
        mButton14 = (Button) findViewById(R.id.button_14);
        mButton15 = (Button) findViewById(R.id.button_15);
        mButton_f = (Button) findViewById(R.id.button_finish);

        mButton_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=options[0]+options[1]+options[2]+options[3]+options[4];
                neu=options[5]+options[6]+options[7]+options[8]+options[9];
                pos=options[10]+options[11]+options[12]+options[13]+options[14];

                Intent intent_add = new Intent(ButtonActivity.this, WriteActivity.class);
                intent_add.putExtra("isNew", true);  //isNew为true代表这是一篇新文章
                intent_add.putExtra("buttons",options);
                intent_add.putExtra("pos",pos);
                intent_add.putExtra("neu",neu);
                intent_add.putExtra("neg",neg);
                startActivity(intent_add);

//                int[] result = new int[15];
//                result = getOptions();
            }
        });

        mButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[0]==0) {
                    options[0]=1;
                    count+=1;
                }
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[1]==0) {
                    options[1]=1;
                    count+=1;
                }
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[2]==0) {
                    options[2]=1;
                    count+=1;
                }
            }
        });

        mButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[3]==0) {
                    options[3]=1;
                    count+=1;
                }
            }
        });

        mButton5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[4]==0) {
                    options[4]=1;
                    count+=1;
                }
            }
        });

        mButton6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[5]==0) {
                    options[5]=1;
                    count+=1;
                }
            }
        });

        mButton7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[6]==0) {
                    options[6]=1;
                    count+=1;
                }
            }
        });

        mButton8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[7]==0) {
                    options[7]=1;
                    count+=1;
                }
            }
        });

        mButton9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[8]==0) {
                    options[8]=1;
                    count+=1;
                }
            }
        });

        mButton10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[9]==0) {
                    options[9]=1;
                    count+=1;
                }
            }
        });

        mButton11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[10]==0) {
                    options[10]=1;
                    count+=1;
                }
            }
        });

        mButton12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[11]==0) {
                    options[11]=1;
                    count+=1;
                }
            }
        });

        mButton13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[12]==0) {
                    options[12]=1;
                    count+=1;
                }
            }
        });

        mButton14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[13]==0) {
                    options[13]=1;
                    count+=1;
                }
            }
        });

        mButton15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(count<6 && options[140]==0) {
                    options[14]=1;
                    count+=1;
                }
            }
        });

    }

    public int[] getOptions(){
        return options;
    }
}